package com.example.muslim.Ui.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.muslim.R;
import com.example.muslim.Ui.AzanFragment.AzanFrgment;
import com.example.muslim.Ui.AzkarFragment.AzkarFragment;
import com.example.muslim.Ui.QuranFragment.QuranFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.simplealertdialog.SimpleAlertDialog;
import com.simplealertdialog.SimpleAlertDialogFragment;

public class MainActivity extends AppCompatActivity
{
    private   int REQUEST_CODE_THIS = 0;
    private static final int REQUEST_CODE_THAT = 1;
    private static final int REQUEST_CODE_THE_OTHER = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OnPressnavigitonBooton();
        ReplaceFragment(new AzanFrgment());

    }



    private void OnPressnavigitonBooton() {
        BottomNavigationView bottomNavigationView=findViewById(R.id.BottonNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.azan:
                        menuItem.setChecked(true);
                        ReplaceFragment(new AzanFrgment());
                        break;

                    case R.id.azkar:
                        menuItem.setChecked(true);
                        ReplaceFragment(new AzkarFragment());
                        break;

                    case R.id.quran:
                        menuItem.setChecked(true);
                        ReplaceFragment(new QuranFragment());
                        break;

                }
                return false;
            }
        });
    }

    public void ReplaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fram,fragment);
        fragmentTransaction.commit();
    }


}
