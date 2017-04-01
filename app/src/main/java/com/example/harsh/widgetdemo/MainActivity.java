package com.example.harsh.widgetdemo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Calendar date;
    Button button;
    TextView textView;
    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        textView=(TextView)findViewById(R.id.chosenDateTime);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        SharedPreferences prefs = getSharedPreferences("myownsharedpref", MODE_PRIVATE);
        time = prefs.getLong("time", 0);
        String dateString = DateFormat.format("dd/MM/yyyy hh:mm aa", new Date(time)).toString();
        textView.setText("Selected Time = "+dateString);
        Log.i("MyService", "Time from shared Pref " + time);
    }

    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        SharedPreferences.Editor editor = getSharedPreferences("myownsharedpref", MODE_PRIVATE).edit();
                        editor.putLong("time", date.getTimeInMillis());
                        editor.commit();
                        String dateString = DateFormat.format("dd/MM/yyyy hh:mm aa", new Date(date.getTimeInMillis())).toString();
                        textView.setText("Selected Time = "+dateString);
                        Log.i("MainActivity", "New Date time chosen = " + date.getTimeInMillis());
                        Intent serviceIntent = new Intent(getApplicationContext(), MyService.class);
                        startService(serviceIntent);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
}

