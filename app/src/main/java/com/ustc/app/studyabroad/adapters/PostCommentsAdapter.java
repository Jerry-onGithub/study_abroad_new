package com.ustc.app.studyabroad.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ustc.app.studyabroad.FirebaseStorageHelper;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.interfaces.Callback;
import com.ustc.app.studyabroad.interfaces.PostReturnCallback;
import com.ustc.app.studyabroad.interfaces.ReturnCallback;
import com.ustc.app.studyabroad.userActivities.PostCommentActivity;
import com.ustc.app.studyabroad.userModels.Comment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostCommentsAdapter extends RecyclerView.Adapter<PostCommentsAdapter.ViewHolder> {
    private Context context;
    private List<Comment> comments;

    public PostCommentsAdapter(Context context, List<Comment> comments){
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);

        setImage(holder, comment.getUserId());
        holder.comment_time.setText(Helper.setTime(comment.getTime()));
        holder.username.setText(comment.getName());
        holder.comment.setText(comment.getCommentMessage());
        holder.likes.setText(String.valueOf(comment.getLikes()));
        holder.dislikes.setText(String.valueOf(comment.getDislikes()));

        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorageHelper.check_(comment.getPostId(), "c", "likers_ids", "likes",comment.getCommentId(), new PostReturnCallback() {
                    @Override
                    public void onSuccess(String l, String d) {
                        if(l!=""){
                            holder.likes.setText(l);
                        }
                        if(d!=""){
                            holder.dislikes.setText(d);
                        }
                        Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>> NEW LIKE "+l);
                    }
                    @Override
                    public void onFailure(String l, String d) {
                        if(l!=""){
                            holder.likes.setText(l);
                        }
                        if(d!=""){
                            holder.dislikes.setText(d);
                        }
                    }
                });
            }
        });
        holder.dislikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorageHelper.check_(comment.getPostId(), "c", "dislikers_ids", "dislikes", comment.getCommentId(), new PostReturnCallback() {
                    @Override
                    public void onSuccess(String l, String d) {
                        if(l!=""){
                            holder.likes.setText(l);
                        }
                        if(d!=""){
                            holder.dislikes.setText(d);
                        }
                        Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>> NEW LIKE "+l);
                    }
                    @Override
                    public void onFailure(String l, String d) {
                        if(l!=""){
                            holder.likes.setText(l);
                        }
                        if(d!=""){
                            holder.dislikes.setText(d);
                        }
                    }
                });
            }
        });
    }

    private void setImage(PostCommentsAdapter.ViewHolder holder, String userId) {
        FirebaseStorageHelper.setImage(context, holder.user_pic, userId, new Callback() {
            @Override
            public void onSuccess() {
                holder.user_pic.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure() { holder.user_pic.setVisibility(View.VISIBLE); }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, comment, likes, dislikes, comment_time;
        CircleImageView user_pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_pic = itemView.findViewById(R.id.user_pic);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
            likes = itemView.findViewById(R.id.likes);
            dislikes = itemView.findViewById(R.id.dislikes);
            comment_time = itemView.findViewById(R.id.comment_time);
        }
    }
}
