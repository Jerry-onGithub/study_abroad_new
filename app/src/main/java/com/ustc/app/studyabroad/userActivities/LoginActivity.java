package com.ustc.app.studyabroad.userActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ustc.app.studyabroad.Helper;
import com.ustc.app.studyabroad.R;
import com.ustc.app.studyabroad.homeActivities.FirstActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button buttonRegLogin = (Button) findViewById(R.id.bRegLogin);
        TextView registerLink = (TextView) findViewById(R.id.tvRegLink);
        TextView forgotPass = (TextView) findViewById(R.id.forgotPassword);

        buttonRegLogin.setOnClickListener(this);

        //registration page access
        registerLink.setOnClickListener(v -> {
            Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            LoginActivity.this.startActivity(registerIntent);
        });

        forgotPass.setOnClickListener(v -> {
            Intent forgotPassword = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            LoginActivity.this.startActivity(forgotPassword);
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.bRegLogin) {
            startSignIn();
        }
    }

    private void startSignIn(){
        final EditText etRegAcc = (EditText) findViewById(R.id.etRegAcc);
        final EditText etRegPass = (EditText) findViewById(R.id.etRegPass);
        final String email = etRegAcc.getText().toString();
        //String shaPassword = sha256(etRegPass.getText().toString());

        ProgressDialog progressDialog = Helper.createProgressDialog(LoginActivity.this);
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, etRegPass.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        progressDialog.dismiss();
                        Intent loginIntent = new Intent(LoginActivity.this, FirstActivity.class);
                        LoginActivity.this.startActivity(loginIntent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                        progressDialog.dismiss();
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(getApplicationContext(), "Successfully signed in!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error: sign in failed.", Toast.LENGTH_SHORT).show();
        }
    }

}