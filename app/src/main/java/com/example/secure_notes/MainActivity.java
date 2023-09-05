package com.example.secure_notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.SharedElementCallback;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase database;
    private NotesDao noteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        config();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CardView addbutton = findViewById(R.id.addbutton);
        RecyclerView recyclerview = findViewById(R.id.recyclerview);


        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        database = AppDatabase.getInstance(this);
        noteDao = database.notesDao();




        noteDao.getAlldata().observe(MainActivity.this, new Observer<List<notes>>() {
            @Override
            public void onChanged(List<notes> notes) {
                myAdapter adapter = new myAdapter(notes,MainActivity.this);
                recyclerview.setAdapter(adapter);

            }
        });

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Add_notes.class);
                                    Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, addbutton, "fab").toBundle();
                startActivity(intent, bundle);

            }
        });
    }
    private void config() {
        setExitSharedElementCallback(new SharedElementCallback() {});
        getWindow().setSharedElementsUseOverlay(false);

    }
}