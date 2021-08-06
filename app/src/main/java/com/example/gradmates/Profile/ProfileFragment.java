package com.example.gradmates.Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gradmates.R;
import com.example.gradmates.authentication.LandingPageActivity;
import com.example.gradmates.authentication.LoginActivity;
import com.parse.ParseUser;

//Fragment for ProfileActivity
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileActivity";
    private ImageView ivProfilePic;
    private TextView tvUserName;
    private TextView tvAge;
    private TextView tvGender;
    private TextView tvPronouns;
    private TextView tvEmail;
    private TextView tvLocation;
    private ImageButton btnLogout;
    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvUserName = view.findViewById(R.id.tvUser);
        tvAge = view.findViewById(R.id.tvAge);
        tvGender = view.findViewById(R.id.tvGender);
        tvPronouns = view.findViewById(R.id.tvPronouns);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvLocation = view.findViewById(R.id.tvLocation);
        btnLogout = view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();

                if(currentUser != null) {
                    ParseUser.logOut();
                    goToLandingPageActivity();
                }
            }
        });
        if(ParseUser.getCurrentUser() != null)
        {
            tvUserName.setText(ParseUser.getCurrentUser().getUsername());
            tvEmail.setText(ParseUser.getCurrentUser().getEmail());
            tvAge.setText((String) ParseUser.getCurrentUser().get("Age"));
            tvGender.setText((String) ParseUser.getCurrentUser().get("Gender"));
            tvPronouns.setText((String) ParseUser.getCurrentUser().get("Pronouns"));

            ParseUser user = ParseUser.getCurrentUser();
            Glide.with(this)
                    .load(user.getParseFile("profileImage").getUrl())
                    .circleCrop() // create an effect of a round profile picture
                    .into(ivProfilePic);
        }
    }

    public void goToLandingPageActivity() {
        Intent i = new Intent(getActivity(), LandingPageActivity.class);
        startActivity(i);
        getActivity().finish();
    }
}
