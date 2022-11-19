package com.ustc.app.studyabroad.jsonResponse;

import android.util.Log;

import com.google.gson.JsonElement;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.data.ApiClient;
import com.ustc.app.studyabroad.data.ApiInterface;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.interfaces.GetPopularDCustomCallback;
import com.ustc.app.studyabroad.models.Category;
import com.ustc.app.studyabroad.models.PopularD;
import com.ustc.app.studyabroad.models.University;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class PopularDepartments {
    public List<PopularD> itemList;
    private ApiInterface apiService;

    public void getResponse(String s, GetPopularDCustomCallback customCallback){
        //String w="\"" + s + "\"";
        //String q="\"" + "categories" + "\"";

        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.categories();
        itemList = new ArrayList<>();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonResponse = response.body().toString();
                        itemList = convertData(jsonResponse, s);
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

    private List<PopularD> convertData(String data, String s) {
        List<PopularD> list=new ArrayList<>();
        ArrayList cats = new ArrayList<>();
        List<Category> categories=new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(data);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject json = arr.getJSONObject(i);
                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    cats.add(key);
                    //Helper.print(">>>>>>Key :" + key + "  >> " + cats); //+ "  Value :" + json.get(key));
                }
            }
            for (int i = 0; i < arr.length(); i++) {
                //Helper.print(">>>>>index " + i + cats.get(i));
                JSONObject o = arr.getJSONObject(i);
                JSONArray unis = o.getJSONArray(String.valueOf(cats.get(i)));
                //Helper.print(cats.get(i) + ">>>>>unis length " + unis.length());
                for (int j = 0; j < unis.length(); j++) {
                    JSONObject dataObj = unis.getJSONObject(j);
                    JSONArray categs = dataObj.getJSONArray("categories");

                    String name=dataObj.getString("name");
                    String index=dataObj.getString("index");

                    for (int k = 0; k < categs.length(); k++) {
                        JSONObject obj = categs.getJSONObject(k);
                        //Helper.print(s + ">>>>>EQUALS "+obj.getString("name"));
                        if (obj.getString("name").equals(s)){
                            //Helper.print(">>>>>EQUALS "+obj.getString("name"));
                            String cat_name = obj.getString("name");
                            String cat_content = obj.getJSONObject("details").getString("content");

                            Category category = new Category(cat_name, cat_content);
                            categories.add(category);
                            PopularD university = new PopularD(name, index, categories);
                            list.add(university);
                        }
                    }
                }
            }
            //Helper.print(">>>>>>>>>>>>>>>>>>>> list size "+list.size()+" .....");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  list;
    }
}
