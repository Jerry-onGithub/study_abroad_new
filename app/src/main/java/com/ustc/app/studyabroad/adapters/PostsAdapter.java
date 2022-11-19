package com.ustc.app.studyabroad.adapters;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.cocosw.bottomsheet.BottomSheet;
import com.ustc.app.studyabroad.FirebaseStorageHelper;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.activities.GetSearchableUniversities;
import com.ustc.app.studyabroad.interfaces.Callback;
import com.ustc.app.studyabroad.interfaces.PostReturnCallback;
import com.ustc.app.studyabroad.interfaces.ReturnCallback;
import com.ustc.app.studyabroad.models.University;
import com.ustc.app.studyabroad.userActivities.EditProfileActivity;
import com.ustc.app.studyabroad.userActivities.PostCommentActivity;
import com.ustc.app.studyabroad.userModels.Post;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    public Activity context;
    public static List<Post> posts;

    public PostsAdapter(Activity context, List<Post> posts){
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.comment_section.setVisibility(View.GONE);
        setImage(holder, post.getUserId());
        holder.post_time.setText(Helper.setTime(post.getTime()));
        holder.username.setText(post.getUsername());
        holder.post_location.setText(post.getLocation());
        holder.post_msg.setText(post.getMessage());
        holder.likes.setText(String.valueOf(post.getLikes()));
        holder.comments.setText(String.valueOf(post.getComments()));

        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorageHelper.check_(post.getPostId(), "p", "likers_ids", "likes",null, new PostReturnCallback() {
                @Override
                    public void onSuccess(String l, String d) {
                        holder.likes.setText(l);
                    }
                    @Override
                    public void onFailure(String l, String d) {
                        holder.likes.setText(l);
                    }
                });
            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PostCommentActivity.class);
                intent.putExtra("POST", post);
                intent.putExtra("index", holder.getAdapterPosition());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.post_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(context, PostCommentActivity.class);
                intent.putExtra("POST", post);
                intent.putExtra("index", holder.getAdapterPosition());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/
            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSheet.Builder(context, R.style.BottomSheet_StyleDialog).title("").sheet(R.menu.more).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.report:
                                ToastDisplay.customMsgShort(context, "Help me!");
                                break;
                            case R.id.cancel:
                                dialog.dismiss();
                                break;
                        }
                    }
                }).show();
            }
        });
    }

    private void setImage(ViewHolder holder, String userId) {
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
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, post_location, post_msg, likes, comments, post_time;
        CircleImageView user_pic;
        LinearLayout comment_section;
        ConstraintLayout post_item;
        ImageView more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comment_section = itemView.findViewById(R.id.comment_section);
            post_item = itemView.findViewById(R.id.post_item);
            user_pic = itemView.findViewById(R.id.user_pic);
            username = itemView.findViewById(R.id.username);
            post_location = itemView.findViewById(R.id.post_location);
            post_msg = itemView.findViewById(R.id.post_msg);
            likes = itemView.findViewById(R.id.likes);
            comments = itemView.findViewById(R.id.comments);
            post_time = itemView.findViewById(R.id.post_time);
            more = itemView.findViewById(R.id.more);

        }
    }

    public void updateItem(Post post, int position, int itemValue){
        post.setComments(itemValue);
        //posts.set(position, post); // update item field
        notifyItemChanged(position, post);
    }

}
