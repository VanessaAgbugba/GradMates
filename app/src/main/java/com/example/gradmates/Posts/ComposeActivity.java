package com.example.gradmates.Posts;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.gradmates.Profile.ProfileFragment;
import com.example.gradmates.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

//This class defines the fragments for the app
public class ComposeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    public Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        //getSupportActionBar().setElevation(0);
        final FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.actionHome:
                        fragment = new PostsFragment();

                        break;
                    case R.id.actionCompose:
                        fragment = new ComposeFragment();

                        break;
                    case R.id.actionProfile:
                        fragment = new ProfileFragment();
                    default:

                        break;
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.flContainer, fragment)
                        .addToBackStack(null)
                        .commit();
                return true;
            }
        });
        //Default page
        bottomNavigationView.setSelectedItemId(R.id.actionHome);
    }

}