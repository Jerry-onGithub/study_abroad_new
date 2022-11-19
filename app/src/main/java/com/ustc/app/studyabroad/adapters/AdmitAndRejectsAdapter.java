package com.ustc.app.studyabroad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ustc.app.studyabroad.FirebaseStorageHelper;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.interfaces.Callback;
import com.ustc.app.studyabroad.interfaces.GetProfileCallback;
import com.ustc.app.studyabroad.userModels.Decisions;
import com.ustc.app.studyabroad.userModels.Profile;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdmitAndRejectsAdapter extends RecyclerView.Adapter<AdmitAndRejectsAdapter.ViewHolder> {
    private Context context;
    private List<Decisions> decisions;

    public AdmitAndRejectsAdapter(List<Decisions> decisionItem, Context context){
        this.context = context;
        this.decisions = decisionItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_admit_reject,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Decisions decision = decisions.get(position);
        setProfile(decision.getUserId(), holder);

        holder.uni_name.setText(decision.getUni_name());
        holder.major.setText(" - " + decision.getMajor());
        holder.app_date.setText(decision.getApp_date());
        holder.status.setText(decision.getStatus().toUpperCase());
        if(decision.getStatus().equals("applied")){
            holder.status.setBackgroundResource(R.color.quantum_orange);
            holder.dec_date_layout.setVisibility(View.GONE);
        } else {
            holder.dec_date.setText(decision.getDec_date());
        }
        if(decision.getStatus().equals("admit")) {
            holder.status.setBackgroundResource(R.color.darkgreen);
            holder.funding.setText(decision.getFund_type() + " - " + "$" + decision.getFund_amount());
        } else {
            holder.funding_layout.setVisibility(View.GONE);
        }
        if(decision.getStatus().equals("reject")) {
            holder.status.setBackgroundResource(R.color.darkred);
        }

        setBookmark(decision, holder.save);

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorageHelper.saveDecisionsForUser("", decision, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.save.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmark_color));
                        notifyDataSetChanged();
                    }
                    @Override
                    public void onFailure() {
                        holder.save.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmark_outline));
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void setBookmark(Decisions decision, ImageView save) {
        FirebaseStorageHelper.saveDecisionsForUser("exists", decision, new Callback() {
            @Override
            public void onSuccess() {
                save.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmark_outline));
            }
            @Override
            public void onFailure() {
                save.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmark_color));
            }
        });
    }

    private void setProfile(String userId, ViewHolder holder) {
        FirebaseStorageHelper.getProfile(userId, new GetProfileCallback() {
            @Override
            public void onSuccess(Profile profile) {
                FirebaseStorageHelper.setImage(context, holder.user_pic, userId, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.user_pic.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onFailure() {
                    }
                });
                holder.username.setText(profile.getUserName());
                holder.edu_level.setText(profile.getDegree_level());
                holder.cgpa.setText(profile.getCgpa());
                holder.work_exp.setText(profile.getWork_exp());
                holder.research_paper.setText(profile.getResearch_paper());
                holder.location.setText(profile.getCountry());
            }
            @Override
            public void onFailure(String er) {
                if(er.equals("f")) {
                    //ToastDisplay.customMsg(getContext(), "Data was unable to be fetched.");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return decisions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView user_pic;
        TextView username, location, uni_name, major, app_date, dec_date, edu_level, cgpa, work_exp, research_paper, funding, status;
        ImageView save;
        LinearLayout dec_date_layout, funding_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_pic = itemView.findViewById(R.id.user_pic);
            username = itemView.findViewById(R.id.username);
            location = itemView.findViewById(R.id.location);
            uni_name = itemView.findViewById(R.id.uni_name);
            major = itemView.findViewById(R.id.major);
            app_date = itemView.findViewById(R.id.app_date);
            dec_date = itemView.findViewById(R.id.dec_date);
            edu_level = itemView.findViewById(R.id.edu_level);
            cgpa = itemView.findViewById(R.id.cgpa);
            work_exp = itemView.findViewById(R.id.work_exp);
            research_paper = itemView.findViewById(R.id.research_paper);
            funding = itemView.findViewById(R.id.funding);
            status = itemView.findViewById(R.id.status);
            save = itemView.findViewById(R.id.save);
            dec_date_layout = itemView.findViewById(R.id.dec_date_layout);
            funding_layout = itemView.findViewById(R.id.funding_layout);

        }
    }
}
