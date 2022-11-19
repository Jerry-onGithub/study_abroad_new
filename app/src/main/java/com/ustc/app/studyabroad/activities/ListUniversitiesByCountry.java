package com.ustc.app.studyabroad.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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
import com.ustc.app.studyabroad.adapters.SearchViewAdapter;
import com.ustc.app.studyabroad.adapters.Tab1Adapter;
import com.ustc.app.studyabroad.homeActivities.SecondActivity;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.jsonResponse.Search;
import com.ustc.app.studyabroad.jsonResponse.Tab3Frag;
import com.ustc.app.studyabroad.models.University;

import java.util.List;

public class ListUniversitiesByCountry extends AppCompatActivity {
    private RecyclerView rvCategory;
    Tab3Frag rs = new Tab3Frag();
    private Tab1Adapter universityAdapter;
    ListView list;
    SearchViewAdapter searchViewAdapter;
    public String name;
    private LinearLayout progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tab1);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("message_subject_intent"));

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Top universities in " + name);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        progress_bar = findViewById(R.id.progress_bar);
        list = (ListView) findViewById(R.id.list_view);

        rvCategory = (RecyclerView) findViewById(R.id.recycler_view);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        System.out.println("NAME ...................... " + name);
        rs.getResponse(name, new CustomCallback() {
            @Override
            public void onSuccess(List<University> universityList) {
                progress_bar.setVisibility(View.GONE);
                universityAdapter = new Tab1Adapter("",false, universityList, getApplicationContext());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        Helper.print("NAME >>>>>>>>>>>>>>>>> "+name);
        SearchManager searchManager = (SearchManager) ListUniversitiesByCountry.this.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            searchView.setQueryHint("Search by university name");
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(ListUniversitiesByCountry.this.getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String newText) {
                    System.out.println("SEARCH " + newText);
                    Search search = new Search();
                    String query=name;
                    if(name.equals("USA")){
                        query="US";
                    }
                    search.getResponse(newText, "country", query, new CustomCallback() {
                        @Override
                        public void onSuccess(List<University> universityList) {
                            searchViewAdapter = new SearchViewAdapter(ListUniversitiesByCountry.this, universityList);
                            list.setAdapter(searchViewAdapter);
                            list.setVisibility(View.VISIBLE);
                            searchViewAdapter.filter(newText);
                            if(newText.equals("")){
                                list.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onFailure() {

                        }
                    });
                    return true;
                }
                public boolean onQueryTextSubmit(String query) {
                    return true;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent i) {
            String name= i.getStringExtra("name");
            String index= i.getStringExtra("index");
            //ToastDisplay.customMsg(getApplicationContext(), ">>>>>>>>>>>>>>>>>>>>"+name);
            String image= i.getStringExtra("image");

            Intent intent = new Intent(context, GetUniversity.class);
            intent.putExtra("name", name);
            intent.putExtra("img", image);
            intent.putExtra("index", index);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    };
}
