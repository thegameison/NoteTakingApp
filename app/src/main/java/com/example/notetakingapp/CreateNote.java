package com.example.notetakingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

public class CreateNote extends AppCompatActivity {
    EditText title;
    ImageView showNote;
    Button next;
    String pathS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        title = findViewById(R.id.title);
        showNote = findViewById(R.id.note);
        next = findViewById(R.id.proceed);

        Bundle extras = getIntent().getExtras();

        pathS = extras.getString("imgPath");
        Bitmap img = BitmapFactory.decodeFile(pathS);
        showNote.setImageBitmap(img);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note newNote = new Note(pathS);
                newNote.setDate(Calendar.getInstance().getTime());
                newNote.setTitle(title.getText().toString());

                Intent toMain = new Intent(v.getContext(), MainActivity.class);
                toMain.putExtra("NoteOb", newNote);
                startActivity(toMain);
            }
        });
    }
}
