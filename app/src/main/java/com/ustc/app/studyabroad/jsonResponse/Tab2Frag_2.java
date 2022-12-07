package com.ustc.app.studyabroad.jsonResponse;

import android.util.Log;

import com.ustc.app.studyabroad.data.ApiClient;
import com.ustc.app.studyabroad.data.ApiInterface;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.models.University;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Tab2Frag_2 {
    public List<University> itemList;
    private ApiInterface apiService;
    private List<University> list;
    public List<University> uniList;

    public void getResponse(String name, CustomCallback customCallback){

        //String n="\"" + name + "\"";
        //String c="\"program\"";
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.programs();
        Call<String> call2 = apiService.list();
        itemList = new ArrayList<>();
        uniList = new ArrayList<>();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        call2.enqueue(new Callback<String>() {
                             @Override
                             public void onResponse(Call<String> call, retrofit2.Response<String> response2) {
                                 if (response2.isSuccessful()) {
                                     if (response2.body() != null) {
                                         String jsonResponse = response.body().toString();
                                         String jsonResponse2 = response2.body().toString();
                                         itemList = convertData(jsonResponse, jsonResponse2, name);
                                         customCallback.onSuccess(itemList);
                                     }
                                 }
                             }

                              @Override
                              public void onFailure(Call<String> call, Throwable t) {

                              }
                          });
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

    private List<University> convertData(String data, String data2, String searchName) {
        list=new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(data);
            for (int i=0; i< arr.length(); i++) {
                int count = 0;
                JSONObject dataObj = arr.getJSONObject(i);
                JSONArray progs = dataObj.getJSONArray("programs");
                for (int j=0; j< progs.length(); j++) {
                    JSONObject prog = progs.getJSONObject(j);
                    String name = prog.getString("name");
                    if (searchProg(name.toLowerCase(), searchName.toLowerCase())){
                        count+=1;
                        int index = Integer.valueOf(dataObj.getString("index"));

                        try {
                            JSONArray arr2 = new JSONArray(data2);
                            int k = index - 1;
                            JSONObject dataObj2 = arr2.getJSONObject(k);
                            String uniName = dataObj2.getString("name");
                            String address = dataObj2.getString("address");
                            //String country = dataObj2.getString("country");
                            //String rank = dataObj2.getString("rank");
                            String uniIndex = dataObj2.getString("index");
                            String image = dataObj2.getString("image");

                            University university = new University(uniName, address, image, name, uniIndex);
                            list.add(university);
                            //System.out.println(" PROGRAM >>>>>>>>>>>>>>> " + university);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                        ///get university from list.json at index index
                        /*Query indexQuery = new Query();
                        indexQuery.getResponse(index, new CustomCallbackGetUni() {
                            @Override
                            public void onSuccess(University uni) {
                                University university = new University(uni.getName(), uni.getAddress(), uni.getImgUrl(), name, uni.getIndex());
                                list.add(university);
                                //System.out.println(" PROGRAM >>>>>>>>>>>>>>> " + university.getProgram());
                                //System.out.println(" LIST PROGRAM >>>>>>>>>>>>>>> " + list.get(10).getProgram());
                                //queryModel.setUniversity(university);
                            }
                            @Override
                            public void onFailure() {
                                System.out.println(" onFailure >>>>>>>>>>>>>>> " + index);
                            }
                        });*/
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //System.out.println(" LIST SIZE >>>>>>>>>>>>>>> " + list.size());
        return  list;
    }

    private boolean searchProg(String progName, String searchName) {
        int count = 0;
        //String[] parts = searchName.split(" ");
        if (progName.contains(searchName)){
            count+=1;
            if (count < 2){
                return true;
            }
        }
/*        if (parts.length > 1){
            for(int i = 0; i<parts.length; i++){
                if (progName.contains(parts[i])){
                    count+=1;
                    if (count < 2){
                        return true;
                    }
                }
            }
        } else {
            if (progName.contains(searchName)){
                count+=1;
                if (count < 2){
                    return true;
                }
            }
        }*/
        return false;
    }
}
