package com.example.gradmates;
import com.parse.Parse;
import android.app.Application;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("omThNBQ0qrI17eYRjzsyYcM9ZLUcjpjHUyIHY91e")
                .clientKey("NU7TfxxXc6rBFYykDf5MRwOIHN2Xtjn8pNRVbeb9")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

}
