package com.ustc.app.studyabroad.userActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cocosw.bottomsheet.BottomSheet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.rpc.Help;
import com.ustc.app.studyabroad.FirebaseStorageHelper;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.adapters.DecisionsAdapter;
import com.ustc.app.studyabroad.adapters.PostCommentsAdapter;
import com.ustc.app.studyabroad.adapters.PostsAdapter;
import com.ustc.app.studyabroad.homeActivities.SecondActivity;
import com.ustc.app.studyabroad.interfaces.Callback;
import com.ustc.app.studyabroad.interfaces.GetCommentsCallback;
import com.ustc.app.studyabroad.interfaces.GetProfileCallback;
import com.ustc.app.studyabroad.interfaces.PostReturnCallback;
import com.ustc.app.studyabroad.interfaces.ReturnCallback;
import com.ustc.app.studyabroad.userModels.Comment;
import com.ustc.app.studyabroad.userModels.Post;
import com.ustc.app.studyabroad.userModels.Profile;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostCommentActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    TextView username, post_location, post_msg, likes, comments, post_time;
    ImageView submit, more;
    EditText comment;
    CircleImageView user_pic, user_pic_comment;
    Post post;
    int position;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.row_item_post);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent i = getIntent();
        post = (Post) i.getSerializableExtra("POST");
        position = i.getIntExtra("index", 0);
        Helper.print("int position >>>>>>>>>>>>>>>>>>>>>>> "+position);

        setView();
        setOnClicks();
        setAdapter();
        saveComment();
    }

    private void setAdapter() {
        FirebaseStorageHelper.getComments(post.getPostId(), new GetCommentsCallback() {
            @Override
            public void onSuccess(List<Comment> comment) {
                recyclerView = findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(new PostCommentsAdapter(getApplicationContext(), comment));
                recyclerView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(String er) {
                if(er.equals("f")) {
                    ToastDisplay.customMsg(getApplicationContext(), "Unable to load comments.");
                } else{
                }
            }
        });
    }

    private void saveComment() {
        textInputListener();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment commenter = new Comment(post.getPostId(), comment.getText().toString(), 0, 0, Helper.getTime());
                FirebaseStorageHelper.saveComment(commenter);
                ToastDisplay.customMsg(getApplicationContext(), "Comment submitted");

                String j = String.valueOf(Integer.valueOf(comments.getText().toString())+1);
                Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+j);
                comments.setText(j);

                recyclerView.setVisibility(View.VISIBLE);
                setAdapter();
                comment.getText().clear();

                //finish();
            }
        });
    }

    private void textInputListener() {
        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0)
                    submit.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setView() {
        recyclerView = findViewById(R.id.recycler_view);
        user_pic = findViewById(R.id.user_pic);
        username = findViewById(R.id.username);
        post_location = findViewById(R.id.post_location);
        post_msg = findViewById(R.id.post_msg);
        likes = findViewById(R.id.likes);
        comments = findViewById(R.id.comments);
        post_time = findViewById(R.id.post_time);
        submit = findViewById(R.id.submit);
        comment = findViewById(R.id.comment);
        user_pic_comment = findViewById(R.id.user_pic_comment);
        more = findViewById(R.id.more);

        setImage();
        username.setText(post.getUsername());
        post_location.setText(post.getLocation());
        post_msg.setText(post.getMessage());
        likes.setText(String.valueOf(post.getLikes()));
        comments.setText(String.valueOf(post.getComments()));
        post_time.setText(Helper.setTime(post.getTime()));

    }

    private void setImage() {
        FirebaseStorageHelper.setImage(getApplicationContext(), user_pic, post.getUserId(), new Callback() {
            @Override
            public void onSuccess() {
                user_pic.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure() { user_pic.setVisibility(View.VISIBLE); }
        });
        FirebaseStorageHelper.setImage(getApplicationContext(), user_pic_comment, FirebaseAuth.getInstance().getUid(), new Callback() {
            @Override
            public void onSuccess() {
                user_pic_comment.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure() { user_pic_comment.setVisibility(View.VISIBLE); }
        });
    }

    private void setOnClicks() {
        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseStorageHelper.check_(post.getPostId(), "p", "likers_ids", "likes",null, new PostReturnCallback() {
                    @Override
                    public void onSuccess(String l, String d) {
                        likes.setText(l);
                    }
                    @Override
                    public void onFailure(String l, String d) {
                        likes.setText(l);
                    }
                });
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BottomSheet.Builder(PostCommentActivity.this, R.style.BottomSheet_StyleDialog).title("").sheet(R.menu.more).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.report:
                                ToastDisplay.customMsgShort(PostCommentActivity.this, "Help me!");
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
}
