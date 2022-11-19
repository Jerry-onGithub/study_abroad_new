package com.ustc.app.studyabroad.homeActivities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ustc.app.studyabroad.FirebaseStorageHelper;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.activities.GetUniversity;
import com.ustc.app.studyabroad.adapters.AdmitAndRejectsAdapter;
import com.ustc.app.studyabroad.adapters.AdmitsAndRejectsTopViewAdapter;
import com.ustc.app.studyabroad.adapters.DecisionsAdapter;
import com.ustc.app.studyabroad.adapters.PostsAdapter;
import com.ustc.app.studyabroad.adapters.SearchViewAdapter;
import com.ustc.app.studyabroad.adapters.Tab1Adapter;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.interfaces.GetDecisionsCallback;
import com.ustc.app.studyabroad.interfaces.GetPostsCallback;
import com.ustc.app.studyabroad.jsonResponse.Search;
import com.ustc.app.studyabroad.models.University;
import com.ustc.app.studyabroad.userActivities.AddDecisionsActivity;
import com.ustc.app.studyabroad.userActivities.EditProfileActivity;
import com.ustc.app.studyabroad.userModels.Decisions;
import com.ustc.app.studyabroad.userModels.Post;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FifthActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private RecyclerView recyclerTopUniversitiesTop, recyclerAdmitRejects, recyclerSavedDecisions;
    private SearchView searchView;
    private TextView total_dec, total_saved_dec, search_btn, total_dec_title, total_saved_dec_title;
    private LinearLayout total_decisions, total_saved_decisions;
    private BottomNavigationView bottomNavigationView;
    private ListView list;
    private SearchViewAdapter searchViewAdapter;
    private FloatingActionButton fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("message_subject_intent"));

        searchView = (SearchView) findViewById(R.id.search);
        searchView.setOnQueryTextListener(this);

        setView();
        bottomNavigationView.setSelectedItemId(R.id.five);
        Helper.setBottomNavigationView(bottomNavigationView, FifthActivity.this);
        setColor(total_dec, total_dec_title, total_saved_dec_title, total_saved_dec);
        //setRecyclerTopUniversitiesTop();
        setTotalSavedDecisionsAdapter();
        setTotalDecAdapter();
        onClicks();

    }

    private void setRecyclerTopUniversitiesTop() {
        recyclerTopUniversitiesTop = findViewById(R.id.search_selection_recycler);
        recyclerTopUniversitiesTop.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        recyclerTopUniversitiesTop.setAdapter(new AdmitsAndRejectsTopViewAdapter());
    }

    private void onClicks() {
        total_saved_decisions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColor(total_saved_dec_title, total_saved_dec, total_dec, total_dec_title);
                recyclerSavedDecisions.setVisibility(View.VISIBLE);
                recyclerAdmitRejects.setVisibility(View.GONE);
            }
        });
        total_decisions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColor(total_dec, total_dec_title, total_saved_dec_title, total_saved_dec);
                recyclerAdmitRejects.setVisibility(View.VISIBLE);
                recyclerSavedDecisions.setVisibility(View.GONE);
            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorageHelper.getDecisions("decisions", null, new GetDecisionsCallback() {
                    @Override
                    public void onSuccess(List<Decisions> decisions) {
                        Intent intentAddDecisions = new Intent(getApplicationContext(), AddDecisionsActivity.class);
                        intentAddDecisions.putExtra("LIST", (Serializable) decisions);
                        startActivity(intentAddDecisions);
                    }
                    @Override
                    public void onFailure(String m) {
                        if(m.equals("f")){
                        } else{
                            List<Decisions> decisions = new ArrayList<>();
                            Intent intentAddDecisions = new Intent(getApplicationContext(), AddDecisionsActivity.class);
                            intentAddDecisions.putExtra("LIST", (Serializable) decisions);
                            startActivity(intentAddDecisions);
                        }
                    }
                });
            }
        });
    }

    private void setColor(TextView one, TextView two, TextView one_, TextView two_) {
        one.setTextColor(getResources().getColor(R.color.material_deep_teal_500));
        two.setTextColor(getResources().getColor(R.color.material_deep_teal_500));
        one_.setTextColor(getResources().getColor(R.color.black));
        two_.setTextColor(getResources().getColor(R.color.black));
    }

    private void setTotalSavedDecisionsAdapter() {
        FirebaseStorageHelper.getDecisions("saved_decisions", "s", new GetDecisionsCallback() {
            @Override
            public void onSuccess(List<Decisions> decisions) {
                recyclerSavedDecisions.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerSavedDecisions.setAdapter(new AdmitAndRejectsAdapter(decisions, getApplicationContext()));
                setTotalSavedDec(decisions.size());
            }
            @Override
            public void onFailure(String m) {
                if(m.equals("f")){
                    ToastDisplay.customMsg(getApplicationContext(), "Data was unable to be fetched.");
                } else {
                    setTotalSavedDec(0);
                }
            }
        });
    }

    private void setTotalSavedDec(int size) {
        total_saved_dec.setText("(" + size + ")");
    }

    private void setTotalDecAdapter() {
        ProgressDialog progressDialog = Helper.createProgressDialog(FifthActivity.this);
        progressDialog.show();
        FirebaseStorageHelper.getDecisions("decisions", "ar", new GetDecisionsCallback() {
            @Override
            public void onSuccess(List<Decisions> decisions) {
                recyclerAdmitRejects.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerAdmitRejects.setAdapter(new AdmitAndRejectsAdapter(decisions, getApplicationContext()));
                setTotalDec(decisions.size());
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(String m) {
                if(m.equals("f")){
                    ToastDisplay.customMsg(getApplicationContext(), "Data was unable to be fetched.");
                }
                progressDialog.dismiss();
            }
        });
    }

    private void setTotalDec(int size) {
        total_dec.setText("(" + size + ")");
    }

    private void setView() {
        bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        searchView = findViewById(R.id.search);
        total_dec = findViewById(R.id.total_dec);
        total_saved_dec = findViewById(R.id.total_saved_dec);
        total_decisions = findViewById(R.id.total_decisions);
        total_saved_decisions = findViewById(R.id.total_saved_decisions);
        total_dec_title = findViewById(R.id.total_dec_title);
        total_saved_dec_title = findViewById(R.id.total_saved_dec_title);
        search_btn = findViewById(R.id.search_btn);
        recyclerAdmitRejects = findViewById(R.id.recyclerAdmitRejects);
        recyclerSavedDecisions = findViewById(R.id.recyclerSavedDecisions);

        search_btn.setVisibility(View.GONE);
        list = (ListView) findViewById(R.id.list_view);
        fb = findViewById(R.id.add_dec);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Helper.print("SEARCH " + newText);
        Search search = new Search();
        if(newText.equals("")){
            list.setVisibility(View.GONE);
            setTotalDecAdapter();
        }
        search.getResponse(newText, "uni_name", "", new CustomCallback() {
            @Override
            public void onSuccess(List<University> universityList) {
                searchViewAdapter = new SearchViewAdapter(FifthActivity.this, universityList);
                list.setAdapter(searchViewAdapter);
                list.setVisibility(View.VISIBLE);
                searchViewAdapter.filter(newText);
                if(newText.equals("")){
                    list.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure() {
                if(newText.equals("")){
                    list.setVisibility(View.GONE);
                }
            }
        });
        return false;
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent i) {
            list.setVisibility(View.GONE);
            String name= i.getStringExtra("name");
            String index= i.getStringExtra("index");
            String image= i.getStringExtra("image");

            ProgressDialog progressDialog = Helper.createProgressDialog(FifthActivity.this);
            progressDialog.show();
            FirebaseStorageHelper.searchDecisions(name, new GetDecisionsCallback() {
                @Override
                public void onSuccess(List<Decisions> decisions) {
                    if(decisions.size()>0) {
                        progressDialog.dismiss();
                        recyclerAdmitRejects.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerAdmitRejects.setAdapter(new AdmitAndRejectsAdapter(decisions, getApplicationContext()));
                        setTotalDec(decisions.size());
                    } else {
                        ToastDisplay.customMsg(getApplicationContext(), "No search result found");
                        progressDialog.dismiss();
                    }
                }
                @Override
                public void onFailure(String er) {
                    if(er.equals("f")) {
                        ToastDisplay.customMsg(getApplicationContext(), "Failed to load result");
                        progressDialog.dismiss();
                    } else{
                        progressDialog.dismiss();
                        ToastDisplay.customMsg(getApplicationContext(), "No search result found");
                    }
                }
            });
        }
    };
}
