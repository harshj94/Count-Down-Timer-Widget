package com.example.harsh.widgetdemo;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

public class MyService extends Service {

    long time, timeDiff;
    Thread t;
    long day;
    long hours;
    long seconds;
    long minutes;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("MyService", "calling build update");
                buildUpdate();
            }
        });
        t.start();
        return START_STICKY;
    }

    private void buildUpdate() {

        //reading date from shared preference
        SharedPreferences prefs = getSharedPreferences("myownsharedpref", MODE_PRIVATE);
        time = prefs.getLong("time", 0);
        Log.i("MyService", "Time from shared Pref " + time);

        //getting current time
        long currentDateInMillis = Calendar.getInstance().getTimeInMillis();

        //calculating time difference
        timeDiff = time - currentDateInMillis;

        seconds = timeDiff / 1000;
        minutes = seconds / 60;
        seconds = seconds % 60;
        hours = minutes / 60;
        minutes = minutes % 60;
        day = hours / 24;
        hours = hours % 24;

        String s = day + ":" + hours + ":" + minutes + ":" + seconds;
        Log.i("MyService", "timeDiff = " + timeDiff);

        RemoteViews view = new RemoteViews(getPackageName(), R.layout.new_app_widget);

        if (timeDiff > 0) {
            view.setTextViewText(R.id.appwidget_text, s);
            ComponentName thisWidget = new ComponentName(this, NewAppWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, view);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            buildUpdate();
        } else {
            view.setTextViewText(R.id.appwidget_text, "Time Over");
            ComponentName thisWidget = new ComponentName(this, NewAppWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, view);
            stopSelf();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
