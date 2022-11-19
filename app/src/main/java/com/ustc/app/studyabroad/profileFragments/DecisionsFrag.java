package com.ustc.app.studyabroad.profileFragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ustc.app.studyabroad.FirebaseStorageHelper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.adapters.DecisionsAdapter;
import com.ustc.app.studyabroad.interfaces.GetDecisionsCallback;
import com.ustc.app.studyabroad.interfaces.GetProfileCallback;
import com.ustc.app.studyabroad.userActivities.AddDecisionsActivity;
import com.ustc.app.studyabroad.userActivities.ResetPasswordActivity;
import com.ustc.app.studyabroad.userModels.Decisions;
import com.ustc.app.studyabroad.userModels.Profile;

import java.io.Serializable;
import java.util.List;

public class DecisionsFrag extends Fragment {
    View view;
    private RecyclerView recyclerDecision;
    TextView btn, zero_dec;
    private RelativeLayout progress_bar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_decisions, container, false);

        btn = view.findViewById(R.id.addButton);
        zero_dec = view.findViewById(R.id.zero_decisions);
        progress_bar = view.findViewById(R.id.progress_bar);

        FirebaseStorageHelper.getDecisions("decisions", null, new GetDecisionsCallback() {
            @Override
            public void onSuccess(List<Decisions> decisions) {
                recyclerDecision = view.findViewById(R.id.recyclerDecision);
                recyclerDecision.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerDecision.setAdapter(new DecisionsAdapter("decisions", decisions, getActivity()));
                progress_bar.setVisibility(View.GONE);
                recyclerDecision.setVisibility(View.VISIBLE);
                setEditButton(decisions);
            }
            @Override
            public void onFailure(String m) {
                if(m.equals("f")){
                    ToastDisplay.customMsg(getContext(), "Data was unable to be fetched.");
                    progress_bar.setVisibility(View.GONE);
                } else{
                    zero_dec.setVisibility(View.VISIBLE);
                    progress_bar.setVisibility(View.GONE);
                    setEditButton(null);
                }
            }
        });
        return view;
    }

    public void setEditButton(List<Decisions> decisions){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddDecisions = new Intent(getContext(), AddDecisionsActivity.class);
                intentAddDecisions.putExtra("LIST", (Serializable) decisions);
                startActivity(intentAddDecisions);
            }
        });
    }
}