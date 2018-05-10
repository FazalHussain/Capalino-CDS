package com.MWBE.Connects.NY.CapalinoServices;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.MWBE.Connects.NY.Activities.BrowseActivity;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.Database.DatabaseBeen.ProcMaster;
import com.MWBE.Connects.NY.JavaBeen.TaggedRFP;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fazal on 5/3/2017.
 */

public class DailyMethodService extends Service {

    private Thread thread_update;

    Context context = this;
    private String lastupdatedate = "";
    private DataBaseHelper dataBaseHelper;




    /*@Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        getData(context);
        getTagedRFP();
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            NotificationCompat.Builder b=new NotificationCompat.Builder(this);
            startForeground(1, Utils.buildForegroundNotification(b));
        }

    }

    private void getTagedRFP() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    dataBaseHelper = new DataBaseHelper(context);
                    dataBaseHelper.createDataBase();
                    dataBaseHelper.openDataBase();

                    Cursor cursor = dataBaseHelper.DBRecord("Select LastUpdateDate from ProcurementRFPPreferences");
                    if (cursor.getCount() > 0) {
                        cursor.moveToLast();
                        lastupdatedate = cursor.getString(0);
                        cursor.close();
                    }


                    HttpClient httpclient = new DefaultHttpClient();

                    SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                    String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getTaggedRFPUpdatedForStore.php?lastUpdateSql=" + lastupdatedate
                            + "&CurrentDate=" + format.format(new Date());
                    link = link.replace(" ", "%20");
                    HttpPost httppost = new HttpPost(link);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    String response = httpclient.execute(httppost,
                            responseHandler);


                    Log.i("Response", "Response : " + response);
                    if (!response.trim().equalsIgnoreCase("Both are Equal")) {
                        dataBaseHelper.delete("ProcurementRFPPreferences");
                        JSONArray jsonarray = new JSONArray(response);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobj = jsonarray.getJSONObject(i);
                            String PreferenceID = jsonobj.getString("PreferenceID");
                            String ProcurementID = jsonobj.getString("ProcurementID");
                            String SettingTypeID = jsonobj.getString("SettingTypeID");
                            String ActualTagID = jsonobj.getString("ActualTagID");
                            String AddedDateTime = jsonobj.getString("AddedDateTime");
                            String lastupdate = jsonobj.getString("LASTUPDATE");
                            String Status = jsonobj.getString("Status");

                            //list_data.add(new ListData(image, contentShortDescription, ContentRelevantDateTime));
                            TaggedRFP been = new TaggedRFP(Integer.valueOf(PreferenceID), Integer.valueOf(ProcurementID), Integer.valueOf(SettingTypeID),
                                    Integer.valueOf(ActualTagID), AddedDateTime, lastupdate, Status);
                            final boolean isinserted = dataBaseHelper.InsertinRFPPrefence(been);


                            if (isinserted) {
                                //Toast.makeText(HomeActivity.this, "Hello", Toast.LENGTH_LONG).show();
                                Log.d("RFPUpdated", "Added");
                            }
                        }

                        stopSelf();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //getData(context);
        getTagedRFP();
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
