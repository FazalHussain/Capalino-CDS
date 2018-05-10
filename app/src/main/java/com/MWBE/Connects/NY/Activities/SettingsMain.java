package com.MWBE.Connects.NY.Activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.MWBE.Connects.NY.CapalinoServices.CapabilitiesService;
import com.MWBE.Connects.NY.Fragments.ChangePassword;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.R;

public class SettingsMain extends FragmentActivity {

    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_main);
        init();
    }

    private void init() {
        utils = new Utils(this);
        Intent j = new Intent(this, CapabilitiesService.class);
        j.putExtra("Userid", utils.getdata("Userid"));
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            startForegroundService(j);
        }else {
            startService(j);
        }
        if(Data.DateExpire){
            ((ImageView)findViewById(R.id.browsebtn)).setEnabled(false);
            ((ImageView)findViewById(R.id.browsebtn)).setAlpha(0.5f);
            ((ImageView)findViewById(R.id.trackbtn)).setEnabled(false);
            ((ImageView)findViewById(R.id.trackbtn)).setAlpha(0.5f);
        }

        setAppVersion();
    }

    private void setAppVersion() {
        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //int versionNumber = pinfo.versionCode;

        String versionName = pinfo.versionName;

        ((TextView)findViewById(R.id.version_number_tv)).setText("App Version " +versionName);
    }

    public void UpdateProfileClick(View view) {
        Intent i = new Intent(this, UpdateProfileActivity.class);
        startActivity(i);
    }
    
    public void SettingsClick(View view){
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
    }

    public void ManageSubscriptionClick(View view){
        Intent i = new Intent(this, SubscribtionActivity.class);
        startActivity(i);
    }

    public void ChangePasswordClick(View view){
       getSupportFragmentManager().beginTransaction().add(R.id.main,new ChangePassword()).addToBackStack(null).commit();
    }

    //Footer Click Listener
    public void HomeClick(View view) {
        Intent i = new Intent(SettingsMain.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void BrowseClick(View view) {
        Intent i = new Intent(SettingsMain.this, BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view) {
        Intent i = new Intent(SettingsMain.this, TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view) {
        Intent i = new Intent(SettingsMain.this, ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        
        startActivity(i);
    }

    boolean exit = false;
    @Override
    public void onBackPressed() {
        //Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main);
        /*Fragment f = getSupportFragmentManager().findFragmentById(R.id.main);
        if (f instanceof ChangePassword)
        {
            getSupportFragmentManager().beginTransaction().remove(f).commit();
        }*/
         if (exit) {
            finish();// finish activity
        } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

        }
    }
}
