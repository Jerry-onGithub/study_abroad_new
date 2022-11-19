package com.ustc.app.studyabroad.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.homeActivities.SecondActivity;
import com.ustc.app.studyabroad.models.University;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchViewAdapter extends BaseAdapter {
    Activity mContext;
    LayoutInflater inflater;

    private List<University> list = null;
    private ArrayList<University> arraylist;

    public SearchViewAdapter(Activity context, List<University> universityList) {
        mContext = context;
        this.list = universityList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<University>();
        this.arraylist.addAll(list);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public University getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(list.get(position).getName());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("message_subject_intent");
                intent.putExtra("name" , list.get(position).getName());
                intent.putExtra("index" , list.get(position).getIndex());
                if(list.get(position).getImgUrl() != null){
                    intent.putExtra("image" , list.get(position).getImgUrl());
                }
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        });
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(arraylist);
        } else {
            for (University wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    list.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
