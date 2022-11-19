package com.ustc.app.studyabroad.jsonResponse;

import android.util.Log;

import com.ustc.app.studyabroad.data.ApiClient;
import com.ustc.app.studyabroad.data.ApiInterface;
import com.ustc.app.studyabroad.interfaces.CustomCallbackGetProg;
import com.ustc.app.studyabroad.models.Program;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class GetPrograms {
    public List<Program> itemList;
    private ApiInterface apiService;

    public void getResponse(String name, String index, CustomCallbackGetProg customCallback){
        String n="\"" + name + "\"";
        String i="\"" + index + "\"";

        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.programs();
        itemList = new ArrayList<>();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonResponse = response.body().toString();
                        itemList = convertData(jsonResponse, name, index);
                        customCallback.onSuccess(itemList);
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

    private List<Program> convertData(String data, String name, String index) {
        List<Program> programs=new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(data);
            JSONObject uni = arr.getJSONObject(Integer.valueOf(index)-1);
            String uniName=uni.getString("name");
            if (uniName.toLowerCase().equals(name.toLowerCase())){
                JSONArray progs = uni.getJSONArray("programs");
                for (int i=0; i< progs.length(); i++) {
                    JSONObject dataObj = progs.getJSONObject(i);
                    String progName=dataObj.getString("name");
                    Program program = new Program(progName);
                    programs.add(program);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  programs;
    }
}
