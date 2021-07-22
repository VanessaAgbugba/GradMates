package com.example.gradmates.Posts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.gradmates.Post;
import com.example.gradmates.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;

//This class is responsible for creating new posts
public class NewPostActivity extends AppCompatActivity {
    public static final String TAG = "NewPostActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public static final int PICK_PHOTO_CODE = 1042;
    private Button btnTakePhoto;
    private Button btnUploadPhoto;
    private Button btnSubmit;
    private ImageView ivPostImage;
    private EditText tvCaption;
    private EditText tvLocation;
    private EditText etAboutMe;
    private EditText etBudget;
    private File photoFile;
    public String photoFileName = "photo.jpg";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivPostImage = findViewById(R.id.ivPostImage);
        tvCaption = findViewById(R.id.tvCaption);
        tvLocation = findViewById(R.id.tvLocation);
        etAboutMe = findViewById(R.id.etAboutMe);
        etBudget = findViewById(R.id.etBudget);

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickPhoto(v);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = tvCaption.getText().toString();
                String location = tvLocation.getText().toString();
                String aboutMe = etAboutMe.getText().toString();
                String budget = etBudget.getText().toString();
                if(description.isEmpty()){
                    Toast.makeText(NewPostActivity.this, "Description box cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(location.isEmpty()){
                    Toast.makeText(NewPostActivity.this, "Location box cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(aboutMe.isEmpty()){
                    Toast.makeText(NewPostActivity.this, "AboutMe box cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(budget.isEmpty()){
                    Toast.makeText(NewPostActivity.this, "Budget box cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(photoFile == null || ivPostImage.getDrawable() == null){
                    Toast.makeText(NewPostActivity.this, "Image box cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description,aboutMe, budget, location, currentUser, photoFile);

            }
        });
    }

    private void launchCamera() {
        //Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(NewPostActivity.this, "com.codepath.fileprovider.gradmates", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((data != null) && requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            Uri photoUri = data.getData();

            Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            ivPostImage.setImageBitmap(takenImage);
        } else {
            Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT).show();
        }
        //uploading image
        if ((data != null) && requestCode == PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();

            Bitmap selectedImage = loadFromUri(photoUri);

            // Load the selected image into a preview
            ImageView ivPreview = (ImageView) findViewById(R.id.ivPostImage);
            ivPreview.setImageBitmap(selectedImage);
        }
    }
    private File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "Failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);
        return file;
    }

    private void savePost(String description,String aboutMe,String budget,String location, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setLocation(location);
        post.setAboutMe(aboutMe);
        post.setBudget(budget);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null)
                {
                    Log.i(TAG, "Error saving post", e);
                    Toast.makeText(NewPostActivity.this, "Error saving post", Toast.LENGTH_SHORT).show();
                }
                tvCaption.setText("");
                tvLocation.setText("");
                etAboutMe.setText("");
                etBudget.setText("");

                ivPostImage.setImageResource(0);
            }
        });
    }
    public void onPickPhoto(View view) {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }
    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
           Log.e(TAG, "Error loading image", e);
        }
        return image;
    }
}