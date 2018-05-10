package com.MWBE.Connects.NY.GCM;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.MWBE.Connects.NY.AppConstants.Utils;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by fazal on 4/11/2017.
 */

public class GcmRegistrationIntentService extends IntentService {

    public static final String registration_success = "success";
    public static final String registrationerror = "error";
    private Utils utils;


    public GcmRegistrationIntentService() {
        super("");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            NotificationCompat.Builder b=new NotificationCompat.Builder(this);
            startForeground(1, Utils.buildForegroundNotification(b));
        }


    }



    @Override
    protected void onHandleIntent(Intent intent) {
        GcmRegisteration();
    }

    public void GcmRegisteration(){
        utils = new Utils(this);
        Intent registerComplet = null;
        String token = null;
        try {
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            token = instanceID.getToken("71528358113", GoogleCloudMessaging.INSTANCE_ID_SCOPE,null);
            Log.d("Token",token);
            registerToken(token);
            registerComplet = new Intent(registration_success);
            registerComplet.putExtra("token",token);
        }catch (Exception e){
            Log.d("Error","RegistrationError");
            registerComplet = new Intent(registrationerror);
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(registerComplet);
    }

    private void registerToken(String token) {
        OkHttpClient client = new OkHttpClient();
       /* RequestBody requestbody = new FormBody.Builder()
                .add("token",token)
                .add("Name","")
                .build();*/
        Log.d("UserID",utils.getdata("Userid"));

        Request request = new Request.Builder()
                .url("http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addCapalinoToken.php?UserID="+
                        utils.getdata("Userid")+"&Token="+token+"&Device=Android")
                //.post(requestbody)
                .build();

        try{
            Response response = client.newCall(request).execute();
            Log.d("token_send",response.body().string());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
