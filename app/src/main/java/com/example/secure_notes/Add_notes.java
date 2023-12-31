package com.example.secure_notes;

import static com.example.secure_notes.AlarmReceiver.channelId;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Add_notes extends AppCompatActivity {


    private AppDatabase database;
    private NotesDao noteDao;
    EditText title;
    TextInputEditText descripation;
    Calendar calendar;
    TextView settime, setdate;
    private static final int ALARM_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config();
        setContentView(R.layout.activity_add_notes);
        title = findViewById(R.id.title);
        descripation = findViewById(R.id.messagebox);
        ImageView savebutton = findViewById(R.id.savebutton);
        ImageView backbutton = findViewById(R.id.backbutton);
        setdate = findViewById(R.id.setdate);
        settime = findViewById(R.id.settime);

        Intent intent = getIntent();
        String intent_title = intent.getStringExtra("title");
        int id_intent = intent.getIntExtra("id", 0);
        String intent_ds = intent.getStringExtra("ds");
        String intent_date = intent.getStringExtra("date");
        String intent_time = intent.getStringExtra("time");


        title.setText(intent_title);
        descripation.setText(intent_ds);
        setdate.setText(intent_date);
        settime.setText(intent_time);


        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openTimePicker();

            }
        });
        setdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openCalendar();

            }
        });


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
                String date = setdate.getText().toString();
                String time = settime.getText().toString();

                if (!titletext.isEmpty() && !descripat.isEmpty() && id_intent != 0) {
                    noteDao.update(titletext, descripat, time, date, id_intent);
                    try {
                        scheduleNotification();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    onBackPressed();

                } else if (!titletext.isEmpty() && !descripat.isEmpty()) {
                    notes note = new notes(titletext, descripat, date, time);
                    noteDao.insert(note);
                    onBackPressed();
                } else {
                    Toast.makeText(Add_notes.this, "Write something in the boxes", Toast.LENGTH_SHORT).show();
                }

//                Toast.makeText(Add_notes.this, "Alarm set successfully", Toast.LENGTH_SHORT).show();

            }
        });

    }


/*    @RequiresApi(api = Build.VERSION_CODES.S)
    private PendingIntent createPendingIntent(Intent intent) {
        PendingIntent pendingIntent;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT
                    | PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        return pendingIntent;
    }*/

    private void openTimePicker() {
        // Get the current time
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // Handle the selected time
                        if (selectedHour>=hour && selectedMinute>minute)
                        {
                            String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                            settime.setText(selectedTime);
                        }
                        else
                        {
                            settime.setText("");
                            Toast.makeText(Add_notes.this, "Invalid Time ", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                hour, minute, false);

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

    private void openCalendar() {
        // Get the current date
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        long minDateMillis = System.currentTimeMillis();
        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        // Handle the selected date
                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        setdate.setText(selectedDate);
                    }
                },
                year, month, dayOfMonth);

        // Show the DatePickerDialog
        datePickerDialog.getDatePicker().setMinDate(minDateMillis);
        datePickerDialog.show();

    }

    private void scheduleNotification() throws ParseException {
        Intent intent = new Intent(this, Notification.class);
        intent.putExtra("titleExtra", title.getText().toString());
        intent.putExtra("messageExtra", descripation.getText().toString());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
        );
        Long time = getTime();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);

    }

    private long getTime() throws ParseException {
        String sDate1 = setdate.getText().toString();
        SimpleDateFormat date1 = new SimpleDateFormat("dd/MM/yyyy");

        Date date = date1.parse(sDate1);
        int year = date.getYear() + 1900;
        int month = date.getMonth();
        int date2 = date.getDate();

        String sTime1 = settime.getText().toString();
        SimpleDateFormat time1 = new SimpleDateFormat("hh:mm:aa");

        Date time2 = time1.parse(sTime1);

        int hour = time2.getHours();
        int minute = time2.getMinutes();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date2, hour, minute);
        return calendar.getTimeInMillis();
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
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