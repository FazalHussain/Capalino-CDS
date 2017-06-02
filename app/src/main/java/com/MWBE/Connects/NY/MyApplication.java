package com.MWBE.Connects.NY;

import android.app.Application;

import com.MWBE.Connects.NY.CapalinoServices.ConnectivityReceiver;

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
