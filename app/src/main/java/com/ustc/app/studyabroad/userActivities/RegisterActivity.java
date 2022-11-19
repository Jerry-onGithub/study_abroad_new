package com.ustc.app.studyabroad.userActivities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        final TextView login = (TextView) findViewById(R.id.login);
        final Button bRegSubmit = (Button) findViewById(R.id.regButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bRegSubmit.setOnClickListener(this);
    }

    private void startSignIn() {
        final EditText etRegName = (EditText) findViewById(R.id.username);
        final EditText etRegAcc = (EditText) findViewById(R.id.email);
        final EditText etRegPass = (EditText) findViewById(R.id.password);
        final EditText etRegRepeatPass = (EditText) findViewById(R.id.password2);
        final String email = etRegAcc.getText().toString();
        final String password = etRegPass.getText().toString();
        final String password2 = etRegRepeatPass.getText().toString();
        final int passLength = password.length();
        if (passLength >= 6) {
            if (passLength < 19) {
                if (password.equals(password2)) {

                    ProgressDialog progressDialog = Helper.createProgressDialog(RegisterActivity.this);
                    progressDialog.show();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "createUserWithEmail:success");
                                    progressDialog.dismiss();
                                    //progressBar.setVisibility(View.GONE);
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(etRegName.getText().toString()).build();
                                    user.updateProfile(profileUpdates);
                                    finish();
                                } else {
                                    mAuth.fetchSignInMethodsForEmail(email)
                                            .addOnCompleteListener(task1 -> {
                                                boolean isNewUser = task1.getResult().getSignInMethods().isEmpty();
                                                if (!isNewUser) {
                                                    Toast.makeText(getApplicationContext(), "User by this email already exists!",
                                                            Toast.LENGTH_SHORT).show();                                                            }
                                            });
                                    try
                                    {
                                        throw task.getException();
                                    }
                                    // if user enters wrong email.
                                    catch (FirebaseAuthInvalidUserException invalidEmail)
                                    {
                                        Log.d("TAG", "onComplete: invalid_email");
                                        Toast.makeText(getApplicationContext(), "Invalid email!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    // if user enters wrong password.
                                    catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                                    {
                                        Log.d("TAG", "onComplete: wrong_password");
                                        Toast.makeText(getApplicationContext(), "Wrong password!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    catch (Exception e) {
                                        Log.d("TAG", "onComplete: " + e.getMessage());
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                        progressDialog.dismiss();
                                    }

                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Password mismatch!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Password can't be more than 18 symbols!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Password must be at least 6 symbols!", Toast.LENGTH_SHORT).show();
        }
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(getApplicationContext(), "Successfully registered!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error: registration failed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.regButton) {
            startSignIn();
        }
    }
}