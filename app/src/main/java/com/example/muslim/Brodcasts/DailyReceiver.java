package com.example.muslim.Brodcasts;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.muslim.R;
import com.example.muslim.Ui.Main.MainActivity;

import static android.provider.Settings.System.getString;
import static androidx.core.content.ContextCompat.getSystemService;

public class DailyReceiver extends BroadcastReceiver {
    //DatabaseHelper databaseHelper;

    @Override
    public void onReceive(Context context, Intent intent) {


        String quote;

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, DailyReceiver.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // get your quote here
        String keyid = intent.getStringExtra("a");
        String key2 = intent.getStringExtra("b");

        //  quote = "five minutes left for fajr prayer- تبقى خمس دقائق لصلاة الفجر";


        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.azan)
                .setContentTitle("The next prayer is " + key2)
                .setContentText(keyid).setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.azan))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(keyid))
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});  // Declair VIBRATOR Permission in AndroidManifest.xml
        notificationManager.notify(5, mNotifyBuilder.build());



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "01")
                    .setContentTitle("The next prayer is " + key2)
                    .setSmallIcon(R.drawable.azan)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(keyid))
                    .setContentText(keyid)
                    .setAutoCancel(true)
                    .setSound(alarmSound)
                    .setChannelId(CHANNEL_ID)
                    .setContentIntent(pendingIntent);

            // For Oreo and greater than it, we required Notification Channel.
            CharSequence name = "My New Channel";                   // The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name, importance); //Create Notification Channel
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(88 /* ID of notification */, notificationBuilder.build());

        }


    }

}