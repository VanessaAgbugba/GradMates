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
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

//This class defines the fragments for the app
public class ComposeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    public Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
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

    //This class is defines the setter and getter functions for the post
        @ParseClassName("Post")
        public static class Post extends ParseObject {

            public static final String KEY_DESCRIPTION = "description";
            public static final String KEY_IMAGE = "image";
            public static final String KEY_USER = "user";
            public static final String KEY_LOCATION = "location";
            public static final String KEY_ABOUT_ME = "about_me";
            public static final String KEY_BUDGET= "budget";
            public static final String KEY_AVAILABLE_DATE = "available_date";


            public String getDescription() {
                return getString(KEY_DESCRIPTION);
            }

            public void setDescription(String description) {
                put(KEY_DESCRIPTION, description);
            }

            public String getLocation() {
            return getString(KEY_LOCATION);
        }

            public void setLocation(String location) {
            put(KEY_LOCATION, location);
            }

            public String getAboutMe() {
            return getString(KEY_ABOUT_ME);
        }

            public void setAboutMe(String aboutMe) {
            put(KEY_ABOUT_ME, aboutMe);
            }

            public String getBudget() {
            return getString(KEY_BUDGET);
        }

            public void setBudget(String budget) {
                put(KEY_BUDGET, budget);
            }

            public String getAvailableDate() {
                return getString(KEY_AVAILABLE_DATE);
            }
            public void setAvailableDate(String availableDate) {
            put(KEY_AVAILABLE_DATE, availableDate);
            }
            public Date getDateObject() throws java.text.ParseException {

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String dateInString = getAvailableDate();
                Date date = sdf.parse(dateInString);

            return date;

            }

            public ParseFile getImage() {
                return getParseFile(KEY_IMAGE);
            }

            public void setImage(ParseFile parseFile) {
                put(KEY_IMAGE, parseFile);
            }

            public ParseUser getUser() {
                ParseUser user = getParseUser(KEY_USER);
                try {
                    user.fetchIfNeeded();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return user;
            }

            public void setUser(ParseUser user) {
                put(KEY_USER, user);
            }
    }
}