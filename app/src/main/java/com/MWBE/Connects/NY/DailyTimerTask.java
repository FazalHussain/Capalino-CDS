package com.MWBE.Connects.NY;

import android.app.Activity;
import android.widget.Toast;

import java.util.TimerTask;

/**
 * Created by Fazal on 9/21/2016.
 */
public class DailyTimerTask extends TimerTask {

    Activity context;

    public DailyTimerTask(Activity context) {
        this.context = context;
    }

    @Override
    public void run() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, "Hello", Toast.LENGTH_LONG).show();
            }
        });
    }
}
