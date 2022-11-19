package com.ustc.app.studyabroad.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.activities.GetUniversity;
import com.ustc.app.studyabroad.models.University;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Tab1Adapter extends RecyclerView.Adapter<Tab1Adapter.ViewHolder> {

    private Context context;
    private List<University> universityItem;
    Boolean check;
    String input;

    List<University> display;

    public Tab1Adapter(String input, Boolean p, List<University> universityItem, Context context){
        this.universityItem = universityItem;
        this.context = context;
        this.check=p;
        this.input=input;

        Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> U SIZE " + this.universityItem.size());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(input == "search_list"){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_uni_searchable, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_university, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //clearData();
        Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> FINAL SIZE " + universityItem.size());

        University university = universityItem.get(position);

        /*if(input == "search_list"){
            holder.name.setText(university.getName());
        }*/
        /*else{
            Glide.with(context)
                    .load(university.getImgUrl())
                    .apply(new RequestOptions().override(80, 80))
                    .into(holder.img);
            holder.name.setText(university.getName());
            holder.address.setText(university.getAddress());
        }*/

        if(input=="search_list"){
            Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> search_list ");
            holder.name.setText(university.getName());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("message_subject_intent");
                    intent.putExtra("name" , university.getName());
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                }
            });
        }
        else {
            Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ELSE ");

            if (check==true){
                holder.program.setVisibility(View.VISIBLE);
                holder.program.setText(university.getProgram());
                holder.program.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dot_16, 0, 0, 0);
                holder.program.setCompoundDrawablePadding(context.getResources().getDimensionPixelOffset(R.dimen.small_padding));
            }
            Glide.with(context)
                    .load(university.getImgUrl())
                    .apply(new RequestOptions().override(80, 80))
                    .into(holder.img);
            holder.name.setText(university.getName());
            holder.address.setText(university.getAddress());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GetUniversity.class);
                    intent.putExtra("name", university.getName());
                    intent.putExtra("img", university.getImgUrl());
                    intent.putExtra("index", university.getIndex());
                    //Helper.print(university.getIndex());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return universityItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        TextView address;
        TextView program;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            if(input == "search_list"){
                name = itemView.findViewById(R.id.uniItem);
                cardView = itemView.findViewById(R.id.card_view);
            }
            else{
                img = itemView.findViewById(R.id.image);
                name = itemView.findViewById(R.id.name);
                address = itemView.findViewById(R.id.address);
                program = itemView.findViewById(R.id.program);
            }
        }
    }

}

