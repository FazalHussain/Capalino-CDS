package com.MWBE.Connects.NY.GCM;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by fazal on 4/11/2017.
 */

public class GCMTokenRefreshListener extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        Intent gcmregister = new Intent(this,GCMTokenRefreshListener.class);
        startService(gcmregister);
    }
}
