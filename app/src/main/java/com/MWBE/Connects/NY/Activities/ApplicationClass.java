package com.MWBE.Connects.NY.Activities;

import android.app.Application;

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
