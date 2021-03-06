package com.example.gradmates.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gradmates.Posts.PostsFragment;
import com.example.gradmates.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
//Description: This class is responsible for user sign up
public class SignupActivity extends AppCompatActivity {
    public static final String TAG = "SignUpActivity";
    EditText etNewUser;
    EditText etEmail;
    EditText etNewPassword;
    Button goToSignIn;
    Button btnSignUp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etNewUser = findViewById(R.id.etNewUser);
        etEmail = findViewById(R.id.etEmail);
        etNewPassword = findViewById(R.id.etNewPassword);
        goToSignIn = findViewById(R.id.goToSignIn);
        goToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginActivity();
            }
        });
        btnSignUp2 = findViewById(R.id.btnSignUp2);
        btnSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etNewUser.getText().toString();
                String password = etNewPassword.getText().toString();
                String email = etEmail.getText().toString();

                signUpUser(username, password, email);
               goToLoginActivity();
            }
        });
    }

    private void signUpUser(String username, String password, String email) {
        Log.i(TAG, "Attempting to login user");

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                //If no one has signed in
                if(e == null) {
                    goToLoginActivity();
                }
                else{
                    Log.e(TAG, "Error signing up");
                }
            }
        });
    }

    public  void goToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
