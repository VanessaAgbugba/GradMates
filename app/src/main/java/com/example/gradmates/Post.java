package com.example.gradmates;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
//Description: This class is defines the setter and getter functions for the post
    @ParseClassName("Post")
    public class Post extends ParseObject {

        public static final String KEY_DESCRIPTION = "description";
        public static final String KEY_IMAGE = "image";
        public static final String KEY_USER = "user";

        public String getDescription() {
            return getString(KEY_DESCRIPTION);
        }

        public void setDescription(String description) {
            put(KEY_DESCRIPTION, description);
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
