package com.ustc.app.studyabroad.jsonResponse;

import android.util.Log;

import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.data.ApiClient;
import com.ustc.app.studyabroad.data.ApiInterface;
import com.ustc.app.studyabroad.interfaces.CustomCallbackGetCat;
import com.ustc.app.studyabroad.interfaces.CustomCallbackGetUni;
import com.ustc.app.studyabroad.models.Category;
import com.ustc.app.studyabroad.models.University;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class GetUni {
    public University university;
    private ApiInterface apiService;
    List<Category> cats;

    public void getResponse(String name, CustomCallbackGetUni customCallback){

        String n="\"" + name + "\"";
        System.out.println("NEW NAME >>>>>>>>>>>>>>> " + n);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<String> call = apiService.main();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String jsonResponse = response.body().toString();
                        customCallback.onSuccess(convertData(jsonResponse, name));
                    } else {
                        Log.i("RESULT", "EMPTY");
                    }
                }
                else {
                    System.out.println("result ...................... failed 222");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("result ...................... failed");
            }
        });
    }

    private University convertData(String data, String n) {
        ArrayList categs = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(data);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject json = arr.getJSONObject(i);
                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    categs.add(key);
                    //Helper.print(">>>>>>Key :" + key + "  >> " + cats); //+ "  Value :" + json.get(key));
                }
            }
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                JSONArray unis = o.getJSONArray(String.valueOf(categs.get(i)));
                //Helper.print(cats.get(i) + ">>>>>unis length " + unis.length());
                for (int j = 0; j < unis.length(); j++) {
                    JSONObject dataObj = unis.getJSONObject(j);

                    String name=dataObj.getString("name");
                    String address=dataObj.getString("address");
                    //String image=dataObj.getString("image");
                    String country=dataObj.getString("country");
                    String rank=dataObj.getString("rank");
                    String uni_type=dataObj.getString("uni_type");
                    String total_stud=dataObj.getString("total_stud");
                    String total_int_stud=dataObj.getString("total_int_stud");
                    String uniIndex=dataObj.getString("index");
                    String content=dataObj.getString("content");

                    if (name.toLowerCase().equals(n.toLowerCase())){
                        //find categories at index-1
                        QueryCategories queryCategories = new QueryCategories();
                        queryCategories.getResponse(Integer.valueOf(uniIndex), String.valueOf(categs.get(i)), i, new CustomCallbackGetCat() {
                            @Override
                            public void onSuccess(List<Category> cat) {
                                university = new University(name, address, country, rank, uni_type, total_stud, total_int_stud, uniIndex, content, cat);
                            }
                            @Override
                            public void onFailure() {

                            }
                        });
                        break;
                    }
                }
            }


            /*JSONObject obj = new JSONObject(data);
            if (!obj.optString("body").isEmpty()) {
                JSONObject dataObj=obj.getJSONObject("body");
                String name=dataObj.getString("name");
                String address=dataObj.getString("address");
                //String image=dataObj.getString("image");
                String country=dataObj.getString("country");
                String rank=dataObj.getString("rank");
                String uni_type=dataObj.getString("uni_type");
                String total_stud=dataObj.getString("total_stud");
                String total_int_stud=dataObj.getString("total_int_stud");
                String index=dataObj.getString("index");
                String content=dataObj.getString("content");

                cats = new ArrayList<>();

                JSONArray categories = dataObj.getJSONArray("categories");
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
                Helper.print("FRAG INDEX >>>>>>>>>>>>> " + index);
                university = new University(name, address, country, rank, uni_type, total_stud, total_int_stud, index, content, cats);
            }*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return  university;
    }
}
