package com.ustc.app.studyabroad.universitiesFragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.adapters.Tab2Adapter;
import com.ustc.app.studyabroad.interfaces.CustomCallbackTab2Frag;
import com.ustc.app.studyabroad.jsonResponse.Tab2Frag;
import com.ustc.app.studyabroad.models.Fields;

public class Tab2Fragment extends Fragment {
    ExpandableListView expandableListView;
    private Tab2Adapter fieldsAdapter;
    private LinearLayout progress_bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tab2, container, false);

        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView);
        progress_bar = rootView.findViewById(R.id.progress_bar);

        Tab2Frag rs = new Tab2Frag();
        rs.getResponse(new CustomCallbackTab2Frag(){
            @Override
            public void onSuccess(Fields fs){
                progress_bar.setVisibility(View.GONE);
                fieldsAdapter = new Tab2Adapter(fs.getListDataHeader(), fs.getListDataChild(), getContext());
                expandableListView.setAdapter(fieldsAdapter);
                expandableListView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(){
                progress_bar.setVisibility(View.GONE);
                ToastDisplay.connErrorMsg(getActivity());
            }
        });
        return rootView;
    }

}