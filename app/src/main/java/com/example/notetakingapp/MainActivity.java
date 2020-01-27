package com.example.notetakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button openCam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            Note receivable = extras.getParcelable("NoteOb");
            Toast.makeText(this, "Path: " + receivable.getPath(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Title: " + receivable.getTitle(), Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Date: " + receivable.getDateS(), Toast.LENGTH_LONG).show();
        }
        openCam = findViewById(R.id.button);
        openCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToCam(v);
            }
        });
    }

    public void switchToCam(View v){
        Intent cam = new Intent(this, AddNote.class);
        startActivity(cam);
    }
}
