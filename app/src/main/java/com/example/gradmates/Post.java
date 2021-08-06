package com.example.gradmates;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

//This class is defines the setter and getter functions for the post
@ParseClassName("Post")
public class Post extends ParseObject {

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
        //user = ParseUser.getCurrentUser();
//        try {
//            user.fetchIfNeeded();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        return user;
    }
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }
}
