package com.example.gradmates;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;
import org.parceler.Parcels;

//This is the activity that expands the details of a post
public class DetailsActivity extends AppCompatActivity {

    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvLocation;
    private TextView tvAboutMe;
    private TextView tvBudget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvDescription = findViewById(R.id.tvDescription);
        tvLocation = findViewById(R.id.tvLocation);
        tvAboutMe = findViewById(R.id.tvAboutMe);
        tvBudget = findViewById(R.id.tvBudget);

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
    }
}