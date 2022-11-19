package com.ustc.app.studyabroad.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.activities.GetUniversity;
import com.ustc.app.studyabroad.models.RecentlyViewed;
import com.ustc.app.studyabroad.models.University;

import java.util.List;

public class RecentViewsAdapter extends RecyclerView.Adapter<RecentViewsAdapter.ViewHolder> {

    private Context context;
    private List<University> universityItem;
    List<RecentlyViewed> recentlyViewed;

    public RecentViewsAdapter(List<RecentlyViewed> recentlyViewed, List<University> universityItem, Context context){
        this.universityItem = universityItem;
        this.context = context;
        this.recentlyViewed = recentlyViewed;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_recent_viewed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        University university = universityItem.get(position);
        Glide.with(context)
                .load(university.getImgUrl())
                .apply(new RequestOptions().override(80, 80))
                .into(holder.img);
        holder.name.setText(university.getName());
        holder.address.setText(university.getAddress());

        for(int i=0; i<recentlyViewed.size(); i++){
            if(university.getIndex().equals(recentlyViewed.get(i).getUni_index())){
                holder.time.setText("Last viewed " + Helper.setTime(recentlyViewed.get(i).getTime()));
                holder.visitors.setText(" "+String.valueOf(recentlyViewed.get(i).getVisited_times()));
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GetUniversity.class);
                intent.putExtra("name", university.getName());
                intent.putExtra("img", university.getImgUrl());
                intent.putExtra("index", university.getIndex());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return universityItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, address, time, visitors;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            time = itemView.findViewById(R.id.time);
            visitors = itemView.findViewById(R.id.visitors);
        }
    }
}

