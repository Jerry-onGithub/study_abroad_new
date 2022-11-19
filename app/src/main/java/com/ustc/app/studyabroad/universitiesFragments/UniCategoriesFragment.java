package com.ustc.app.studyabroad.universitiesFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.Utils;
import com.ustc.app.studyabroad.models.Category;

public class UniCategoriesFragment extends Fragment {

    private Category category;

    TextView total_enrollment;
    TextView male;
    TextView female;
    TextView int_stud;
    TextView country;
    TextView app_fee;
    TextView app_deadline;
    TextView masters_acc_rate;
    TextView int_app_fee;
    TextView int_app_deadline;
    TextView phd_acc_rate;
    TextView ave_gpa;
    TextView toefl_mini;
    TextView toefl_mean;
    TextView ielts_mini;
    TextView gre_verbal;
    TextView gre_quant;
    TextView gre_awa;
    TextView tuition;
    TextView living_exp;
    TextView fellowship;
    TextView teaching_assist;
    TextView research_assist;
    TextView financial_aid_officer;
    TextView financial_aid_officer_contact;
    TextView bar_passage_rate;
    TextView employed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.uni_categories_fragment, container, false);

        Bundle args = getArguments();
        String categoryJsonString = args.getString("CATEGORY");
        category= Utils.getGsonParser().fromJson(categoryJsonString, Category.class);

        total_enrollment = (TextView) rootView.findViewById(R.id.total_enrollment);
        male = (TextView) rootView.findViewById(R.id.male);
        female = (TextView) rootView.findViewById(R.id.female);
        int_stud = (TextView) rootView.findViewById(R.id.int_stud);
        country = (TextView) rootView.findViewById(R.id.country);
        app_fee = (TextView) rootView.findViewById(R.id.app_fee);
        app_deadline = (TextView) rootView.findViewById(R.id.app_deadline);
        masters_acc_rate = (TextView) rootView.findViewById(R.id.masters_acc_rate);
        int_app_fee = (TextView) rootView.findViewById(R.id.int_app_fee);
        int_app_deadline = (TextView) rootView.findViewById(R.id.int_app_deadline);
        phd_acc_rate = (TextView) rootView.findViewById(R.id.phd_acc_rate);
        ave_gpa = (TextView) rootView.findViewById(R.id.ave_gpa);
        toefl_mini = (TextView) rootView.findViewById(R.id.toefl_mini);
        toefl_mean = (TextView) rootView.findViewById(R.id.toefl_mean);
        ielts_mini = (TextView) rootView.findViewById(R.id.ielts_mini);
        gre_verbal = (TextView) rootView.findViewById(R.id.gre_verbal);
        gre_quant = (TextView) rootView.findViewById(R.id.gre_quant);
        gre_awa = (TextView) rootView.findViewById(R.id.gre_awa);
        tuition = (TextView) rootView.findViewById(R.id.tuition);
        living_exp = (TextView) rootView.findViewById(R.id.living_exp);
        fellowship = (TextView) rootView.findViewById(R.id.fellowship);
        teaching_assist = (TextView) rootView.findViewById(R.id.teaching_assist);
        research_assist = (TextView) rootView.findViewById(R.id.research_assist);
        financial_aid_officer = (TextView) rootView.findViewById(R.id.financial_aid_officer);
        financial_aid_officer_contact = (TextView) rootView.findViewById(R.id.financial_aid_officer_contact);
        employed = (TextView) rootView.findViewById(R.id.employed);
        bar_passage_rate = (TextView) rootView.findViewById(R.id.bar_passage_rate);

        total_enrollment.setText(category.getTotal_enrollment());
        male.setText(category.getMale());
        female.setText(category.getFemale());
        int_stud.setText(category.getInt_stud());
        country.setText("US");
        app_fee.setText(category.getUs_app_fee());
        app_deadline.setText(category.getUs_app_deadline());
        masters_acc_rate.setText(category.getMasters_acc_rate());
        int_app_fee.setText(category.getInt_app_fee());
        int_app_deadline.setText(category.getInt_app_deadline());
        phd_acc_rate.setText(category.getPhd_acc_rate());
        ave_gpa.setText(category.getAve_gpa());
        toefl_mini.setText(category.getToefl_mini());
        toefl_mean.setText(category.getToefl_mean());
        ielts_mini.setText(category.getIelts_mini());
        gre_verbal.setText(category.getGre_verbal());
        gre_quant.setText(category.getGre_quant());
        gre_awa.setText(category.getGre_awa());
        tuition.setText(category.getTuition());
        living_exp.setText(category.getLiving_exp());
        fellowship.setText(category.getFellowship());
        teaching_assist.setText(category.getTeaching_assist());
        research_assist.setText(category.getResearch_assist());
        financial_aid_officer.setText(category.getFinancial_aid_officer());
        financial_aid_officer_contact.setText(category.getFinancial_aid_officer_contact());
        employed.setText(category.getEmployed());
        bar_passage_rate.setText(category.getBar_passage_rate());

        TextView phd_acc_rate = (TextView) rootView.findViewById(R.id.phd_acc_rate);
        LinearLayout masters_acc_rate_title = (LinearLayout) rootView.findViewById(R.id.masters_acc_rate_title);
        TextView avg_lang_scores_title = (TextView) rootView.findViewById(R.id.avg_lang_scores_title);
        LinearLayout gmat_score = (LinearLayout) rootView.findViewById(R.id.gmat_score);
        LinearLayout lsat_score = (LinearLayout) rootView.findViewById(R.id.lsat_score);
        LinearLayout toefl_score = (LinearLayout) rootView.findViewById(R.id.toefl_score);
        LinearLayout gre_score = (LinearLayout) rootView.findViewById(R.id.gre_score);
        LinearLayout employment_figures = (LinearLayout) rootView.findViewById(R.id.employment_figures);
        LinearLayout bar = (LinearLayout) rootView.findViewById(R.id.bar);

        switch (category.getCat_name()){
            case("engineering"):
                gmat_score.setVisibility(View.GONE);
                lsat_score.setVisibility(View.GONE);
                employment_figures.setVisibility(View.GONE);
                System.out.println("CURRENT " + category.getCat_name());

            case("business"):
                gre_score.setVisibility(View.GONE);
                lsat_score.setVisibility(View.GONE);
                bar.setVisibility(View.GONE);
                System.out.println("CURRENT " + category.getCat_name());

            case("law"):
                phd_acc_rate.setText(category.getAcc_rate());
                masters_acc_rate_title.setVisibility(View.GONE);
                gmat_score.setVisibility(View.GONE);
                toefl_score.setVisibility(View.GONE);
                gre_score.setVisibility(View.GONE);
                avg_lang_scores_title.setVisibility(View.GONE);
                System.out.println("CURRENT " + category.getCat_name());

            case("medicine"):
                gmat_score.setVisibility(View.GONE);
                toefl_score.setVisibility(View.GONE);
                gre_score.setVisibility(View.GONE);
                lsat_score.setVisibility(View.GONE);
                avg_lang_scores_title.setVisibility(View.GONE);
                employment_figures.setVisibility(View.GONE);
                System.out.println("CURRENT " + category.getCat_name());

            case("undergrad"):
                phd_acc_rate.setText(category.getAcc_rate());
                masters_acc_rate_title.setVisibility(View.GONE);
                gre_score.setVisibility(View.GONE);
                lsat_score.setVisibility(View.GONE);
                gmat_score.setVisibility(View.GONE);
                employment_figures.setVisibility(View.GONE);
                System.out.println("CURRENT " + category.getCat_name());

        }
        return rootView;
    }
}