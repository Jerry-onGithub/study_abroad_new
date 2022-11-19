package com.ustc.app.studyabroad.profileFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.ustc.app.studyabroad.FirebaseStorageHelper;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.interfaces.GetProfileCallback;
import com.ustc.app.studyabroad.userActivities.EditProfileActivity;
import com.ustc.app.studyabroad.userModels.Profile;

public class ProfileFrag extends Fragment {
    View view;
    TextView gender, address, birthday, edu_country, edu_place_name, cgpa, research_paper,
            work_exp, target_uni_name, target_major, interested_term;
    TextView add_work_exp, add_proj, add_research_paper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        setView(view);
        getData();
        setOnClick();

        return view;
    }

    private void getData() {
        ProgressDialog progressDialog = Helper.createProgressDialog(getContext());
        progressDialog.show();
        FirebaseStorageHelper.getProfile(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GetProfileCallback() {
            @Override
            public void onSuccess(Profile profile) {
                setData(profile);
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(String er) {
                if(er.equals("f")) {
                    ToastDisplay.customMsg(getContext(), "Data was unable to be fetched.");
                } else{
                    setNullData();
                }
                progressDialog.dismiss();
            }
        });
    }

    private void setData(Profile profile) {
        if(profile.getGender().equals("Female")){
            gender.setCompoundDrawablesWithIntrinsicBounds(R.drawable.femenine, 0, 0, 0);
        } else if(profile.getGender().equals("Male")){
            gender.setCompoundDrawablesWithIntrinsicBounds(R.drawable.male_gender, 0, 0, 0);
        }

        cgpa.setText(profile.getCgpa());
        address.setText(profile.getAddress());
        gender.setText(profile.getGender());
        birthday.setText(profile.getBirthday());
        edu_country.setText(profile.getEdu_country());
        target_uni_name.setText(profile.getTarget_uni_name());
        target_major.setText(profile.getTarget_major());
        interested_term.setText(profile.getInterested_term());
        edu_place_name.setText(profile.getEdu_place_name());
        research_paper.setText(profile.getResearch_paper());
        work_exp.setText(profile.getWork_exp() + " months");
        //setMargin(cgpa, target_major, interested_term, research_paper, work_exp, edu_country, target_uni_name);
    }

    private  void setMargin(TextView tv1, TextView tv2, TextView tv3, TextView tv4, TextView tv5, TextView tv6,/* TextView tv7, */TextView tv8){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(85,0,0,0);
        tv1.setLayoutParams(params);
        tv2.setLayoutParams(params);
        tv3.setLayoutParams(params);
        tv4.setLayoutParams(params);
        tv5.setLayoutParams(params);
        tv6.setLayoutParams(params);
        //tv7.setLayoutParams(params);
        tv8.setLayoutParams(params);
    }

    private void setNullData() {
        cgpa.setText("cgpa");
        setOnClickEdit(cgpa, true);
        address.setText("address");
        setOnClickEdit(address, false);
        gender.setText("gender");
        setOnClickEdit(gender, false);
        birthday.setText("birthday");
        setOnClickEdit(birthday, false);
        edu_country.setText("country");
        setOnClickEdit(edu_country, false);
        target_uni_name.setText("target university");
        setOnClickEdit(target_uni_name, true);
        target_major.setText("target major");
        setOnClickEdit(target_major, true);
        interested_term.setText("interested term");
        setOnClickEdit(interested_term, true);
        edu_place_name.setText("institution place");
        setOnClickEdit(edu_place_name, false);
        research_paper.setText("research paper");
        setOnClickEdit(research_paper, true);
        work_exp.setText("work experience");
        setOnClickEdit(work_exp, true);

    }

    private void setOnClickEdit(TextView edit, boolean t) {
        edit.setTypeface(null, Typeface.ITALIC);
        //edit.setTextColor(getResources().getColor(R.color.material_deep_teal_500));
        edit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0);
        if(t==true){
            edit.setCompoundDrawablePadding(getResources().getDimensionPixelOffset(R.dimen.padding1));
            //edit.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0);
        }
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEditProfile = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intentEditProfile);
            }
        });
    }


    private void setOnClick() {
        add_work_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        add_proj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        add_research_paper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setView(View view) {
        cgpa = view.findViewById(R.id.cgpa);
        address = view.findViewById(R.id.address);
        gender = view.findViewById(R.id.gender);
        birthday = view.findViewById(R.id.birthday);
        edu_country = view.findViewById(R.id.edu_country);
        target_uni_name = view.findViewById(R.id.target_uni_name);
        target_major = view.findViewById(R.id.target_major);
        interested_term = view.findViewById(R.id.interested_term);
        edu_place_name = view.findViewById(R.id.edu_place_name);
        research_paper = view.findViewById(R.id.research_paper);
        work_exp = view.findViewById(R.id.work_exp);
        add_work_exp = view.findViewById(R.id.add_work_exp);
        add_proj = view.findViewById(R.id.add_proj);
        add_research_paper = view.findViewById(R.id.add_research_paper);
    }
}