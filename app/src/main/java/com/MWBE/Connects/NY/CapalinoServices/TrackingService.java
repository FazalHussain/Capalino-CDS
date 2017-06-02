package com.MWBE.Connects.NY.CapalinoServices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.Database.DatabaseBeen.TrackingData;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Fazal on 8/3/2016.
 */
public class TrackingService extends Service {

    Context context = this;
    private String userid;
    private double rating1;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        userid = extras.getString("Userid");
        GetTrackedData(userid);
        return Service.START_STICKY;
    }

    private void GetTrackedData(final String userid) {
        try {

            Thread thread_update = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Activity activity = (Activity)context;
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                        dataBaseHelper.createDataBase();
                        dataBaseHelper.openDataBase();
                        HttpClient httpclient = new DefaultHttpClient();
                        //showPB("Loading....");

                        String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getUserTrackListUpdated.php?UserID="+userid;
                        HttpPost httppost = new HttpPost(link);

                        ResponseHandler<String> responseHandler = new BasicResponseHandler();

                        final String response = httpclient.execute(httppost,
                                responseHandler);


                        Log.i("Response", "Response : " + response);

                            dataBaseHelper.delete("TrackListing");
                            JSONArray jsonarray = new JSONArray(response);
                            for (int i = 0; i < jsonarray.length(); i++) {
                              JSONObject jsonobj = jsonarray.getJSONObject(i);

                                String title = jsonobj.getString("ProcurementTitle");
                                String agency = jsonobj.getString("AgencyTitle");
                                String tracked_date = jsonobj.getString("TrackDate");
                                String deadline_date = jsonobj.getString("ProposalDeadLine");
                                String userid = jsonobj.getString("UserID");
                                String rating = jsonobj.getString("Rating");
                                String ProcId = jsonobj.getString("ProcurementID");
                                if(!rating.equalsIgnoreCase("")){
                                    try{
                                        rating1 = Double.parseDouble(rating);
                                    }catch (NumberFormatException e){
                                        rating1 = 0.0;
                                    }
                                }

                                title = title.replace("'","\\u0027");
                                agency = agency.replace("'","\\u0027");

                                 boolean isInserted = dataBaseHelper.InsertUserProcurmentTracking(new TrackingData(title, agency, tracked_date,
                                        deadline_date, userid, rating1,ProcId));
                                Log.d("trackeddata", "tracked");

                                    //list_data.add(new ListData(image, contentShortDescription, ContentRelevantDateTime));

                                    //isinserted = dataBaseHelper.InsertUserProcurmentTracking(been);
                                }




                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    stopSelf();
                }
            });
            thread_update.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
