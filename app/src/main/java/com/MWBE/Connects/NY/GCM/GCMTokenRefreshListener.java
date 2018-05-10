package com.MWBE.Connects.NY.GCM;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.NotificationCompat;

import com.MWBE.Connects.NY.AppConstants.Utils;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by fazal on 4/11/2017.
 */

public class GCMTokenRefreshListener extends InstanceIDListenerService {

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationCompat.Builder b=new NotificationCompat.Builder(this);
        startForeground(1, Utils.buildForegroundNotification(b));
    }

    @Override
    public void onTokenRefresh() {

        Intent gcmregister = new Intent(this,GCMTokenRefreshListener.class);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            startForegroundService(gcmregister);
        }else {
            startService(gcmregister);
        }

    }
}
