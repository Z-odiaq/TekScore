package com.example.tekscore;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class AnnouncementActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAnnouncements;
    private AnnouncementAdapter announcementAdapter;
    private List<Announcement> announcementList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);

        recyclerViewAnnouncements = findViewById(R.id.recyclerViewAnnouncements);
        recyclerViewAnnouncements.setLayoutManager(new LinearLayoutManager(this));

        // Create an empty list for announcements
        announcementList = new ArrayList<>();

        // Create and set the adapter
        announcementAdapter = new AnnouncementAdapter(announcementList);
        recyclerViewAnnouncements.setAdapter(announcementAdapter);

        // Load announcements from Firebase
        loadAnnouncementsFromFirebase();
    }

    private void loadAnnouncementsFromFirebase() {
        Intent j = getIntent();
        String userClass = j.getStringExtra("classroom");


        DatabaseReference announcementsRef = FirebaseDatabase.getInstance("https://tekscore-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("announcement")
                .child(userClass);
        Log.d(TAG, "loadAnnouncementsFromFirebase: " + announcementsRef.toString());
        announcementsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                announcementList.clear(); // Clear the existing list before adding new announcements
                Log.d("TAG", "onDataChange: " + dataSnapshot.getChildren());
                DataSnapshot announcementsSnapshot = dataSnapshot.child("announcements");


                for (DataSnapshot announcementSnapshot : announcementsSnapshot.getChildren()) {
                    Log.d("TAG", "onDataChangessss: " + announcementSnapshot.getValue());

                    Announcement announcement = announcementSnapshot.getValue(Announcement.class);
                    announcementList.add(announcement);
                }


                announcementAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }


}
