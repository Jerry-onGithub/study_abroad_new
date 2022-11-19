package com.ustc.app.studyabroad.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.adapters.Tab1Adapter;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.jsonResponse.Tab2Frag_2;
import com.ustc.app.studyabroad.models.University;

import java.util.List;

public class ListUniversitiesByProgram extends AppCompatActivity {
    private RecyclerView rvCategory;
    Tab2Frag_2 rs = new Tab2Frag_2();
    private Tab1Adapter universityAdapter;
    private LinearLayout progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tab1);
        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        progress_bar = findViewById(R.id.progress_bar);
        rvCategory = (RecyclerView) findViewById(R.id.recycler_view);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        System.out.println("NAME ...................... " + name);
        rs.getResponse(name, new CustomCallback() {
            @Override
            public void onSuccess(List<University> universityList) {
                System.out.println(" SIZE >>>>>>>>>>>>> ...................... " + universityList.size());

                progress_bar.setVisibility(View.GONE);
                universityAdapter = new Tab1Adapter("",true, universityList, getApplicationContext());
                rvCategory.setAdapter(universityAdapter);
                rvCategory.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure() {
                progress_bar.setVisibility(View.GONE);
                ToastDisplay.connErrorMsg(getApplicationContext());
            }
        });
    }

}
