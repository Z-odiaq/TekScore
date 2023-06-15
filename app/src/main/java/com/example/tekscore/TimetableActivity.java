package com.example.tekscore;


import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class TimetableActivity extends AppCompatActivity {

    private TableLayout tableLayout;

    private DatabaseReference timetableReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        tableLayout = findViewById(R.id.tableLayout);
        String classroom = getIntent().getStringExtra("classroom");

        DatabaseReference timetableReference = FirebaseDatabase.getInstance("https://tekscore-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("timetables")
                .child(classroom).child("days");
        Log.d(TAG, "loadAnnouncementsFromFirebase: " + timetableReference);
        timetableReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tableLayout.removeAllViews();

                // Create table header row

                TableRow headerRow = createHeaderRow("Heur", "Matière", "Salle");
                tableLayout.addView(headerRow);

                for (DataSnapshot daySnapshot : dataSnapshot.getChildren()) {
                    String day = daySnapshot.child("day").getValue(String.class);
                    Log.d(TAG, "loadAnnouncssementsFrase: " + dataSnapshot.getChildren()+" "+day);

                    TableRow dayRow = createDayHeaderRow(day);
                    tableLayout.addView(dayRow);
                    for (DataSnapshot timetableSnapshot : daySnapshot.child("timetable").getChildren()) {
                        String time = timetableSnapshot.child("time").getValue(String.class);
                        String subject = timetableSnapshot.child("subject").getValue(String.class);
                        String location = timetableSnapshot.child("location").getValue(String.class);

                        TableRow row = createTimetableRow(time, subject, location);
                        tableLayout.addView(row);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
    private TableRow createDayHeaderRow(String day) {
        TableRow row = new TableRow(this);


        TextView header1TextView = new TextView(this);
        header1TextView.setText(day);
        header1TextView.setTextColor(Color.rgb(0,0,0));

        //header1TextView.setBackgroundResource(R.drawable.table_header_background);
        header1TextView.setPadding(16, 16, 16, 16);

        // Customize other properties of dayTextView

        // Add dayTextView to the row
        row.addView(header1TextView);
        return row;
    }
    private TableRow createHeaderRow(String header1, String header2, String header3) {
        //Log.d(TAG, "dqsdqsdqdq: " + header1);

        TableRow row = new TableRow(this);

        TextView header1TextView = new TextView(this);
        header1TextView.setText(header1);
        header1TextView.setTextColor(Color.rgb(0,0,0));
        header1TextView.setBackgroundResource(R.drawable.table_header_background);
        header1TextView.setPadding(16, 16, 16, 16);

        TextView header2TextView = new TextView(this);
        header2TextView.setText(header2);
        header2TextView.setTextColor(Color.rgb(0,0,0));
        header2TextView.setBackgroundResource(R.drawable.table_header_background);
        header2TextView.setPadding(16, 16, 16, 16);

        TextView header3TextView = new TextView(this);
        header3TextView.setText(header3);
        header3TextView.setTextColor(Color.rgb(0,0,0));
        header3TextView.setBackgroundResource(R.drawable.table_header_background);
        header3TextView.setPadding(16, 16, 16, 16);

        row.addView(header1TextView);
        row.addView(header2TextView);
        row.addView(header3TextView);

        return row;
    }

    private TableRow createTimetableRow(String time, String subject, String location) {
        TableRow row = new TableRow(this);
        Log.d(TAG, "createTimetableRow: " + time + " " + subject + " " + location   );
        TextView timeTextView = new TextView(this);
        timeTextView.setText(time);
        timeTextView.setTextColor(Color.rgb(0, 0, 0));
        timeTextView.setBackgroundResource(R.drawable.table_row_background);
        timeTextView.setPadding(16, 16, 16, 16);

        TextView subjectTextView = new TextView(this);
        subjectTextView.setText(subject);
        subjectTextView.setBackgroundResource(R.drawable.table_row_background);
        subjectTextView.setPadding(16, 16, 16, 16);
        subjectTextView.setTextColor(Color.rgb(0, 0, 0));

        TextView locationTextView = new TextView(this);
        locationTextView.setText(location);
        locationTextView.setBackgroundResource(R.drawable.table_row_background);
        locationTextView.setPadding(16, 16, 16, 16);
        locationTextView.setTextColor(Color.rgb(0, 0, 0)); // Fixed here

        row.addView(timeTextView);
        row.addView(subjectTextView);
        row.addView(locationTextView);

        return row;
    }
}

