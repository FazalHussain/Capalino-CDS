package com.MWBE.Connects.NY.Activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.MWBE.Connects.NY.AppConstants.Constants;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.CustomViews.CustomEditText_Book;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.GCM.GCMPushRecieverService;
import com.MWBE.Connects.NY.JavaBeen.ListData_RFP;
import com.MWBE.Connects.NY.JavaBeen.TaggedRFP;
import com.MWBE.Connects.NY.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BrowseActivity extends Activity {

    private ToggleButton switchbtn;
    private CustomEditText_Book agency;
    private CustomEditText_Book procurment;
    private CustomEditText_Book contractvalue;
    private Context context = this;
    private CustomEditText_Book search;
    private boolean isOpen;
    private Utils utils;
    private ProgressDialog pb;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private String contractvalue_tag_id = "";
    private Button browsebtn;
    private ArrayList<ListData_RFP> list = new ArrayList<>();
    private String capabilityID = "";
    private String lastupdatedate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brows);
        utils = new Utils(this);
        init();


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        //getGeographic();
        pb = new ProgressDialog(BrowseActivity.this);

        //hideSoftKeyboard();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        switchbtn = (ToggleButton) findViewById(R.id.switchbtn);

        search = (CustomEditText_Book) findViewById(R.id.keyword_search);

        agency = (CustomEditText_Book) findViewById(R.id.agency);

        procurment = (CustomEditText_Book) findViewById(R.id.procurement);

        contractvalue = (CustomEditText_Book) findViewById(R.id.contract);

        browsebtn = (Button) findViewById(R.id.browsebtn);
        browsebtn.setEnabled(false);
        browsebtn.getBackground().setAlpha(200);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(search.getText().length()>0){
                    browsebtn.setEnabled(true);
                    browsebtn.getBackground().setAlpha(255);
                }
            }
        });

        //switchbtn.setShowText(true);
        /*switchbtn.setChecked(false);
        switchbtn.getThumbDrawable().setColorFilter(Color.parseColor("#909090"), PorterDuff.Mode.MULTIPLY);*/
        switchbtn.setChecked(false);
        getTagedRFP();
        if(getIntent().getStringExtra("notif_status")!=null){
            GCMPushRecieverService.rfp_match = "";
            switchbtn.setChecked(true);
            if(switchbtn.isChecked()) {
                search.setVisibility(View.GONE);
                agency.setVisibility(View.GONE);
                procurment.setVisibility(View.GONE);
                contractvalue.setVisibility(View.GONE);
                if (Data.istrial)
                    ((View) findViewById(R.id.footertext)).setVisibility(View.VISIBLE);
                browsebtn.setEnabled(true);
                browsebtn.getBackground().setAlpha(255);
                ((TextView) findViewById(R.id.searchlabel)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.agencylabel)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.procurementlabel)).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.contractlabel)).setVisibility(View.GONE);
            }
            Data.isOpen = switchbtn.isChecked();

            browsebtn.performClick();


        }

        switchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = switchbtn.isChecked();
                Data.isOpen = isOpen;
                isOpen = false;

                if(switchbtn.isChecked()){
                    search.setVisibility(View.GONE);
                    agency.setVisibility(View.GONE);
                    procurment.setVisibility(View.GONE);
                    contractvalue.setVisibility(View.GONE);
                    if(Data.istrial)
                    ((View)findViewById(R.id.footertext)).setVisibility(View.VISIBLE);
                    browsebtn.setEnabled(true);
                    browsebtn.getBackground().setAlpha(255);
                    ((TextView) findViewById(R.id.searchlabel)).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.agencylabel)).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.procurementlabel)).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.contractlabel)).setVisibility(View.GONE);
                } else {
                    browsebtn.setEnabled(false);
                    browsebtn.getBackground().setAlpha(200);
                    search.setVisibility(View.VISIBLE);
                    agency.setVisibility(View.VISIBLE);
                    procurment.setVisibility(View.VISIBLE);
                    contractvalue.setVisibility(View.VISIBLE);
                    ((View)findViewById(R.id.footertext)).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.searchlabel)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.agencylabel)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.procurementlabel)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.contractlabel)).setVisibility(View.VISIBLE);
                }
            }
        });

        /*switchbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchColor(isChecked);
                isOpen = isChecked;
                Data.isOpen = isOpen;
                isOpen = false;

                if (isChecked) {
                    search.setVisibility(View.GONE);
                    agency.setVisibility(View.GONE);
                    procurment.setVisibility(View.GONE);
                    contractvalue.setVisibility(View.GONE);
                    ((View)findViewById(R.id.footertext)).setVisibility(View.VISIBLE);
                    browsebtn.setEnabled(true);
                    browsebtn.getBackground().setAlpha(255);
                    ((TextView) findViewById(R.id.searchlabel)).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.agencylabel)).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.procurementlabel)).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.contractlabel)).setVisibility(View.GONE);
                } else {
                    browsebtn.setEnabled(false);
                    browsebtn.getBackground().setAlpha(200);
                    search.setVisibility(View.VISIBLE);
                    agency.setVisibility(View.VISIBLE);
                    procurment.setVisibility(View.VISIBLE);
                    contractvalue.setVisibility(View.VISIBLE);
                    ((View)findViewById(R.id.footertext)).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.searchlabel)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.agencylabel)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.procurementlabel)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.contractlabel)).setVisibility(View.VISIBLE);
                }
            }
        });*/
    }




    public void ShowAlertPopup(View view){
        try {
            new AlertDialog.Builder(BrowseActivity.this)
                    .setTitle("INFO")
                    .setMessage("Turn on Capalino BID Matching to see ONLY RFPs that match your business’ profile. " +
                            "Turn it off to search manually using the fields below.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getGeographic() {
        //Data.populatelist();
        Data.Actual_id_list_geographic.clear();
        String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getUserGeoTags.php?UserID="+utils.getdata("Userid");
        new AsyncTask<String, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(String... params) {
                try {

                    HttpClient httpclient = new DefaultHttpClient();

                    HttpPost httppost = new HttpPost(params[0]);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    String response = httpclient.execute(httppost,
                            responseHandler);

                    //Log.i("Response", "Response : " + response);
                    response = response.replace("\n","");
                    if(response!=null) {
                        if (response.equalsIgnoreCase("[]")) {
                            getTargetContract();
                        } else {
                            JSONArray jsonArray = new JSONArray(response);
                            jsonObject = new JSONObject();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                Data.Actual_id_list_geographic.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                if (i == jsonArray.length() - 1) {
                                    getTargetContract();
                                }
                            }
                            return jsonObject;
                        }
                    }
                    return null;

                } catch (Exception e) {
                    hidePB();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.AlertInternetConnection(BrowseActivity.this);
                        }
                    });
                    e.printStackTrace();
                }
                return jsonObject;
            }


        }.execute(link, "", "");
    }

    private void getTargetContract() {
        //Data.populatelist();
        Data.Actual_id_list_target.clear();
        String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getUserContractTags.php?UserID="+utils.getdata("Userid");
        new AsyncTask<String, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(String... params) {
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");

                    HttpPost httppost = new HttpPost(params[0]);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    if(response!=null) {
                        response = response.replace("\n","");
                        if(response.equalsIgnoreCase("[]")){
                            getCapabilitiesCount();
                        }

                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = null;
                        for(int i=0;i<jsonArray.length();i++){
                            jsonObject = jsonArray.getJSONObject(i);
                            Data.Actual_id_list_target.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                            if (i == jsonArray.length() - 1) {
                                getCapabilitiesCount();
                            }
                        }
                        return jsonObject;
                    }
                    return null;

                } catch (Exception e) {
                    hidePB();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.AlertInternetConnection(BrowseActivity.this);
                        }
                    });
                    e.printStackTrace();
                }
                return null;
            }

        }.execute(link, "", "");
    }

    private void getCapabilitiesCount() {
        Data.SettingTypeID_capab_search.clear();
        //showPB("Loading...");
        Utils utils = new Utils(context);
        if (Utils.isConnected(this)) {
            String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getUserCapabilitiesSelectedTagsFinal.php?UserID=" + utils.getdata("Userid");

            new AsyncTask<String, Void, Void>() {
                @Override
                protected Void doInBackground(String... params) {
                    try {

                        HttpClient httpclient = new DefaultHttpClient();
                        //showPB("Loading....");

                        HttpPost httppost = new HttpPost(params[0]);

                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        String response = httpclient.execute(httppost,
                                responseHandler);

                        response = response.replace("\n", "");

                        Log.i("Response", "Response : " + response);
                        response = response.replace("\n", "");
                        jsonArray = new JSONArray();
                        if (response != null) {
                            if (response.equalsIgnoreCase("[]")) {
                                hidePB();
                                Intent j = new Intent(BrowseActivity.this, REFListingActivity.class);
                                //Data.isOpen = isOpen;
                                startActivity(j);
                            } else {
                                jsonArray = new JSONArray(response);
                                JSONObject jsonObject = null;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    String settingid = jsonObject.get("SettingTypeID").toString();
                                    if (settingid.length() > 0)
                                        Data.SettingTypeID_capab_search.add(Integer.valueOf(settingid));

                                    if (i == jsonArray.length() - 1) {
                                        Intent j = new Intent(BrowseActivity.this, REFListingActivity.class);
                                        //Data.isOpen = isOpen;
                                        startActivity(j);
                                        finish();
                                        hidePB();
                                    }


                                }

                            }
                        }
                    } catch (Exception e) {
                        hidePB();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.AlertInternetConnection(BrowseActivity.this);
                            }
                        });
                        e.printStackTrace();
                    }

                    return null;
                }
            }.execute(link, "", "");
        }
    }

    /*private void switchColor(boolean checked) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            switchbtn.getThumbDrawable().setColorFilter(checked ? Color.parseColor("#2b3f04") : Color.parseColor("#909090"), PorterDuff.Mode.MULTIPLY);
            switchbtn.getTrackDrawable().setColorFilter(!checked ? Color.parseColor("#909090") : Color.parseColor("#2b3f04"), PorterDuff.Mode.MULTIPLY);
        }
    }*/

    public void BrowseButtonClick(View view){
        try{

            String contract_value = contractvalue.getText().toString();
            String search_keyword = search.getText().toString();
            String agency_title = agency.getText().toString();
            String proc_title = procurment.getText().toString();
            if(((contract_value.length()>0 || search_keyword.length()>0 || agency_title.length()>0) || proc_title.length()>0) && !switchbtn.isChecked()) {
                Intent i = new Intent(BrowseActivity.this,REFListingActivity.class);
                i.putExtra("contact_value", contract_value);
                i.putExtra("contracttagid",contractvalue_tag_id);
                //Data.isOpen = isOpen;
                Data.agency = agency_title;
                if(search_keyword.length()>0){
                   // showPB("Loading....");
                    Data.search = search_keyword;
                }

                getBrowseData(i, contractvalue_tag_id);
               // hidePB();
                //startActivity(i);
                //overridePendingTransition(0, 0);
            }else {
                showPB("Running Algorithm....");
                if(Data.isOpen)
                    getGeographic();
                //getCapabilitiesCount();
                else {
                    hidePB();
                /*Intent i = new Intent(BrowseActivity.this,REFListingActivity.class);
                Data.isOpen = isOpen;
                startActivity(i);*/
                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Please select at least one option.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getBrowseData(Intent i, String contractvalue_tag_id) {
        try{
            DataBaseHelper dataBaseHelper = new DataBaseHelper(BrowseActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            list.clear();
            Cursor cursor = dataBaseHelper.getDataFromProcurementMaster(contractvalue_tag_id, Data.agency, capabilityID , Data.search);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (Data.isOpen) {
                        list.add(new ListData_RFP(cursor.getString(0),"Capalino+Company Match", 3.0, cursor.getString(2), cursor.getString(1),
                                cursor.getString(4), cursor.getString(3)));
                    } else {
                        list.add(new ListData_RFP(cursor.getString(0),"Capalino+Company Match", 0.0, cursor.getString(2), cursor.getString(1),
                                cursor.getString(4), cursor.getString(3)));
                    }

                }
            }


            Data.list = list;
            startActivity(i);
            hidePB();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void AgencyClick(View view){
        try{
            Intent i = new Intent(BrowseActivity.this,AgencyActivity.class);
            startActivityForResult(i, Constants.request_agency);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void ProcurementClick(View view){

        try{
            Intent i = new Intent(BrowseActivity.this,ProcurementTypeActivity.class);
            startActivityForResult(i, Constants.request_procurement_type);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void ContractClick(View view){

        try{
            Intent i = new Intent(BrowseActivity.this,ContractValueActivity.class);
            startActivityForResult(i, Constants.request_contract_value);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    //Footer Tab Click Events

    public void HomeClick(View view){

        try{
            Intent i = new Intent(BrowseActivity.this,HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void SettingsClick(View view){

        try{
            Intent i = new Intent(BrowseActivity.this,SettingsMain.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void TrackClick(View view){

        try{
            Intent i = new Intent(BrowseActivity.this,TrackList.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void ResourceClick(View view){
        Intent i = new Intent(BrowseActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //switchbtn.getThumbDrawable().setColorFilter(Color.parseColor("#909090"), PorterDuff.Mode.MULTIPLY);
        switch(requestCode){
            case Constants.request_agency:{
                if(data!=null){
                    if(data.getStringExtra("agency")!=null){
                        agency.setText(data.getStringExtra("agency"));
                        browsebtn.setEnabled(true);
                        browsebtn.getBackground().setAlpha(255);
                    }
                }
                break;
            }

            case Constants.request_contract_value:{
                if(data!=null){
                    if(data.getStringExtra("contractvalue")!=null){
                        contractvalue.setText(data.getStringExtra("contractvalue"));
                        contractvalue_tag_id = data.getStringExtra("contracttagid");
                        browsebtn.setEnabled(true);
                        browsebtn.getBackground().setAlpha(255);
                    }
                }
                break;
            }

            case Constants.request_procurement_type:{
                if(data!=null){
                    if(data.getStringExtra("capabilitytype")!=null){
                        procurment.setText(data.getStringExtra("capabilitytype"));
                        capabilityID = data.getStringExtra("capabilityID");
                        browsebtn.setEnabled(true);
                        browsebtn.getBackground().setAlpha(255);
                    }
                }
                break;
            }
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

    private void getTagedRFP() {
        Thread thread_update_rfp = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    showPB("Loading new City and State RFPs. Please wait, this may take up to a minute. Thanks for your patience.");
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(BrowseActivity.this);
                    dataBaseHelper.createDataBase();
                    dataBaseHelper.openDataBase();

                    Cursor cursor = dataBaseHelper.DBRecord("Select LastUpdateDate from ProcurementRFPPreferences");
                    if(cursor.getCount()>0){
                        while (cursor.moveToNext()){
                            lastupdatedate = cursor.getString(0);



                        }
                    }

                    HttpClient httpclient = new DefaultHttpClient();

                    SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                    String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getTaggedRFPUpdatedForStore.php?lastUpdateSql=" +lastupdatedate
                            +"&CurrentDate="+format.format(new Date());
                    link = link.replace(" ","%20");
                    HttpPost httppost = new HttpPost(link);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    String response = httpclient.execute(httppost,
                            responseHandler);


                    Log.i("Response", "Response : " + response);
                    if(!response.trim().equalsIgnoreCase("Both are Equal")) {
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

                            //list_data.add(new ListData(image, contentShortDescription, ContentRelevantDateTime));
                            TaggedRFP been = new TaggedRFP(Integer.valueOf(PreferenceID), Integer.valueOf(ProcurementID), Integer.valueOf(SettingTypeID),
                                    Integer.valueOf(ActualTagID), AddedDateTime, lastupdate);
                           final boolean isinserted = dataBaseHelper.InsertinRFPPrefence(been);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (isinserted) {
                                        //Toast.makeText(HomeActivity.this, "Hello", Toast.LENGTH_LONG).show();
                                        Log.d("RFPUpdated", "Added");
                                    }
                                }
                            });
                        }
                        hidePB();
                    }else {
                        hidePB();
                    }
                } catch (Exception e) {
                    hidePB();
                    e.printStackTrace();
                }
            }
        });
        thread_update_rfp.start();

    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb.setMessage(message);
                pb.setCancelable(false);
                pb.show();
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
}
