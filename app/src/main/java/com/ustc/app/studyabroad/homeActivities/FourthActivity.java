package com.ustc.app.studyabroad.homeActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ustc.app.studyabroad.FirebaseStorageHelper;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.adapters.TabLayoutAdapter;
import com.ustc.app.studyabroad.interfaces.Callback;
import com.ustc.app.studyabroad.interfaces.GetUrlCallback;
import com.ustc.app.studyabroad.userActivities.EditProfileActivity;
import com.ustc.app.studyabroad.userActivities.LoginActivity;
import com.ustc.app.studyabroad.userActivities.ResetPasswordActivity;

import java.io.IOException;

public class FourthActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ImageView img, more;
    TextView username;
    TabLayout tabLayout;
    ViewPager viewPager;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Uri imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_nav_menu);
        bottomNavigationView.setSelectedItemId(R.id.four);
        Helper.setBottomNavigationView(bottomNavigationView, FourthActivity.this);

        String tab = "fourth";

        img = (ImageView) findViewById(R.id.profile_pic);
        username = (TextView) findViewById(R.id.username);
        more = findViewById(R.id.more);

        user = mAuth.getCurrentUser();
        username.setText(Helper.capitalize(user.getDisplayName()));
        setImage();
        onClicks();
        more();

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        tabLayout.addTab(tabLayout.newTab().setText("Profile"));
        tabLayout.addTab(tabLayout.newTab().setText("Decisions"));
        tabLayout.addTab(tabLayout.newTab().setText("Resume"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabLayoutAdapter adapter = new TabLayoutAdapter(tab, this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void onClicks() {
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), 22);
            }
        });
    }

    private void more() {
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(FourthActivity.this, more);
                popupMenu.getMenuInflater().inflate(R.menu.profile_toolbar, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:
                                Intent intentEditProfile = new Intent(getApplicationContext(), EditProfileActivity.class);
                                startActivity(intentEditProfile);
                                break;
                            case R.id.resetPw:
                                Intent intentLogin = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                                startActivity(intentLogin);
                                break;
                            case R.id.logout:
                                mAuth.signOut();
                                Intent intentResetPwd = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intentResetPwd);
                                ToastDisplay.customMsg(FourthActivity.this.getBaseContext(),"Logged out!");
                                break;
                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });
    }

    private void setImage() {
        FirebaseStorageHelper.setImage(getApplicationContext(), img, user.getUid(), new Callback() {
            @Override
            public void onSuccess() {
                img.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure() {
                img.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            imagePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                img.setImageBitmap(bitmap);

                saveImage(imagePath);
            }
            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void saveImage(Uri imagePath) {
        ProgressDialog progressDialog = Helper.createProgressDialog(FourthActivity.this);
        progressDialog.show();
        FirebaseStorageHelper.uploadImage(null, "profile", imagePath, new GetUrlCallback() {
            @Override
            public void onSuccess(String imgUrl) {
                progressDialog.dismiss();
                ToastDisplay.customMsgShort(FourthActivity.this, "Upload success.");
            }
            @Override
            public void onFailure(String msg) {
                progressDialog.dismiss();
                ToastDisplay.customMsg(FourthActivity.this, "Uploading failed. Please try again.");
            }
        });
    }
}
