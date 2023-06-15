package com.example.tekscore;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class MarksActivity extends AppCompatActivity {

    private ListView listViewMarks;
    private ArrayAdapter<String> marksAdapter;
    private List<String> marksList;

    private DatabaseReference marksReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks);

        listViewMarks = findViewById(R.id.listViewMarks);
        marksList = new ArrayList<>();
        marksAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, android.R.id.text1, marksList);
        listViewMarks.setAdapter(marksAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            marksReference = FirebaseDatabase.getInstance("https://tekscore-default-rtdb.europe-west1.firebasedatabase.app").getReference("marks").child(uid).child("matieres");

            marksReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    marksList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String nom = snapshot.child("nom").getValue(String.class);
                        String note = snapshot.child("note").getValue().toString();

                        String item = nom + "\n" + "Note: " + note;
                        marksList.add(item);


                    }
                    marksAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }
    }
}
