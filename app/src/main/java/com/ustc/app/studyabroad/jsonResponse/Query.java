package com.ustc.app.studyabroad.jsonResponse;

import android.util.Log;

import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.data.ApiClient;
import com.ustc.app.studyabroad.data.ApiInterface;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.interfaces.CustomCallbackGetProg;
import com.ustc.app.studyabroad.interfaces.CustomCallbackGetUni;
import com.ustc.app.studyabroad.models.Program;
import com.ustc.app.studyabroad.models.University;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Query {
    public List<University> itemList;
    private ApiInterface apiService;

    public void getResponse(int index, CustomCallbackGetUni customCallbackGetUni){
        System.out.println(" QUERY BEING INITIATED >>>>>>>>>>>>>>> ");

        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.list();
        itemList = new ArrayList<>();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonResponse = response.body().toString();
                        University uni = convertData(jsonResponse, index);
                        customCallbackGetUni.onSuccess(uni);
                    } else {
                        customCallbackGetUni.onFailure();
                        Log.i("RESULT", "EMPTY");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private University convertData(String data, int ind) {
        University university = null;
        try {
            JSONArray arr = new JSONArray(data);
            int i = ind - 1;
            //Helper.print(">>>>>>>>>>>>>>>>>>>> index i is "+i+" .....");
            JSONObject dataObj = arr.getJSONObject(i);
            String name = dataObj.getString("name");
            String address = dataObj.getString("address");
            String country = dataObj.getString("country");
            String rank = dataObj.getString("rank");
            String index = dataObj.getString("index");
            String image = dataObj.getString("image");

            university = new University(name, image, country, rank, index, address);

            //System.out.println(" PROGRAM >>>>>>>>>>>>>>> " + university);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  university;
    }
}

