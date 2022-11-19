package com.ustc.app.studyabroad.jsonResponse;

import android.util.Log;

import com.ustc.app.studyabroad.data.ApiClient;
import com.ustc.app.studyabroad.data.ApiInterface;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.interfaces.ListStringCallback;
import com.ustc.app.studyabroad.models.University;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class GetGalleryImages {
    public List<String> itemList;
    private ApiInterface apiService;

    public void getResponse(ListStringCallback customCallback){
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.getGalleryImages();
        itemList = new ArrayList<>();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonResponse = response.body().toString();
                        itemList = convertData(jsonResponse);
                        customCallback.onSuccess(itemList);
                    } else {
                        Log.i("RESULT", "EMPTY");
                    }
                }
                else {
                    customCallback.onFailure("");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                customCallback.onFailure("");
            }
        });
    }

    private List<String> convertData(String data) {
        List<String> list=new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(data);
            if (!obj.optString("body").isEmpty()) {
                JSONArray dataArray = obj.getJSONArray("body");
                for (int i=0; i< dataArray.length(); i++) {
                    JSONObject dataObj = dataArray.getJSONObject(i);
                    String image=dataObj.getString("url");

                    list.add(image);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  list;
    }
}
