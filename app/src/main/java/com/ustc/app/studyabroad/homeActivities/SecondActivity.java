package com.ustc.app.studyabroad.homeActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.rpc.Help;
import com.ustc.app.studyabroad.FirebaseStorageHelper;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.MyLocation;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.adapters.PostsAdapter;
import com.ustc.app.studyabroad.adapters.SearchViewAdapter;
import com.ustc.app.studyabroad.interfaces.Callback;
import com.ustc.app.studyabroad.interfaces.GetPostsCallback;
import com.ustc.app.studyabroad.models.AddressInfo;
import com.ustc.app.studyabroad.search.SearchablePostItems;
import com.ustc.app.studyabroad.userModels.Post;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class SecondActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerPosts;
    private Spinner topic;
    ArrayAdapter<String> schoolSpinnerAdapter;
    CircleImageView profile_img;
    EditText message;
    LinearLayout photo, progress_bar;
    TextView photo_name, submit, cur_location, title;
    private Uri imagePath;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ImageView search, back;
    public static PostsAdapter postsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        setView();
        bottomNavigationView.setSelectedItemId(R.id.two);
        Helper.setBottomNavigationView(bottomNavigationView, SecondActivity.this);
        setSchoolSpinner();
        setAdapter();
        setImage();
        //cur_location.setVisibility(View.GONE);
        //setLocation();
        choosePostImage();
        submitPost();
        setOnClicks();
        setSearchableTopic("", "");
    }

    public void setSearchableTopic(String topic, String msg) {
        if(topic!=""){
            recyclerPosts.removeAllViewsInLayout();
            ProgressDialog progressDialog = Helper.createProgressDialog(SecondActivity.this);
            progressDialog.show();
            Helper.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+topic);

            title.setText("Discussions "+topic);
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setAdapter();
                    back.setVisibility(View.GONE);
                    title.setText("Discussions");
                }
            });

            FirebaseStorageHelper.searchPost("", topic, new GetPostsCallback() {
                @Override
                public void onSuccess(List<Post> posts) {
                    if(posts.size()>0) {
                        recyclerPosts = findViewById(R.id.recycler_posts);
                        recyclerPosts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerPosts.setAdapter(new PostsAdapter(SecondActivity.this, posts));
                    } else {
                        ToastDisplay.customMsg(getApplicationContext(), "No search result found");
                    }
                    progressDialog.dismiss();
                }
                @Override
                public void onFailure(String er) {
                    if(er.equals("f")) {
                        ToastDisplay.customMsg(getApplicationContext(), "Failed to load posts");
                        progressDialog.dismiss();
                    } else{
                        ToastDisplay.customMsg(getApplicationContext(), "No search result found");
                    }
                }
            });
        }
    }

    private void setOnClicks() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchablePostItems postItems = new SearchablePostItems();
                postItems.show(getSupportFragmentManager(), "ModalBottomSheet");
            }
        });
    }

    private void setImage() {
        FirebaseStorageHelper.setImage(getApplicationContext(), profile_img, user.getUid(), new Callback() {
            @Override
            public void onSuccess() {
                profile_img.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure() { profile_img.setVisibility(View.VISIBLE); }
        });
    }

    private void setAdapter() {
        FirebaseStorageHelper.getPosts(new GetPostsCallback() {
            @Override
            public void onSuccess(List<Post> posts) {
                progress_bar.setVisibility(View.GONE);
                recyclerPosts = findViewById(R.id.recycler_posts);
                postsAdapter = new PostsAdapter(SecondActivity.this, posts);
                recyclerPosts.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerPosts.setAdapter(postsAdapter);
                recyclerPosts.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(String er) {
                if(er.equals("f")) {
                    progress_bar.setVisibility(View.GONE);
                    ToastDisplay.customMsg(getApplicationContext(), "Failed to load posts");
                } progress_bar.setVisibility(View.GONE);
            }
        });
    }

    private void setLocation() {
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                try {
                    AddressInfo info = Helper.getAddress(getApplicationContext(), location.getLatitude(), location.getLongitude());
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> LOCATION   " + info.getAddress()+";;;; "+info.getState());
                    cur_location.setText(info.getCountry());
                } catch (Exception e){
                    System.out.println("ERROR   " + e);
                    cur_location.setText("");
                }
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);
    }

    private void submitPost() {
        String time = Helper.getTime();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = new Post(topic.getSelectedItem().toString(), message.getText().toString(), cur_location.getText().toString(), time, 0, 0);
                FirebaseStorageHelper.savePost(post, imagePath);
                ToastDisplay.customMsg(getApplicationContext(), "Post created.");
                setAdapter();
            }
        });
    }

    private void choosePostImage() {
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), 1);
            }
        });
    }

    private void setView() {
        bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        topic = findViewById(R.id.topic);
        profile_img = findViewById(R.id.profile_img);
        message = findViewById(R.id.message);
        photo = findViewById(R.id.photo);
        photo_name = findViewById(R.id.image_name);
        submit = findViewById(R.id.submit);
        cur_location = findViewById(R.id.location);
        search = findViewById(R.id.search);
        back = findViewById(R.id.back);
        title = findViewById(R.id.title);
        progress_bar = findViewById(R.id.progress_bar);
    }

    private void setSchoolSpinner() {
        schoolSpinnerAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner_list_item, getResources().getStringArray(R.array.topics));
        schoolSpinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        topic.setAdapter(schoolSpinnerAdapter);
        topic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    topic.setSelection(position);
                    System.out.println("CURRENT: >>>>>>>" + topic.getSelectedItem().toString());
                    //((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            imagePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                //profile_img.setImageBitmap(bitmap);
                ToastDisplay.customMsgShort(getApplicationContext(), "Image uploaded");
                photo_name.setText(Helper.getNameFromURI(getApplicationContext(), imagePath));
            }
            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}
