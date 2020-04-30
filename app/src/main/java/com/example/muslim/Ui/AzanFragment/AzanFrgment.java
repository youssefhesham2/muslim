package com.example.muslim.Ui.AzanFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.StringUtil;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.example.muslim.Brodcasts.DailyAzan;
import com.example.muslim.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.example.muslim.Brodcasts.DailyReceiver;
import com.simplealertdialog.SimpleAlertDialog;
import com.simplealertdialog.SimpleAlertDialogFragment;
import com.simplealertdialog.SimpleAlertDialogSupportFragment;


public class AzanFrgment extends Fragment implements SimpleAlertDialog.SingleChoiceArrayItemProvider  {
    View view;
    ImageView menu_image;
    RecyclerView recycler;
    TextView time,nextsala,timeleft,locationnn,calumethodname;
    CircularProgressBar circularProgressBar;
    double Longitude;
    double latitude;
    TimeZone timez;
    Context context;
    PrayTime prayTime;
    SharedPreferences preferences;
    int fajrhour,fajrminutes,dhuhrhour,dhuhrminutes,asrhour,asrminutes,maghribhour,maghribminutes,aishahour,aishaminutes,hourleft,minuteleft,which;
    private static final int REQUEST_CODE_THIS = 0;
    public static final String saharedname ="notificationsetting";
    private static final int REQUEST_CODE_THis2 = 1003;
    int choiceitem,choiceitem2=0;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.azan_fragment_xml, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prayTime=new PrayTime();
        intialviews();
        timer();
        intiloc();
        intialshardprefrance();
        makemuneu();
    }

    private void intialshardprefrance() {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         context = getActivity().getApplicationContext();
        setHasOptionsMenu(true);
    }
    @Override
    public CharSequence[] onCreateSingleChoiceArray(final SimpleAlertDialog dialog, int requestCode) {
        if (requestCode == REQUEST_CODE_THIS) {
            //Toast.makeText(context, "aaa", Toast.LENGTH_SHORT).show();
            return getResources().getTextArray(R.array.NotificationSetting);

        }
        if (requestCode == REQUEST_CODE_THis2) {
            //Toast.makeText(context, "aaa", Toast.LENGTH_SHORT).show();
            return getResources().getTextArray(R.array.CalculationSetting);

        }
        return null;
    }
    @Override
    public void onSingleChoiceArrayItemClick(final SimpleAlertDialog dialog, int requestCode,
                                             int position) {
        if (requestCode == REQUEST_CODE_THIS) {
            // Do something
            editor.putInt(saharedname,position);
            editor.commit();
            choiceitem = preferences.getInt(saharedname, 0);
            setfajradan(choiceitem);
        }
        if (requestCode == REQUEST_CODE_THis2) {
            editor.putInt("saharedname2",position);
            editor.commit();
            choiceitem2 = preferences.getInt("saharedname2", 0);
            switch (position){
                case 1:
                    prayTime.setCalcMethod(prayTime.getMWL());
                    inialpyertime();
                    calumethodname.setText("Muslim World League");
                    break;
               case 2:
                   prayTime.setCalcMethod(prayTime.getEgypt());
                   inialpyertime();
                   calumethodname.setText("Egyptian General Authority of Survey");
                   break;
                case 3:
                    prayTime.setCalcMethod(prayTime.getKarachi());
                    inialpyertime();
                    calumethodname.setText("University Of Islamic Sciences, Karachi");
                    break;
                case 4:
                    prayTime.setCalcMethod(prayTime.getMakkah());
                    inialpyertime();
                    calumethodname.setText("Umm Al-Qura");
                    break;
                case 5:
                    prayTime.setCalcMethod(prayTime.getISNA());
                    inialpyertime();
                    calumethodname.setText("Islamic Society of North America");
                    break;
            }

        }
    }
    private void inialpyertime() {
        prayTime.setTimeFormat(prayTime.Time24);
      //  prayTime.setCalcMethod(prayTime.getCustom());
        prayTime.setAsrJuristic(prayTime.Shafii);
        prayTime.setAdjustHighLats(prayTime.getAngleBased());
        int[] offsets = {0, 0, 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
        prayTime.tune(offsets);

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        int hours=cal.get(Calendar.HOUR);
        int minutes=cal.get(Calendar.MINUTE);

        timez = cal.getTimeZone().getDefault();
      //  double hoursDiff = (timez.getRawOffset() / 1000.0) / 3600;
        Double hoursDiff = (TimeZone.getTimeZone(Time.getCurrentTimezone()).getOffset(System.currentTimeMillis())) / (1000.0 * 60.0 * 60.0);

        ArrayList<String> prayerTimes = prayTime.getPrayerTimes(cal,
                latitude,Longitude , hoursDiff);
        ArrayList<String> prayerNames = prayTime.getTimeNames();

        AzanAdpter azanAdpter=new AzanAdpter(prayerTimes,prayerNames);
        recycler.setAdapter(azanAdpter);
        for (int i=0;i<7;i++){
            String Hora = prayerTimes.get(i);
            int hour = Integer.parseInt(Hora.substring(0, 2));
            int minute = Integer.parseInt(Hora.substring(3));
            switch (i){
                case 0:
                    fajrhour=hour;
                    fajrminutes=minute;
                    editor.putInt("fajrhour",hour);
                    editor.putInt("fajrminutes",minute);
                    editor.commit();
                    break;

                case 2:
                    dhuhrhour=hour;
                    dhuhrminutes=minute;
                    editor.putInt("dhuhrhour",hour);
                    editor.putInt("dhuhrminutes",minute);
                    editor.commit();
                    break;
                case 3:
                    asrhour=hour;
                    asrminutes=minute;
                    editor.putInt("asrhour",hour);
                    editor.putInt("asrminutes",minute);
                    editor.commit();
                    break;
                case 5:
                    maghribhour=hour;
                    maghribminutes=minute;
                    editor.putInt("maghribhour",hour);
                    editor.putInt("maghribminutes",minute);
                    editor.commit();
                    break;
                case 6:
                    aishahour=hour;
                    aishaminutes=minute;
                    editor.putInt("aishahour",hour);
                    editor.putInt("aishaminutes",minute);
                    editor.commit();
                    break;

            }
        }

        choiceitem = preferences.getInt(saharedname, 0);
        getallalrm(choiceitem);

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
            nextsala.setText("fajr");
            which=1;
            editor.putInt("whichnext",which);
            editor.commit();
            if(curr.get(Calendar.HOUR_OF_DAY)>fajrhour){
                hourleft=(fajrhour)+12-(curr.get(Calendar.HOUR_OF_DAY)-12);
                // hourleft=hou-fajrhour;
            }
            else{
                hourleft=fajrhour-curr.get(Calendar.HOUR_OF_DAY);

            }
            if(hourleft==9){
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
            }
        }

        if(curr.after(calendarfajr)&&curr.before(calendardhur)){
            nextsala.setText("Dhuhr");
            which=2;
            editor.putInt("whichnext",which);
            editor.commit();
            hourleft=dhuhrhour-curr.get(Calendar.HOUR_OF_DAY);

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
            }

        }
        if(curr.after(calendardhur)&&curr.before(calendarasr)){
            nextsala.setText("Asr");
            which=3;
            editor.putInt("whichnext",which);
            editor.commit();
            hourleft=asrhour-curr.get(Calendar.HOUR_OF_DAY);
            if(hourleft<4){
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
            }
        }

        if(curr.after(calendarasr)&&curr.before(calendarmaghrib)){
            nextsala.setText("Maghrib");
            which=4;
            editor.putInt("whichnext",which);
            editor.commit();
            hourleft=maghribhour-curr.get(Calendar.HOUR_OF_DAY);

            if(hourleft<4){
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
            }
        }

        if(curr.after(calendarmaghrib)&&curr.before(calendaisha)) {
            nextsala.setText("Isha");
            which=5;
            editor.putInt("whichnext",which);
            editor.commit();
            hourleft=aishahour-curr.get(Calendar.HOUR_OF_DAY);

            if(hourleft<4){
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
            }
        }

    }

    private void intiloc() {

        final FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {

            @Override
            public void onSuccess(Location location) {
                if (location != null)
                {
                    latitude =location.getLatitude();
                    Longitude= location.getLongitude();
                   // inialpyertime();
                }

                if(location ==null){
                    latitude =fusedLocationClient.getLastLocation().getResult().getLatitude();
                    Longitude= location.getLongitude();
                }
                TimeZone aaaa = TimeZone.getDefault();
                String filename = aaaa.getID();     // full file name
                int iend = filename.indexOf("/"); //this finds the first occurrence of "."
                String continent =null;
                String county=preferences.getString("country", null);
                if (iend != -1)
                {
                    continent= filename.substring(0 , iend); //this will give abc
                    //county= filename.substring(iend+1 , filename.length()); //this will give abc
                }

               Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String city = addresses.get(0).getLocality();
                    String country0 = addresses.get(0).getCountryName();
                    locationnn.setText(city+","+country0);
                    editor.putString("country",country0);
                    editor.commit();
                } catch (IOException e)
                {
                    e.printStackTrace();
                    locationnn.setText(county+","+continent);

                }


                prayTime.setCalcMethod(prayTime.getCustom());
                switch (continent){
                    case "Europe":
                        prayTime.setCalcMethod(prayTime.getMWL());
                        calumethodname.setText("Muslim World League");
                        break;
                    case "The Far East":
                        prayTime.setCalcMethod(prayTime.getMWL());
                        calumethodname.setText("Muslim World League");
                        break;
                    case "Africa":
                        prayTime.setCalcMethod(prayTime.getEgypt());
                        calumethodname.setText("Egyptian General Authority of Survey");
                        break;
                    case "The Arabian Peninsula":
                        prayTime.setCalcMethod(prayTime.getMakkah());
                        calumethodname.setText("Umm Al-Qura");
                        break;
                    case "Asia":
                        prayTime.setCalcMethod(prayTime.getMakkah());
                        calumethodname.setText("Umm Al-Qura");
                        break;
                }

                county=preferences.getString("country", null);
                switch (county){
                    case "USA":
                        prayTime.setCalcMethod(prayTime.getMWL());
                        calumethodname.setText("Muslim World League");
                        break;
                    case "Syria":
                        prayTime.setCalcMethod(prayTime.getEgypt());
                        calumethodname.setText("Egyptian General Authority of Survey");
                        break;
                    case "Iraq":
                        prayTime.setCalcMethod(prayTime.getEgypt());
                        calumethodname.setText("Egyptian General Authority of Survey");
                        break;
                    case "Lebanon":
                        prayTime.setCalcMethod(prayTime.getEgypt());
                        calumethodname.setText("Egyptian General Authority of Survey");
                        break;
                    case "Malaysia":
                        prayTime.setCalcMethod(prayTime.getEgypt());
                        calumethodname.setText("Egyptian General Authority of Survey");
                        break;
                    case "Pakistan":
                        prayTime.setCalcMethod(prayTime.getKarachi());
                        calumethodname.setText("University Of Islamic Sciences, Karachi");
                        break;
                    case "Bangladesh":
                        prayTime.setCalcMethod(prayTime.getKarachi());
                        calumethodname.setText("University Of Islamic Sciences, Karachi");
                        break;
                    case "India":
                        prayTime.setCalcMethod(prayTime.getKarachi());
                        calumethodname.setText("University Of Islamic Sciences, Karachi");
                        break;
                    case "Afghanistan":
                        prayTime.setCalcMethod(prayTime.getKarachi());
                        calumethodname.setText("University Of Islamic Sciences, Karachi");
                        break;
                    case "Canada":
                        prayTime.setCalcMethod(prayTime.getISNA());
                        calumethodname.setText("Islamic Society of North America");
                        break;
                    case "UK":
                        prayTime.setCalcMethod(prayTime.getISNA());
                        calumethodname.setText("Islamic Society of North America");
                        break;
                }

                    inialpyertime();


            }

        });

    }

    private void timer() {
        int timeinminutes = 30*60;
        CountDownTimer timer = new CountDownTimer(timeinminutes * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                SimpleDateFormat ss=new SimpleDateFormat("HH:mm:ss");
                String curenttime = new SimpleDateFormat(" hh:mm:ss a").format(new Date());
                time.setText(curenttime);

                Calendar cal = Calendar.getInstance();

                int minut=cal.get(Calendar.MINUTE);
                int hou=cal.get(Calendar.HOUR);
             //   Toast.makeText(context,hou+"",Toast.LENGTH_LONG).show();
                int mm;
                int hh;
                switch (which){
                    case 1:
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
                timeleft.setText("-"+hourleft+":"+minuteleft);

                circularProgressBar.setProgress(circularProgressBar.getProgress()+.005f);



            }


            public void onFinish() {

            }

        }.start();
    }

    private void intialviews() {
        recycler=view.findViewById(R.id.Recycler3);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity().getApplicationContext(),RecyclerView.VERTICAL,false);
        recycler.setLayoutManager(layoutManager);
        nextsala=view.findViewById(R.id.nextsala);
        time=view.findViewById(R.id.time);
        timeleft=view.findViewById(R.id.timeleft);
        circularProgressBar=view.findViewById(R.id.progress_bar);
        locationnn=view.findViewById(R.id.cityname);
        menu_image=view.findViewById(R.id.menue_button);
        calumethodname=view.findViewById(R.id.calumethodname);
    }
    public void onPopupButtonClick(View button) {
        PopupMenu popup = new PopupMenu(context, button);
        popup.getMenuInflater().inflate(R.menu.menu_setting, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.NotificationSetting:
                        item.setChecked(true);
                        choiceitem = preferences.getInt(saharedname, 0);
                        new SimpleAlertDialogSupportFragment.Builder().setTargetFragment(AzanFrgment.this)
                                .setTitle("Notification Setting")
                                .setSingleChoiceCheckedItem(choiceitem) // This enables a single choice list
                                .setRequestCode(REQUEST_CODE_THIS)
                                .create().show(getFragmentManager(), "dialog");
                        break;
                    case R.id.AzanTimesetting:
                        new SimpleAlertDialogSupportFragment.Builder().setTargetFragment(AzanFrgment.this)
                                .setTitle("Calculation Method")
                                .setSingleChoiceCheckedItem(choiceitem2) // This enables a single choice list
                                .setRequestCode(REQUEST_CODE_THis2)
                                .create().show(getFragmentManager(), "dialog2");
                        return true;
                    default:
                        break;
                }
                return true;
            }
        });

        popup.show();
    }
    void makemuneu(){

        menu_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPopupButtonClick(menu_image);
            }
        });
    }
    class AzanAdpter extends RecyclerView.Adapter<AzanAdpter.AzanViewHolder>{

        ArrayList<String> prayerTimes;
        ArrayList<String> prayerNames;

        public AzanAdpter(ArrayList<String> prayerTimes, ArrayList<String> prayerNames) {
            this.prayerTimes = prayerTimes;
            this.prayerNames = prayerNames;
        }

        @NonNull
        @Override
        public AzanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.azan_item,parent,false);
            return new AzanViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AzanViewHolder holder, int position) {

            String pryertime=prayerTimes.get(position);
            int hour22 = Integer.parseInt(pryertime.substring(0, 2));
            int minu33 = Integer.parseInt(pryertime.substring(3));

            String prayerNamess=prayerNames.get(position);

            holder.salaname.setText(prayerNamess);
            String am_pm="AM";
            if(hour22>12||(hour22==12&&minu33>0))
            {
                hour22=hour22-12;
                am_pm="PM";
            }

            if(position==0){

            }
            holder.azantime.setText(hour22+":"+minu33+"  "+am_pm);

        }

        @Override
        public int getItemCount() {
            return prayerNames.size();
        }

        class AzanViewHolder extends RecyclerView.ViewHolder
        {

            TextView salaname,azantime;
            public AzanViewHolder(@NonNull View itemView) {
                super(itemView);

                salaname=itemView.findViewById(R.id.salaname);
                azantime=itemView.findViewById(R.id.azantime);
            }
        }

    }


        public class PrayTime {

            // ---------------------- Global Variables --------------------
            private int calcMethod; // caculation method
            private int asrJuristic; // Juristic method for Asr
            private int dhuhrMinutes; // minutes after mid-day for Dhuhr
            private int adjustHighLats; // adjusting method for higher latitudes
            private int timeFormat; // time format
            private double lat; // latitude
            private double lng; // longitude
            private double timeZone; // time-zone
            private double JDate; // Julian date
            // ------------------------------------------------------------
            // Calculation Methods
            private int Jafari; // Ithna Ashari
            private int Karachi; // University of Islamic Sciences, Karachi
            private int ISNA; // Islamic Society of North America (ISNA)
            private int MWL; // Muslim World League (MWL)
            private int Makkah; // Umm al-Qura, Makkah
            private int Egypt; // Egyptian General Authority of Survey
            private int Custom; // Custom Setting
            private int Tehran; // Institute of Geophysics, University of Tehran
            // Juristic Methods
            private int Shafii; // Shafii (standard)
            private int Hanafi; // Hanafi
            // Adjusting Methods for Higher Latitudes
            private int None; // No adjustment
            private int MidNight; // middle of night
            private int OneSeventh; // 1/7th of night
            private int AngleBased; // angle/60th of night
            // Time Formats
            private int Time24; // 24-hour format
            private int Time12; // 12-hour format
            private int Time12NS; // 12-hour format with no suffix
            private int Floating; // floating point number
            // Time Names
            private ArrayList<String> timeNames;
            private String InvalidTime; // The string used for invalid times
            // --------------------- Technical Settings --------------------
            private int numIterations; // number of iterations needed to compute times
            // ------------------- Calc Method Parameters --------------------
            private HashMap<Integer, double[]> methodParams;

            /*
             * this.methodParams[methodNum] = new Array(fa, ms, mv, is, iv);
             *
             * fa : fajr angle ms : maghrib selector (0 = angle; 1 = minutes after
             * sunset) mv : maghrib parameter value (in angle or minutes) is : isha
             * selector (0 = angle; 1 = minutes after maghrib) iv : isha parameter value
             * (in angle or minutes)
             */
            private double[] prayerTimesCurrent;
            private int[] offsets;

            public PrayTime() {
                // Initialize vars

                this.setCalcMethod(0);
                this.setAsrJuristic(0);
                this.setDhuhrMinutes(0);
                this.setAdjustHighLats(1);
                this.setTimeFormat(0);

                // Calculation Methods
                this.setJafari(0); // Ithna Ashari
                this.setKarachi(1); // University of Islamic Sciences, Karachi
                this.setISNA(2); // Islamic Society of North America (ISNA)
                this.setMWL(3); // Muslim World League (MWL)
                this.setMakkah(4); // Umm al-Qura, Makkah
                this.setEgypt(5); // Egyptian General Authority of Survey
                this.setTehran(6); // Institute of Geophysics, University of Tehran
                this.setCustom(7); // Custom Setting

                // Juristic Methods
                this.setShafii(0); // Shafii (standard)
                this.setHanafi(1); // Hanafi

                // Adjusting Methods for Higher Latitudes
                this.setNone(0); // No adjustment
                this.setMidNight(1); // middle of night
                this.setOneSeventh(2); // 1/7th of night
                this.setAngleBased(3); // angle/60th of night

                // Time Formats
                this.setTime24(0); // 24-hour format
                this.setTime12(1); // 12-hour format
                this.setTime12NS(2); // 12-hour format with no suffix
                this.setFloating(3); // floating point number

                // Time Names
                timeNames = new ArrayList<String>();
                timeNames.add("Fajr");
                timeNames.add("Sunrise");
                timeNames.add("Dhuhr");
                timeNames.add("Asr");
                timeNames.add("Sunset");
                timeNames.add("Maghrib");
                timeNames.add("Isha");

                InvalidTime = "-----"; // The string used for invalid times

                // --------------------- Technical Settings --------------------

                this.setNumIterations(1); // number of iterations needed to compute
                // times

                // ------------------- Calc Method Parameters --------------------

                // Tuning offsets {fajr, sunrise, dhuhr, asr, sunset, maghrib, isha}
                offsets = new int[7];
                offsets[0] = 0;
                offsets[1] = 0;
                offsets[2] = 0;
                offsets[3] = 0;
                offsets[4] = 0;
                offsets[5] = 0;
                offsets[6] = 0;

                /*
                 *
                 * fa : fajr angle ms : maghrib selector (0 = angle; 1 = minutes after
                 * sunset) mv : maghrib parameter value (in angle or minutes) is : isha
                 * selector (0 = angle; 1 = minutes after maghrib) iv : isha parameter
                 * value (in angle or minutes)
                 */
                methodParams = new HashMap<Integer, double[]>();

                // Jafari
                double[] Jvalues = {16,0,4,0,14};
                methodParams.put(Integer.valueOf(this.getJafari()), Jvalues);

                // Karachi
                double[] Kvalues = {18,1,0,0,18};
                methodParams.put(Integer.valueOf(this.getKarachi()), Kvalues);

                // ISNA
                double[] Ivalues = {15,1,0,0,15};
                methodParams.put(Integer.valueOf(this.getISNA()), Ivalues);

                // MWL
                double[] MWvalues = {18,1,0,0,17};
                methodParams.put(Integer.valueOf(this.getMWL()), MWvalues);

                // Makkah
                double[] MKvalues = {18.5,1,0,1,90};
                methodParams.put(Integer.valueOf(this.getMakkah()), MKvalues);

                // Egypt
                double[] Evalues = {19.5,1,0,0,17.5};
                methodParams.put(Integer.valueOf(this.getEgypt()), Evalues);

                // Tehran
                double[] Tvalues = {17.7,0,4.5,0,14};
                methodParams.put(Integer.valueOf(this.getTehran()), Tvalues);

                // Custom
                double[] Cvalues = {18,1,0,0,17};
                methodParams.put(Integer.valueOf(this.getCustom()), Cvalues);

            }

            // ---------------------- Trigonometric Functions -----------------------
            // range reduce angle in degrees.
            private double fixangle(double a) {

                a = a - (360 * (Math.floor(a / 360.0)));

                a = a < 0 ? (a + 360) : a;

                return a;
            }

            // range reduce hours to 0..23
            private double fixhour(double a) {
                a = a - 24.0 * Math.floor(a / 24.0);
                a = a < 0 ? (a + 24) : a;
                return a;
            }

            // radian to degree
            private double radiansToDegrees(double alpha) {
                return ((alpha * 180.0) / Math.PI);
            }

            // deree to radian
            private double DegreesToRadians(double alpha) {
                return ((alpha * Math.PI) / 180.0);
            }

            // degree sin
            private double dsin(double d) {
                return (Math.sin(DegreesToRadians(d)));
            }

            // degree cos
            private double dcos(double d) {
                return (Math.cos(DegreesToRadians(d)));
            }

            // degree tan
            private double dtan(double d) {
                return (Math.tan(DegreesToRadians(d)));
            }

            // degree arcsin
            private double darcsin(double x) {
                double val = Math.asin(x);
                return radiansToDegrees(val);
            }

            // degree arccos
            private double darccos(double x) {
                double val = Math.acos(x);
                return radiansToDegrees(val);
            }

            // degree arctan
            private double darctan(double x) {
                double val = Math.atan(x);
                return radiansToDegrees(val);
            }

            // degree arctan2
            private double darctan2(double y, double x) {
                double val = Math.atan2(y, x);
                return radiansToDegrees(val);
            }

            // degree arccot
            private double darccot(double x) {
                double val = Math.atan2(1.0, x);
                return radiansToDegrees(val);
            }

            // ---------------------- Time-Zone Functions -----------------------
            // compute local time-zone for a specific date
            private double getTimeZone1() {
                TimeZone timez = TimeZone.getDefault();
                double hoursDiff = (timez.getRawOffset() / 1000.0) / 3600;
                return hoursDiff;
            }

            // compute base time-zone of the system
            private double getBaseTimeZone() {
                TimeZone timez = TimeZone.getDefault();
                double hoursDiff = (timez.getRawOffset() / 1000.0) / 3600;
                return hoursDiff;

            }

            // detect daylight saving in a given date
            private double detectDaylightSaving() {
                TimeZone timez = TimeZone.getDefault();
                double hoursDiff = timez.getDSTSavings();
                return hoursDiff;
            }

            // ---------------------- Julian Date Functions -----------------------
            // calculate julian date from a calendar date
            private double julianDate(int year, int month, int day) {

                if (month <= 2) {
                    year -= 1;
                    month += 12;
                }
                double A = Math.floor(year / 100.0);

                double B = 2 - A + Math.floor(A / 4.0);

                double JD = Math.floor(365.25 * (year + 4716))
                        + Math.floor(30.6001 * (month + 1)) + day + B - 1524.5;

                return JD;
            }

            // convert a calendar date to julian date (second method)
            private double calcJD(int year, int month, int day) {
                double J1970 = 2440588.0;
                Date date = new Date();

                double ms = date.getTime(); // # of milliseconds since midnight Jan 1,
                // 1970
                double days = Math.floor(ms / (1000.0 * 60.0 * 60.0 * 24.0));
                return J1970 + days - 0.5;

            }

            // ---------------------- Calculation Functions -----------------------
            // References:
            // http://www.ummah.net/astronomy/saltime
            // http://aa.usno.navy.mil/faq/docs/SunApprox.html
            // compute declination angle of sun and equation of time
            private double[] sunPosition(double jd) {

                double D = jd - 2451545;
                double g = fixangle(357.529 + 0.98560028 * D);
                double q = fixangle(280.459 + 0.98564736 * D);
                double L = fixangle(q + (1.915 * dsin(g)) + (0.020 * dsin(2 * g)));

                // double R = 1.00014 - 0.01671 * [self dcos:g] - 0.00014 * [self dcos:
                // (2*g)];
                double e = 23.439 - (0.00000036 * D);
                double d = darcsin(dsin(e) * dsin(L));
                double RA = (darctan2((dcos(e) * dsin(L)), (dcos(L))))/ 15.0;
                RA = fixhour(RA);
                double EqT = q/15.0 - RA;
                double[] sPosition = new double[2];
                sPosition[0] = d;
                sPosition[1] = EqT;

                return sPosition;
            }

            // compute equation of time
            private double equationOfTime(double jd) {
                double eq = sunPosition(jd)[1];
                return eq;
            }

            // compute declination angle of sun
            private double sunDeclination(double jd) {
                double d = sunPosition(jd)[0];
                return d;
            }

            // compute mid-day (Dhuhr, Zawal) time
            private double computeMidDay(double t) {
                double T = equationOfTime(this.getJDate() + t);
                double Z = fixhour(12 - T);
                return Z;
            }

            // compute time for a given angle G
            private double computeTime(double G, double t) {

                double D = sunDeclination(this.getJDate() + t);
                double Z = computeMidDay(t);
                double Beg = -dsin(G) - dsin(D) * dsin(this.getLat());
                double Mid = dcos(D) * dcos(this.getLat());
                double V = darccos(Beg/Mid)/15.0;

                return Z + (G > 90 ? -V : V);
            }

            // compute the time of Asr
            // Shafii: step=1, Hanafi: step=2
            private double computeAsr(double step, double t) {
                double D = sunDeclination(this.getJDate() + t);
                double G = -darccot(step + dtan(Math.abs(this.getLat() - D)));
                return computeTime(G, t);
            }

            // ---------------------- Misc Functions -----------------------
            // compute the difference between two times
            private double timeDiff(double time1, double time2) {
                return fixhour(time2 - time1);
            }

            // -------------------- Interface Functions --------------------
            // return prayer times for a given date
            private ArrayList<String> getDatePrayerTimes(int year, int month, int day,
                                                         double latitude, double longitude, double tZone) {
                this.setLat(latitude);
                this.setLng(longitude);
                this.setTimeZone(tZone);
                this.setJDate(julianDate(year, month, day));
                double lonDiff = longitude / (15.0 * 24.0);
                this.setJDate(this.getJDate() - lonDiff);
                return computeDayTimes();
            }

            // return prayer times for a given date
            private ArrayList<String> getPrayerTimes(Calendar date, double latitude,
                                                     double longitude, double tZone) {

                int year = date.get(Calendar.YEAR);
                int month = date.get(Calendar.MONTH);
                int day = date.get(Calendar.DATE);

                return getDatePrayerTimes(year, month+1, day, latitude, longitude, tZone);
            }

            // set custom values for calculation parameters
            private void setCustomParams(double[] params) {

                for (int i = 0; i < 5; i++) {
                    if (params[i] == -1) {
                        params[i] = methodParams.get(this.getCalcMethod())[i];
                        methodParams.put(this.getCustom(), params);
                    } else {
                        methodParams.get(this.getCustom())[i] = params[i];
                    }
                }
                this.setCalcMethod(this.getCustom());
            }

            // set the angle for calculating Fajr
            public void setFajrAngle(double angle) {
                double[] params = {angle, -1, -1, -1, -1};
                setCustomParams(params);
            }

            // set the angle for calculating Maghrib
            public void setMaghribAngle(double angle) {
                double[] params = {-1, 0, angle, -1, -1};
                setCustomParams(params);

            }

            // set the angle for calculating Isha
            public void setIshaAngle(double angle) {
                double[] params = {-1, -1, -1, 0, angle};
                setCustomParams(params);

            }

            // set the minutes after Sunset for calculating Maghrib
            public void setMaghribMinutes(double minutes) {
                double[] params = {-1, 1, minutes, -1, -1};
                setCustomParams(params);

            }

            // set the minutes after Maghrib for calculating Isha
            public void setIshaMinutes(double minutes) {
                double[] params = {-1, -1, -1, 1, minutes};
                setCustomParams(params);

            }

            // convert double hours to 24h format
            public String floatToTime24(double time) {

                String result;

                if (Double.isNaN(time)) {
                    return InvalidTime;
                }

                time = fixhour(time + 0.5 / 60.0); // add 0.5 minutes to round
                int hours = (int)Math.floor(time);
                double minutes = Math.floor((time - hours) * 60.0);

                if ((hours >= 0 && hours <= 9) && (minutes >= 0 && minutes <= 9)) {
                    result = "0" + hours + ":0" + Math.round(minutes);
                } else if ((hours >= 0 && hours <= 9)) {
                    result = "0" + hours + ":" + Math.round(minutes);
                } else if ((minutes >= 0 && minutes <= 9)) {
                    result = hours + ":0" + Math.round(minutes);
                } else {
                    result = hours + ":" + Math.round(minutes);
                }
                return result;
            }

            // convert double hours to 12h format
            public String floatToTime12(double time, boolean noSuffix) {

                if (Double.isNaN(time)) {
                    return InvalidTime;
                }

                time = fixhour(time + 0.5 / 60); // add 0.5 minutes to round
                int hours = (int)Math.floor(time);
                double minutes = Math.floor((time - hours) * 60);
                String suffix, result;
                if (hours >= 12) {
                    suffix = "pm";
                } else {
                    suffix = "am";
                }
                hours = ((((hours+ 12) -1) % (12))+ 1);
        /*hours = (hours + 12) - 1;
        int hrs = (int) hours % 12;
        hrs += 1;*/
                if (noSuffix == false) {
                    if ((hours >= 0 && hours <= 9) && (minutes >= 0 && minutes <= 9)) {
                        result = "0" + hours + ":0" + Math.round(minutes) + " "
                                + suffix;
                    } else if ((hours >= 0 && hours <= 9)) {
                        result = "0" + hours + ":" + Math.round(minutes) + " " + suffix;
                    } else if ((minutes >= 0 && minutes <= 9)) {
                        result = hours + ":0" + Math.round(minutes) + " " + suffix;
                    } else {
                        result = hours + ":" + Math.round(minutes) + " " + suffix;
                    }

                } else {
                    if ((hours >= 0 && hours <= 9) && (minutes >= 0 && minutes <= 9)) {
                        result = "0" + hours + ":0" + Math.round(minutes);
                    } else if ((hours >= 0 && hours <= 9)) {
                        result = "0" + hours + ":" + Math.round(minutes);
                    } else if ((minutes >= 0 && minutes <= 9)) {
                        result = hours + ":0" + Math.round(minutes);
                    } else {
                        result = hours + ":" + Math.round(minutes);
                    }
                }
                return result;

            }

            // convert double hours to 12h format with no suffix
            public String floatToTime12NS(double time) {
                return floatToTime12(time, true);
            }

            // ---------------------- Compute Prayer Times -----------------------
            // compute prayer times at given julian date
            private double[] computeTimes(double[] times) {

                double[] t = dayPortion(times);

                double Fajr = this.computeTime(
                        180 - methodParams.get(this.getCalcMethod())[0], t[0]);

                double Sunrise = this.computeTime(180 - 0.833, t[1]);

                double Dhuhr = this.computeMidDay(t[2]);
                double Asr = this.computeAsr(1 + this.getAsrJuristic(), t[3]);
                double Sunset = this.computeTime(0.833, t[4]);

                double Maghrib = this.computeTime(
                        methodParams.get(this.getCalcMethod())[2], t[5]);
                double Isha = this.computeTime(
                        methodParams.get(this.getCalcMethod())[4], t[6]);

                double[] CTimes = {Fajr, Sunrise, Dhuhr, Asr, Sunset, Maghrib, Isha};

                return CTimes;

            }

            // compute prayer times at given julian date
            private ArrayList<String> computeDayTimes() {
                double[] times = {5, 6, 12, 13, 18, 18, 18}; // default times

                for (int i = 1; i <= this.getNumIterations(); i++) {
                    times = computeTimes(times);
                }

                times = adjustTimes(times);
                times = tuneTimes(times);

                return adjustTimesFormat(times);
            }

            // adjust times in a prayer time array
            private double[] adjustTimes(double[] times) {
                for (int i = 0; i < times.length; i++) {
                    times[i] += this.getTimeZone() - this.getLng() / 15;
                }

                times[2] += this.getDhuhrMinutes() / 60; // Dhuhr
                if (methodParams.get(this.getCalcMethod())[1] == 1) // Maghrib
                {
                    times[5] = times[4] + methodParams.get(this.getCalcMethod())[2]/ 60;
                }
                if (methodParams.get(this.getCalcMethod())[3] == 1) // Isha
                {
                    times[6] = times[5] + methodParams.get(this.getCalcMethod())[4]/ 60;
                }

                if (this.getAdjustHighLats() != this.getNone()) {
                    times = adjustHighLatTimes(times);
                }

                return times;
            }

            // convert times array to given time format
            private ArrayList<String> adjustTimesFormat(double[] times) {

                ArrayList<String> result = new ArrayList<String>();

                if (this.getTimeFormat() == this.getFloating()) {
                    for (double time : times) {
                        result.add(String.valueOf(time));
                    }
                    return result;
                }

                for (int i = 0; i < 7; i++) {
                    if (this.getTimeFormat() == this.getTime12()) {
                        result.add(floatToTime12(times[i], false));
                    } else if (this.getTimeFormat() == this.getTime12NS()) {
                        result.add(floatToTime12(times[i], true));
                    } else {
                        result.add(floatToTime24(times[i]));
                    }
                }
                return result;
            }

            // adjust Fajr, Isha and Maghrib for locations in higher latitudes
            private double[] adjustHighLatTimes(double[] times) {
                double nightTime = timeDiff(times[4], times[1]); // sunset to sunrise

                // Adjust Fajr
                double FajrDiff = nightPortion(methodParams.get(this.getCalcMethod())[0]) * nightTime;

                if (Double.isNaN(times[0]) || timeDiff(times[0], times[1]) > FajrDiff) {
                    times[0] = times[1] - FajrDiff;
                }

                // Adjust Isha
                double IshaAngle = (methodParams.get(this.getCalcMethod())[3] == 0) ? methodParams.get(this.getCalcMethod())[4] : 18;
                double IshaDiff = this.nightPortion(IshaAngle) * nightTime;
                if (Double.isNaN(times[6]) || this.timeDiff(times[4], times[6]) > IshaDiff) {
                    times[6] = times[4] + IshaDiff;
                }

                // Adjust Maghrib
                double MaghribAngle = (methodParams.get(this.getCalcMethod())[1] == 0) ? methodParams.get(this.getCalcMethod())[2] : 4;
                double MaghribDiff = nightPortion(MaghribAngle) * nightTime;
                if (Double.isNaN(times[5]) || this.timeDiff(times[4], times[5]) > MaghribDiff) {
                    times[5] = times[4] + MaghribDiff;
                }

                return times;
            }

            // the night portion used for adjusting times in higher latitudes
            private double nightPortion(double angle) {
                double calc = 0;

                if (adjustHighLats == AngleBased)
                    calc = (angle)/60.0;
                else if (adjustHighLats == MidNight)
                    calc = 0.5;
                else if (adjustHighLats == OneSeventh)
                    calc = 0.14286;

                return calc;
            }

            // convert hours to day portions
            private double[] dayPortion(double[] times) {
                for (int i = 0; i < 7; i++) {
                    times[i] /= 24;
                }
                return times;
            }

            // Tune timings for adjustments
            // Set time offsets
            public void tune(int[] offsetTimes) {

                for (int i = 0; i < offsetTimes.length; i++) { // offsetTimes length
                    // should be 7 in order
                    // of Fajr, Sunrise,
                    // Dhuhr, Asr, Sunset,
                    // Maghrib, Isha
                    this.offsets[i] = offsetTimes[i];
                }
            }

            private double[] tuneTimes(double[] times) {
                for (int i = 0; i < times.length; i++) {
                    times[i] = times[i] + this.offsets[i] / 60.0;
                }

                return times;
            }


            public int getCalcMethod() {
                return calcMethod;
            }

            public void setCalcMethod(int calcMethod) {
                this.calcMethod = calcMethod;
            }

            public int getAsrJuristic() {
                return asrJuristic;
            }

            public void setAsrJuristic(int asrJuristic) {
                this.asrJuristic = asrJuristic;
            }

            public int getDhuhrMinutes() {
                return dhuhrMinutes;
            }

            public void setDhuhrMinutes(int dhuhrMinutes) {
                this.dhuhrMinutes = dhuhrMinutes;
            }

            public int getAdjustHighLats() {
                return adjustHighLats;
            }

            public void setAdjustHighLats(int adjustHighLats) {
                this.adjustHighLats = adjustHighLats;
            }

            public int getTimeFormat() {
                return timeFormat;
            }

            public void setTimeFormat(int timeFormat) {
                this.timeFormat = timeFormat;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public double getTimeZone() {
                return timeZone;
            }

            public void setTimeZone(double timeZone) {
                this.timeZone = timeZone;
            }

            public double getJDate() {
                return JDate;
            }

            public void setJDate(double jDate) {
                JDate = jDate;
            }

            private int getJafari() {
                return Jafari;
            }

            private void setJafari(int jafari) {
                Jafari = jafari;
            }

            private int getKarachi() {
                return Karachi;
            }

            private void setKarachi(int karachi) {
                Karachi = karachi;
            }

            private int getISNA() {
                return ISNA;
            }

            private void setISNA(int iSNA) {
                ISNA = iSNA;
            }

            private int getMWL() {
                return MWL;
            }

            private void setMWL(int mWL) {
                MWL = mWL;
            }

            private int getMakkah() {
                return Makkah;
            }

            private void setMakkah(int makkah) {
                Makkah = makkah;
            }

            private int getEgypt() {
                return Egypt;
            }

            private void setEgypt(int egypt) {
                Egypt = egypt;
            }

            private int getCustom() {
                return Custom;
            }

            private void setCustom(int custom) {
                Custom = custom;
            }

            private int getTehran() {
                return Tehran;
            }

            private void setTehran(int tehran) {
                Tehran = tehran;
            }

            private int getShafii() {
                return Shafii;
            }

            private void setShafii(int shafii) {
                Shafii = shafii;
            }

            private int getHanafi() {
                return Hanafi;
            }

            private void setHanafi(int hanafi) {
                Hanafi = hanafi;
            }

            private int getNone() {
                return None;
            }

            private void setNone(int none) {
                None = none;
            }

            private int getMidNight() {
                return MidNight;
            }

            private void setMidNight(int midNight) {
                MidNight = midNight;
            }

            private int getOneSeventh() {
                return OneSeventh;
            }

            private void setOneSeventh(int oneSeventh) {
                OneSeventh = oneSeventh;
            }

            private int getAngleBased() {
                return AngleBased;
            }

            private void setAngleBased(int angleBased) {
                AngleBased = angleBased;
            }

            private int getTime24() {
                return Time24;
            }

            private void setTime24(int time24) {
                Time24 = time24;
            }

            private int getTime12() {
                return Time12;
            }

            private void setTime12(int time12) {
                Time12 = time12;
            }

            private int getTime12NS() {
                return Time12NS;
            }

            private void setTime12NS(int time12ns) {
                Time12NS = time12ns;
            }

            private int getFloating() {
                return Floating;
            }

            private void setFloating(int floating) {
                Floating = floating;
            }

            private int getNumIterations() {
                return numIterations;
            }

            private void setNumIterations(int numIterations) {
                this.numIterations = numIterations;
            }

            public ArrayList<String> getTimeNames() {
                return timeNames;
            }
        }

        public void setfagrAlarm() {
        // Quote in Morning at 08:32:00 AM
        int hhh;
        int mmm;
        Calendar calendar = Calendar.getInstance();
        if(fajrminutes<=5){
            hhh=fajrhour-1;
            mmm=(60-5)+fajrminutes;

        }
        else {
            hhh=fajrhour;
            mmm=fajrminutes-5;
        }
        calendar.set(Calendar.HOUR_OF_DAY, hhh);
        calendar.set(Calendar.MINUTE, mmm);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cur = Calendar.getInstance();

        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent myIntent = new Intent(context, DailyReceiver.class);
            myIntent.putExtra("a","five minutes left for fajr prayer- تبقى خمس دقائق لصلاة الفجر");
            myIntent.putExtra("b","fajr");

            final int ALARM1_ID = 10001;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

            setdhourAlarm();

        }
    public void setdhourAlarm() {
        // Quote in Morning at 08:32:00 AM
        int hhh;
        int mmm;
        Calendar calendar = Calendar.getInstance();
        if(dhuhrminutes<=5){
            hhh=dhuhrhour-1;
            mmm=(60-5)+dhuhrminutes;
        }
        else {
            hhh=dhuhrhour;
            mmm=dhuhrminutes-5;
        }
        calendar.set(Calendar.HOUR_OF_DAY, hhh);
        calendar.set(Calendar.MINUTE, mmm);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cur = Calendar.getInstance();

        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent myIntent = new Intent(context, DailyReceiver.class);
        myIntent.putExtra("a","five minutes left for Dhur prayer- تبقى خمس دقائق لصلاة الظهر");
        myIntent.putExtra("b","dhur");

       final int ALARM1_ID = 10002;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        setasrAlarm();

    }

    public void setasrAlarm() {
        // Quote in Morning at 08:32:00 AM
        int hhh;
        int mmm;
        Calendar calendar = Calendar.getInstance();
        if(asrminutes<=5){
            hhh=asrhour-1;
            mmm=(60-5)+asrminutes;
        }
        else {
            hhh=asrhour;
            mmm=asrminutes-5;
        }
        calendar.set(Calendar.HOUR_OF_DAY, hhh);
        calendar.set(Calendar.MINUTE, mmm);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cur = Calendar.getInstance();

        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent myIntent = new Intent(context, DailyReceiver.class);
        myIntent.putExtra("a","five minutes left for asr prayer- تبقى خمس دقائق لصلاة العصر");
        myIntent.putExtra("b","asr");

        final    int ALARM1_ID = 10003;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        setmaghribAlarm();
    }

      public void setmaghribAlarm() {
        // Quote in Morning at 08:32:00 AM
        int hhh;
        int mmm;
        Calendar calendar = Calendar.getInstance();
        if(maghribminutes<=5){
            hhh=maghribhour-1;
            mmm=(60-5)+maghribminutes;
        }
        else {
            hhh=maghribhour;
            mmm=maghribminutes-5;
        }
        calendar.set(Calendar.HOUR_OF_DAY, hhh);
        calendar.set(Calendar.MINUTE, mmm);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cur = Calendar.getInstance();

        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

          Intent myIntent = new Intent(context, DailyReceiver.class);
          myIntent.putExtra("a","five minutes left for maghrib prayer- تبقى خمس دقائق لصلاة المغرب");
          myIntent.putExtra("b","maghrib");

          final int ALARM1_ID = 10009;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        setaishaAlarm();
    }
    public void setaishaAlarm() {
        // Quote in Morning at 08:32:00 AM
        int hhh;
        int mmm;
        Calendar calendar = Calendar.getInstance();
        if(aishaminutes<=5){
            hhh=aishahour-1;
            mmm=(60-5)+aishaminutes;
        }
        else {
            hhh=aishahour;
            mmm=aishaminutes-5;
        }
        calendar.set(Calendar.HOUR_OF_DAY, hhh);
        calendar.set(Calendar.MINUTE, mmm);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cur = Calendar.getInstance();

       if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent myIntent = new Intent(context, DailyReceiver.class);
        myIntent.putExtra("a","five minutes left for isha prayer- تبقى خمس دقائق لصلاة العشاء");
        myIntent.putExtra("b","isha");

        final int ALARM1_ID = 1000010;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

       // setfagrAlarm();
    }


    public void setfajradan(int a) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY,fajrhour);
        calendar.set(Calendar.MINUTE, fajrminutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cur = Calendar.getInstance();

        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 5);
        }

        Intent myIntent = new Intent(context, DailyAzan.class);
        myIntent.putExtra("b2",a);
        final int ALARM1_ID = 10004;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        setdhourAdan(a);
    }
   public void setdhourAdan(int a) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, dhuhrhour);
        calendar.set(Calendar.MINUTE, dhuhrminutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cur = Calendar.getInstance();

       if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent myIntent = new Intent(context, DailyAzan.class);
       myIntent.putExtra("b2",a);
       final int ALARM1_ID = 10005;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        setasrAdan(a);
    }

    public void setasrAdan(int a) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, asrhour);
        calendar.set(Calendar.MINUTE, asrminutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cur = Calendar.getInstance();

        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent myIntent = new Intent(context, DailyAzan.class);
        myIntent.putExtra("b2",a);
        final int ALARM1_ID = 10006;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        setmaghribAdan(a);
    }

    public void setmaghribAdan(int a) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, maghribhour);
        calendar.set(Calendar.MINUTE, maghribminutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cur = Calendar.getInstance();

       if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent myIntent = new Intent(context, DailyAzan.class);
        myIntent.putExtra("b2",a);
        final int ALARM1_ID = 10007;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        setishaAdan(a);
    }

    public void setishaAdan(int a) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, aishahour);
        calendar.set(Calendar.MINUTE, aishaminutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar cur = Calendar.getInstance();

        if (cur.after(calendar)) {
            calendar.add(Calendar.DATE, 1);
        }

        Intent myIntent = new Intent(context, DailyAzan.class);
        myIntent.putExtra("b2",a);
        final int ALARM1_ID = 10008;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, ALARM1_ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
       // setfajradan();
    }


    void getallalrm(int a){
        setfagrAlarm();
        setfajradan(a);
    }
}

