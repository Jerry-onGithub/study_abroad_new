package com.ustc.app.studyabroad.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.adapters.ProgramsAdapter;
import com.ustc.app.studyabroad.interfaces.CustomCallbackGetProg;
import com.ustc.app.studyabroad.jsonResponse.GetPrograms;
import com.ustc.app.studyabroad.models.Program;

import java.util.List;

public class GetUniPrograms extends AppCompatActivity {
    private RecyclerView rvCategory;
    private ProgramsAdapter programsAdapter;
    private RelativeLayout progress_bar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_uni_programs);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        String index = intent.getExtras().getString("index");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Programs in " + name);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        rvCategory = (RecyclerView) findViewById(R.id.recycler_view);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        progress_bar = findViewById(R.id.progress_bar);

        GetPrograms rs = new GetPrograms();
        rs.getResponse(name, index, new CustomCallbackGetProg(){
            @Override
            public void onSuccess(List<Program> programList){
                programsAdapter = new ProgramsAdapter(programList, getApplicationContext());
                rvCategory.setAdapter(programsAdapter);
                progress_bar.setVisibility(View.GONE);
                rvCategory.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure() {

            }
        });
    }
}
