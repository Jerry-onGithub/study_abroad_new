package com.ustc.app.studyabroad.universitiesFragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.adapters.Tab3Adapter;
import com.ustc.app.studyabroad.homeActivities.ThirdActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tab3Fragment extends Fragment {
    private RecyclerView rvCategory;
    private Tab3Adapter countryAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab3, container, false);

        rvCategory = (RecyclerView) rootView.findViewById(R.id.recyclerCountry);
        rvCategory.setLayoutManager(new GridLayoutManager(getContext(), 2));


        String[] categories = getResources().getStringArray(R.array.countries);
        List<String> list = new ArrayList<>(Arrays.asList(categories));

        countryAdapter = new Tab3Adapter(list, getContext());
        rvCategory.setAdapter(countryAdapter);

        return rootView;
    }

/*    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }*/
}