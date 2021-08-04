package com.example.gradmates.Posts;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gradmates.DatePickerFragment;
import com.example.gradmates.Post;
import com.example.gradmates.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

//Fragment for NewPostActivity
public class ComposeFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    public static final String TAG = "NewPostActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public static final int PICK_PHOTO_CODE = 1042;
    private Button btnTakePhoto;
    private Button btnUploadPhoto;
    private Button btnSubmit;
    private ImageView ivPostImage;
    private TextView tvCaption;
    private EditText tvLocation;
    private EditText etAboutMe;
    private EditText etBudget;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    private Button minStay;
    private TextView tvMinStay;

    public ComposeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnTakePhoto = view.findViewById(R.id.btnTakePhoto);
        btnUploadPhoto = view.findViewById(R.id.btnUploadPhoto);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        ivPostImage = view.findViewById(R.id.ivPostImage);
        tvCaption = view.findViewById(R.id.tvCaption);
        tvLocation = view.findViewById(R.id.tvLocation);
        etAboutMe = view.findViewById(R.id.etAboutMe);
        etBudget = view.findViewById(R.id.etBudget);
        minStay = view.findViewById(R.id.minStay);
        tvMinStay = view.findViewById(R.id.tvMinStay);

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
                    Toast.makeText(getContext(), "Description box cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(location.isEmpty()){
                    Toast.makeText(getContext(), "Location box cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(aboutMe.isEmpty()){
                    Toast.makeText(getContext(), "AboutMe box cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(budget.isEmpty()){
                    Toast.makeText(getContext(), "Budget box cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(photoFile == null || ivPostImage.getDrawable() == null){
                    Toast.makeText(getContext(), "Image box cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description,aboutMe, budget, location, currentUser, photoFile);
            }
        });

        minStay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.setTargetFragment(ComposeFragment.this, 0);
                datePicker.show(getActivity().getSupportFragmentManager(), "date picker");
            }
        });
    }

    private void launchCamera() {
        //Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider.gradmates", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((data != null) && requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            Uri photoUri = data.getData();

            Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            ivPostImage.setImageBitmap(takenImage);
        }
        //uploading image
        if ((data != null) && requestCode == PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();

            Bitmap selectedImage = loadFromUri(photoUri);

            ImageView ivPreview;
            ivPreview = getView().findViewById(R.id.ivPostImage);
            ivPreview.setImageBitmap(selectedImage);
        }
    }
    private File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

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
                    Toast.makeText(getContext(), "Error saving post", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(intent, PICK_PHOTO_CODE);
    }
    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContext().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error loading image", e);
        }
        return image;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance().format(calendar.getTime());
        tvMinStay.setText(currentDateString);
    }
}