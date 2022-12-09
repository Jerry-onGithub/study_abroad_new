package com.ustc.app.studyabroad.homeActivities;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.activities.GetSearchableUniversities;
import com.ustc.app.studyabroad.activities.GetUniversity;
import com.ustc.app.studyabroad.activities.ListUniversitiesByProgram;
import com.ustc.app.studyabroad.adapters.SearchViewAdapter;
import com.ustc.app.studyabroad.adapters.TabLayoutAdapter;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.jsonResponse.Search;
import com.ustc.app.studyabroad.models.University;
import java.util.List;

public class ThirdActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    TabLayout tabLayout;
    ViewPager viewPager;
    static SearchView searchView;
    BottomNavigationView bottomNavigationView;
    ListView list;
    SearchViewAdapter searchViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("message_subject_intent"));

        bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        bottomNavigationView.setSelectedItemId(R.id.three);
        Helper.setBottomNavigationView(bottomNavigationView, ThirdActivity.this);

        list = (ListView) findViewById(R.id.list_view);
        searchView = (SearchView) findViewById(R.id.search);
        searchView.setQueryHint("Search by university name");
        searchView.setOnQueryTextListener(this);

        setTabs();

    }

    private void setTabs() {
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        tabLayout.addTab(tabLayout.newTab().setText("University"));
        tabLayout.addTab(tabLayout.newTab().setText("Program"));
        tabLayout.addTab(tabLayout.newTab().setText("Country"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        TabLayoutAdapter adapter = new TabLayoutAdapter("", this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 2) {
                    searchView.setVisibility(View.GONE);
                }
                if (tab.getPosition() == 1) {
                    searchView.setVisibility(View.VISIBLE);
                    searchView.setQueryHint("Search by program name");
                }
                if (tab.getPosition() == 0) {
                    searchView.setVisibility(View.VISIBLE);
                    searchView.setQueryHint("Search by university name");
                }
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Helper.print("SEARCH " + newText);
        int tab = tabLayout.getSelectedTabPosition();
        Helper.print("tab: " + String.valueOf(tabLayout.getSelectedTabPosition()));
        Search search = new Search();
        String query = null;
        if(tab==0){
            query="uni_name";
        } else if(tab==1){
            query="prog_name";
        }
        if(newText.equals("")){
            list.setVisibility(View.GONE);
        }
        search.getResponse(newText, query, "", new CustomCallback() {
            @Override
            public void onSuccess(List<University> universityList) {
                Helper.print("RESULT >>>>>>> success");
                Helper.print("RESULT >>>>>>> SIZE " + universityList.size()); // + "FIRST: " + universityList.get(0));
                searchViewAdapter = new SearchViewAdapter(ThirdActivity.this, universityList);
                list.setAdapter(searchViewAdapter);
                list.setVisibility(View.VISIBLE);
                searchViewAdapter.filter(newText);
                if(newText.equals("")){
                    list.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure() {
                Helper.print("RESULT >>>>>>> failed");
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
            String name= i.getStringExtra("name");
            String index= i.getStringExtra("index");

            if(tabLayout.getSelectedTabPosition()==0){
                String image= i.getStringExtra("image");

                Intent intent = new Intent(context, GetUniversity.class);
                intent.putExtra("name", name);
                intent.putExtra("img", image);
                intent.putExtra("index", index);
                //Helper.print(university.getIndex());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else if(tabLayout.getSelectedTabPosition()==1){
                Intent intent = new Intent(context, ListUniversitiesByProgram.class);
                intent.putExtra("name", name);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    };
}

