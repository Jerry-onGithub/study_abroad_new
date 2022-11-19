package com.ustc.app.studyabroad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ustc.app.studyabroad.interfaces.Callback;

public class UpdateUser {

    private void changeUsername(Context context, FirebaseUser user, Callback callback) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText edittext = new EditText(context);
        //alert.setMessage("Change username");
        alert.setTitle("Enter new username \n");
        alert.setView(edittext);
        alert.setPositiveButton("Done", (dialog, whichButton) -> {
            String newUserName = edittext.getText().toString();
/*            progressDialog.setMessage("Changing username ... please wait");
            progressDialog.show();*/
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newUserName).build();
            assert user != null;
            user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //progressDialog.cancel();
                    //Toast.makeText(getApplicationContext(), "Username Changed!", Toast.LENGTH_SHORT).show();
                    callback.onSuccess();
                }
                else{
                    callback.onFailure();
                }
            });
        });
        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
            // what ever you want to do with No option.
            dialog.dismiss();
        });
        alert.show();
    }

    public void changeEmail(Context context, FirebaseUser user, Callback callback){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final EditText edittext = new EditText(context);
        //alert.setMessage("Change username");
        alert.setTitle("Enter new email address \n");
        alert.setView(edittext);
        alert.setPositiveButton("Done", (dialog, whichButton) -> {
            String newEmail = edittext.getText().toString();
/*            progressDialog.setMessage("Changing email address... please wait");
            progressDialog.show();*/
            user.updateEmail(newEmail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    //Toast.makeText(getApplicationContext(), "Email changed!", Toast.LENGTH_SHORT).show();
                    //progressDialog.cancel();
                    callback.onSuccess();
                }
                else{
                    /*Toast.makeText(getApplicationContext(), "Email could not be changed!", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();*/
                    callback.onFailure();
                }
            });
        });
        alert.setNegativeButton("Cancel", (dialog, whichButton) -> {
            // what ever you want to do with No option.
            dialog.dismiss();
        });
        alert.show();
    }

    public void deleteAccount(Context context, FirebaseUser user, Callback callback){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("Are you sure you want to delete this account? \n");
        alert.setTitle("Delete Account \n");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if(user!=null) {
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                                db.removeValue();
                                //Toast.makeText(getApplicationContext(), "Account successfully deactivated!", Toast.LENGTH_LONG).show();
                                Log.d("delete ", "delete success");
                                callback.onSuccess();
                            }
                            else{
                                callback.onFailure();
                            }
                        }
                    });
                }
                else{

                }
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
