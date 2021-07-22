package com.example.gradmates.Posts;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.gradmates.MapFragment;
import com.example.gradmates.ParcelableObject;
import com.example.gradmates.Post;
import com.example.gradmates.R;
import com.parse.ParseFile;
import com.parse.ParseUser;
import org.parceler.Parcels;

import java.util.Date;

//This is the activity that expands the details of a post
public class DetailsActivity extends AppCompatActivity {

    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvLocation;
    private TextView tvAboutMe;
    private TextView tvBudget;
    private TextView timestamp;
    public Fragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Initialize fragment
        Fragment fragment = new MapFragment();

        //Open fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, fragment).commit();

        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvLocation = findViewById(R.id.tvLocation);
        tvAboutMe = findViewById(R.id.tvAboutMe);
        tvBudget = findViewById(R.id.tvBudget);
        timestamp = findViewById(R.id.timeStamp);

        ParcelableObject objectReceived = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        Post postReceived = objectReceived.getPost();
        Log.d("DetailsActivity", "Post received = " + postReceived.getDescription());

        ParseUser postUser = postReceived.getUser();

        ParseFile image = postReceived.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivImage);
        }

        if (tvDescription != null) {
            tvDescription.setText(postReceived.getDescription());
        }

        if (tvLocation != null) {
            tvLocation.setText(postReceived.getLocation());
        }

        if (tvAboutMe != null) {
            tvAboutMe.setText(postReceived.getAboutMe());
        }

        if (tvBudget != null) {
            tvBudget.setText(postReceived.getBudget());
        }

        if(postUser != null){
            Log.d("DetailsActivity", "user = " + postUser.getUsername());
            tvUsername.setText(postUser.getUsername());
        }
        if(timestamp != null) {
            timestamp.setText(calculateTimeAgo(postReceived.getCreatedAt()));
        }

    }
    public static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " days ago";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }
        return "";
    }
}