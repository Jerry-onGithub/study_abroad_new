package com.ustc.app.studyabroad.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.activities.ListUniversitiesByCountry;

import java.util.List;

public class Tab3Adapter extends RecyclerView.Adapter<Tab3Adapter.ViewHolder> {

    private Context context;
    private List<String> countryItem;

    public Tab3Adapter(List<String> countryItem, Context context){
        this.countryItem = countryItem;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_country,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch(position){
            case 0:
                holder.img.setImageResource(R.drawable.ca_64);
                break;
            case 1:
                holder.img.setImageResource(R.drawable.au_64);
                break;
            case 2:
                holder.img.setImageResource(R.drawable.de_64);
                break;
            case 3:
                holder.img.setImageResource(R.drawable.gb_64);
                break;
            case 4:
                holder.img.setImageResource(R.drawable.us_64);
                break;
        }
        //clearData();
        String s = countryItem.get(position);
        holder.name.setText(s);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListUniversitiesByCountry.class);
                intent.putExtra("name", s);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
        }

    }
}

