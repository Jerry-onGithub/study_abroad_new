package com.ustc.app.studyabroad.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.models.Program;

import java.util.List;

public class ProgramsAdapter extends RecyclerView.Adapter<ProgramsAdapter.ViewHolder> {

    private Context context;
    private List<Program> programItem;

    public ProgramsAdapter(List<Program> programItem, Context context){
        this.programItem = programItem;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_uni_programs,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //clearData();
        Program program = programItem.get(position);
/*        Glide.with(context)
                .load(university.getImgUrl())
                .apply(new RequestOptions().override(550, 750))
                .into(holder.img);*/
        holder.name.setText(program.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                Intent intent = new Intent(context, GetUniversity.class);
                intent.putExtra("name", university.getName());
                intent.putExtra("img", university.getImgUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return programItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.programItem);

        }

    }
}

