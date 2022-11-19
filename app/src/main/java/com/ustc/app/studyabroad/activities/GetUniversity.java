package com.ustc.app.studyabroad.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.tabs.TabLayout;
import com.google.rpc.Help;
import com.libRG.CustomTextView;
import com.ustc.app.studyabroad.FirebaseStorageHelper;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.adapters.CategoriesAdapter;
import com.ustc.app.studyabroad.interfaces.CustomCallbackGetUni;
import com.ustc.app.studyabroad.jsonResponse.GetUni;
import com.ustc.app.studyabroad.models.Category;
import com.ustc.app.studyabroad.models.RecentlyViewed;
import com.ustc.app.studyabroad.models.University;

import java.util.List;

public class GetUniversity extends AppCompatActivity {
    ImageView img;
    TextView name_, address, uni_type, total_stud, total_int_stud, rank, button, content, phd_acc_rate_title, cat_name;
    AppCompatImageView im;
    TabLayout tabLayout;
    List<Category> categories;
    CustomTextView t1, t2, t3, t4, t5;

    TextView total_enrollment, male, female, int_stud, country, app_fee, app_deadline, masters_acc_rate, int_app_fee, 
            int_app_deadline, phd_acc_rate, ave_gpa, toefl_mini, toefl_mean, ielts_mini, gre_verbal, gre_quant, gre_awa, 
            tuition, living_exp, fellowship, teaching_assist, research_assist, financial_aid_officer, 
            financial_aid_officer_contact, bar_passage_rate, employed, avg_lang_scores_title, lsat;

    LinearLayout masters_acc_rate_title, gmat_score, lsat_score, toefl_score, gre_score, employment_figures, bar, top_view, bottom_view;
    CircularProgressIndicator progress_bar_masters, progress_bar_phd, progress_bar_employed, progress_bar_bar;

