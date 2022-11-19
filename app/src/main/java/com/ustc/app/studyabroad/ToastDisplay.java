package com.ustc.app.studyabroad;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastDisplay {

    public static void connErrorMsg(Context c){
        Toast toast = Toast.makeText(c, "Connection error, please try again!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void customMsg(Context c, String msg){
        Toast toast = Toast.makeText(c, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    public static void customMsgShort(Context c, String msg){
        Toast toast = Toast.makeText(c, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
