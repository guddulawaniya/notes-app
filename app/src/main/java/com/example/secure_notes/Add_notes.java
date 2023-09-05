package com.example.secure_notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

public class Add_notes extends AppCompatActivity {


    private AppDatabase database;
    private NotesDao noteDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        config();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        EditText title = findViewById(R.id.title);
        TextInputEditText descripation = findViewById(R.id.messagebox);
        ImageView savebutton = findViewById(R.id.savebutton);
        ImageView backbutton = findViewById(R.id.backbutton);

        Intent intent = getIntent();
        String intent_title = intent.getStringExtra("title");
        String intent_ds = intent.getStringExtra("ds");
        title.setText(intent_title);
        descripation.setText(intent_ds);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        database = AppDatabase.getInstance(this);
        noteDao = database.notesDao();


        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String titletext = title.getText().toString();
                String descripat = descripation.getText().toString();

                if (!titletext.isEmpty() && !descripat.isEmpty())
                {
                    notes note = new notes(titletext,descripat);
                    noteDao.insert(note);
                }

                onBackPressed();
            }
        });


    }
    private void config() {
        findViewById(android.R.id.content).setTransitionName("fab");

        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        MaterialContainerTransform transform = new MaterialContainerTransform();
        transform.addTarget(android.R.id.content);
        transform.setDuration(500);

        getWindow().setSharedElementEnterTransition(transform);
        getWindow().setSharedElementReturnTransition(transform);


    }
}