package com.example.secure_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.SharedElementCallback;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase database;
    private NotesDao noteDao;
    private List<notes> recyclerDataArrayList;
    myAdapter adapter;
    RecyclerView recyclerview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        config();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CardView addbutton = findViewById(R.id.addbutton);
         recyclerview = findViewById(R.id.recyclerview);
       TextView emptytext = findViewById(R.id.emptytext);


        recyclerDataArrayList = new ArrayList<>();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        database = AppDatabase.getInstance(this);
        noteDao = database.notesDao();




        noteDao.getAlldata().observe(MainActivity.this, new Observer<List<notes>>() {
            @Override
            public void onChanged(List<notes> notes) {

                recyclerDataArrayList= notes;
                Collections.reverse(recyclerDataArrayList);


                adapter = new myAdapter(recyclerDataArrayList,MainActivity.this);
                recyclerview.setAdapter(adapter);

            }
        });

        if (recyclerDataArrayList.isEmpty())
        {
            emptytext.setVisibility(View.VISIBLE);
        }
        else
        {
            emptytext.setVisibility(View.GONE);
        }

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Add_notes.class);
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, addbutton, "fab").toBundle();
                startActivity(intent, bundle);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                notes deletedCourse = recyclerDataArrayList.get(viewHolder.getAdapterPosition());


                int position = viewHolder.getAdapterPosition();


                noteDao.delete(deletedCourse);


                Snackbar.make(recyclerview, deletedCourse.getText(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerDataArrayList.add(position, deletedCourse);
                        adapter.notifyItemInserted(position);
                    }
                }).show();
            }
        }).attachToRecyclerView(recyclerview);
    }
    private void config() {
        setExitSharedElementCallback(new SharedElementCallback() {});
        getWindow().setSharedElementsUseOverlay(false);

    }
}