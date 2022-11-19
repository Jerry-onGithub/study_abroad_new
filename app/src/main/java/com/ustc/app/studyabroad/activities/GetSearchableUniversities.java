package com.ustc.app.studyabroad.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.adapters.ProgramsAdapter;
import com.ustc.app.studyabroad.adapters.SearchViewAdapter;
import com.ustc.app.studyabroad.adapters.Tab1Adapter;
import com.ustc.app.studyabroad.homeActivities.ThirdActivity;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.interfaces.CustomCallbackGetProg;
import com.ustc.app.studyabroad.jsonResponse.GetPrograms;
import com.ustc.app.studyabroad.jsonResponse.Search;
import com.ustc.app.studyabroad.jsonResponse.Tab1Frag;
import com.ustc.app.studyabroad.models.Program;
import com.ustc.app.studyabroad.models.University;

import java.util.List;

public class GetSearchableUniversities extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private RecyclerView rvCategory;
    private Tab1Adapter universityAdapter;
    static SearchView searchView;
    ListView list;
    SearchViewAdapter searchViewAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("message_subject_intent"));

        list = (ListView) findViewById(R.id.list_view);
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setQueryHint("Search by university name");
        searchView.setOnQueryTextListener(this);

        rvCategory = (RecyclerView) findViewById(R.id.recycler_view);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        ProgressDialog progressDialog = Helper.createProgressDialog(GetSearchableUniversities.this);
        progressDialog.show();
        Tab1Frag rs = new Tab1Frag();
        rs.getResponse(new CustomCallback(){
            @Override
            public void onSuccess(List<University> universityList){
                universityAdapter = new Tab1Adapter("search_list", false, universityList, getApplicationContext());
                rvCategory.setAdapter(universityAdapter);
                progressDialog.dismiss();
            }
            @Override
            public void onFailure() {
                progressDialog.dismiss();
                ToastDisplay.connErrorMsg(getApplicationContext());
            }
        });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name= intent.getStringExtra("name");

            Intent data = new Intent();
            data.putExtra("value", name);
            GetSearchableUniversities.this.setResult(123, data);
            finish();
        }
    };

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Helper.print("SEARCH " + newText);

        Search search = new Search();
        search.getResponse(newText, "uni_name", "", new CustomCallback() {
            @Override
            public void onSuccess(List<University> universityList) {
                searchViewAdapter = new SearchViewAdapter(GetSearchableUniversities.this, universityList);
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
}
