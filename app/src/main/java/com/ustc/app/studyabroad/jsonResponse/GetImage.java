package com.ustc.app.studyabroad.jsonResponse;

import android.util.Log;

import com.ustc.app.studyabroad.data.ApiClient;
import com.ustc.app.studyabroad.data.ApiInterface;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.interfaces.GetUrlCallback;
import com.ustc.app.studyabroad.models.University;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class GetImage {
    public String img;
    private ApiInterface apiService;

    public void getResponse(String index, GetUrlCallback customCallback){

        String i="\"" + index + "\"";
        String q="\"img\"";
        System.out.println("NEW NAME >>>>>>>>>>>>>>> " + i + "and" + q);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.images();
        //itemList = new ArrayList<>();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonResponse = response.body().toString();
                        img = convertData(jsonResponse, index);
                        customCallback.onSuccess(img);
                    } else {
                        Log.i("RESULT", "EMPTY");
                    }
                }
                else {
                    customCallback.onFailure("f");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                customCallback.onFailure("fn");
                System.out.println("result ...................... failed");
            }
        });
    }

    private String convertData(String data, String index) {
        String url = null;
        int i = Integer.valueOf(index) - 1;
        try {
            JSONArray arr = new JSONArray(data);
            JSONObject uni = arr.getJSONObject(i);
            url=uni.getString("image");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return url;
    }
}
