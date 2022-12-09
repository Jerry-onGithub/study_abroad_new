package com.ustc.app.studyabroad.jsonResponse;

import android.util.Log;

import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.data.ApiClient;
import com.ustc.app.studyabroad.data.ApiInterface;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.models.University;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Search {
    public List<University> itemList;
    private ApiInterface apiService;

    public void getResponse(String search, String query, String country, CustomCallback customCallback){

        String ch="\"" + search + "\"";
        String q="\"" + query + "\"";
        String c="\"" + country + "\"";

        apiService = ApiClient.getClient().create(ApiInterface.class);
        //Call<String> call = apiService.list();
        Call<String> call = null;
        if (query.equals("uni_name")){
            call = apiService.list();
        } else if (query.equals("prog_name")){
            call = apiService.programs();
        } else if (query.equals("country")){
            call = apiService.main();
        }

        //Call<String> call = apiService.searchQuery(ch, q, c);
        itemList = new ArrayList<>();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonResponse = response.body().toString();
                        itemList = convertData(jsonResponse, query, search, country);
                        customCallback.onSuccess(itemList);
                    } else {
                        Log.i("RESULT", "EMPTY");
                    }
                }
                else {
                    customCallback.onFailure();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                customCallback.onFailure();
                System.out.println("result ...................... failed");
            }
        });
    }

    private List<University> convertData(String data, String query, String search, String cu) {
        //University university = null;
        List<University> list=new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(data);
            for (int i=0; i< arr.length(); i++) {
                JSONObject dataObj = arr.getJSONObject(i);
                if(query.equals("uni_name")){
                    Helper.print("Query equals >>>>>>>> uni_name");
                    String name = dataObj.getString("name");
                    if(name.toLowerCase().contains(search.toLowerCase())){
                        Helper.print("name equals >>>>>>>> search");
                        if (!checkForDup(list, name)) {
                            Helper.print("Dup not found >>>>>>>>");
                            String address = dataObj.getString("address");
                            String index = dataObj.getString("index");
                            String image = dataObj.getString("image");
                            University university = new University(name, address, image, index);
                            list.add(university);
                        }
                    }
                } else if(query.equals("prog_name")){
                    JSONArray progs = dataObj.getJSONArray("programs");
                    for (int j=0; j< progs.length(); j++) {
                        JSONObject prog = progs.getJSONObject(j);
                        String name = prog.getString("name");
                        if(name.toLowerCase().contains(search.toLowerCase())){
                            if (!checkForDup(list, name)) {
                                String index = dataObj.getString("index");
                                University university = new University(name, index);
                                list.add(university);
                            }
                        }
                    }
                } else if(query.equals("country")){
                    JSONArray array = new JSONArray(data);
                    for (int k = 0; k < array.length(); k++){
                        JSONObject json = array.getJSONObject(k);
                        Iterator<String> keys = json.keys();
                        while (keys.hasNext()) {
                            String country = keys.next();
                            if(country.toLowerCase().equals(cu.toLowerCase())){
                                JSONArray unis = json.getJSONArray(String.valueOf(country));
                                for (int n = 0; n < unis.length(); n++){
                                    JSONObject o = unis.getJSONObject(n);
                                    String name = o.getString("name");
                                    if(name.toLowerCase().contains(search.toLowerCase())){
                                        if (!checkForDup(list, name)) {
                                            String index = o.getString("index");
                                            University university = new University(name, index);
                                            list.add(university);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Helper.print("LIST SIZE IS >>>>>>>> " + list.size());
        return  list;
    }

    private boolean checkForDup(List<University> list, String name) {
        if (list.size() == 0){
            return false;
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getName().toLowerCase().equals(name.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }
}
