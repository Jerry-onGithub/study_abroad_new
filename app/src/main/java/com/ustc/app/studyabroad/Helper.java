package com.ustc.app.studyabroad;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ustc.app.studyabroad.homeActivities.FifthActivity;
import com.ustc.app.studyabroad.homeActivities.FirstActivity;
import com.ustc.app.studyabroad.homeActivities.FourthActivity;
import com.ustc.app.studyabroad.homeActivities.SecondActivity;
import com.ustc.app.studyabroad.homeActivities.ThirdActivity;
import com.ustc.app.studyabroad.interfaces.Callback;
import com.ustc.app.studyabroad.interfaces.ReturnCallback;
import com.ustc.app.studyabroad.models.AddressInfo;
import com.ustc.app.studyabroad.models.Time;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.util.ByteArrayBuffer;

public class Helper {

    public static void print(String s){
        System.out.println(s);
    }

    public static void getItem(String[] s, Context c, AutoCompleteTextView v){
        List<String> list = new ArrayList<>(Arrays.asList(s));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (c, android.R.layout.select_dialog_item, list);
        v.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        v.setTextColor(Color.BLACK);
    }

    public static void getItemResponse(String[] s, Context c, AutoCompleteTextView v, ReturnCallback callback){
        List<String> list = new ArrayList<>(Arrays.asList(s));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (c, android.R.layout.select_dialog_item, list);
        v.setThreshold(1);//will start working from first character
        v.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        v.setTextColor(Color.BLACK);

        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                callback.onSuccess(selectedItem);
                //System.out.println("selectedItem >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + selectedItem);
            }
        });
    }

    @SuppressLint("Range")
    public static String getNameFromURI(@NonNull Context context, @NonNull Uri uri) {
        String result = null;
        Cursor c = null;
        try {
            c = context.getContentResolver().query(uri, null, null, null, null);
            c.moveToFirst();
            result = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        }
        catch (Exception e){
            // error occurs
        }
        finally {
            if(c != null){
                c.close();
            }
        }
        return result;
    }

    public static void setDate(TextView tx, Context ctx) {
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                DatePickerDialog datePickerDialog = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        tx.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    public static void setResumeFilename(String resume, TextView resume_filename) {
        if(resume.toLowerCase().contains("png")){
            resume_filename.setText("resume."+"png");
        }
        if(resume.toLowerCase().contains("jpeg")){
            resume_filename.setText("resume."+"jpeg");
        }
        if(resume.toLowerCase().contains("pdf")){
            resume_filename.setText("resume."+"pdf");
        }
        if(resume.toLowerCase().contains("jpg")){
            resume_filename.setText("resume."+"jpg");
        }
    }

    public static AddressInfo getAddress(Context context, double LATITUDE, double LONGITUDE){
        AddressInfo found_address = null;
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                found_address = new AddressInfo(address, city, state, country, postalCode, knownName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return found_address;
    }

    public static Time printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return new Time(Long.toString(elapsedDays), Long.toString(elapsedHours), Long.toString(elapsedMinutes), Long.toString(elapsedSeconds));
    }

    public static String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String now = formatter.format(date);
        System.out.println("NOW   " + now);
        return now;
    }

    public static String setTime(String post) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String now = formatter.format(date);
        String re = null;
        try {
            Date date1 = formatter.parse(post);
            Date date2 = formatter.parse(now);
            Time time = Helper.printDifference(date1, date2);
            String d=time.getDays(), h=time.getHours(), m=time.getMinutes(), s=time.getSeconds();
            if(!d.equals("0")){
                if(!d.equals("1")) {
                    re=d + " days ago";
                } else {
                    re=d + " day ago";
                }
            } else if(!h.equals("0")){
                if(!h.equals("1")) {
                    re=h + " hours ago";
                } else {
                    re=h + " hour ago";
                }
            } else if(!m.equals("0")){
                if(!m.equals("1")) {
                    re=m + " minutes ago";
                } else {
                    re=m + " minute ago";
                }
            } else if(!s.equals("0")){
                if(!s.equals("1")) {
                    re=s + " seconds ago";
                } else {
                    re=s + " second ago";
                }
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return re;
    }

    public static void setBottomNavigationView(BottomNavigationView bottomNavigationView, Context context) {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.one:
                        return start(context, FirstActivity.class);
                    case R.id.two:
                        return start(context, SecondActivity.class);
                    case R.id.three:
                        return start(context, ThirdActivity.class);
                    case R.id.four:
                        return start(context, FourthActivity.class);
                    case R.id.five:
                        return start(context, FifthActivity.class);
                }
                return false;
            }
        });
    }

    private static boolean start(Context context, Class activity) {
        ((Activity) context).startActivity(new Intent(context, activity));
        ((Activity) context).overridePendingTransition(0,0);
        return true;
    }

    public static String capitalize(String str) {
        if(str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_circular);
        // dialog.setMessage(Message);
        return dialog;
    }

    public static ProgressDialog createProgressDialogLinear(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.loading_linear);
        // dialog.setMessage(Message);
        return dialog;
    }

    public static Object[] getSortedArray(Object[] objArray, Comparator<Object> comparator) {
        Object[] sorted = objArray.clone();
        Arrays.sort(sorted, comparator);
        return sorted;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = null;
        connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void alertDialog(Activity context, Drawable d, String title, String msg, Callback callback){
        AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.Style_Dialog_Rounded_Corner)
                .setIcon(d)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.onSuccess();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        callback.onFailure();
                    }
                })
                .show();
    }

    public static void slider(HorizontalScrollView hv) {
        ObjectAnimator animator1 = ObjectAnimator.ofInt(hv, "scrollX",  7000).setDuration(10000);
        ObjectAnimator animator2 = ObjectAnimator.ofInt(hv, "scrollX",  0).setDuration(10000);

        animator1.start();
        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animator2.start();
            }
        });

        animator2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animator1.start();
            }
        });
    }

    public static void downloadFile(Context context, String resume, ReturnCallback callback) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean downloading = true;
                try {
                    URL url = new URL(resume);
                    URLConnection ucon = url.openConnection();
                    ucon.setReadTimeout(5000);
                    ucon.setConnectTimeout(10000);
                    InputStream is = ucon.getInputStream();
                    BufferedInputStream inStream = new BufferedInputStream(is, 1024 * 5);

                    File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "/resume.pdf");

                    if (file.exists()) {
                        file.delete();
                    }
                    file.createNewFile();
                    FileOutputStream outStream = new FileOutputStream(file);
                    byte[] buff = new byte[5 * 1024];
                    int len;
                    while ((len = inStream.read(buff)) != -1) {
                        outStream.write(buff, 0, len);
                    }
                    outStream.flush();
                    outStream.close();
                    inStream.close();

                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> SUCCESS");
                    callback.onSuccess(file.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>> FAIL");
                    callback.onFailure("");
                }
            }
        });
        thread.start();
    }

}
