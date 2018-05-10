package com.MWBE.Connects.NY;

import android.app.Application;
import android.support.annotation.NonNull;

import com.MWBE.Connects.NY.CapalinoServices.ConnectivityReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Fazal on 9/19/2016.
 */
public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();




    }

    public synchronized MyApplication getInstance() {
        mInstance = this;
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
