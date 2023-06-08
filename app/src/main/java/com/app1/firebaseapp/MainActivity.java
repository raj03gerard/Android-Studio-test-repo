package com.app1.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText editTitle, editGenre;
    Button button, getButton, btnSignOut;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTitle = findViewById(R.id.edtTitle);
        editGenre= findViewById(R.id.edtGenre);
        button= findViewById(R.id.btnSendData);
        getButton= findViewById(R.id.btnGetData);
        textView= findViewById(R.id.txtRetrievedData);
        btnSignOut= findViewById(R.id.btnSignOut);

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance("https://fir-app-4f2ee-default-rtdb.asia-southeast1.firebasedatabase.app")
                        .getReference("movies").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot snapShot: snapshot.getChildren())
                                {
                                    Movie movie= snapShot.getValue(Movie.class);
                                    textView.setText(textView.getText().toString()+" "+movie.getTitle());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title= editTitle.getText().toString();
                String genre= editGenre.getText().toString();

                FirebaseDatabase.getInstance("https://fir-app-4f2ee-default-rtdb.asia-southeast1.firebasedatabase.app")
                        .getReference("movies").push().setValue(new Movie(title, genre))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                Toast.makeText(MainActivity.this, "Data inserted!", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(MainActivity.this, "Not successful", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, SignInActivity.class).setFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));

                finish();
            }
        });
    }
}