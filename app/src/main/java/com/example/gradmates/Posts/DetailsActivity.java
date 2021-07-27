package com.example.gradmates.Posts;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.gradmates.MapFragment;
import com.example.gradmates.ParcelableObject;
import com.example.gradmates.Post;
import com.example.gradmates.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.codepath.asynchttpclient.AsyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.Date;

import okhttp3.Headers;

//This is the activity that expands the details of a post
public class DetailsActivity extends AppCompatActivity {

    public static final String TAG = "DetailsActivity";
    private TextView tvUsername;
    private ImageView ivImage;
    private TextView tvDescription;
    private TextView tvLocation;
    private TextView tvAboutMe;
    private TextView tvBudget;
    private TextView timestamp;
    public Fragment fragment;
    JSONObject jsonObject;
    JSONArray jsonArrayResults;
    public Double longitude, latitude;

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
        timestamp = findViewById(R.id.timeStamp);

        ParcelableObject objectReceived = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        Post postReceived = objectReceived.getPost();
        Log.d("DetailsActivity", "User received = " + postReceived.getDescription());

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
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        //Using geocode to get the longitude and latitude of the location
        client.get("https://maps.googleapis.com/maps/api/geocode/json?address=" + postReceived.getLocation() + "&key=AIzaSyCmMy40SbmX4ZDHWGyLlhUD_OPLYepw0Jg", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Access a JSON object response with `json.jsonObject`
                Log.d("DEBUG OBJECT", json.jsonObject.toString());
                jsonObject = json.jsonObject;
                //result-> geometry-> location-> long/lat
                try {
                    jsonArrayResults = (JSONArray) jsonObject.get("results");
                    Log.i("Results", String.valueOf(jsonArrayResults));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    JSONObject geometry = (JSONObject) jsonArrayResults.getJSONObject(0).get("geometry");
                    JSONObject location = (JSONObject) geometry.get("location");
                    latitude = (Double) location.get("lat");
                    longitude = (Double) location.get("lng");
                    Log.i("geometry", String.valueOf(geometry));
                    Log.i("location", String.valueOf(location));
                    Log.i("latitude", String.valueOf(latitude));
                    Log.i("longitude", String.valueOf(longitude));

                    //Initialize fragment
                    Fragment fragment = new MapFragment(latitude, longitude);

                    //Open fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, fragment).commit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Toast.makeText(DetailsActivity.this, "Failure retrieving location", Toast.LENGTH_SHORT).show();
            }
        });

        Log.i("DetailsActivity", "sent geocode request");

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
