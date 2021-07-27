package com.example.gradmates.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.gradmates.R;
import com.example.gradmates.authentication.LoginActivity;
import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
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

        if(ParseUser.getCurrentUser() != null)
        {
            tvUserName.setText("Username: " + ParseUser.getCurrentUser().getUsername());
            tvEmail.setText("Email: " + ParseUser.getCurrentUser().getEmail());
            tvAge.setText("Age: " + ParseUser.getCurrentUser().get("Age"));
            tvGender.setText("Gender: " + ParseUser.getCurrentUser().get("Gender"));
            tvPronouns.setText("Pronouns: " + ParseUser.getCurrentUser().get("Pronouns"));

            ParseUser user = ParseUser.getCurrentUser();
            Glide.with(this)
                    .load(user.getParseFile("profileImage").getUrl())
                    .circleCrop() // create an effect of a round profile picture
                    .into(ivProfilePic);
        }
    }

    public void goToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }



}
