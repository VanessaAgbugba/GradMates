package com.example.gradmates.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gradmates.ParcelableObject;
import com.example.gradmates.Post;
import com.example.gradmates.R;
import com.example.gradmates.authentication.LoginActivity;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class ProfileActivity extends AppCompatActivity {
    private ImageView ivProfilePic;
    private TextView tvUserName;
    private TextView tvAge;
    private TextView tvGender;
    private TextView tvPronouns;
    private TextView tvEmail;
    private TextView tvLocation;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvUserName = findViewById(R.id.tvUser);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        tvPronouns = findViewById(R.id.tvPronouns);
        tvEmail = findViewById(R.id.tvEmail);
        tvLocation = findViewById(R.id.tvLocation);
        btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();

                if(currentUser != null) {
                    ParseUser.logOut();
                    goToLoginActivity();
                }
            }
        });


    }
    public void goToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
