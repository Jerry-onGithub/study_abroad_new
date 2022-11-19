package com.ustc.app.studyabroad.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ustc.app.studyabroad.activities.ListUniversitiesByCountry;
import com.ustc.app.studyabroad.profileFragments.DecisionsFrag;
import com.ustc.app.studyabroad.profileFragments.ProfileFrag;
import com.ustc.app.studyabroad.profileFragments.ResumeFrag;
import com.ustc.app.studyabroad.universitiesFragments.Tab1Fragment;
import com.ustc.app.studyabroad.universitiesFragments.Tab2Fragment;
import com.ustc.app.studyabroad.universitiesFragments.Tab3Fragment;

public class TabLayoutAdapter extends FragmentPagerAdapter {

    Context mContext;
    int mTotalTabs;
    String tab;

    public TabLayoutAdapter(String tab_, Context context , FragmentManager fragmentManager , int totalTabs) {
        super(fragmentManager);
        mContext = context;
        mTotalTabs = totalTabs;
        tab = tab_;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(tab == "fourth"){
            switch (position) {
                case 0:
                    return new ProfileFrag();
                case 1:
                    return new DecisionsFrag();
                case 2:
                    return new ResumeFrag();
                default:
                    return null;

            }
        }
        else{
            switch (position) {
                case 0:
                    return new Tab1Fragment();
                case 1:
                    return new Tab2Fragment();
                case 2:
                    return new Tab3Fragment();
                default:
                    return null;

            }
        }
    }

    @Override
    public int getCount() {
        return mTotalTabs;
    }
}