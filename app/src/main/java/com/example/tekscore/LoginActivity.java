package com.example.tekscore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef; // Reference to the "users" node in the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        firebaseAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (currentUser != null) {
                                String uid = currentUser.getUid();
                                DatabaseReference userRef = FirebaseDatabase.getInstance("https://tekscore-default-rtdb.europe-west1.firebasedatabase.app")
                                        .getReference("users")
                                        .child(uid);
                                Log.d("Firebase", String.valueOf(userRef), null);

                                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Log.d("Firebase", "User data snapshot exists: " + snapshot.exists());
                                        if (snapshot.exists()) {
                                            String name = snapshot.child("name").getValue(String.class);
                                            String lastname = snapshot.child("lastname").getValue(String.class);
                                            String classroom = snapshot.child("class").getValue(String.class);
                                            String year = snapshot.child("year").getValue(String.class);

                                            String photoUrl = snapshot.child("photoUrl").getValue(String.class);
                                            Log.d("Firebase", "Name: " + name);


                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            intent.putExtra("userId", uid);
                                            intent.putExtra("name", name);
                                            intent.putExtra("lastname", lastname);
                                            intent.putExtra("classroom", classroom);
                                            intent.putExtra("year", year);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(LoginActivity.this, "User information not found", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("Firebase", "Failed to retrieve user information: " + error.getMessage());
                                        Toast.makeText(LoginActivity.this, "Failed to retrieve user information", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
