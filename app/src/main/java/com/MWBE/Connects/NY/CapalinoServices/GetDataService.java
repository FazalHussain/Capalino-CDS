package com.MWBE.Connects.NY.CapalinoServices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.MWBE.Connects.NY.AppConstants.Utils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

/**
 * Created by Fazal on 8/16/2016.
 */
public class GetDataService extends Service {
    Context context = this;
    private String userid;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        userid = extras.getString("Userid");
        getGeographicCount(userid);
        getTargetCount(userid);
        getCertificationCount(userid);
        return Service.START_STICKY;
    }

    private void getGeographicCount(String userid) {

        final Utils utils = new Utils(context);

        String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getUserGeoTags.php?UserID="+userid;
        new AsyncTask<String, Void, Integer>() {
            @Override
            protected Integer doInBackground(String... params) {
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");

                    HttpPost httppost = new HttpPost(params[0]);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    if(response!=null) {
                        JSONArray jsonArray = new JSONArray(response);
                        int count = jsonArray.length();
                        return count;
                    }
                    return 0;

                } catch (Exception e) {
                    e.printStackTrace();
                }

                stopSelf();

                return 0;
            }

            @Override
            protected void onPostExecute(Integer s) {
                super.onPostExecute(s);
                utils.savedata("geographiccount", s + " Selected");
            }
        }.execute(link, "", "");

        stopSelf();
    }

    private void getTargetCount(String userid) {

        final Utils utils = new Utils(context);

        String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getUserContractTags.php?UserID="+userid;
        new AsyncTask<String, Void, Integer>() {
            @Override
            protected Integer doInBackground(String... params) {
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");

                    HttpPost httppost = new HttpPost(params[0]);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    if(response!=null) {
                        JSONArray jsonArray = new JSONArray(response);
                        int count = jsonArray.length();
                        return count;
                    }
                    return 0;

                } catch (Exception e) {
                    e.printStackTrace();
                }



                return 0;
            }

            @Override
            protected void onPostExecute(Integer s) {
                super.onPostExecute(s);
                utils.savedata("targetcount", s + " Selected");
            }
        }.execute(link, "", "");

        stopSelf();
    }

    private void getCertificationCount(String userid) {

        final Utils utils = new Utils(context);

        String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getUserCertificationTags.php?UserID="+userid;
        new AsyncTask<String, Void, Integer>() {
            @Override
            protected Integer doInBackground(String... params) {
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");

                    HttpPost httppost = new HttpPost(params[0]);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    if(response!=null) {
                        JSONArray jsonArray = new JSONArray(response);
                        int count = jsonArray.length();
                        return count;
                    }
                    return 0;

                } catch (Exception e) {
                    e.printStackTrace();
                }



                return 0;
            }

            @Override
            protected void onPostExecute(Integer s) {
                super.onPostExecute(s);

                utils.savedata("certificationcount",s+" Selected");
            }
        }.execute(link, "", "");

        stopSelf();
    }
}
