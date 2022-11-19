package com.ustc.app.studyabroad.adapters;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ustc.app.studyabroad.Utils;
import com.ustc.app.studyabroad.models.Category;
import com.ustc.app.studyabroad.universitiesFragments.UniCategoriesFragment;

import java.util.List;

public class CategoriesAdapter extends FragmentPagerAdapter {
    List<Category> categories;
    int tabCount;

    public CategoriesAdapter(List<Category> categories, FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.tabCount = numberOfTabs;
        this.categories = categories;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return getFrag(0);
            case 1:
                return getFrag(1);
            case 2:
                return getFrag(2);
            case 3:
                return getFrag(3);
            case 4:
                return getFrag(4);
            case 5:
                return getFrag(5);
            default:
                return null;
        }
    }

    public Fragment getFrag(int i){
        UniCategoriesFragment frag = new UniCategoriesFragment();
        Bundle b = new Bundle();
        String categoryJsonString = Utils.getGsonParser().toJson(categories.get(i));
        b.putString("CATEGORY", categoryJsonString);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}