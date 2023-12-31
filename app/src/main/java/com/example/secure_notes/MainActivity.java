package com.example.secure_notes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.SharedElementCallback;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private static final int ALARM_REQUEST_CODE = 123;

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




        long intervalMillis = 60 * 1000; // 60 seconds
        long startTime = SystemClock.elapsedRealtime(); // Start immediately

        // Use setRepeating for repeating alarms



        noteDao.getAlldata().observe(MainActivity.this, new Observer<List<notes>>() {
            @Override
            public void onChanged(List<notes> notes) {

                recyclerDataArrayList = notes;
                Collections.reverse(recyclerDataArrayList);


                adapter = new myAdapter(recyclerDataArrayList, MainActivity.this);
                recyclerview.setAdapter(adapter);

            }
        });

        if (!recyclerDataArrayList.isEmpty()) {
            emptytext.setVisibility(View.GONE);
        } else {
            emptytext.setVisibility(View.VISIBLE);
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
        setExitSharedElementCallback(new SharedElementCallback() {
        });
        getWindow().setSharedElementsUseOverlay(false);

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the alarm when the activity is destroyed
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_MUTABLE);
        alarmManager.cancel(pendingIntent);
    }


}