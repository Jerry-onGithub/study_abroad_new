package com.ustc.app.studyabroad.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.activities.ListUniversitiesByProgram;

import java.util.HashMap;
import java.util.List;

public class Tab2Adapter extends BaseExpandableListAdapter {

    private Context context;
    private HashMap<String, List<String>> expandableDetailList;
    private List<String> expandableTitleList;

    public Tab2Adapter(List<String> expandableTitleList, HashMap<String, List<String>> expandableDetailList, Context context){
        this.expandableTitleList=expandableTitleList;
        this.expandableDetailList = expandableDetailList;
        this.context = context;
    }

    @Override
    public Object getChild(int lstPosn, int expanded_ListPosition) {
        return this.expandableDetailList.get(this.expandableTitleList.get(lstPosn)).get(expanded_ListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expanded_ListPosition) {
        return expanded_ListPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getChildView(int lstPosn, final int expanded_ListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(lstPosn, expanded_ListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_item_department, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);

        expandedListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListUniversitiesByProgram.class);
                intent.putExtra("name", expandedListText);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    // Gets the number of children in a specified group.
    public int getChildrenCount(int listPosition) {
        return this.expandableDetailList.get(this.expandableTitleList.get(listPosition)).size();
    }

    @Override
    // Gets the data associated with the given group.
    public Object getGroup(int listPosition) {
        return this.expandableTitleList.get(listPosition);
    }

    @Override
    // Gets the number of groups.
    public int getGroupCount() {
        return this.expandableTitleList.size();
    }

    @Override
    // Gets the ID for the group at the given position. This group ID must be unique across groups.
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    // Gets a View that displays the given group.
    // This View is only for the group--the Views for the group's children
    // will be fetched using getChildView()
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_item_program, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.NORMAL);
        listTitleTextView.setText(listTitle);
        ImageView img = convertView.findViewById(R.id.image);
        setImage(listPosition, img);

        return convertView;
    }

    @Override
    // Whether the child at the specified position is selectable.
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public void setImage(int p, ImageView img){
        switch(p){
            case 0:
                img.setImageResource(R.drawable.logo_engi);
                break;
            case 1:
                img.setImageResource(R.drawable.logo_computing);
                break;
            case 2:
                img.setImageResource(R.drawable.logo_business);
                break;
            case 3:
                img.setImageResource(R.drawable.logo_science);
                break;
            case 4:
                img.setImageResource(R.drawable.logo_architecture);
                break;
            case 5:
                img.setImageResource(R.drawable.logo_textile);
                break;
            case 6:
                img.setImageResource(R.drawable.logo_socialscience);
                break;
            case 7:
                img.setImageResource(R.drawable.logo_medicine);
                break;
        }
    }
}

