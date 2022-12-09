package com.ustc.app.studyabroad.homeActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ustc.app.studyabroad.FirebaseStorageHelper;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.JSONParser;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.adapters.PopularDepartmentsAdapter;
import com.ustc.app.studyabroad.adapters.RecentViewsAdapter;
import com.ustc.app.studyabroad.adapters.TopUniversitiesAdapter;
import com.ustc.app.studyabroad.adapters.ViewPagerAdapter;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.interfaces.GetPopularDCustomCallback;
import com.ustc.app.studyabroad.interfaces.GetRecentlyViewedCallback;
import com.ustc.app.studyabroad.interfaces.ListStringCallback;
import com.ustc.app.studyabroad.jsonResponse.GetGalleryImages;
import com.ustc.app.studyabroad.jsonResponse.IndexQuery;
import com.ustc.app.studyabroad.jsonResponse.PopularDepartments;
import com.ustc.app.studyabroad.jsonResponse.TopUniversities;
import com.ustc.app.studyabroad.models.PopularD;
import com.ustc.app.studyabroad.models.RecentlyViewed;
import com.ustc.app.studyabroad.models.University;
import com.ustc.app.studyabroad.userActivities.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity {
    private RecentViewsAdapter recentViewsAdapter;
    private TopUniversitiesAdapter topUniversitiesAdapter;
    private PopularDepartmentsAdapter popularDepartmentsAdapter;
    private RecyclerView recycler_view_top, recycler_view_programs, recycler_view_recent;
    private LinearLayout top, programs, recent;
    private RadioGroup radioGroup;
    private RadioButton engineering, business, law, medicine, radioButton;
    private ImageView reload;
    private RelativeLayout progress_bar;
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.material_grey_50));
        toolbar.setTitle("");
        setSupportActionBar(toolbar);*/

        checkStatus();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Helper.print(">>>>>>>>>>>>>>>>>>>> user is "+user+" .....");
        if(user==null){
            Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            setView();
            setBottomNavigationView();
            setOnClicks();
            setImageView();
            setTopUniversitiesAdapter();
            checkRadioButtons();
            setRecentlyViewedAdapter();
            //setAdapters();

        }
    }

    private void setImageView() {
        List<String> sliderItems = new ArrayList<>();
        GetGalleryImages getGalleryImages = new GetGalleryImages();
        getGalleryImages.getResponse(new ListStringCallback() {
            @Override
            public void onSuccess(List<String> data) {
                for (int i = 0; i < data.size(); i++) {
                    //Helper.print(">>>>>>>>>>>>>>>>>>>> image is "+data.get(i)+" .....");
                    sliderItems.add(data.get(i));
                }
                mViewPagerAdapter = new ViewPagerAdapter(FirstActivity.this, sliderItems);
                mViewPager.setAdapter(mViewPagerAdapter);
                mViewPager.setClipToPadding(false);
                mViewPager.setPadding(48, 0, 48, 0);
                mViewPager.setPageMargin(24);

                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        int currentPage = mViewPager.getCurrentItem();
                        if (currentPage == mViewPagerAdapter.getCount()-1) {
                            currentPage = 0;
                        } else {
                            currentPage++;
                        }
                        mViewPager.setCurrentItem(currentPage, true);
                        handler.postDelayed(this, 5000);
                    }
                };
                handler.postDelayed(Update, 500);
            }
            @Override
            public void onFailure(String msg) {
            }
        });
    }

    private void checkStatus() {
        if(Helper.isNetworkAvailable(getApplicationContext())){
            Helper.print(">>>>>>>>>>>>>>>>>>>>>>> network IS available ...............");
        } else {
            Helper.print(">>>>>>>>>>>>>>>>>>>>>>> network NOT available ...............");
            Snackbar snackbar = Snackbar.make(findViewById(R.id.snack_bar), "No Internet Connection", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            snackbar.setTextColor(getResources().getColor(R.color.white));
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.material_deep_teal_500));
            snackbar.setDuration(8000);
            snackbar.show();

        }
    }

    private void setOnClicks() {
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                setPopularDepartmentsAdapter(radioButton.getText().toString().toLowerCase());
            }
        });
    }

    private void setBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        bottomNavigationView.setSelectedItemId(R.id.one);
        Helper.setBottomNavigationView(bottomNavigationView, FirstActivity.this);
    }

    private void setTopUniversitiesAdapter() {
        TopUniversities rs = new TopUniversities();
        rs.getResponse(new CustomCallback(){
            @Override
            public void onSuccess(List<University> universityList){
                //Helper.print(">>>>>>>>>>>>>>>>>>>>>>> size of universityList ..............." + universityList.size());
                //progress_bar_top.setVisibility(View.GONE);
                topUniversitiesAdapter = new TopUniversitiesAdapter(universityList, getApplicationContext());
                recycler_view_top.setAdapter(topUniversitiesAdapter);
                recycler_view_top.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                SnapHelper snapHelper = new PagerSnapHelper();
                snapHelper.attachToRecyclerView(recycler_view_top);
                //recycler_view_top.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure() {
                ToastDisplay.connErrorMsg(getApplicationContext());
            }
        });
    }

    private void setPopularDepartmentsAdapter(String r){
        PopularDepartments pd = new PopularDepartments();
        Helper.print(">>>>>>>>>>>>>>>>>>>> r ... " + r);
        pd.getResponse(r, new GetPopularDCustomCallback(){
            @Override
            public void onSuccess(List<PopularD> popularDS){
                Helper.print(">>>>>>>>>>>>>>>>>>>> popularDS "+popularDS.size()+" .....");
                Helper.print(">>>>>>>>>>>>>>>>>>>> uni name ... "+popularDS.get(0).getName()+" ...cat name... "+popularDS.get(0).getCategory().getCat_name()+" ...content... "+popularDS.get(0).getCategory().getCat_content());
                popularDepartmentsAdapter = new PopularDepartmentsAdapter(r, popularDS, FirstActivity.this);
                recycler_view_programs.setAdapter(popularDepartmentsAdapter);

                recycler_view_programs.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                SnapHelper snapHelper = new PagerSnapHelper();
                recycler_view_programs.setOnFlingListener(null);
                snapHelper.attachToRecyclerView(recycler_view_programs);
            }
            @Override
            public void onFailure() {
                ToastDisplay.connErrorMsg(getApplicationContext());
            }
        });
    }

    private void setRecentlyViewedAdapter(){
        FirebaseStorageHelper.getRecentlyViewed(new GetRecentlyViewedCallback(){
            @Override
            public void onSuccess(List<RecentlyViewed> recentlyViewed){
                Helper.print(">>>>>>>>>>>>>>>>>>>> recentlyViewed length is "+recentlyViewed.size()+" .....");

                List<String> uni_indexes = new ArrayList<>();
                for(int i=0; i<recentlyViewed.size(); i++){
                    String idx = recentlyViewed.get(i).getUni_index();
                    uni_indexes.add(idx);
                }
                IndexQuery indexQuery = new IndexQuery();
                indexQuery.getResponse(uni_indexes, "uni", new CustomCallback() {
                    @Override
                    public void onSuccess(List<University> universityList) {
                        //progress_bar_recent.setVisibility(View.GONE);
                        recentViewsAdapter = new RecentViewsAdapter(recentlyViewed, universityList, getApplicationContext());
                        recycler_view_recent.setAdapter(recentViewsAdapter);
                        recycler_view_recent.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                        SnapHelper snapHelper = new PagerSnapHelper();
                        recycler_view_recent.setOnFlingListener(null);
                        snapHelper.attachToRecyclerView(recycler_view_recent);
                        setAdapters();
                    }
                    @Override
                    public void onFailure() {

                    }
                });
            }
            @Override
            public void onFailure(String str) {

            }
        });
    }

    private void setAdapters() {
        progress_bar.setVisibility(View.GONE);
        recent.setVisibility(View.VISIBLE);
        top.setVisibility(View.VISIBLE);
        programs.setVisibility(View.VISIBLE);
        //viewPager2.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
    }

    private void checkRadioButtons(){
        setPopularDepartmentsAdapter("engineering");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (engineering.isChecked()) {
                    setPopularDepartmentsAdapter("engineering");
                } else if (business.isChecked()) {
                    setPopularDepartmentsAdapter("business");
                } else if (law.isChecked()) {
                    setPopularDepartmentsAdapter("law");
                } else if (medicine.isChecked()) {
                    setPopularDepartmentsAdapter("medicine");
                }
            }
        });
    }

    @SuppressLint("WrongViewCast")
    private void setView() {
        recycler_view_top = findViewById(R.id.recycler_view_top);
        recycler_view_recent = findViewById(R.id.recycler_view_recent);
        recycler_view_programs = findViewById(R.id.recycler_view_programs);
        radioGroup = findViewById(R.id.radio_group);
        engineering = findViewById(R.id.engineering);
        business = findViewById(R.id.business);
        law = findViewById(R.id.law);
        medicine = findViewById(R.id.medicine);
        reload = findViewById(R.id.reload);
        top = findViewById(R.id.top);
        recent = findViewById(R.id.recent);
        programs = findViewById(R.id.programs);
        progress_bar = findViewById(R.id.progress_bar);
        mViewPager = (ViewPager)findViewById(R.id.viewPagerMain);
    }

/*    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
    }*/
}