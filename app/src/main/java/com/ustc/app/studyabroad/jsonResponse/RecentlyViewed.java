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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class RecentlyViewed {
    public List<University> itemList;
    private ApiInterface apiService;

    public void getResponse(CustomCallback customCallback){
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.list();
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
                    customCallback.onFailure();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                customCallback.onFailure();
            }
        });
    }

    private List<University> convertData(String data) {
        List<University> list=new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(data);
            //Helper.print(">>>>>>>>>>>>>>>>>>>> arr length is "+arr.length()+" .....");
            for (int i=0; i< arr.length(); i++) {
                JSONObject dataObj = arr.getJSONObject(i);
                String name=dataObj.getString("name");
                String address=dataObj.getString("address");
                String image=dataObj.getString("image");
                String index=dataObj.getString("index");

                University university = new University(name, address, image, index);
                list.add(university);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  list;
    }
}
