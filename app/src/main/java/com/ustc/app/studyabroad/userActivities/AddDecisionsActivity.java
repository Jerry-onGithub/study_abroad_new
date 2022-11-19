package com.ustc.app.studyabroad.userActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.collection.LLRBNode;
import com.ustc.app.studyabroad.FirebaseStorageHelper;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.activities.GetSearchableUniversities;
import com.ustc.app.studyabroad.adapters.DecisionsAdapter;
import com.ustc.app.studyabroad.userModels.Decisions;

import java.util.ArrayList;
import java.util.List;

public class AddDecisionsActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser user;
    Context ctx;
    List<Decisions> applied, admit, reject;

    private LinearLayout btnApplied, btnAdmit, btnReject, viewApplied, viewAdmit, viewReject,
            layoutAppliedBottom, layoutAdmitBottom, layoutRejectBottom, viewAppliedBottom, viewAdmitBottom, viewRejectBottom;
    ImageView im;
    RecyclerView recyclerApplied;
    RecyclerView recyclerAdmit;
    RecyclerView recyclerReject;

    TextView uni_name;
    ImageView search_uni_name;
    TextView submitBtn, applied_date, admit_applied_date, admit_decision_date, reject_applied_date, reject_decision_date;
    EditText major_name, fund_amount;
    AutoCompleteTextView fund_type;

    TextView zero_applied, zero_admit, zero_reject;
    TextView apply, admit_, rej;
    View applied_view, admit_view, reject_view;

    String[] fund_type_items;
    List<Decisions> decisions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_decisions);
        applied=new ArrayList<Decisions>();
        admit=new ArrayList<Decisions>();
        reject=new ArrayList<Decisions>();

        Helper helper=new Helper();
        ctx = AddDecisionsActivity.this;
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        Intent i = getIntent();
        decisions = (List<Decisions>) i.getSerializableExtra("LIST");

        setView();
        setOnClick();
        if(decisions!=null){
            setAdapters();
        } else {
            setDefault();
        }
        searchUni();
        setAutoCompleteTextView();
        helper.setDate(applied_date, ctx);
        helper.setDate(admit_applied_date, ctx);
        helper.setDate(admit_decision_date, ctx);
        helper.setDate(reject_applied_date, ctx);
        helper.setDate(reject_decision_date, ctx);
    }

    private void setDefault() {
        zero_admit.setVisibility(View.VISIBLE);
        zero_applied.setVisibility(View.VISIBLE);
        zero_reject.setVisibility(View.VISIBLE);
    }

    private void setAdapters() {
        for (Decisions dec : decisions) {
            if(dec.getStatus().equals("applied")){
                applied.add(dec);
            } else if(dec.getStatus().equals("admit")){
                admit.add(dec);
            } else if(dec.getStatus().equals("reject")){
                reject.add(dec);
            }
        }

        if(admit.size()>0) {
            recyclerAdmit = findViewById(R.id.recyclerAdmits);
            recyclerAdmit.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerAdmit.setAdapter(new DecisionsAdapter("admit", admit, AddDecisionsActivity.this));
        } else{
            zero_admit.setVisibility(View.VISIBLE);
        }

        if(applied.size()>0) {
            recyclerApplied = findViewById(R.id.recyclerApplied);
            recyclerApplied.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerApplied.setAdapter(new DecisionsAdapter("applied", applied, AddDecisionsActivity.this));
        } else{
            zero_applied.setVisibility(View.VISIBLE);
        }

        if(reject.size()>0) {
            recyclerReject = findViewById(R.id.recyclerRejects);
            recyclerReject.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerReject.setAdapter(new DecisionsAdapter("reject", reject, AddDecisionsActivity.this));
        } else{
            zero_reject.setVisibility(View.VISIBLE);
        }
    }

    private void setAutoCompleteTextView() {
        fund_type_items = getResources().getStringArray(R.array.fund_type);
        Helper.getItem(fund_type_items, this, fund_type);
    }

    public void save(String s){
        submitBtn = findViewById(R.id.submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Decisions decision;
                switch (s){
                    case "applied":
                        decision = new Decisions(user.getUid(), uni_name.getText().toString(), major_name.getText().toString(),
                                applied_date.getText().toString(), "applied");
                        FirebaseStorageHelper.saveDecision(decision);
                        ToastDisplay.customMsg(ctx, "SAVED");
                        break;
                    case "admit":
                        decision = new Decisions(user.getUid(), uni_name.getText().toString(), major_name.getText().toString(),
                                admit_applied_date.getText().toString(), admit_decision_date.getText().toString(), fund_amount.getText().toString(),
                                fund_type.getText().toString(), "admit");
                        FirebaseStorageHelper.saveDecision(decision);
                        break;
                    case "reject":
                        decision = new Decisions(user.getUid(), uni_name.getText().toString(), major_name.getText().toString(),
                                reject_applied_date.getText().toString(), reject_decision_date.getText().toString(), "reject");
                        FirebaseStorageHelper.saveDecision(decision);
                        break;
                    default:
                        break;
                }
                System.out.println("STATUS >>>>>>>>>>>>>>> " + s);
            }
        });
    }

    private void setOnClick() {
        btnApplied = findViewById(R.id.btnApplied);
        btnApplied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewApplied.setVisibility(View.VISIBLE);
                viewAdmit.setVisibility(View.GONE);
                viewReject.setVisibility(View.GONE);

                apply.setTextColor(getResources().getColor(R.color.btn));
                admit_.setTextColor(getResources().getColor(R.color.light_black));
                rej.setTextColor(getResources().getColor(R.color.light_black));
                save("applied");
            }
        });
        btnAdmit = findViewById(R.id.btnAdmit);
        btnAdmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewApplied.setVisibility(View.GONE);
                viewAdmit.setVisibility(View.VISIBLE);
                viewReject.setVisibility(View.GONE);

                admit_.setTextColor(getResources().getColor(R.color.btn));
                apply.setTextColor(getResources().getColor(R.color.light_black));
                rej.setTextColor(getResources().getColor(R.color.light_black));
                save("admit");
            }
        });
        btnReject = findViewById(R.id.btnReject);
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewApplied.setVisibility(View.GONE);
                viewAdmit.setVisibility(View.GONE);
                viewReject.setVisibility(View.VISIBLE);

                rej.setTextColor(getResources().getColor(R.color.btn));
                admit_.setTextColor(getResources().getColor(R.color.light_black));
                apply.setTextColor(getResources().getColor(R.color.light_black));
                save("reject");
            }
        });
        layoutAppliedBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAdmitBottom.setVisibility(View.GONE);
                viewRejectBottom.setVisibility(View.GONE);
                viewAppliedBottom.setVisibility(View.VISIBLE);

                applied_view.setVisibility(View.VISIBLE);
                admit_view.setVisibility(View.GONE);
                reject_view.setVisibility(View.GONE);
            }
        });
        layoutAdmitBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAppliedBottom.setVisibility(View.GONE);
                viewRejectBottom.setVisibility(View.GONE);
                viewAdmitBottom.setVisibility(View.VISIBLE);

                admit_view.setVisibility(View.VISIBLE);
                applied_view.setVisibility(View.GONE);
                reject_view.setVisibility(View.GONE);
            }
        });
        layoutRejectBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAppliedBottom.setVisibility(View.GONE);
                viewAdmitBottom.setVisibility(View.GONE);
                viewRejectBottom.setVisibility(View.VISIBLE);

                reject_view.setVisibility(View.VISIBLE);
                admit_view.setVisibility(View.GONE);
                applied_view.setVisibility(View.GONE);
            }
        });
    }

    private void setView() {
        layoutAppliedBottom = findViewById(R.id.layoutAppliedBottom);
        layoutAdmitBottom = findViewById(R.id.layoutAdmitBottom);
        layoutRejectBottom = findViewById(R.id.layoutRejectBottom);

        viewApplied = findViewById(R.id.viewApplied);
        viewAdmit = findViewById(R.id.viewAdmit);
        viewReject = findViewById(R.id.viewReject);
        viewAppliedBottom = findViewById(R.id.viewAppliedBottom);
        viewAdmitBottom = findViewById(R.id.viewAdmitBottom);
        viewRejectBottom = findViewById(R.id.viewRejectBottom);
        im = (ImageView) findViewById(R.id.back);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        uni_name = findViewById(R.id.uni_name);
        major_name = findViewById(R.id.major_name);
        search_uni_name = findViewById(R.id.search_uni_name);
        applied_date = findViewById(R.id.applied_date);
        admit_applied_date = findViewById(R.id.admit_applied_date);
        admit_decision_date = findViewById(R.id.admit_decision_date);
        reject_applied_date = findViewById(R.id.reject_applied_date);
        reject_decision_date = findViewById(R.id.reject_decision_date);
        fund_amount = findViewById(R.id.fund_amount);
        fund_type = findViewById(R.id.fund_type);

        zero_applied = findViewById(R.id.zero_applied);
        zero_admit = findViewById(R.id.zero_admit);
        zero_reject = findViewById(R.id.zero_reject);

        zero_applied.setVisibility(View.GONE);
        zero_admit.setVisibility(View.GONE);
        zero_reject.setVisibility(View.GONE);

        apply = findViewById(R.id.apply);
        admit_ = findViewById(R.id.admit);
        rej = findViewById(R.id.rej);
        applied_view = findViewById(R.id.applied_view);
        admit_view = findViewById(R.id.admit_view);
        reject_view = findViewById(R.id.reject_view);
    }

    private void searchUni() {
        search_uni_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddDecisionsActivity.this, GetSearchableUniversities.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 123 && requestCode == 1) {
            String returnData = data.getStringExtra("value");
            uni_name.setText(returnData);
        }
    }
}
