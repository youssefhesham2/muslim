package com.example.muslim;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    int which;
    RemoteViews views;
    int fajrhour,fajrminutes,dhuhrhour,dhuhrminutes,asrhour,asrminutes,maghribhour,maghribminutes,aishahour,aishaminutes,hourleft,minuteleft;
    @Override
    public void onReceive(final Context context, Intent intent) {
       final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        fajrhour = preferences.getInt("fajrhour", 0);
        fajrminutes=preferences.getInt("fajrminutes", 0);

        dhuhrhour = preferences.getInt("dhuhrhour", 0);
        dhuhrminutes=preferences.getInt("dhuhrminutes", 0);

        asrhour = preferences.getInt("asrhour", 0);
        asrminutes=preferences.getInt("asrminutes", 0);

        maghribhour = preferences.getInt("maghribhour", 0);
        maghribminutes=preferences.getInt("maghribminutes", 0);

        aishahour = preferences.getInt("aishahour", 0);
        aishaminutes=preferences.getInt("aishaminutes", 0);
        nextpyer();
        long mMilliseconds = 70000;
        CountDownTimer mCountDownTimer = new CountDownTimer(mMilliseconds, 1000) {
            // int which = preferences.getInt("whichnext", 0);

            @Override
            public void onFinish() {
                //counter(context,appWidgetManager,appWidgetId);
            }

            @SuppressLint("ResourceAsColor")
            public void onTick(long millisUntilFinished) {
                SimpleDateFormat ss=new SimpleDateFormat("H:mm:ss");
                String curenttime = new SimpleDateFormat("hh:mm:ss a").format(new Date());
                 views = new RemoteViews(context.getPackageName(), R.layout.mywidget);
                 views.setTextViewText(R.id.oclock, curenttime+"");

               // views.setProgressBar(R.id.progress_bar2, 10,95,true);

                // Instruct the widget manager to update the widget

                Calendar cal = Calendar.getInstance();

                int minut=cal.get(Calendar.MINUTE);
                int hou=cal.get(Calendar.HOUR);
                //   Toast.makeText(context,hou+"",Toast.LENGTH_LONG).show();
                int mm;
                int hh;

                switch (which){
                    case 1:
                        views.setTextViewText(R.id.nextsala2, "Fajr");
                        minuteleft=fajrminutes-minut;
                        if(hou>fajrhour){
                            //hourleft=(fajrhour)+12-(hou-12);
                            hourleft=(12-hou)+(fajrhour);
                            //  Toast.makeText(context,fajrhour+"",Toast.LENGTH_LONG).show();
                        }
                        else{
                            hourleft=fajrhour-hou;
                        }
                        mm=fajrminutes-minut;
                       // hh=fajrhour-hou;
                        if(mm<0){
                            minuteleft=(60-minut)+fajrminutes;
                            hourleft=hourleft-1;
                        }
                        break;
                    case 2:
                        views.setTextViewText(R.id.nextsala2, "Dhuhr");
                        minuteleft=dhuhrminutes-minut;
                        mm=dhuhrminutes-minut;
                        hourleft=dhuhrhour-hou;
                        hh=dhuhrhour-hou;
                        if(mm<0){
                            minuteleft=(60-minut)+dhuhrminutes;
                            hourleft=hh-1;
                        }
                        break;
                    case 3:
                        views.setTextViewText(R.id.nextsala2, "Asr");
                        minuteleft=asrminutes-minut;
                        hourleft=asrhour-hou;
                        mm=asrminutes-minut;
                        hh=asrhour-hou;
                        if(mm<0){
                            minuteleft=(60-minut)+asrminutes;
                            hourleft=hh-1;
                        }
                        hourleft=hourleft-12;
                        break;
                    case 4:
                        views.setTextViewText(R.id.nextsala2, "Maghrib");

                        minuteleft=maghribminutes-minut;
                        hourleft=maghribhour-hou;
                        mm=maghribminutes-minut;
                        hh=maghribhour-hou;
                        if(mm<0){
                            minuteleft=(60-minut)+maghribminutes;
                            hourleft=hh-1;
                        }
                        hourleft=hourleft-12;

                        break;
                    case 5:
                        views.setTextViewText(R.id.nextsala2, "Aish");

                        minuteleft=aishaminutes-minut;
                        hourleft=aishahour-hou;
                        mm=aishaminutes-minut;
                        hh=aishahour-hou;
                        if(mm<0){
                            minuteleft=(60-minut)+aishaminutes;
                            hourleft=hh-1;
                        }
                        hourleft=hourleft-12;

                        break;
                }
                views.setTextViewText(R.id.timeleft2, "-"+hourleft+":"+minuteleft+"");
              //  appWidgetManager.updateAppWidget(appWidgetId, views);
                pryertime();

                ComponentName componentName = new ComponentName(context, mywidget.class);
                AppWidgetManager.getInstance(context).updateAppWidget(componentName, views);
            }

        }.start();

    }
    void  nextpyer(){
        Calendar calendarfajr=Calendar.getInstance();
        calendarfajr.set(Calendar.HOUR_OF_DAY,fajrhour);
        calendarfajr.set(Calendar.MINUTE,fajrminutes);

        Calendar calendardhur=Calendar.getInstance();
        calendardhur.set(Calendar.HOUR_OF_DAY,dhuhrhour);
        calendardhur.set(Calendar.MINUTE,dhuhrminutes);

        Calendar calendarasr=Calendar.getInstance();
        calendarasr.set(Calendar.HOUR_OF_DAY,asrhour);
        calendarasr.set(Calendar.MINUTE,asrminutes);

        Calendar calendarmaghrib=Calendar.getInstance();
        calendarmaghrib.set(Calendar.HOUR_OF_DAY,maghribhour);
        calendarmaghrib.set(Calendar.MINUTE,maghribminutes);

        Calendar calendaisha=Calendar.getInstance();
        calendaisha.set(Calendar.HOUR_OF_DAY,aishahour);
        calendaisha.set(Calendar.MINUTE,aishaminutes);
        Calendar curr=Calendar.getInstance();
        if(curr.after(calendaisha)||curr.before(calendarfajr)){
            //nextsala.setText("fajr");
            which=1;
            //editor.putInt("whichnext",which);
            //editor.commit();
           /* if(curr.get(Calendar.HOUR_OF_DAY)>fajrhour){
                hourleft=(fajrhour)+12-(curr.get(Calendar.HOUR_OF_DAY)-12);
                // hourleft=hou-fajrhour;
            }
            else{
                hourleft=fajrhour-curr.get(Calendar.HOUR_OF_DAY);

            }
         /*   if(hourleft==9){
                circularProgressBar.setProgress(0);
            }
            if(hourleft<9){
                circularProgressBar.setProgress(20);
            }

            if(hourleft<8){
                circularProgressBar.setProgress(30);
            }
            if(hourleft<7){
                circularProgressBar.setProgress(40);
            }
            if(hourleft<6){
                circularProgressBar.setProgress(45);
            }
            if(hourleft<5){
                circularProgressBar.setProgress(50);
            }
            if(hourleft<4){
                circularProgressBar.setProgress(60);
            }
            if(hourleft<3){
                circularProgressBar.setProgress(70);
            }
            if(hourleft<2){
                circularProgressBar.setProgress(80);
            }
            if(hourleft<1){
                circularProgressBar.setProgress(90);
            }*/
        }

        if(curr.after(calendarfajr)&&curr.before(calendardhur)){
            //nextsala.setText("Dhuhr");
            which=2;
            //  editor.putInt("whichnext",which);
            //editor.commit();
          /*  hourleft=dhuhrhour-curr.get(Calendar.HOUR_OF_DAY);

            if(hourleft==9){
                circularProgressBar.setProgress(0);
            }

            if(hourleft<9){
                circularProgressBar.setProgress(20);
            }

            if(hourleft<8){
                circularProgressBar.setProgress(20);
            }
            if(hourleft<7){
                circularProgressBar.setProgress(40);
            }
            if(hourleft<6){
                circularProgressBar.setProgress(45);
            }
            if(hourleft<5){
                circularProgressBar.setProgress(50);
            }
            if(hourleft<4){
                circularProgressBar.setProgress(60);
            }
            if(hourleft<3){
                circularProgressBar.setProgress(70);
            }
            if(hourleft<2){
                circularProgressBar.setProgress(80);
            }
            if(hourleft<1){
                circularProgressBar.setProgress(90);
            }*/

        }
        if(curr.after(calendardhur)&&curr.before(calendarasr)){
          //  nextsala.setText("Asr");
            which=3;
            //editor.putInt("whichnext",which);
            //editor.commit();
            hourleft=asrhour-curr.get(Calendar.HOUR_OF_DAY);
          /*  if(hourleft<4){
                circularProgressBar.setProgress(20);
            }
            if(hourleft<3){
                circularProgressBar.setProgress(40);
            }
            if(hourleft<2){
                circularProgressBar.setProgress(80);
            }
            if(hourleft<1){
                circularProgressBar.setProgress(90);
            }*/
        }

        if(curr.after(calendarasr)&&curr.before(calendarmaghrib)){
            //  nextsala.setText("Maghrib");
            which=4;
            // editor.putInt("whichnext",which);
            //editor.commit();
            hourleft=maghribhour-curr.get(Calendar.HOUR_OF_DAY);

            /*if(hourleft<4){
                circularProgressBar.setProgress(20);
            }
            if(hourleft<3){
                circularProgressBar.setProgress(40);
            }
            if(hourleft<2){
                circularProgressBar.setProgress(80);
            }
            if(hourleft<1){
                circularProgressBar.setProgress(90);
            }*/
        }

        if(curr.after(calendarmaghrib)&&curr.before(calendaisha)) {
            // nextsala.setText("Isha");
            which=5;
            // editor.putInt("whichnext",which);
            //  editor.commit();
            hourleft=aishahour-curr.get(Calendar.HOUR_OF_DAY);

           /* if(hourleft<4){
                circularProgressBar.setProgress(20);
            }
            if(hourleft<3){
                circularProgressBar.setProgress(40);
            }
            if(hourleft<2){
                circularProgressBar.setProgress(80);
            }
            if(hourleft<1){
                circularProgressBar.setProgress(90);
            }*/
        }

    }
    void pryertime(){

        views.setTextViewText(R.id.fajr_time, fajrhour+":"+fajrminutes);
        views.setTextViewText(R.id.dhour_time, dhuhrhour-12+":"+dhuhrminutes);
        views.setTextViewText(R.id.asr_time, asrhour-12+":"+asrminutes);
        views.setTextViewText(R.id.maghrib_time, maghribhour-12+":"+maghribminutes);
        views.setTextViewText(R.id.aish_time, aishahour-12+":"+aishaminutes);
    }

}

