package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.MWBE.Connects.NY.BuildConfig;
import com.MWBE.Connects.NY.AppConstants.Constants;
import com.MWBE.Connects.NY.CapalinoServices.CapabilitiesService;
import com.MWBE.Connects.NY.CapalinoServices.DailyMethodService;
import com.MWBE.Connects.NY.Database.DatabaseBeen.ProcMaster;
import com.MWBE.Connects.NY.GCM.GcmRegistrationIntentService;
import com.MWBE.Connects.NY.Storage.Storage;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.JavaBeen.ContentMasterUpdatedModel;
import com.MWBE.Connects.NY.JavaBeen.ListData;
import com.MWBE.Connects.NY.JavaBeen.ViewHolder;
import com.MWBE.Connects.NY.R;
import com.MWBE.Connects.NY.UpdateHelper;
import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;

public class HomeActivity extends Activity implements
        UpdateHelper.onUpdateCheckListener {

    private ListView lv;
    private ArrayList<ListData> list_data;
    private CustomListAdapter adapter;
    float historicX = Float.NaN, historicY = Float.NaN;
    static final int DELTA = 50;
    private boolean isvisibleswipe;
    private ArrayList<Boolean> isvisible = new ArrayList<>();
    ArrayList<Integer> pos = new ArrayList<>();
    private int currentpos;
    private static final int MIN_DISTANCE = 100;
    private SwipeGestureListener gesturelistener;
    private int image;
    private Context context = this;
    private String post;
    private Utils utils;
    private JSONObject jsonobj;
    private String date;
    private boolean isinserted;
    private String lastupdate = "";
    private ProgressDialog pb;
    private String lastupdatedate = "";
    private String ExpirationDate;
    private String PaymentStatus;
    private String contenturl = "";
    private String contentdescrip;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_CALENDAR,
            android.Manifest.permission.WRITE_CALENDAR,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };


    private BroadcastReceiver broadcastreciever;
    private View view;
    private SimpleDateFormat dateformat;
    private String lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{

            final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build();
            firebaseRemoteConfig.setConfigSettings(configSettings);

            HashMap<String, Object> map = new HashMap<>();
            map.put(UpdateHelper.KEY_UPDATE_ENABLE, false);
            map.put(UpdateHelper.KEY_UPDATE_VERSION, 1.0);
            map.put(UpdateHelper.KEY_UPDATE_APP_URL, "app url");

            firebaseRemoteConfig.setDefaults(map);

            firebaseRemoteConfig.fetch(5).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    firebaseRemoteConfig.activateFetched();
                    UpdateHelper.with(HomeActivity.this)
                            .onUpdateCheck(HomeActivity.this)
                            .check();
                }
            });



            broadcastreciever = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if(intent.getAction().endsWith(GcmRegistrationIntentService.registration_success)){
                        String token = intent.getStringExtra("token");
                    }
                }
            };

            int resultcode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
            if(ConnectionResult.SUCCESS!=resultcode){
                if(GooglePlayServicesUtil.isUserRecoverableError(resultcode)){
                    Log.d("GooglePlayError","Googel Play Service is not avaiable in this device!!!");
                }
            }else {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                    Intent intent = new Intent(this, GcmRegistrationIntentService.class);
                    startForegroundService(intent);
                }else {
                    Intent intent = new Intent(this, GcmRegistrationIntentService.class);
                    startService(intent);
                }

            }

        }catch (IllegalStateException e){
            e.printStackTrace();
        }
        setContentView(R.layout.activity_home);
        verifyCalenderPermissions(HomeActivity.this);
        initalize_facebook();
        initalize_twitter();
        init();

    }

    private void welcomeBack() {
        try{

            new AlertDialog.Builder(this)
                    .setTitle("Welcome back!")
                    .setMessage("Every week we add hundreds of new RFPs from the " +
                            "City, State, and private companies to MWBE Connect NY. Please visit " +
                            "the homepage to see new events and announcements.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            utils.savedata("last_used_date", dateformat.format(new Date()));
                        }
                    }).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void verifyCalenderPermissions(Activity activity) {
        // Check if we have write permission
        int permissionCheck = ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.WRITE_CALENDAR);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initalize_facebook();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastreciever,
                new IntentFilter(GcmRegistrationIntentService.registration_success));

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastreciever,
                new IntentFilter(GcmRegistrationIntentService.registrationerror));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastreciever);
        hidePB();
    }

    private void initalize_twitter() {
        TwitterAuthConfig authConfig =  new TwitterAuthConfig(Constants.twitter_api, Constants.getTwitter_api_secret);
        Fabric.with(this, new TwitterCore(authConfig), new TweetComposer());
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);
    }

    private void initalize_facebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
    }

    public void logoutClick(View view) {

        new AlertDialog.Builder(context)
                .setTitle("Alert!")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddLastLogout();
                        Logout();
                        utils.savedata("Updateddate", "");
                        utils.savedata("UpdateddateProc", "");
                        utils.savedata("RFPUpdatedDate", "");
                        stopService(new Intent(HomeActivity.this, CapabilitiesService.class));
                        if (!utils.getdata("remeberme").equalsIgnoreCase("true")){
                            utils.savedata("email", "");
                        }
                        utils.savedata("fname", "");
                        utils.savedata("lname", "");
                        utils.savedata("pass", "");
                        //utils.savedata("ischecked", "");
                        utils.savedata("logout" , "logout");
                        Storage.updateData = null;
                        Storage.count1=0;
                        Storage.count2=0;
                        Storage.count3=0;
                        Storage.count4=0;
                        Storage.count5=0;
                        Storage.count6=0;
                        Storage.count7=0;
                        Storage.count8=0;
                        Storage.count9=0;
                        Storage.count10=0;
                        Data.ActualID_list_advertising.clear();
                        Data.ActualID_list_architectural.clear();
                        Data.ActualID_list_construction.clear();
                        Data.ActualID_list_envoirnmental.clear();
                        Data.ActualID_list_solidwaste.clear();
                        Data.ActualID_list_facilities.clear();
                        Data.ActualID_list_safety.clear();
                        Data.ActualID_list_it.clear();
                        Data.ActualID_list_humanservice.clear();
                        Data.ActualID_list_others.clear();
                        Intent i = new Intent(HomeActivity.this,
                                LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void AddLastLogout() {

        final SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/" +
                "addLoginReportCapalino.php";

        new AsyncTask<String, Void, String>(){


            @Override
            protected String doInBackground(String... params) {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost1 = new HttpPost(params[0]);
                    //LastLoginTime

                    List<NameValuePair> param = new ArrayList<>();
                    String email = utils.getdata("email");
                    String UserID = utils.getdata("Userid");
                    //LastLogoutTime
                    String date = dateformat.format(new Date());
                    param.add(new BasicNameValuePair("UserEmailAddress", email));
                    param.add(new BasicNameValuePair("LastLogoutTime", date));
                    param.add(new BasicNameValuePair("UserID", UserID));

                    httppost1.setEntity(new UrlEncodedFormEntity(param));

                    ResponseHandler<String> responseHandler1 = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost1,
                            responseHandler1);

                    Log.d("responsee", response1);
                    return response1;
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return null;
            }
        }.execute(url);
    }

    private void Logout() {

        String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/" +
                "deleteUserFromMaintainUpdates.php?UserID="+utils.getdata("Userid");

        new AsyncTask<String, Void, String>(){


            @Override
            protected String doInBackground(String... params) {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost1 = new HttpPost(params[0]);

                    ResponseHandler<String> responseHandler1 = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost1,
                            responseHandler1);

                    Log.d("responsee", response1);
                    return response1;
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return null;
            }
        }.execute(url);
    }


    private void init() {
        //deleteDatabase("CapalinoDataBase.sqlite");
        utils = new Utils(HomeActivity.this);
        getPaymentStatus();
        //getTagedRFP();

        Data.ActualID_list_advertising.clear();
        Data.ActualID_list_architectural.clear();
        Data.ActualID_list_construction.clear();
        Data.ActualID_list_envoirnmental.clear();
        Data.ActualID_list_solidwaste.clear();
        Data.ActualID_list_facilities.clear();
        Data.ActualID_list_safety.clear();
        Data.ActualID_list_it.clear();
        Data.ActualID_list_humanservice.clear();
        Data.ActualID_list_others.clear();

        if(utils.getdata("ischecked").equalsIgnoreCase("false1"))
        utils.savedata("ischecked","true");
        lv = (ListView) findViewById(R.id.list_lv);
        if (getIntent().getStringExtra("islogin") != null) {
           try{
               AlertDialog dialog = new AlertDialog.Builder(context)
                       .setTitle("Alert!")
                       .setMessage(getString(R.string.aggrement))
                       .setPositiveButton("I Accept", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {

                           }
                       })
                       .setCancelable(false)
                       .setIcon(android.R.drawable.ic_dialog_alert)
                       .show();

               TextView textView = (TextView) dialog.findViewById(android.R.id.message);
               Typeface face = Typeface.createFromAsset(getAssets(), "gotham_book.otf");
               textView.setTypeface(face);
           }catch (Exception e){
               e.printStackTrace();
           }




        }

        /*if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            Intent i = new Intent(this, TrackingService.class);
            i.putExtra("Userid", utils.getdata("Userid"));
            startForegroundService(i);
        }else {
            Intent i = new Intent(this, TrackingService.class);
            i.putExtra("Userid", utils.getdata("Userid"));
            startService(i);
        }*/



        list_data = new ArrayList<ListData>();

        populateList();
        if(utils.getdata("UpdateddateProc") == null || utils.getdata("UpdateddateProc").equalsIgnoreCase("")){
            DailyRunMethod("FreshDb");
        }else {
            DailyRunMethod(utils.getdata("UpdateddateProc"));
        }

        try {
            if (utils.getdata("last_used_date") != null && !utils.getdata("last_used_date").equalsIgnoreCase("")) {
                String today_date = dateformat.format(new Date());
                Date date1_current = dateformat.parse(today_date);
                String last_used = utils.getdata("last_used_date");
                Date last_used_date = dateformat.parse(last_used);

                long diff = getDifferenceDays(last_used_date, date1_current);
                Log.d("datediff", diff + "days");
                //If someone has not used app in a long time, when they return there should be a
                // "Welcome Back" message that they see first
                if(diff >= 5){
                    welcomeBack();
                }

            }

            utils.savedata("last_used_date", dateformat.format(new Date()));
            Log.d("datediff", "days saved");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    private void getPaymentStatus() {

        final Thread getData_thread = new Thread(new Runnable() {
            @Override
            public void run() {

                dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                final Date date = new Date();

                String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getPaymentStatus.php?UserID=" +utils.getdata("Userid");
                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");
                    HttpPost httppost = new HttpPost(url);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    JSONArray jsonarray = new JSONArray(response);
                    for(int i = 0; i<jsonarray.length(); i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        PaymentStatus = jsonobj.getString("PaymentStatus");
                        ExpirationDate = jsonobj.getString("ExpirationDate");
                    }
                    if(ExpirationDate!=null)
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    if(dateformat.parse(ExpirationDate).before(dateformat.parse(dateformat.format(date)))) {
                                        Data.DateExpire = true;
                                        ((ImageView)findViewById(R.id.browsebtn)).setEnabled(false);
                                        ((ImageView)findViewById(R.id.browsebtn)).setAlpha(0.5f);
                                        ((ImageView)findViewById(R.id.trackbtn)).setEnabled(false);
                                        ((ImageView)findViewById(R.id.trackbtn)).setAlpha(0.5f);
                                    }else {
                                        Data.DateExpire = false;
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    if(PaymentStatus!=null)
                    if(PaymentStatus.equalsIgnoreCase("trial")){
                        Data.istrial = true;
                        utils.savedata("paymentStatus","trail");
                    }else {
                        Data.istrial = false;
                        utils.savedata("paymentStatus","paid");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        getData_thread.start();


    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try{
                    if(pb == null) {
                        pb = new ProgressDialog(context);
                        pb.setMessage(message);
                        pb.setCancelable(false);
                    }
                    pb.show();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }

    void hidePB() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (pb != null && pb.isShowing()){
                    pb.dismiss();
                    //Log.d("hidepb","hide");
                }

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hidePB();
    }


    private void LastUpdateDate(final String date_updated) {
        try {

            Thread thread_update = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(HomeActivity.this);
                        dataBaseHelper.createDataBase();
                        dataBaseHelper.openDataBase();
                        HttpClient httpclient = new DefaultHttpClient();
                        //showPB("Loading....");

                        //String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/isNewContentUpdated.php?lastUpdateSql=" +date;
                        //utils.getdata("Userid")
                        String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/isNewContentUpdated_j.php?ContentLastUpdateDate=" + date_updated +
                                "&UserID=" + utils.getdata("Userid") + "&TableName=ContentMaster_J";
                        link = link.replace(" ","%20");
                        link = link.replace("\n","");
                        HttpPost httppost = new HttpPost(link);

                        ResponseHandler<String> responseHandler = new BasicResponseHandler();

                        final String response = httpclient.execute(httppost,
                                responseHandler);


                        Log.i("Response", "Response : " + response);
                        if (!response.equalsIgnoreCase("") && !response.equalsIgnoreCase("No new content available.")) {



                            JSONObject jsonObject = new JSONObject(response);
                            String dataStatus = jsonObject.getString("datastatus");

                            if(dataStatus.equalsIgnoreCase("Full")){
                                showPB("Loading...");
                                dataBaseHelper.delete("ContentMasterUpdated");
                            }

                            String results = jsonObject.getString("results");
                            JSONArray jsonarray = new JSONArray(results);
                            String lastUpdateFinal = jsonObject.getString("lastupdatedate");
                            utils.savedata("Updateddate", lastUpdateFinal);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                ArrayList<String> deletedatalist = utils.getArray();
                                jsonobj = jsonarray.getJSONObject(i);
                                lastupdate = jsonobj.getString("LASTUPDATE");
                                int contentid = Integer.parseInt(jsonobj.getString("ContentID"));
                                    String contenttype = jsonobj.getString("ContentType");
                                    SetupImageIconFromServer(contenttype);
                                    String ContentTitle = jsonobj.getString("ContentTitle");
                                    String ContentDescription = jsonobj.getString("ContentDescription");
                                    String ReferenceURL = jsonobj.getString("ReferenceURL");
                                    String EventStartDateTime = jsonobj.getString("EventStartDateTime");
                                    String ContentPostedDate = jsonobj.getString("ContentPostedDate");

                                    String EventEndDateTime = jsonobj.getString("EventEndDateTime");
                                    String EventLocation = jsonobj.getString("EventLocation");
                                    String EventCost = jsonobj.getString("EventCost");

                                    String ContentStatus = jsonobj.getString("ContentStatus");
                                    String Action = jsonobj.getString("Action");

                                    ContentTitle = ContentTitle.replace("'", "''");
                                    ContentDescription = ContentDescription.replace("'", "''");
                                    EventLocation = EventLocation.replace("'","''");

                                    if(dataStatus.equalsIgnoreCase("Full")){
                                        if(deletedatalist.size()>0){
                                            if(!deletedatalist.contains(ContentTitle)){
                                                ContentMasterUpdatedModel been = new ContentMasterUpdatedModel(contentid,ContentTitle, ContentDescription,
                                                        EventStartDateTime, EventEndDateTime, EventLocation, EventCost,
                                                        ReferenceURL, ContentPostedDate, lastupdate, contenttype, ContentStatus, Action);
                                                isinserted = dataBaseHelper.InsertContentMaster(been);
                                                if (isinserted) {
                                                    utils.savedata("Updateddate", lastUpdateFinal);
                                                }
                                            }
                                        }else {
                                            ContentMasterUpdatedModel been = new ContentMasterUpdatedModel(contentid,ContentTitle, ContentDescription,
                                                    EventStartDateTime, EventEndDateTime, EventLocation, EventCost,
                                                    ReferenceURL, ContentPostedDate, lastupdate, contenttype, ContentStatus, Action);
                                            isinserted = dataBaseHelper.InsertContentMaster(been);
                                            if (isinserted) {
                                                utils.savedata("Updateddate", lastUpdateFinal);
                                            }
                                        }
                                    }else {
                                        if (Action.equalsIgnoreCase("add")){
                                            Log.d("add_title", ContentTitle);
                                            ContentMasterUpdatedModel been = new ContentMasterUpdatedModel(contentid,ContentTitle, ContentDescription,
                                                    EventStartDateTime, EventEndDateTime, EventLocation, EventCost,
                                                    ReferenceURL, ContentPostedDate, lastupdate, contenttype, ContentStatus, Action);
                                            isinserted = dataBaseHelper.InsertContentMaster(been);
                                            if(isinserted) {
                                                Log.i("Added" + ContentTitle, "added data");
                                                utils.savedata("Updateddate", lastUpdateFinal);
                                            }
                                        }

                                        if (Action.equalsIgnoreCase("edit")){
                                            ContentMasterUpdatedModel been = new ContentMasterUpdatedModel(contentid,ContentTitle, ContentDescription,
                                                    EventStartDateTime, EventEndDateTime, EventLocation, EventCost,
                                                    ReferenceURL, ContentPostedDate, lastupdate, contenttype, ContentStatus, Action);
                                            dataBaseHelper.UpdateContentMaster(been);
                                            utils.savedata("Updateddate", lastUpdateFinal);
                                        }

                                        if (Action.equalsIgnoreCase("delete")){
                                            ContentMasterUpdatedModel been = new ContentMasterUpdatedModel(contentid,ContentTitle, ContentDescription,
                                                    EventStartDateTime, EventEndDateTime, EventLocation, EventCost,
                                                    ReferenceURL, ContentPostedDate, lastupdate, contenttype, ContentStatus, Action);
                                            dataBaseHelper.deleteContentMasterRecord(been.getContentID());


                                        }
                                    }



                                //}else {
                                    //return;
                                //}
                            }




                            hidePB();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                        //Toast.makeText(HomeActivity.this, "Hello", Toast.LENGTH_LONG).show();
                                        populateList();
                                        Log.d("IsItemAdded", "Added");

                                }
                            });
                        }
                    } catch (Exception e) {
                        hidePB();
                        e.printStackTrace();
                    }
                }
            });
            thread_update.start();


        } catch (Exception e) {
            hidePB();
            e.printStackTrace();
        }
    }

    private void SetupImageIconFromServer(String contenttype) {
        switch (contenttype) {
            case "1": {
                image = R.drawable.icon2;
                break;
            }

            case "2": {
                image = R.drawable.announcement;
                break;
            }

            case "3": {
                image = R.drawable.icon1;
                break;
            }

            case "4": {
                image = R.drawable.icon4;
                break;
            }

            case "5": {
                image = R.drawable.icon3;
                break;
            }
        }
    }

    private void setuplistinit() {
        for (int i = 0; i < list_data.size(); i++) {
            isvisible.add(i, false);
        }
    }

    private void populateList() {
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(HomeActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();

            //Cursor cursor = dataBaseHelper.getDataFromDB("", "", "ContentMasterUpdated", false,true,"ContentPostedDate");
            Cursor cursor = dataBaseHelper.getDataFromQuery("Select * from ContentMasterUpdated where ContentStatus=1");
            list_data.clear();
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String contentTitle = cursor.getString(1).replace("\\u0027","'");
                    //String contentDescription = cursor.getString(1).replace("\\u0027","'");
                    setupimageicon(cursor);

                    //list_data.add(new ListData(image, cursor.getString(1), cursor.getString(8)));
                    String[] date_arr = cursor.getString(3).split(" ");
                    if(image == R.drawable.icon2){
                        list_data.add(new ListData(image, contentTitle, date_arr[0]));
                    }else {
                        list_data.add(new ListData(image, contentTitle, cursor.getString(8)));
                    }

                    date = cursor.getString(9);

                    Log.d("status", contentTitle + cursor.getString(11));
                }


                setuplistinit();

                final DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
                Collections.sort(list_data, new Comparator<ListData>() {
                    @Override
                    public int compare(ListData o1, ListData o2) {
                        try {
                            return f.parse(o2.getTime()).compareTo(f.parse(o1.getTime()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        return 0;
                    }
                });
            }



            adapter = new CustomListAdapter(HomeActivity.this, R.layout.activity_home, list_data);
            lv.setAdapter(adapter);
            listclick();
            swipelist(lv);

            if(utils.getdata("Updateddate") == null || utils.getdata("Updateddate").equalsIgnoreCase("")){
                LastUpdateDate("FreshDb");
            }else {
                LastUpdateDate(utils.getdata("Updateddate"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DailyRunMethod(final String date_updated){
       /* Date date2pm = new Date();
        date2pm.setHours(10);
        date2pm.setMinutes(46);

        Timer timer = new Timer();

        timer.schedule(new DailyTimerTask(HomeActivity.this), date2pm, 86400000);*/
       /* AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(HomeActivity.this, DailyMethodService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0, i, 0);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 3000, pendingIntent);*/
        /*Intent i = new Intent(HomeActivity.this, DailyMethodService.class);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            startForegroundService(i);
        }else {
            startService(i);
        }*/



        Thread thread_update = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Activity activity = (Activity)context;
                    Log.d("st", String.valueOf(System.currentTimeMillis()));
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                    dataBaseHelper.createDataBase();
                    dataBaseHelper.openDataBase();

                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");
                    //utils.getdata("Userid");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                    Date date = new Date();
                    String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getProcurementDaily_J.php?UserID="
                            +utils.getdata("Userid")+"&currentDate="+simpleDateFormat.format(date) +
                            "&ContentLastUpdateDate=" + date_updated;
                    link = link.replace(" ","%20");
                    HttpPost httppost = new HttpPost(link);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);

                    if (!response.equalsIgnoreCase("No new content available.") && !response.equalsIgnoreCase("")) {



                        JSONObject jsonObject = new JSONObject(response);
                        String dataStatus = jsonObject.getString("datastatus");

                        if (dataStatus.equalsIgnoreCase("Full")) {
                            showPB("Loading...");
                            if (!dataBaseHelper.sqliteDataBase.isOpen()) {
                                dataBaseHelper.openDataBase();
                            }
                            dataBaseHelper.delete("ProcurementMaster");
                        }

                        String results = jsonObject.getString("results");
                        JSONArray jsonarray = new JSONArray(results);

                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobj = jsonarray.getJSONObject(i);

                            String ProcurementID = jsonobj.getString("ProcurementID");
                            String ProcurementEPIN = jsonobj.getString("ProcurementEPIN");
                            String ProcurementSource = jsonobj.getString("ProcurementSource");
                            String ProcurementAgencyID = jsonobj.getString("ProcurementAgencyID");


                            ProcurementAgencyID = ProcurementAgencyID.replace("'", "''");

                            String ProcurementTypeIDP = jsonobj.getString("ProcurementTypeIDP");
                            String ProcurementTitle = jsonobj.getString("ProcurementTitle");
                            //Log.d("ProcurementTitle",ProcurementTitle);

                            ProcurementTitle = ProcurementTitle.replace("'", "''");

                            String ProcurementShortDescription = jsonobj.getString("ProcurementShortDescription");


                            ProcurementShortDescription = ProcurementShortDescription.replace("'", "''");


                            String ProcurementLongDescription = jsonobj.getString("ProcurementLongDescription");

                            ProcurementLongDescription = ProcurementLongDescription.replace("'", "''");

                            String ProcurementProposalDeadline = jsonobj.getString("ProcurementProposalDeadline");
                            String ProcurementPreConferenceDate = jsonobj.getString("ProcurementPreConferenceDate");
                            String ProcurementQuestionDeadline = jsonobj.getString("ProcurementQuestionDeadline");
                            String ProcurementAgencyURL = jsonobj.getString("ProcurementAgencyURL");

                            String ProcurementDocument1URL = jsonobj.getString("ProcurementDocument1URL");
                            String ProcurementDocument2URL = jsonobj.getString("ProcurementDocument2URL");
                            String ProcurementDocument3URL = jsonobj.getString("ProcurementDocument3URL");
                            String ProcurementDocument4URL = jsonobj.getString("ProcurementDocument4URL");
                            String ProcurementDocument5URL = jsonobj.getString("ProcurementDocument5URL");
                            String ProcurementAddedDate = jsonobj.getString("ProcurementAddedDate");

                            String ProcurementContractValueID = jsonobj.getString("ProcurementContractValueID");
                            String Status = jsonobj.getString("Status");
                            String LASTEDITEDUSERNAME = jsonobj.getString("LASTEDITEDUSERNAME");
                            String PDFPath = jsonobj.getString("PDFPath");
                            String RecordUpdatedDate = jsonobj.getString("RecordUpdatedDate");
                            String Action = jsonobj.getString("Action");

                            if (dataStatus.equalsIgnoreCase("Full")) {
                                dataBaseHelper.InsertProcurementMaster(new ProcMaster(Integer.valueOf(ProcurementID), ProcurementEPIN, ProcurementSource,
                                        ProcurementAgencyID, ProcurementTypeIDP, ProcurementTitle, ProcurementShortDescription, ProcurementLongDescription, ProcurementProposalDeadline,
                                        ProcurementPreConferenceDate, ProcurementQuestionDeadline, ProcurementAgencyURL, ProcurementDocument1URL, ProcurementDocument2URL, ProcurementDocument3URL,
                                        ProcurementDocument4URL, ProcurementDocument5URL, ProcurementAddedDate, ProcurementContractValueID,
                                        Status, LASTEDITEDUSERNAME, PDFPath, RecordUpdatedDate ,Action));

                                utils.savedata("UpdateddateProc", "dba");
                            }else {
                                if (Action.equalsIgnoreCase("add")){
                                    dataBaseHelper.InsertProcurementMaster(new ProcMaster(Integer.valueOf(ProcurementID), ProcurementEPIN, ProcurementSource,
                                            ProcurementAgencyID, ProcurementTypeIDP, ProcurementTitle, ProcurementShortDescription, ProcurementLongDescription, ProcurementProposalDeadline,
                                            ProcurementPreConferenceDate, ProcurementQuestionDeadline, ProcurementAgencyURL, ProcurementDocument1URL, ProcurementDocument2URL, ProcurementDocument3URL,
                                            ProcurementDocument4URL, ProcurementDocument5URL, ProcurementAddedDate, ProcurementContractValueID,
                                            Status, LASTEDITEDUSERNAME, PDFPath, RecordUpdatedDate ,Action));
                                }

                                if (Action.equalsIgnoreCase("edit")){
                                    dataBaseHelper.UpdateProcurementMaster(new ProcMaster(Integer.valueOf(ProcurementID), ProcurementEPIN, ProcurementSource,
                                            ProcurementAgencyID, ProcurementTypeIDP, ProcurementTitle, ProcurementShortDescription, ProcurementLongDescription, ProcurementProposalDeadline,
                                            ProcurementPreConferenceDate, ProcurementQuestionDeadline, ProcurementAgencyURL, ProcurementDocument1URL, ProcurementDocument2URL, ProcurementDocument3URL,
                                            ProcurementDocument4URL, ProcurementDocument5URL, ProcurementAddedDate, ProcurementContractValueID,
                                            Status, LASTEDITEDUSERNAME, PDFPath, RecordUpdatedDate ,Action));
                                }
                            }


                        }

                        Log.d("et", String.valueOf(System.currentTimeMillis()));
                        hidePB();
                    }

                    hidePB();

                    getNotificationData();




                } catch (Exception e) {
                    hidePB();
                    e.printStackTrace();
                }

            }
        });

        thread_update.start();
    }

    private void getNotificationData() {
       /* if(GCMPushRecieverService.rfp_match.equalsIgnoreCase("RFP Matched")){

            getTagedRFP();

        }*/
        if(getIntent().getStringExtra("notif_status")!=null){
            Log.d("notif_status",getIntent().getStringExtra("notif_status"));

            if (Data.DateExpire){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(HomeActivity.this)
                                .setTitle("Alert!")
                                .setMessage("Your subscription or trial period has expired. " +
                                        "Kindly subscribe to monthly or annual subscription to enjoy using premium features.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                    }
                });

                return;

            }

            if(getIntent().getStringExtra("notif_status").equalsIgnoreCase("Announcement")){
                String title = getIntent().getStringExtra("notif_title");
                Intent i = new Intent(HomeActivity.this, HomeItem_ViewActivity.class);
                i.putExtra("Title",title);
                i.putExtra("headertext", "ANNOUNCEMENTS");
                i.putExtra("image", R.drawable.announcement);
                startActivity(i);
            }else if(getIntent().getStringExtra("notif_status").equalsIgnoreCase("Alert")){
                String title = getIntent().getStringExtra("notif_title");
                Intent i = new Intent(HomeActivity.this, HomeItem_ViewActivity.class);
                i.putExtra("Title",title);
                i.putExtra("headertext", "ALERT");
                i.putExtra("image", R.drawable.icon3);
                startActivity(i);
            }else if(getIntent().getStringExtra("notif_status").equalsIgnoreCase("Event")){
                String title = getIntent().getStringExtra("notif_title");
                Intent i = new Intent(HomeActivity.this, HomeItem_ViewActivity.class);
                i.putExtra("Title",title);
                i.putExtra("headertext", "EVENT");
                i.putExtra("image", R.drawable.icon2);
                startActivity(i);
            }else if(getIntent().getStringExtra("notif_status").equalsIgnoreCase("Browse")){
                Intent i = new Intent(HomeActivity.this, BrowseActivity.class);
                startActivity(i);
            }else if(getIntent().getStringExtra("notif_status").equalsIgnoreCase("RFP Matched")){
                Intent i = new Intent(HomeActivity.this, BrowseActivity.class);
                i.putExtra("notif_status","RFP Matched");
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }else if(getIntent().getStringExtra("notif_status").equalsIgnoreCase("RFP Updated")){
                String title = getIntent().getStringExtra("notif_title");
                Intent i = new Intent(HomeActivity.this, TrackList.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("title",title);
                i.putExtra("notif_status","RFP Updated");
                startActivity(i);
            }
        }
    }

    private void listclick() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(HomeActivity.this, HomeItem_ViewActivity.class);
                i.putExtra("Title",list_data.get(position).getText());
                switch (list_data.get(position).getImage()) {
                    case R.drawable.icon1: {
                        i.putExtra("headertext", "EXPERT");
                        i.putExtra("image", R.drawable.icon1);
                        i.putExtra("ContentID", String.valueOf(position + 1));
                        break;
                    }

                    case R.drawable.icon2: {
                        i.putExtra("headertext", "EVENT");
                        i.putExtra("image", R.drawable.icon2);
                        i.putExtra("ContentID", String.valueOf(position + 1));
                        break;
                    }

                    case R.drawable.icon3: {
                        i.putExtra("headertext", "ALERT");
                        i.putExtra("image", R.drawable.icon3);
                        i.putExtra("ContentID", String.valueOf(position + 1));
                        break;
                    }

                    case R.drawable.icon4: {
                        i.putExtra("headertext", "NEWS");
                        i.putExtra("image", R.drawable.icon4);
                        i.putExtra("ContentID", String.valueOf(position + 1));
                        break;
                    }

                    case R.drawable.announcement: {
                        i.putExtra("headertext", "ANNOUNCEMENTS");
                        i.putExtra("image", R.drawable.announcement);
                        i.putExtra("ContentID", String.valueOf(position + 1));
                        break;
                    }
                }

                /*i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);*/
                //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(i);


            }
        });
    }

    private void setupimageicon(Cursor cursor) {
        try {
            switch (cursor.getString(10)) {
                case "1": {
                    image = R.drawable.icon2;
                    break;
                }

                case "2": {
                    image = R.drawable.announcement;
                    break;
                }

                case "3": {
                    image = R.drawable.icon1;
                    break;
                }

                case "4": {
                    image = R.drawable.icon4;
                    break;
                }

                case "5": {
                    image = R.drawable.icon3;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void swipelist(final ListView lv) {
        gesturelistener = new SwipeGestureListener(HomeActivity.this);
        lv.setOnTouchListener(gesturelistener);
    }

    //Fotter Tab Functions

    public void BrowseClick(View view) {
        Intent i = new Intent(HomeActivity.this, BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void SettingsClick(View view) {
        Intent i = new Intent(HomeActivity.this, SettingsMain.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view) {
        Intent i = new Intent(HomeActivity.this, ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view) {
        Intent i = new Intent(HomeActivity.this, TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    @Override
    public void onUpdateCheckListener(final String update_url) {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("New Update Available!")
                    .setMessage("A new and improved version of MWBE Connect NY is available. " +
                            "Please click on upgrade to update the app.")
                    .setPositiveButton("Upgrade", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(update_url));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("Close App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Custom List Adapter
    public class CustomListAdapter extends ArrayAdapter<ListData> {

        private String contenttitle;
        private CallbackManager callbackManager;
        private ShareDialog shareDialog;

        public CustomListAdapter(Context context, int resource, List<ListData> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                pos.add(position);
                currentpos = position;
                ViewHolder viewHolder;
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_row, null);

                    viewHolder = new ViewHolder(convertView);
                    convertView.setTag(viewHolder);
                    //convertView.setTag(position);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                ListData data = getItem(position);
                viewHolder.text.setText(data.getText());
                viewHolder.time.setText(data.getTime());
                viewHolder.image_icon.setImageResource(data.getImage());

                //Log.d("pos", String.valueOf(isvisible.get(position)));

                if (isvisible.size() > 0 && isvisible.get(position)) {
                    view = convertView.findViewById(R.id.swipe_layout);
                    view.setVisibility(View.VISIBLE);
                    onClick(view, position);
                    //isvisibleswipe = false;
                    //isvisible.set(position, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        private void onClick(final View view, final int position) {

            try {

                isvisibleswipe = false;
                isvisible.set(position, false);

                DataBaseHelper dataBaseHelper = new DataBaseHelper(HomeActivity.this);
                dataBaseHelper.createDataBase();
                dataBaseHelper.openDataBase();

                Cursor cursor = dataBaseHelper.getDataFromDB("ContentTitle", list_data.get(position).getText(),"ContentMasterUpdated",true);
                if(cursor.getCount()>0){
                    while ((cursor.moveToNext())){
                        contentdescrip = cursor.getString(2).replace("\\u0027","'");
                        contenturl = cursor.getString(7);
                    }
                }

                if(contenturl.equalsIgnoreCase("") || contenturl.equalsIgnoreCase("NA")){
                    contenturl = "http://www.capalino.com";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            ((ImageView) view.findViewById(R.id.fb)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //senddatatofb();



                    callbackManager = CallbackManager.Factory.create();
                    shareDialog = new ShareDialog(HomeActivity.this);
                    // this part is optional
                    shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                        @Override
                        public void onSuccess(Sharer.Result result) {


                        }

                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onError(FacebookException error) {

                        }

                         });



                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentTitle(list_data.get(position).getText())
                                .setContentDescription(contentdescrip)
                                .setContentUrl(Uri.parse(contenturl))
                                .build();

                        shareDialog.show(linkContent);
                    }

                   /* ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("https://www.capalino.com"))
                            .setContentDescription(contenttitle)
                            .build();
                    ShareDialog shareDialog = new ShareDialog(HomeActivity.this);
                    shareDialog.show(content, ShareDialog.Mode.NATIVE);*/

                    view.setVisibility(View.GONE);
                }
            });

            ((ImageView) view.findViewById(R.id.linkdin)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    senddatatolinkdin(view,list_data.get(position).getText());
                }
            });

            ((ImageView) view.findViewById(R.id.twitter)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    senddatatwitter(view,list_data.get(position).getText());
                }
            });

            ((ImageView) view.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.setVisibility(View.GONE);
                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Are you sure you want to delete?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    delete(position);
                                    isvisible.set(pos.get(position), false);

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })

                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

            ((ImageView) view.findViewById(R.id.message)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EmailClick(position, view);
                }
            });


        }

        private void delete(int pos) {
            try {
                ArrayList<String> deleteRecordlist = new ArrayList<>();
                DataBaseHelper databaseHelper = new DataBaseHelper(HomeActivity.this);
                databaseHelper.createDataBase();
                databaseHelper.openDataBase();
                Cursor cursor = databaseHelper.getDataFromDB("", "", "ContentMasterUpdated", false);
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        if (list_data.get(pos).getText().equalsIgnoreCase(cursor.getString(1))){
                            deleteRecordlist.add(list_data.get(pos).getText());
                            //Set the values
                            Set<String> set = new HashSet<String>();
                            set.addAll(deleteRecordlist);
                            utils.saveArray(set);
                            databaseHelper.deleteContentMasterRecord(cursor.getInt(0));
                            adapter.notifyDataSetChanged();
                            lv.setAdapter(adapter);
                        }

                    }
                }

                list_data.remove(pos);
                adapter = new CustomListAdapter(HomeActivity.this, R.layout.activity_home, list_data);
                lv.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void senddatatofb() {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://post"));
            final PackageManager packageManager = HomeActivity.this.getPackageManager();
            final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.isEmpty()) {

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.facebook.com/?mds=%2Fmessage%2Fcompose%2fdialog%2F&mdf=1"));
            }
            startActivity(intent);
        }
    }

    public void senddatatolinkdin(View view,String contentTitle) {
        /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://m//share"));
        final PackageManager packageManager = this.getPackageManager();
        final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.isEmpty()) {

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com/m/share"));
        }
        view.setVisibility(View.GONE);
        startActivity(intent);*/

        //Intent shareIntent = new Intent();

        String linkdinUrl = "https://www.linkedin.com/shareArticle?mini=true&url="+contenturl+"&title="+contentTitle+"&summary=Capalino+Company&source="+contenturl;
        Intent shareIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkdinUrl));

        //shareIntent.setAction(Intent.ACTION_SEND);

        //https://www.linkedin.com/shareArticle?mini=true&url=http://stackoverflow.com/questions/10713542/how-to-make-custom-linkedin-share-button/10737122&title=Winter%20Gala&summary=&source=stackoverflow.com





        /*try{
            DataBaseHelper dataBaseHelper = new DataBaseHelper(HomeActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            StringBuilder sb;
            Cursor cursor = dataBaseHelper.DBRecord("Select ContentDescription from ContentMasterUpdated where ContentTitle='"+contentTitle+"'");
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/

        //shareIntent.putExtra(Intent.EXTRA_TEXT, contentTitle+" "+contentdescrip+" "+contenturl);
        //shareIntent.putExtra(Intent.EXTRA_TEXT,Html.fromHtml(new StringBuilder().append("<p>"+contentTitle+"</p>")
          //      .append("<p>"+contentdescrip+"</p>").toString()));


        //shareIntent.setType("text/plain");

        //shareIntent.putExtra(Intent.EXTRA_TEXT, contentTitle);

        //startActivity(Intent.createChooser(shareIntent, "Share Events"));

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(shareIntent,  PackageManager.MATCH_DEFAULT_ONLY);

        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(shareIntent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.linkedin")) {
                shareIntent.setPackage(info.activityInfo.packageName);
            }
        }

        startActivity(shareIntent);

    }

    public void senddatatwitter(View view,String contentTitle) {
       /* Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://m//share"));
        final PackageManager packageManager = this.getPackageManager();
        final List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.isEmpty()) {

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mobile.twitter.com/compose/tweet"));
        }
        view.setVisibility(View.GONE);
        startActivity(intent);*/
        //Intent shareIntent = new Intent();

        //shareIntent.setAction(Intent.ACTION_SEND);



        /*try{
            DataBaseHelper dataBaseHelper = new DataBaseHelper(HomeActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            StringBuilder sb;
            Cursor cursor = dataBaseHelper.DBRecord("Select ContentDescription from ContentMasterUpdated where ContentTitle='"+contentTitle+"'");
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }*/

        //shareIntent.putExtra(Intent.EXTRA_TEXT, contentTitle);
        //shareIntent.setType("text/plain");

        //shareIntent.putExtra(Intent.EXTRA_TEXT, contentTitle);

        //startActivity(Intent.createChooser(shareIntent, "Share Events"));

        //PackageManager packManager = getPackageManager();
        //List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(shareIntent,  PackageManager.MATCH_DEFAULT_ONLY);

        /*boolean resolved = false;
        for(ResolveInfo resolveInfo: resolvedInfoList){
            if(resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")){
                shareIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name );
                resolved = true;
                break;
            }
        }
        if(resolved){
            startActivity(shareIntent);
        }else{
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }*/
        //****************Working Code***************************
        /*String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s",
                urlEncode(contentTitle),
                urlEncode(contenturl));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));

        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName);
            }
        }

        startActivity(intent);*/

        try {
            URL url_twitter = new URL(contenturl);
            TweetComposer.Builder builder = new TweetComposer.Builder(this)
                    .text(contentTitle)
                    .url(url_twitter);
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (Exception e) {
            Log.wtf("UTF8", "UTF-8 should always be supported", e);
            throw new RuntimeException("URLEncoder.encode() failed for " + s);
        }
    }

    public void EmailClick(int position, View view) {
        try {

            //getPost(position);
            ArrayList<String> data = setdata(position);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT, list_data.get(position).getText());
            if(list_data.get(position).getImage() == R.drawable.icon2){
                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(new StringBuilder()
                        .append("<p> Start Date  "+data.get(0)+"<p/>")
                        .append("<p> End Date  " + data.get(1) + "<p/>")
                        .append("<p> Cost        "+data.get(2)+"<p/>")
                        .append("<p> Location        " + data.get(3) + "<p/>")
                        .toString()));
            } else {
                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(new StringBuilder()
                        .append("<p>"+data.get(0)+"<p/>")
                        .append("<a><p>" + data.get(1) + "<p/></a>")
                        .toString()));
            }

            intent.setType("text/plain");
            intent.setType("message/rfc822");
            view.setVisibility(View.GONE);
            startActivityForResult(Intent.createChooser(intent, "Send Email"), Constants.Content_email_Constants);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getPost(int position) {
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(HomeActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            Cursor cursor = dataBaseHelper.getDataFromDB("", "", "ContentMaster", false);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (list_data.get(position).getText().equalsIgnoreCase(cursor.getString(3))) {
                        post = cursor.getString(4);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> setdata(int currentpos) {
        try{

            ArrayList<String> data = new ArrayList<>();

            DataBaseHelper dataBaseHelper = new DataBaseHelper(HomeActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            Cursor cursor = dataBaseHelper.getDataFromDB("", "", "ContentMasterUpdated", false);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    if(list_data.get(currentpos).getText().equalsIgnoreCase(cursor.getString(1))) {
                        if (list_data.get(currentpos).getImage() == R.drawable.icon2) {
                            data.add(cursor.getString(3));
                            data.add(cursor.getString(4));
                            data.add(cursor.getString(6));
                            data.add(cursor.getString(5));
                        } else {
                            data.add(cursor.getString(3));
                            data.add(cursor.getString(2));

                        }
                    }

                }
            }

            return data;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    //Gesture inner Class
    class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener implements
            View.OnTouchListener {
        Context context;
        GestureDetector gDetector;
        static final int SWIPE_MIN_DISTANCE = 120;
        static final int SWIPE_MAX_OFF_PATH = 250;
        static final int SWIPE_THRESHOLD_VELOCITY = 200;

        public SwipeGestureListener() {
            super();
        }

        public SwipeGestureListener(Context context) {
            this(context, null);
        }

        public SwipeGestureListener(Context context, GestureDetector gDetector) {

            if (gDetector == null)
                gDetector = new GestureDetector(context, this);

            this.context = context;
            this.gDetector = gDetector;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            try {
                final int position = lv.pointToPosition(
                        Math.round(e1.getX()), Math.round(e1.getY()));

                //String item_name = (String) lv.getItemAtPosition(position);

                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                    if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH
                            || Math.abs(velocityY) < SWIPE_THRESHOLD_VELOCITY) {
                        return false;
                    }
                    if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) {
                        // Toast.makeText(HomeActivity.this, "bottomToTop" + item_name,
                        //       Toast.LENGTH_SHORT).show();
                    } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) {
                        //Toast.makeText(DemoSwipe.this,
                        //      "topToBottom  " + item_name, Toast.LENGTH_SHORT)
                        //    .show();
                    }
                } else {
                    if (Math.abs(velocityX) < SWIPE_THRESHOLD_VELOCITY) {
                        return false;
                    }
                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
                        // Toast.makeText(DemoSwipe.this,
                        //       "swipe RightToLeft " + item_name, 5000).show();

                        isvisible.set(position, true);
                        Log.d("pos", String.valueOf(position));
                        isvisibleswipe = true;
                        int currentPosition = lv.getFirstVisiblePosition();
                        adapter = new CustomListAdapter(HomeActivity.this, R.layout.activity_home, list_data);
                        lv.setAdapter(adapter);

                        lv.setSelection(currentPosition);

                        /*View convertView = getLayoutInflater().inflate(R.layout.list_row, null);
                        View v = convertView.findViewById(R.id.swipe_layout);
                        v.setVisibility(View.VISIBLE);*/

                        //adapter.notifyDataSetChanged();

                        return true;


                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
                        //Toast.makeText(DemoSwipe.this,
                        //      "swipe LeftToright  " + item_name, 5000).show();
                        isvisible.set(position, false);
                        isvisibleswipe = false;
                        int currentPosition = lv.getFirstVisiblePosition();
                        adapter = new CustomListAdapter(HomeActivity.this, R.layout.activity_home, list_data);
                        lv.setAdapter(adapter);

                        lv.setSelection(currentPosition);

                       /* View convertView = getLayoutInflater().inflate(R.layout.list_row, null);
                        View v = convertView.findViewById(R.id.swipe_layout);
                        v.setVisibility(View.GONE);*/

                        return true;

                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            return gDetector.onTouchEvent(event);
        }

        public GestureDetector getDetector() {
            return gDetector;
        }

    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }
}
