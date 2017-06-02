package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.MWBE.Connects.NY.Storage.Storage;
import com.MWBE.Connects.NY.AppConstants.Constants;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.CustomViews.CustomEditText_Book;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.Database.DatabaseBeen.SettingsModel;
import com.MWBE.Connects.NY.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SettingsActivity extends Activity {

    private ArrayList<Integer> positionArray;
    private ArrayList<Integer> positionArray_contractvalue;
    private ArrayList<Integer> position_Array;
    private Context context = this;
    private String Geographic_Name = "";
    private int count_geographic;
    private String[] output_geographic;
    private String[] output_contractvalue;
    private String Certification_Name = "";
    private String[] output_certification;
    private DataBaseHelper dataBaseHelper;
    private int count_certification;
    private String target_contract_value = "";
    private String capablities;
    private int count_contractvalue;
    private ProgressDialog pb;
    private String link;
    private int count;
    private Utils utils;
    private int totalcount;
    private int finalI;
    private boolean onlygeographic;
    private boolean onlyTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    public void BackClick(View view){
      //  finish();
        Intent i = new Intent(SettingsActivity.this, SettingsMain.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void init() {
        try {
            utils = new Utils(SettingsActivity.this);
            /*Intent j = new Intent(SettingsActivity.this, CapabilitiesService.class);
            j.putExtra("Userid", utils.getdata("Userid"));
            startService(j);*/



            //dataBaseHelper.delete("SettingsMaster");
            dataBaseHelper = new DataBaseHelper(SettingsActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            try{

                if(Utils.isConnected(SettingsActivity.this))
                     getGeographicCount();
               else
                   Utils.AlertInternetConnection(SettingsActivity.this);
                if(Utils.isConnected(SettingsActivity.this))
                     getTargetCount();

                if(Utils.isConnected(SettingsActivity.this))
                     getCertificationCount();

                if(Utils.isConnected(SettingsActivity.this)){
                    for(int i=100;i<1100;i+=100)
                        getCapabilitiesCount1(i);
                }



               /* if(Utils.isConnected(SettingsActivity.this))
                     getCapabCount();*/


            }catch (Exception e){
                    e.printStackTrace();
                }




            //getCapabilitiesCount();
            /*if(count>0){
                ((CustomEditText_Book) findViewById(R.id.capability)).setText(count+" Selected");
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getCapabCount() {
        totalcount = 0;
        if(Storage.count1>0){
            totalcount++;
        }

        if(Storage.count2>0){
            totalcount++;
        }
        if(Storage.count3>0){
            totalcount++;
        }
        if(Storage.count4>0){
            totalcount++;
        }

        if(Storage.count5>0){
            totalcount++;
        }

        if(Storage.count6>0){
            totalcount++;
        }

        if(Storage.count7>0){
            totalcount++;
        }

        if(Storage.count8>0){
            totalcount++;
        }

        if(Storage.count9>0){
            totalcount++;
        }

        if(Storage.count10>0){
            totalcount++;
            ((CustomEditText_Book) findViewById(R.id.capability)).setText(totalcount +" Selected");
        }else {
            ((CustomEditText_Book) findViewById(R.id.capability)).setText(totalcount +" Selected");
        }


    }

    private void getGeographicCount() {
        if(!onlygeographic)
        showPB("Loading...");
        String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getUserGeoTags.php?UserID="+utils.getdata("Userid");
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

                        if(response.contains("[]")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((CustomEditText_Book) findViewById(R.id.geographic_coverage)).setText("");
                                }
                            });
                        }
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if(jsonObject.getString("ActualTagID").equalsIgnoreCase("1111")){
                                count--;
                            }
                        }
                        return count;
                    }
                    return 0;

                } catch (Exception e) {
                    hidePB();
                    //hidePB();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Utils.AlertInternetConnection(SettingsActivity.this);
                        }
                    });
                    e.printStackTrace();
                }
                return 0;
            }

            @Override
            protected void onPostExecute(Integer s) {
                super.onPostExecute(s);
                if(s!=0){
                    ((CustomEditText_Book) findViewById(R.id.geographic_coverage)).setText(s+" Selected");
                }else {
                    ((CustomEditText_Book) findViewById(R.id.geographic_coverage)).setText("");
                }
            }
        }.execute(link, "", "");
    }

    private void getTargetCount() {

        Utils utils = new Utils(SettingsActivity.this);

        String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getUserContractTags.php?UserID="+utils.getdata("Userid");
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

                        if(response.contains("[]")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((CustomEditText_Book) findViewById(R.id.target_contract_value)).setText("");

                                }
                            });
                        }

                        JSONArray jsonArray = new JSONArray(response);
                        int count = jsonArray.length();
                        return count;
                    }
                    return 0;

                } catch (Exception e) {
                    e.printStackTrace();
                    hidePB();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Utils.AlertInternetConnection(SettingsActivity.this);
                        }
                    });
                }
                return 0;
            }

            @Override
            protected void onPostExecute(Integer s) {
                super.onPostExecute(s);
                if(s!=0){
                    ((CustomEditText_Book) findViewById(R.id.target_contract_value)).setText(s+" Selected");
                }else {
                    ((CustomEditText_Book) findViewById(R.id.target_contract_value)).setText("");
                }
            }
        }.execute(link, "", "");
    }

    private void getCertificationCount() {

        Utils utils = new Utils(SettingsActivity.this);

        String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getUserCertificationTags.php?UserID="+utils.getdata("Userid");
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
                    hidePB();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Utils.AlertInternetConnection(SettingsActivity.this);
                        }
                    });
                }
                return 0;
            }

            @Override
            protected void onPostExecute(Integer s) {
                super.onPostExecute(s);
                if(s!=0){
                    ((CustomEditText_Book) findViewById(R.id.certifications)).setText(s+" Selected");
                }else {
                    ((CustomEditText_Book) findViewById(R.id.certifications)).setText("");
                }
            }
        }.execute(link, "", "");
    }

    private void getCapabilitiesCount1(final int settingTypeID) {

        Utils utils = new Utils(context);
        if (Utils.isConnected(this)) {
            String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getUserCapabilitiesSelectedTags.php?UserID=" + utils.getdata("Userid") +
                    "&SettingTypeID=" + settingTypeID;

            new AsyncTask<String, Void, JSONArray>() {
                @Override
                protected JSONArray doInBackground(String... params) {
                    try {

                        HttpClient httpclient = new DefaultHttpClient();
                        //showPB("Loading....");

                        HttpPost httppost = new HttpPost(params[0]);

                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        String response = httpclient.execute(httppost,
                                responseHandler);

                        response = response.replace("\n", "");

                        Log.i("Response", "Response : " + response);
                        if (response != null) {
                            JSONArray jsonArray = new JSONArray(response);
                            return jsonArray;


                        }


                        return null;

                    } catch (Exception e) {
                        hidePB();
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(JSONArray jsonArray) {
                    if (jsonArray != null) {
                        switch (settingTypeID) {
                            case 100: {
                                if(jsonArray.length()>0)
                                    count++;
                                break;
                            }

                            case 200: {
                                if(jsonArray.length()>0)
                                    count++;
                                break;
                            }

                            case 300: {
                                if(jsonArray.length()>0)
                                   count++;
                                break;
                            }

                            case 400: {
                                if(jsonArray.length()>0)
                                    count++;
                                break;
                            }

                            case 500: {
                                if(jsonArray.length()>0)
                                    count++;
                                break;
                            }

                            case 600: {
                                if(jsonArray.length()>0)
                                    count++;
                                break;
                            }

                            case 700: {
                                if(jsonArray.length()>0)
                                    count++;
                                break;
                            }

                            case 800: {
                                if(jsonArray.length()>0)
                                    count++;
                                break;
                            }

                            case 900: {
                                if(jsonArray.length()>0)
                                    count++;
                                break;
                            }

                            case 1000: {
                                if(jsonArray.length()>0)
                                    count++;

                                ((CustomEditText_Book) findViewById(R.id.capability)).setText(count+" Selected");
                                hidePB();
                                break;
                            }
                        }
                    }

                }
            }.execute(link, "", "");
        }
    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(SettingsActivity.this);
                pb.setMessage(message);
                pb.show();
            }
        });

    }

    void showPBForPDF(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(SettingsActivity.this);
                pb.setMessage(message);
                pb.setCancelable(false);
                pb.show();

                long delayInMillis = 25000;
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        pb.dismiss();
                    }
                }, delayInMillis);
            }
        });
    }

    void hidePB() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (pb != null && pb.isShowing())
                    pb.dismiss();
            }
        });

    }

    //EditText Click Listener
    public void GeographicCoverageClick(View view) {
        try {
            Intent i = new Intent(SettingsActivity.this, GeographicCoverageActivity.class);
            i.putIntegerArrayListExtra("geographic_pos_array", positionArray);
            i.putExtra("SettingID",1);
           /*if(output_geographic!=null) {
               if (output_geographic.length > 0) {
                   i.putExtra("output_geographic", output_geographic);
               }
           }*/
            startActivityForResult(i, Constants.request_geographic_coverage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ContractClick(View view) {
        try {
            Intent i = new Intent(SettingsActivity.this, TargetContractActivity.class);
            i.putIntegerArrayListExtra("contractvalue_pos_array", positionArray_contractvalue);
            i.putExtra("SettingID", 2);
            if (output_contractvalue != null) {
                if (output_contractvalue.length > 0) {
                    i.putExtra("output_contract", output_contractvalue);
                }
            }
            startActivityForResult(i, Constants.request_target_contract_value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CertificationActivity(View view) {
        try {
            Intent i = new Intent(SettingsActivity.this, CertificationActivity.class);
            i.putIntegerArrayListExtra("Certification_pos_array", position_Array);
            i.putExtra("SettingID", 3);
            if (output_certification != null)
                if (output_certification.length > 0) {
                    i.putExtra("output_certification", output_certification);
                }
            startActivityForResult(i, Constants.request_certification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CapablitiesClick(View view) {
        Intent i = new Intent(SettingsActivity.this, CapablitiesActivity.class);
        startActivityForResult(i, Constants.request_capabilites);
    }

    public void UpdateProfileClick(View view) {
        Intent i = new Intent(this, UpdateProfileActivity.class);
        startActivity(i);
    }

    public void UpdateClick() {
        try {
            String geographic_coverage = ((CustomEditText_Book) findViewById(R.id.geographic_coverage)).getText().toString();
            //String target_contract_value = ((CustomEditText_Book) findViewById(R.id.target_contract_value)).getText().toString();
            String certifications = ((CustomEditText_Book) findViewById(R.id.certifications)).getText().toString();
            String capability = ((CustomEditText_Book) findViewById(R.id.capability)).getText().toString();

            if (dataBaseHelper == null) {
                dataBaseHelper = new DataBaseHelper(SettingsActivity.this);
                dataBaseHelper.openDataBase();
            }

            //if(geographic_coverage.length()>0 && target_contract_value.length()>0 && certifications.length()>0 && capability.length()>0) {
            boolean isInserted = dataBaseHelper.InsertSettingsMaster(new SettingsModel(Geographic_Name, target_contract_value,
                    Certification_Name, capability));
            if (isInserted) {

                   /* new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Data has been updated...")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //Footer Click Listener
    public void HomeClick(View view) {
        Intent i = new Intent(SettingsActivity.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        UpdateClick();
        startActivity(i);
    }

    public void BrowseClick(View view) {
        Intent i = new Intent(SettingsActivity.this, BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        UpdateClick();
        startActivity(i);
    }

    public void TrackClick(View view) {
        Intent i = new Intent(SettingsActivity.this, TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        UpdateClick();
        startActivity(i);
    }

    public void ResourceClick(View view) {
        Intent i = new Intent(SettingsActivity.this, ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        UpdateClick();
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case Constants.request_geographic_coverage: {
                    if (data.getStringExtra("geographic_count") != null) {
                        positionArray = data.getIntegerArrayListExtra("geographic_pos_array");
                        onlygeographic = true;
                        getGeographicCount();

                        //((CustomEditText_Book) findViewById(R.id.geographic_coverage)).setText(data.getStringExtra("geographic_count"));

                    }
                    break;
                }

                case Constants.request_target_contract_value: {
                    if (data.getStringExtra("contract_count") != null) {
                        positionArray = data.getIntegerArrayListExtra("contractvalue_pos_array");
                        getTargetCount();
                        onlyTarget = true;
                        //((CustomEditText_Book) findViewById(R.id.target_contract_value)).setText(data.getStringExtra("contract_count"));

                    }
                    break;
                }

                case Constants.request_certification: {
                    if (data.getStringExtra("certification_count") != null) {
                        position_Array = data.getIntegerArrayListExtra("Certification_pos_array");

                        //getCertificationCount();
                        ((CustomEditText_Book) findViewById(R.id.certifications)).setText(data.getStringExtra("certification_count"));
                        break;
                    }
                }

                case Constants.request_capabilites: {
                    if (data.getIntExtra("count", 0) != 0) {
                        ((CustomEditText_Book) findViewById(R.id.capability)).setText(data.getIntExtra("count", 0) + " Selected");
                    }
                    //getCapabilitiesCount();
                }


            }
        }


    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {

        Intent i = new Intent(SettingsActivity.this, SettingsMain.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
       //finish();
    }
}
