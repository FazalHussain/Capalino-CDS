package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.CapalinoServices.CapabilitiesService;
import com.MWBE.Connects.NY.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends Activity {

    private Timer timer;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startService();
        final DonutProgress progress = (DonutProgress) findViewById(R.id.donut_progress);
        ImageView imageview = (ImageView) findViewById(R.id.splash_image);
        int[] images = new int[] {R.drawable.splash1, R.drawable.splash2, R.drawable.splash3};

        int imageId = (int)(Math.random() * images.length);

        imageview.setBackgroundResource(images[imageId]);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.setProgress(progress.getProgress() + 5);
                        if(progress.getProgress()==progress.getMax()){
                            Intent i = new Intent(Splash.this,HomeActivity.class);
                            //i.putExtra("islogin","yes");
                            startActivity(i);
                            finish();
                        }
                    }
                });
            }
        }, 1000, 100);

    }

    private void startService() {
        utils = new Utils(this);
        Intent j = new Intent(this, CapabilitiesService.class);
        j.putExtra("Userid", utils.getdata("Userid"));
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            startForegroundService(j);
        }else{
            startService(j);
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }
}
