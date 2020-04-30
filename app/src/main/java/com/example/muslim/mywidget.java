package com.example.muslim;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Implementation of App Widget functionality.
 */
public class mywidget extends AppWidgetProvider {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    AppWidgetManager appWidgetManager2;
    Context contextt;
    int appWidgetId2;
    RemoteViews views;
    private PendingIntent service = null;
    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {
      /*  RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.mywidget);
        views.setProgressBar(R.id.progress_bar2, 100,0,true);*/

    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {
             views = new RemoteViews(context.getPackageName(), R.layout.mywidget);
            //views.setProgressBar(R.id.progress_bar2, 100,0,true);
            updateAppWidget(context, appWidgetManager, appWidgetId);
            appWidgetManager2=appWidgetManager;
            contextt=context;
            appWidgetId2=appWidgetId;
            SetAlarm(context,1,mywidget.class);
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
       // startAlarm(context);
        SetAlarm(context,1,mywidget.class);

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        SetAlarm(context,1,mywidget.class);

    }


    public void SetAlarm(Context context, int minuts, Class widgetService)
    {
        AlarmManager alarmManager=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(),
                1*60*1000,
                pendingIntent);

    }

}

