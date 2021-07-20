package com.example.gradmates;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
//This class allows the user to search through posts in FeedActivity
public class SearchActivity extends AppCompatActivity {
    SearchView svSearch;
    EditText etSearchBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        svSearch = findViewById(R.id.svSearch);
        etSearchBar = findViewById(R.id.etSearchBar);
    }
}
