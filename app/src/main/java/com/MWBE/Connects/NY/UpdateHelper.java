package com.MWBE.Connects.NY;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

/**
 * Created by fazal on 1/11/2018.
 */

public class UpdateHelper {

    public static String KEY_UPDATE_ENABLE = "isUpdate";
    public static String KEY_UPDATE_VERSION = "version";
    public static String KEY_UPDATE_APP_URL = "app_url";

    public interface onUpdateCheckListener{
        void onUpdateCheckListener(String update_url);
    }

    Context context;
    onUpdateCheckListener onUpdateCheckListener;

    public UpdateHelper(Context context, UpdateHelper.onUpdateCheckListener onUpdateCheckListener) {
        this.context = context;
        this.onUpdateCheckListener = onUpdateCheckListener;
    }

    public void check(){
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        if(firebaseRemoteConfig.getBoolean(KEY_UPDATE_ENABLE)){
            String current_version = firebaseRemoteConfig.getString(KEY_UPDATE_VERSION);
            String appversion = getAppversion();
            String update_url = firebaseRemoteConfig.getString(KEY_UPDATE_APP_URL);

            if(!TextUtils.equals(current_version, appversion) && onUpdateCheckListener!=null){
                onUpdateCheckListener.onUpdateCheckListener(update_url);
            }
        }
    }

    private String getAppversion() {
        PackageInfo pinfo = null;
        try {
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //int versionNumber = pinfo.versionCode;

        return pinfo.versionName;
    }

    public static Builder with(Context context){
        return new Builder(context);
    }

    public static class Builder{
        Context context;
        onUpdateCheckListener onUpdateCheckListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onUpdateCheck(onUpdateCheckListener onUpdateCheckListener){
            this.onUpdateCheckListener = onUpdateCheckListener;
            return this;
        }

        public UpdateHelper build(){
            return new UpdateHelper(context, onUpdateCheckListener);
        }

        public UpdateHelper check(){
            UpdateHelper updateHelper = build();
            updateHelper.check();

            return updateHelper;
        }


    }

}
