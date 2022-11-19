package com.ustc.app.studyabroad.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.rpc.Help;
import com.ustc.app.studyabroad.FirebaseStorageHelper;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.interfaces.Callback;
import com.ustc.app.studyabroad.interfaces.GetDecisionsCallback;
import com.ustc.app.studyabroad.userModels.Decisions;

import java.util.List;


public class DecisionsAdapter extends RecyclerView.Adapter<DecisionsAdapter.ViewHolder> {
    private Activity context;
    private String v;
    private List<Decisions> decisions;

    public DecisionsAdapter(String view, List<Decisions> decisionItem, Activity context){
        this.context = context;
        this.v=view;
        this.decisions = decisionItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_decisions, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Decisions decision = decisions.get(position);
        switch (v){
            case "decisions":
                setDecisions(decision, holder);
                break;
            case "applied":
                holder.status.setText("Applied");
                holder.funding.setVisibility(View.GONE);
                setData(holder, decision);
                break;
            case "admit":
                holder.status.setText("Admit");
                setData(holder, decision);
                holder.dec_date.setText(decision.getDec_date());
                holder.fund_amount.setText(decision.getFund_amount());
                holder.fund_type.setText(decision.getFund_type());
                break;
            default:
                holder.funding.setVisibility(View.GONE);
                holder.status.setText("Reject");
                setData(holder, decision);
                holder.dec_date.setText(decision.getDec_date());
                break;
        }

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.remove);
                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                DrawableCompat.setTint(wrappedDrawable, Color.WHITE);
                Helper.alertDialog(context, /*context.getResources().getDrawable(R.drawable.remove)*/wrappedDrawable, "Are you sure to delete?",
                        "Decision item will be removed", new Callback() {
                            @Override
                            public void onSuccess() {
                                deleteDecision(decision, holder.getAdapterPosition());
                                ToastDisplay.customMsgShort(context.getApplicationContext(), "Successfully removed!");
                                notifyDataSetChanged();
                            }
                            @Override
                            public void onFailure() {

                            }
                        });
            }
        });
    }

    private void deleteDecision(Decisions decision, int position) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait.....");
        progressDialog.show();
        FirebaseStorageHelper.deleteDecision(decision.getDecisionId(), decision.getStatus(), new Callback() {
            @Override
            public void onSuccess() {
                decisions.remove(position);
                notifyDataSetChanged();
                progressDialog.dismiss();
            }
            @Override
            public void onFailure() {
                ToastDisplay.customMsg(context, "Failed to delete.");
                progressDialog.dismiss();
            }
        });
    }

    private void setDecisions(Decisions decision, ViewHolder holder) {
        if(decision.getStatus().equals("applied")) {
            holder.status.setText("Applied");
            holder.funding.setVisibility(View.GONE);
            holder.dec_date.setText("-");
            setData(holder, decision);
        }else if(decision.getStatus().equals("admit")) {
            holder.status.setText("Admit");
            setData(holder, decision);
            holder.dec_date.setText(decision.getDec_date());
            holder.fund_amount.setText(decision.getFund_amount());
            holder.fund_type.setText(decision.getFund_type());
        }else if(decision.getStatus().equals("reject")) {
            holder.funding.setVisibility(View.GONE);
            holder.status.setText("Reject");
            setData(holder, decision);
            holder.dec_date.setText(decision.getDec_date());
        }
    }

    private void setData(ViewHolder holder, Decisions decision) {
        holder.uni_name.setText(decision.getUni_name());
        holder.major_name.setText(decision.getMajor());
        holder.app_date.setText(decision.getApp_date());
    }

    @Override
    public int getItemCount() {
        return decisions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout funding;
        TextView status, uni_name, major_name, app_date, dec_date, fund_amount, fund_type;
        ImageView more;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            funding = itemView.findViewById(R.id.funding);
            status = itemView.findViewById(R.id.status);
            uni_name = itemView.findViewById(R.id.uni_name);
            major_name = itemView.findViewById(R.id.major_name);
            app_date = itemView.findViewById(R.id.app_date);
            dec_date = itemView.findViewById(R.id.dec_date);
            fund_amount = itemView.findViewById(R.id.fund_amount);
            fund_type = itemView.findViewById(R.id.fund_type);
            more = itemView.findViewById(R.id.more);
        }
    }
}
