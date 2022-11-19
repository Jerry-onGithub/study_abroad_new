package com.ustc.app.studyabroad.profileFragments;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.internal.Constants;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.rpc.Help;
import com.ustc.app.studyabroad.FirebaseStorageHelper;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.ToastDisplay;
import com.ustc.app.studyabroad.interfaces.Callback;
import com.ustc.app.studyabroad.interfaces.GetProfileCallback;
import com.ustc.app.studyabroad.interfaces.GetUrlCallback;
import com.ustc.app.studyabroad.interfaces.ReturnCallback;
import com.ustc.app.studyabroad.userActivities.EditProfileActivity;
import com.ustc.app.studyabroad.userModels.Profile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResumeFrag extends Fragment {
    View view;
    TextView upload_resume, no_resume, resume_file;
    LinearProgressIndicator progressIndicator;
    private ActivityResultLauncher<String[]> activityResultLauncher;
    Uri filePath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_resume, container, false);

        upload_resume = view.findViewById(R.id.upload_resume);
        no_resume = view.findViewById(R.id.no_resume);
        resume_file = view.findViewById(R.id.resume_file);
        progressIndicator = view.findViewById((R.id.progress_bar));

        setResume("");
        onClicks();

        return view;
    }

    private void onClicks() {
        upload_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intent, 234);
            }
        });
    }

    private void downloadResume(String resume) {
        resume_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog = Helper.createProgressDialog(getContext());
                progressDialog.show();
                Helper.downloadFile(getContext(), resume, new ReturnCallback() {
                    @Override
                    public void onSuccess(String filepath) {
                        progressDialog.dismiss();
                        getActivity().runOnUiThread(() -> {
                            Toast t = Toast.makeText(getActivity(), "File is saved in "+filepath,Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        });
                    }
                    @Override
                    public void onFailure(String f) {
                        progressDialog.dismiss();
                        getActivity().runOnUiThread(() -> {
                            Toast t = Toast.makeText(getActivity(), "Download failed, please try again.",Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        });
                    }
                });
            }
        });
    }

    private void setResume(String resume) {
        if(resume!=""){
            resume_file.setText("download resume file");
            upload_resume.setText("Change Resume");
            resume_file.setPaintFlags(upload_resume.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            no_resume.setVisibility(View.GONE);
            resume_file.setVisibility(View.VISIBLE);
            downloadResume(resume);
        } else {
            FirebaseStorageHelper.getResume(FirebaseStorageHelper.getUserId(), new GetUrlCallback() {
                @Override
                public void onSuccess(String url) {
                    resume_file.setText("download resume file");
                    upload_resume.setText("Change Resume");
                    resume_file.setPaintFlags(upload_resume.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                    downloadResume(url);
                }
                @Override
                public void onFailure(String f) {
                    no_resume.setVisibility(View.VISIBLE);
                    upload_resume.setText("Upload Resume");
                    resume_file.setVisibility(View.GONE);
                }
            });
        }

    }

    public ResumeFrag() {
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                Log.e("activityResultLauncher", "" + result.toString());
                Boolean areAllGranted = true;
                for (Boolean b : result.values()) {
                    areAllGranted = areAllGranted && b;
                }
                if (areAllGranted) {
                    System.out.println("TRUE >>>>>>>>>>>>>>>>>>>>>>>> ");
                    FirebaseStorageHelper.downloadFile(resume_file.getText().toString());
                }
            }
        });
    }

    @SuppressLint("Range")
    private String statusMessage(Cursor c) {
        String msg = "???";

        switch (c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
            case DownloadManager.STATUS_FAILED:
                msg = "Download failed!";
                break;

            case DownloadManager.STATUS_PAUSED:
                msg = "Download paused!";
                break;

            case DownloadManager.STATUS_PENDING:
                msg = "Download pending!";
                break;

            case DownloadManager.STATUS_RUNNING:
                msg = "Download in progress!";
                break;

            case DownloadManager.STATUS_SUCCESSFUL:
                msg = "Download complete!";
                break;

            default:
                msg = "Download is nowhere in sight";
                break;
        }

        return (msg);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 234 && data != null && data.getData() != null) {
            filePath = data.getData();
            String fileName = Helper.getNameFromURI(getContext(), filePath);

            Drawable unwrappedDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.admit);
            Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
            DrawableCompat.setTint(wrappedDrawable, Color.WHITE);
            Helper.alertDialog(getActivity(), wrappedDrawable, "Upload resume "+ fileName + " ?", "Resume " + fileName + " will be added to your profile.", new Callback() {
                @Override
                public void onSuccess() {
                    saveResume(filePath);
                }
                @Override
                public void onFailure() {

                }
            });
        }

    }

    private void saveResume(Uri file) {
        ProgressDialog progressDialog = Helper.createProgressDialog(getContext());
        progressDialog.show();
        FirebaseStorageHelper.uploadFile(file, new GetUrlCallback() {
            @Override
            public void onSuccess(String fileUrl) {
                progressDialog.dismiss();
                setResume(fileUrl);
                ToastDisplay.customMsg(getActivity(), "Upload success.");

            }
            @Override
            public void onFailure(String msg) {
                progressDialog.dismiss();
                ToastDisplay.customMsg(getActivity(), "Uploading failed. Please try again.");
            }
        });
    }

}



