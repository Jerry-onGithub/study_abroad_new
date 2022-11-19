package com.ustc.app.studyabroad.universitiesFragments;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.adapters.Tab1Adapter;
import com.ustc.app.studyabroad.jsonResponse.Tab1Frag;
import com.ustc.app.studyabroad.interfaces.CustomCallback;
import com.ustc.app.studyabroad.models.University;
import java.util.List;

public class Tab1Fragment extends Fragment {
    private RecyclerView rvCategory;
    private Tab1Adapter universityAdapter;
    private LinearLayout progress_bar;

    Tab1Frag rs = new Tab1Frag();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab1, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        progress_bar = rootView.findViewById(R.id.progress_bar);
        rvCategory = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(getContext()));

        rs.getResponse(new CustomCallback(){
            @Override
            public void onSuccess(List<University> universityList){
                progress_bar.setVisibility(View.GONE);
                universityAdapter = new Tab1Adapter("", false, universityList, getContext());
                rvCategory.setAdapter(universityAdapter);
                rvCategory.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure() {
                progress_bar.setVisibility(View.GONE);
                ToastDisplay.connErrorMsg(getActivity());
            }
        });
        return rootView;
    }
}