    View world_view, male_view, female_view;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_uni);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        String imgUrl = intent.getExtras().getString("img");
        String index = intent.getExtras().getString("index");

        Helper.print("INDEX: "+index);
        FirebaseStorageHelper.saveRecentlyViewed(index);
        setView();
        onClicks(name, index);
        getCategories(name, imgUrl);

        Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>> "+name+" >>>>>>>>> "+imgUrl);

        //#0600FF

    }

    private void getCategories(String name, String imgUrl) {
        GetUni rs = new GetUni();
        rs.getResponse(name, new CustomCallbackGetUni() {
            @Override
            public void onSuccess(University uni) {
                setTopView(uni, imgUrl);
                setCategories(uni);
            }
            @Override
            public void onFailure() {
                Toast.makeText(getApplicationContext(), "Connection error, please try again!", Toast.LENGTH_LONG).show();
                System.out.println("Failed ...................... " + name);
            }
        });
    }

    private void setCategories(University uni) {
        categories = uni.getCategories();
        for (int i=0; i<categories.size(); i++) {
            String cat_name = categories.get(i).getCat_name().substring(0, 1).toUpperCase() + categories.get(i).getCat_name().substring(1);
            switch(i){
                case 0:
                    t1.setText(cat_name);
                    t1.setVisibility(View.VISIBLE);
                    t1.setBackgroundColor(getResources().getColor(R.color.white));
                    setCategoriesView(categories.get(0));
                    break;
                case 1:
                    t2.setText(cat_name);
                    t2.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    t3.setText(cat_name);
                    t3.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    t4.setText(cat_name);
                    t4.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    t5.setText(cat_name);
                    t5.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            System.out.println("Category " + i + categories.get(i).getCat_name());
        }
        setOnClicks(categories);

    }

    private void setOnClicks(List<Category> categories) {
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(t1);
                setCategoriesView(categories.get(0));
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(t2);
                setCategoriesView(categories.get(1));
            }
        });
        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(t3);
                setCategoriesView(categories.get(2));
            }
        });
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(t4);
                setCategoriesView(categories.get(3));
            }
        });
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackgroundColor(t5);
                setCategoriesView(categories.get(4));
            }
        });
    }

    private void setBackgroundColor(CustomTextView t) {
        t.setBackgroundColor(getResources().getColor(R.color.white));
        t.setTextColor(getResources().getColor(R.color.bg_color));
        setColor(t1, t);
        setColor(t2, t);
        setColor(t3, t);
        setColor(t4, t);
        setColor(t5, t);
    }

    private void setColor(CustomTextView t_, CustomTextView t){
        if(!t_.equals(t)){
            t_.setBackgroundColor(getResources().getColor(R.color.bg_color));
            t_.setTextColor(getResources().getColor(R.color.white));
        }
    }
    private void setCategoriesView(Category category) {
        cat_name.setText(Helper.capitalize(category.getCat_name()));
        content.setText(category.getCat_content());
        total_enrollment.setText(category.getTotal_enrollment());
        male.setText(category.getMale());
        setBackgroundView(category.getMale(), male_view);

        female.setText(category.getFemale());
        setBackgroundView(category.getFemale(), female_view);

        int_stud.setText(category.getInt_stud());
        setBackgroundView(category.getInt_stud(), world_view);

        country.setText("US");
        app_fee.setText(category.getUs_app_fee());
        app_deadline.setText(category.getUs_app_deadline());
        masters_acc_rate.setText(category.getMasters_acc_rate());

        setProgressBar(category.getMasters_acc_rate(), progress_bar_masters);

        int_app_fee.setText(category.getInt_app_fee());
        int_app_deadline.setText(category.getInt_app_deadline());
        phd_acc_rate.setText(category.getPhd_acc_rate());

        setProgressBar(category.getPhd_acc_rate(), progress_bar_phd);

        ave_gpa.setText(category.getAve_gpa());
        toefl_mini.setText(category.getToefl_mini());
        toefl_mean.setText(category.getToefl_mean());
        ielts_mini.setText(category.getIelts_mini());

        setText(category.getGre_verbal(), gre_verbal);
        setText(category.getGre_quant(), gre_quant);
        setText(category.getGre_awa(), gre_awa);

        //gre_verbal.setText(category.getGre_verbal());
        //gre_quant.setText(category.getGre_quant());
        //gre_awa.setText(category.getGre_awa());

        tuition.setText(category.getTuition());
        living_exp.setText(category.getLiving_exp());
        fellowship.setText(category.getFellowship());
        teaching_assist.setText(category.getTeaching_assist());
        research_assist.setText(category.getResearch_assist());
        financial_aid_officer.setText(category.getFinancial_aid_officer());
        financial_aid_officer_contact.setText(category.getFinancial_aid_officer_contact());
        lsat.setText(category.getLsat());

        setProgressBar(category.getEmployed(), progress_bar_employed);
        setProgressBar(category.getBar_passage_rate(), progress_bar_bar);

        setText(category.getBar_passage_rate(), bar_passage_rate);
        setText(category.getEmployed(), employed);
        Helper.print("BAR PASSAGE RATE >>>>>>>>>>>>>>>>>>>>>> "+category.getBar_passage_rate());

        switch (category.getCat_name()){
            case("engineering"):
                setProgressBar(category.getPhd_acc_rate(), progress_bar_phd);
                gmat_score.setVisibility(View.GONE);
                lsat_score.setVisibility(View.GONE);
                employment_figures.setVisibility(View.GONE);
                masters_acc_rate_title.setVisibility(View.VISIBLE);
                masters_acc_rate.setText(category.getMasters_acc_rate());
                phd_acc_rate.setText(category.getPhd_acc_rate());
                avg_lang_scores_title.setVisibility(View.VISIBLE);
                toefl_score.setVisibility(View.VISIBLE);
                gre_score.setVisibility(View.VISIBLE);

                System.out.println("CURRENT " + category.getCat_name()+ ", " +category.getMasters_acc_rate() + ", " + category.getPhd_acc_rate());
                break;

            case("business"):
                setProgressBar(category.getPhd_acc_rate(), progress_bar_phd);
                gre_score.setVisibility(View.GONE);
                lsat_score.setVisibility(View.GONE);
                bar.setVisibility(View.GONE);
                employment_figures.setVisibility(View.VISIBLE);
                System.out.println("CURRENT " + category.getCat_name());
                break;

            case("law"):
                phd_acc_rate.setText(category.getAcc_rate());
                setProgressBar(category.getAcc_rate(), progress_bar_phd);
                phd_acc_rate_title.setText("Acceptance Rate");

                masters_acc_rate_title.setVisibility(View.GONE);
                gmat_score.setVisibility(View.GONE);
                toefl_score.setVisibility(View.GONE);
                gre_score.setVisibility(View.GONE);
                avg_lang_scores_title.setVisibility(View.GONE);
                employment_figures.setVisibility(View.VISIBLE);
                lsat_score.setVisibility(View.VISIBLE);
                System.out.println("CURRENT " + category.getCat_name());
                break;

            case("medicine"):
                setProgressBar(category.getPhd_acc_rate(), progress_bar_phd);
                gmat_score.setVisibility(View.GONE);
                toefl_score.setVisibility(View.GONE);
                gre_score.setVisibility(View.GONE);
                lsat_score.setVisibility(View.GONE);
                avg_lang_scores_title.setVisibility(View.GONE);
                employment_figures.setVisibility(View.GONE);
                System.out.println("CURRENT " + category.getCat_name());
                break;

            case("undergrad"):
                phd_acc_rate.setText(category.getAcc_rate());
                setProgressBar(category.getAcc_rate(), progress_bar_phd);
                phd_acc_rate_title.setText("Acceptance Rate");

                masters_acc_rate_title.setVisibility(View.GONE);
                gre_score.setVisibility(View.GONE);
                lsat_score.setVisibility(View.GONE);
                gmat_score.setVisibility(View.GONE);
                employment_figures.setVisibility(View.GONE);
                System.out.println("CURRENT " + category.getCat_name());
                break;

            default:
                break;

        }

        bottom_view.setVisibility(View.VISIBLE);
    }

    private void setText(String str, TextView tv) {
        if(!str.equals("")){
            tv.setText(str);
        } else{
            tv.setText("-");
        }
    }

    private void setProgressBar(String value, CircularProgressIndicator p) {
        //if(!value.contains("-") || !value.equals("")){
        if(value.contains("%")){
            String acc_rate=value.replace("%", "");
            Helper.print("VALUE >>>>>>>>>>>>>>>>>>>>> " + Math.round(Float.valueOf(acc_rate)));
            p.setProgress(Math.round(Float.valueOf(acc_rate)));
            p.setVisibility(View.VISIBLE);
        } else{
            p.setVisibility(View.GONE);
        }
    }

    private void setBackgroundView(String value, View v) {
        if(value.contains("%")){
            float level = 100 * Float.valueOf(value.replace("%", ""));
            int percentage=Math.round(level);
            Helper.print("VALUE >>>>>>>>>>>>>>>>>>>>> " + Math.round(Integer.valueOf(percentage)));
            v.getBackground().setLevel(percentage);
        } else{

        }
    }

    private void setTopView(University uni, String imgUrl) {
        Glide.with(getApplicationContext())
                .load(imgUrl)
                .apply(new RequestOptions().override(100, 100)).into(img);
        name_.setText(uni.getName());
        address.setText(uni.getAddress().trim());
        uni_type.setText(uni.getUni_type());
        total_stud.setText(uni.getTotal_stud());
        total_int_stud.setText(uni.getTotal_int_stud());
        rank.setText(uni.getRank());
        top_view.setVisibility(View.VISIBLE);
    }

    private void onClicks(String name, String index) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GetUniPrograms.class);
                intent.putExtra("name", name);
                intent.putExtra("index", index);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void setView() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        img = (ImageView) findViewById(R.id.image);
        name_ = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.address);
        uni_type = (TextView) findViewById(R.id.uni_type);
        total_stud = (TextView) findViewById(R.id.total_stud);
        total_int_stud = (TextView) findViewById(R.id.total_int_stud);
        rank = (TextView) findViewById(R.id.rank);
        content = (TextView) findViewById(R.id.content);
        button = (TextView) findViewById(R.id.programButton);
        im = (AppCompatImageView) findViewById(R.id.back);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        total_enrollment = (TextView)findViewById(R.id.total_enrollment);
        male = (TextView)findViewById(R.id.male);
        female = (TextView)findViewById(R.id.female);
        int_stud = (TextView)findViewById(R.id.int_stud);
        country = (TextView)findViewById(R.id.country);
        app_fee = (TextView)findViewById(R.id.app_fee);
        app_deadline = (TextView)findViewById(R.id.app_deadline);
        masters_acc_rate = (TextView)findViewById(R.id.masters_acc_rate);
        int_app_fee = (TextView)findViewById(R.id.int_app_fee);
        int_app_deadline = (TextView)findViewById(R.id.int_app_deadline);
        phd_acc_rate = (TextView)findViewById(R.id.phd_acc_rate);
        ave_gpa = (TextView)findViewById(R.id.ave_gpa);
        toefl_mini = (TextView)findViewById(R.id.toefl_mini);
        toefl_mean = (TextView)findViewById(R.id.toefl_mean);
        ielts_mini = (TextView)findViewById(R.id.ielts_mini);
        gre_verbal = (TextView)findViewById(R.id.gre_verbal);
        gre_quant = (TextView)findViewById(R.id.gre_quant);
        gre_awa = (TextView)findViewById(R.id.gre_awa);
        tuition = (TextView)findViewById(R.id.tuition);
        living_exp = (TextView)findViewById(R.id.living_exp);
        fellowship = (TextView)findViewById(R.id.fellowship);
        teaching_assist = (TextView)findViewById(R.id.teaching_assist);
        research_assist = (TextView)findViewById(R.id.research_assist);
        financial_aid_officer = (TextView)findViewById(R.id.financial_aid_officer);
        financial_aid_officer_contact = (TextView)findViewById(R.id.financial_aid_officer_contact);
        employed = (TextView)findViewById(R.id.employed);
        bar_passage_rate = (TextView)findViewById(R.id.bar_passage_rate);
        phd_acc_rate_title = (TextView)findViewById(R.id.phd_acc_rate_title);
        cat_name = (TextView)findViewById(R.id.cat_name);
        lsat = (TextView)findViewById(R.id.lsat);

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        t4 = findViewById(R.id.t4);
        t5 = findViewById(R.id.t5);

        phd_acc_rate = (TextView)findViewById(R.id.phd_acc_rate);
        masters_acc_rate_title = (LinearLayout)findViewById(R.id.masters_acc_rate_title);
        avg_lang_scores_title = (TextView)findViewById(R.id.avg_lang_scores_title);
        gmat_score = (LinearLayout)findViewById(R.id.gmat_score);
        lsat_score = (LinearLayout)findViewById(R.id.lsat_score);
        toefl_score = (LinearLayout)findViewById(R.id.toefl_score);
        gre_score = (LinearLayout)findViewById(R.id.gre_score);
        employment_figures = (LinearLayout)findViewById(R.id.employment_figures);
        bar = (LinearLayout)findViewById(R.id.bar);

        progress_bar_masters = (CircularProgressIndicator)findViewById(R.id.progress_bar_masters);
        progress_bar_phd = (CircularProgressIndicator)findViewById(R.id.progress_bar_phd);
        progress_bar_employed = (CircularProgressIndicator)findViewById(R.id.progress_bar_employed);
        progress_bar_bar = (CircularProgressIndicator)findViewById(R.id.progress_bar_bar);

        world_view = findViewById(R.id.world_view);
        male_view = findViewById(R.id.male_view);
        female_view = findViewById(R.id.female_view);

        top_view = findViewById(R.id.top_view);
        bottom_view = findViewById(R.id.bottom_view);

    }
}
