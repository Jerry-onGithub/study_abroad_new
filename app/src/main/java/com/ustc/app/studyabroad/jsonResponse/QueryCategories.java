package com.ustc.app.studyabroad.jsonResponse;


import android.util.Log;

import com.ustc.app.studyabroad.data.ApiClient;
import com.ustc.app.studyabroad.data.ApiInterface;
import com.ustc.app.studyabroad.interfaces.CustomCallbackGetCat;
import com.ustc.app.studyabroad.models.Category;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class QueryCategories {
    public List<Category> itemList;
    private ApiInterface apiService;

    public void getResponse(int index, String country, int i, CustomCallbackGetCat customCallbackGetCat){

        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.categories();
        itemList = new ArrayList<>();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonResponse = response.body().toString();
                        itemList = convertData(jsonResponse, index, country, i);
                        customCallbackGetCat.onSuccess(itemList);
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

    private List<Category> convertData(String data, int ind, String country, int j) {
        List<Category> cats=new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(data);
            JSONObject o = arr.getJSONObject(j);

            JSONArray unis = o.getJSONArray(country);
            JSONObject o2 = unis.getJSONObject(ind);

            JSONArray categories = o2.getJSONArray("categories");
            for (int i=0; i< categories.length(); i++) {
                JSONObject cat = categories.getJSONObject(i);
                String cat_name = cat.getString("name");

                JSONObject details=cat.getJSONObject("details");
                String cat_content = details.getString("content");
                String male = details.getString("male");
                String female = details.getString("female");
                String int_stud = details.getString("int_stud");
                String toefl_mini = details.getString("toefl_mini");
                String toefl_mean = details.getString("toefl_mean");
                String ielts_mini = details.getString("ielts_mini");
                String gre_verbal = details.getString("gre_verbal");
                String gre_quant = details.getString("gre_quant");
                String gre_awa = details.getString("gre_awa");
                String fellowship = details.getString("fellowship");
                String teaching_assist = details.getString("teaching_assist");
                String research_assist = details.getString("research_assist");
                String masters_acc_rate = details.getString("masters_acc_rate");
                String phd_acc_rate = details.getString("phd_acc_rate");
                String tuition = details.getString("tuition");
                String living_exp = details.getString("living_exp");
                String financial_aid_officer = details.getString("financial_aid_officer");
                String financial_aid_officer_contact = details.getString("financial_aid_officer_contact");
                String us_app_fee = details.getString("us_app_fee");
                String us_app_deadline = details.getString("us_app_deadline");
                String int_app_fee = details.getString("int_app_fee");
                String int_app_deadline = details.getString("int_app_deadline");
                String acc_rate = details.getString("acc_rate");
                String stud_rec_aid = details.getString("stud_rec_aid");
                String private_ = details.getString("private");
                String public_ = details.getString("public");
                String bar_passage_rate = details.getString("bar_passage_rate");
                String employed = details.getString("employed");
                String total_enrollment = details.getString("total_enrollment");
                String ave_gpa = details.getString("ave_gpa");
                String gmat = details.getString("gmat");
                String lsat = details.getString("lsat");

                Category uniCategory = new Category(cat_name, cat_content, male, female, int_stud, toefl_mini, toefl_mean, ielts_mini,
                        gre_verbal, gre_quant, gre_awa, fellowship, teaching_assist, research_assist, masters_acc_rate, phd_acc_rate, tuition, living_exp,
                        financial_aid_officer, financial_aid_officer_contact, us_app_fee, us_app_deadline, int_app_fee, int_app_deadline, acc_rate, stud_rec_aid,
                        private_, public_, bar_passage_rate, employed, total_enrollment, ave_gpa, gmat, lsat);

                cats.add(uniCategory);
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cats;
    }
}


