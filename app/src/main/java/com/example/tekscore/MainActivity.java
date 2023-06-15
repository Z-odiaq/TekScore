package com.example.tekscore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView nameTextView;
    private Button btnMarks;
    private Button btnTimetable;
    private Button btnAnnouncements;
    private ImageView profileImageView;
    private TextView classTextView;

    private DatabaseReference userRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the buttons in the layout
        btnMarks = findViewById(R.id.btnMarks);
        btnTimetable = findViewById(R.id.btnTimetable);
        btnAnnouncements = findViewById(R.id.btnAnnouncements);
        //get nameTextView
        nameTextView = findViewById(R.id.nameTextView);
        profileImageView = findViewById(R.id.profileImageView);
        classTextView = findViewById(R.id.classTextView);
        // Set click listeners for the buttons
        btnMarks.setOnClickListener(this);
        btnTimetable.setOnClickListener(this);
        btnAnnouncements.setOnClickListener(this);

        Intent j = getIntent();
        String userId = j.getStringExtra("userId");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("usersMedia/" + userId + ".png");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String imageUrl = uri.toString();
                // Now you have the URL of the image, you can use it as needed
                // For example, you can concatenate it with the user ID
                String profilePictureUrl = imageUrl + userId;
                Log.d("TAG", "onSuccess: " + profilePictureUrl);
                //profileImageView.setImageURI(Uri.parse(profilePictureUrl));
                Glide.with(MainActivity.this)
                        .load(imageUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .apply(new RequestOptions()
                                .placeholder(R.drawable.default_profile_picture) // Placeholder image while loading
                                .error(R.drawable.default_profile_picture)) // Error image if loading fails
                        .into(profileImageView);
                // Perform further actions with the profilePictureUrl
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors that occurred during the URL retrieval
            }
        });


        String name = j.getStringExtra("name");
        String lastname = j.getStringExtra("lastname");
        String className = j.getStringExtra("classroom");
        String year = j.getStringExtra("year");

        nameTextView.setText(name+" "+lastname);
        classTextView.setText(className+" - "+year);

        // Retrieve user information from Firebase Realtime Database
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {


            case R.id.btnMarks:
                // Handle marks button click
                intent = new Intent(MainActivity.this, MarksActivity.class);
                startActivity(intent);
                break;

            case R.id.btnTimetable:
                // Handle timetable button click
                intent = new Intent(MainActivity.this, TimetableActivity.class);
                intent.putExtra("classroom", getIntent().getStringExtra("classroom"));
                //intent.putExtra("userId", getIntent().getStringExtra("userId"));

                startActivity(intent);
                break;



            case R.id.btnAnnouncements:
                // Handle announcements button click
                intent = new Intent(MainActivity.this, AnnouncementActivity.class);
                intent.putExtra("userId", getIntent().getStringExtra("userId"));

                startActivity(intent);
                break;
        }
    }
}
