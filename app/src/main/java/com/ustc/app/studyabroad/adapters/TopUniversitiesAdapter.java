package com.ustc.app.studyabroad.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.activities.GetUniversity;
import com.ustc.app.studyabroad.models.University;
import java.util.List;

public class TopUniversitiesAdapter extends RecyclerView.Adapter<TopUniversitiesAdapter.CardViewHolder>  {

    private Context context;
    private List<University> listItem;

    public TopUniversitiesAdapter(List<University> listItem, Context context) {
        this.context = context;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_universities_display, viewGroup, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder cardViewHolder, final int position) {
        //clearData();
        University item = listItem.get(position);
        Glide.with(context)
                .load(item.getImgUrl())
                .apply(new RequestOptions().override(550, 750))
                .into(cardViewHolder.img);
        cardViewHolder.name.setText(item.getName());
        cardViewHolder.rank.setText(item.getRank());
        cardViewHolder.country.setText(item.getCountry().toUpperCase());

        cardViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GetUniversity.class);
                intent.putExtra("name", item.getName());
                intent.putExtra("img", item.getImgUrl());
                intent.putExtra("index", item.getIndex());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, country, rank;
        LinearLayout cardView;
        public CardViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            country = itemView.findViewById(R.id.country);
            rank = itemView.findViewById(R.id.rank);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }

    public void clearData() {
        listItem.clear(); // clear list
        notifyDataSetChanged(); // let your adapter know about the changes and reload view.
    }
}
