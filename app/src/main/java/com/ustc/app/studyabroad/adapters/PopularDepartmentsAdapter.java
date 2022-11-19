package com.ustc.app.studyabroad.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ustc.app.studyabroad.CustomDialog;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.activities.GetUniversity;
import com.ustc.app.studyabroad.interfaces.GetUrlCallback;
import com.ustc.app.studyabroad.jsonResponse.GetImage;
import com.ustc.app.studyabroad.models.Category;
import com.ustc.app.studyabroad.models.PopularD;

import java.util.List;

public class PopularDepartmentsAdapter extends RecyclerView.Adapter<PopularDepartmentsAdapter.CardViewHolder>  {

    private Context context;
    private List<PopularD> listItem;
    private String chosen;
    CustomDialog d;

    public PopularDepartmentsAdapter(String chosen, List<PopularD> listItem, Context context) {
        this.context = context;
        this.listItem = listItem;
        this.chosen = chosen;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.popular_departments_display, viewGroup, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder cardViewHolder, final int position) {
        //clearData();
        PopularD item = listItem.get(position);

        cardViewHolder.name.setText(item.getName());
        for(Category c : item.getCategory()){
            switch (c.getCat_name()){
                case "engineering":
                    check(chosen, "engineering", cardViewHolder.engineering, c, cardViewHolder, item.getIndex(), item.getName());
                    setText(cardViewHolder.engineering, "Engineering", cardViewHolder.ecv);
                    break;
                case "business":
                    check(chosen, "business", cardViewHolder.business, c, cardViewHolder, item.getIndex(), item.getName());
                    setText(cardViewHolder.business, "Business", cardViewHolder.bcv);
                    break;
                case "law":
                    check(chosen, "law", cardViewHolder.law, c, cardViewHolder, item.getIndex(), item.getName());
                    setText(cardViewHolder.law, "Law", cardViewHolder.lcv);
                    break;
                case "medicine":
                    check(chosen, "medicine", cardViewHolder.medicine, c, cardViewHolder, item.getIndex(), item.getName());
                    setText(cardViewHolder.medicine, "Medicine", cardViewHolder.mcv);
                    break;
            }
        }

        cardViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setText(TextView tv, String text, CardView cv) {
        tv.setText(text);
        cv.setVisibility(View.VISIBLE);
    }

    private void check(String chosen, String textview, TextView cardViewHolder, Category c, CardViewHolder vh, String index, String name) {
        if(chosen.equals(textview)){
            cardViewHolder.setTextColor(Color.parseColor("#ff008577"));
            //Helper.print(">>>>>EQUALS "+c.getCat_content());
            String[] words = c.getCat_content().split(" ");
            int n = 20;
            String newString = "";
            if(words.length > n){
                for (int i = 0; i < n; i++) {
                    newString = newString + words[i] + " ";
                }
            }
            //Helper.print(">>>>>EQUALS "+newString);
            vh.content.setText(newString + "...");

            setOnClick(cardViewHolder, c.getCat_content(), index, name);
            setOnClick(vh.content, c.getCat_content(), index, name);
        }
    }

    private void setOnClick(TextView cardViewHolder, String content, String i, String name) {
        cardViewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());
                final View yourCustomView = inflater.inflate(R.layout.content_popup, null);

                Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(true);
                dialog.setContentView(yourCustomView);
                ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress_circular);
                TextView name_ = dialog.findViewById(R.id.name);
                TextView desc = dialog.findViewById(R.id.desc);
                TextView more = dialog.findViewById(R.id.more);
                // name.setText(cardViewHolder.getText().toString());
                desc.setText(content);


                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        GetImage pd = new GetImage();
                        pd.getResponse(i, new GetUrlCallback(){
                            @Override
                            public void onSuccess(String url){
                                Intent intent = new Intent(context, GetUniversity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("img", url);
                                intent.putExtra("index", i);
                                //Helper.print(university.getIndex());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                            @Override
                            public void onFailure(String f) {
                                ToastDisplay.connErrorMsg(context);
                            }
                        });
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        TextView name, engineering, business, law, medicine, content;
        CardView ecv, bcv, lcv, mcv;
        LinearLayout cardView;
        public CardViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            engineering = itemView.findViewById(R.id.engineering);
            business = itemView.findViewById(R.id.business);
            law = itemView.findViewById(R.id.law);
            medicine = itemView.findViewById(R.id.medicine);
            content = itemView.findViewById(R.id.content);
            cardView = itemView.findViewById(R.id.card_view);
            ecv = itemView.findViewById(R.id.ecv);
            bcv = itemView.findViewById(R.id.bcv);
            lcv = itemView.findViewById(R.id.lcv);
            mcv = itemView.findViewById(R.id.mcv);

        }
    }

}

