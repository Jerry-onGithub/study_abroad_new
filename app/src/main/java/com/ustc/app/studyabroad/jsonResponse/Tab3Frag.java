package com.ustc.app.studyabroad.jsonResponse;

import android.util.Log;

import com.ustc.app.studyabroad.data.ApiClient;
import com.ustc.app.studyabroad.data.ApiInterface;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.models.University;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Tab3Frag {
    public List<University> itemList;
    private ApiInterface apiService;

    public void getResponse(String name, CustomCallback customCallback){

        String n=name.toLowerCase();
        String c="\"country\"";
        //System.out.println("NEW NAME >>>>>>>>>>>>>>> " + n + "and" + c);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.list();
        itemList = new ArrayList<>();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonResponse = response.body().toString();
                        itemList = convertData(jsonResponse, n);
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

    private List<University> convertData(String data, String name) {
        List<University> list=new ArrayList<>();
        String[] countries = {"canada", "australia", "germany", "uk", "usa"};

        if (Arrays.stream(countries).anyMatch(name::equals)) {
            try {
                int start = 0, end = 0;
                if(name.equals(countries[0])){
                    start=0; end=36;
                } else if(name.equals(countries[1])){
                    start=37; end=69;
                } else if(name.equals(countries[2])){
                    start=70; end=131;
                } else if(name.equals(countries[3])){
                    start=132; end=209;
                } else if(name.equals(countries[4])){
                    start=210; end=2128;
                }

                JSONArray arr = new JSONArray(data);
                //int total = (end+1)-start;
                for (int i = start; i <= end; i++) {
                    JSONObject dataObj = arr.getJSONObject(i);
                    String uniname = dataObj.getString("name");
                    String address = dataObj.getString("address");
                    String image = dataObj.getString("image");
                    String index = dataObj.getString("index");

                    University university = new University(uniname, address, image, index);
                    list.add(university);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("LIST SIZE  >>>>>>>>>>>>>>>> ...................... " + list.size());
        return  list;
    }
}
