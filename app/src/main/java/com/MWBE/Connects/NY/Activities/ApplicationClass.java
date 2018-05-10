package com.MWBE.Connects.NY.Activities;

import android.app.Application;
import android.support.annotation.NonNull;

import com.MWBE.Connects.NY.BuildConfig;
import com.MWBE.Connects.NY.UpdateHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;

import io.branch.referral.Branch;

/**
 * Created by fazal on 5/9/2017.
 */

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the Branch object
        Branch.getAutoInstance(this);

    }
}
