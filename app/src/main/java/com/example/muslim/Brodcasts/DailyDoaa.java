package com.example.muslim.Brodcasts;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import com.example.muslim.R;

public class DailyDoaa extends BroadcastReceiver {
    static MediaPlayer mp = null;
    String quote ;

    @Override
    public void onReceive(Context context, Intent intent) {


        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.release();
            mp=null;
        }
        else {
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
                }
                else {
                    mp.stop();
                    mp.release();
                }

            }
        });

    }
}
