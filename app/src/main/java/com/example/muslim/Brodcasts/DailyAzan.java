package com.example.muslim.Brodcasts;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.muslim.R;

public class DailyAzan extends BroadcastReceiver {
    //DatabaseHelper databaseHelper;

   static MediaPlayer mp = null;
    String quote ;

    @Override
    public void onReceive(Context context, Intent intent) {
        int key2 = intent.getIntExtra("b2", -1);
        if (key2 == 0) {
            if (mp != null && mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = null;
            } else {
                mp = null;
            }

            mp = MediaPlayer.create(context, R.raw.abdulbasit);
            mp.start();
            Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(1000);

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (!mp.isPlaying()) {
                        mp.release();
                    } else {
                        mp.stop();
                        mp.release();
                    }

                }
            });
            long when = System.currentTimeMillis();
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);

            Intent notificationIntent = new Intent(context, DailyAzan.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // get your quote here
            quote = "Establish your prayers-اقم صلاتك";


            NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                    context).setSmallIcon(R.drawable.azan)
                    .setContentTitle("prayer is now - الصلاة الأن")
                    .setContentText(quote).setSound(alarmSound)
                    .setAutoCancel(true).setWhen(when)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.azan))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(quote))
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});  // Declair VIBRATOR Permission in AndroidManifest.xml
            notificationManager.notify(15, mNotifyBuilder.build());


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String CHANNEL_ID = "my_channel_01";
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "02")
                        .setContentTitle("prayer is now - الصلاة الأن")
                        .setSmallIcon(R.drawable.azan)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(quote))
                        .setContentText(quote)
                        .setAutoCancel(true)
                        .setSound(alarmSound)
                        .setChannelId(CHANNEL_ID)
                        .setContentIntent(pendingIntent).setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});  // Declair VIBRATOR Permission in AndroidManifest.xml
                ;

                // For Oreo and greater than it, we required Notification Channel.
                CharSequence name = "My New Channel";                   // The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;

                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance); //Create Notification Channel
                notificationManager.createNotificationChannel(channel);
                notificationManager.notify(89 /* ID of notification */, notificationBuilder.build());

            }
        }
            else if(key2==1){
            Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(1000);

                long when2 = System.currentTimeMillis();

            NotificationManager notificationManager2 = (NotificationManager) context
                        .getSystemService(Context.NOTIFICATION_SERVICE);

                Intent notificationIntent2 = new Intent(context, DailyAzan.class);
                notificationIntent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0,
                        notificationIntent2, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri alarmSound2 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                // get your quote here
                quote = "Establish your prayers-اقم صلاتك";


                NotificationCompat.Builder mNotifyBuilder2 = new NotificationCompat.Builder(
                        context).setSmallIcon(R.drawable.azan)
                        .setContentTitle("prayer is now - الصلاة الأن")
                        .setContentText(quote).setSound(alarmSound2)
                        .setAutoCancel(true).setWhen(when2)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.azan))
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(quote))
                        .setContentIntent(pendingIntent2)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});  // Declair VIBRATOR Permission in AndroidManifest.xml
                notificationManager2.notify(15, mNotifyBuilder2.build());


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String CHANNEL_ID = "my_channel_01";
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "02")
                            .setContentTitle("prayer is now - الصلاة الأن")
                            .setSmallIcon(R.drawable.azan)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(quote))
                            .setContentText(quote)
                            .setAutoCancel(true)
                            .setSound(alarmSound2)
                            .setChannelId(CHANNEL_ID)
                            .setContentIntent(pendingIntent2).setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});  // Declair VIBRATOR Permission in AndroidManifest.xml
                    ;

                    // For Oreo and greater than it, we required Notification Channel.
                    CharSequence name = "My New Channel";                   // The user-visible name of the channel.
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance); //Create Notification Channel
                    notificationManager2.createNotificationChannel(channel);
                    notificationManager2.notify(89 /* ID of notification */, notificationBuilder.build());

                }
        }

            else if(key2==2){

            }
    }
    }
