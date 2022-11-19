package com.ustc.app.studyabroad.jsonResponse;

import android.util.Log;

import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.data.ApiClient;
import com.ustc.app.studyabroad.data.ApiInterface;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.interfaces.CustomCallbackGetProg;
import com.ustc.app.studyabroad.models.Program;
import com.ustc.app.studyabroad.models.University;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class IndexQuery {
    public List<University> itemList;
    private ApiInterface apiService;

    public void getResponse(List<String> indexes, String query, CustomCallback customCallback){
        //String q="\"" + query + "\"";
        for (int j = 0; j<indexes.size(); j++) {
            String i=indexes.get(j);
            apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<String> call = apiService.list();
            itemList = new ArrayList<>();
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            String jsonResponse = response.body().toString();
                            University uni = convertData(jsonResponse, i, query);
                            itemList.add(uni);
                            if(itemList.size()==indexes.size()){
                                customCallback.onSuccess(itemList);
                            }
                        } else {
                            Log.i("RESULT", "EMPTY");
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }
    }

    private University convertData(String data, String ind, String query) {
        University university = null;
        try {
            JSONArray arr = new JSONArray(data);
            int i = Integer.parseInt(ind) - 1;
            //Helper.print(">>>>>>>>>>>>>>>>>>>> index i is "+i+" .....");
            JSONObject dataObj = arr.getJSONObject(i);

            String name = dataObj.getString("name");
            String address = dataObj.getString("address");
            String country = dataObj.getString("country");
            String rank = dataObj.getString("rank");
            String index = dataObj.getString("index");
            String image = dataObj.getString("image");

            university = new University(name, image, country, rank, index, address);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  university;
    }
}
