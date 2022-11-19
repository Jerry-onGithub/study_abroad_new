package com.ustc.app.studyabroad.jsonResponse;

import android.util.Log;

import com.ustc.app.studyabroad.data.ApiClient;
import com.ustc.app.studyabroad.data.ApiInterface;
import com.ustc.app.studyabroad.interfaces.CustomCallbackTab2Frag;
import com.ustc.app.studyabroad.models.Fields;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Tab2Frag {
    private ApiInterface apiService;
    HashMap<String, List<String>> listDataChild;
    List<String> listDataHeader;

    public void getResponse(CustomCallbackTab2Frag customCallback) {
        listDataChild = new HashMap<String, List<String>>();
        listDataHeader = new ArrayList<String>();

        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.fields();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonResponse = response.body().toString();
                        try {
                            JSONArray arr = new JSONArray(jsonResponse);
                            for (int i=0; i< arr.length(); i++) {
                                JSONObject dataObj = arr.getJSONObject(i);
                                String field = dataObj.getString("field");
                                JSONArray dept = dataObj.getJSONArray("programs");
                                List<String> list = new ArrayList<String>();
                                for (int j = 0; j < dept.length(); j++) {
                                    String dt = dept.getString(j);
                                    list.add(dt);
                                }
                                listDataHeader.add(field);
                                listDataChild.put(listDataHeader.get(i), list);
                            }
                            Fields fs=new Fields(listDataHeader, listDataChild);
                            customCallback.onSuccess(fs);

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
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
            }
        });
    }
}
