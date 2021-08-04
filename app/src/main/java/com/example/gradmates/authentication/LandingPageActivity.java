package com.example.gradmates.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.gradmates.Posts.ComposeActivity;
import com.example.gradmates.R;
import com.parse.ParseUser;
//Landing Page
//This is the first page users see when they open the app(If they have not logged in). It holds the logo, login and sign up button.
public class LandingPageActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnSignUp;
    ImageView icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        if(ParseUser.getCurrentUser() != null){
            goComposeActivity();
        }

        icon = findViewById(R.id.icon);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.anim);
        icon.startAnimation(animation);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogin();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUp();
            }
        });
    }

    public void onLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void onSignUp() {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
        finish();
    }

    private void goComposeActivity() {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivity(i);
        finish();
    }
}