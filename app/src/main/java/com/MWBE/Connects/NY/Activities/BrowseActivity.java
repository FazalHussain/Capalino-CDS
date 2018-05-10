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
import com.MWBE.Connects.NY.JavaBeen.PreferenceModel;
import com.MWBE.Connects.NY.JavaBeen.TaggedRFP;
import com.MWBE.Connects.NY.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private Button todaysRFP_btn;
    private ArrayList<ListData_RFP> list = new ArrayList<>();
    private String capabilityID = "";
    private String lastupdatedate;
    private DataBaseHelper dataBaseHelper;
    private List<ListData_RFP> liststar1 =new ArrayList<>();
    private List<ListData_RFP> liststar2 =new ArrayList<>();
    private List<ListData_RFP> liststar3 =new ArrayList<>();
    private ArrayList<ListData_RFP> listFinal = new ArrayList<>();
    private List<ListData_RFP> list_1star = new ArrayList<>();

    private List<PreferenceModel> list_pref = new ArrayList<>();
    private ArrayList<ListData_RFP> geographiclist = new ArrayList<>();
    private List<ListData_RFP> list_2star = new ArrayList<>();
    private ArrayList<Integer> list_procurement;

    ArrayList<ListData_RFP> list_todays = new ArrayList<>();

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

        try{
            dataBaseHelper = new DataBaseHelper(BrowseActivity.this);
            dataBaseHelper.createDataBase();
            if(!dataBaseHelper.sqliteDataBase.isOpen()) {
                dataBaseHelper.openDataBase();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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

        todaysRFP_btn = (Button) findViewById(R.id.todaysRFP);

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


        switchbtn.setChecked(false);
        if (utils.getdata("RFPUpdatedDate") == null || utils.getdata("RFPUpdatedDate").equalsIgnoreCase("")){
            getTagedRFP("FreshDb");
        }else {
            getTagedRFP(utils.getdata("RFPUpdatedDate"));
        }

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

            todaysRFP_btn.performClick();
        }

        switchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = switchbtn.isChecked();
                Data.isOpen = isOpen;
                isOpen = false;

                if(switchbtn.isChecked()){
                    ((Button)findViewById(R.id.todaysRFP)).setVisibility(View.GONE);
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
                    ((Button)findViewById(R.id.todaysRFP)).setVisibility(View.VISIBLE);
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
    }

    public void ShowAlertPopup(View view){
        try {
            new AlertDialog.Builder(BrowseActivity.this)
                    .setTitle("INFO")
                    .setMessage("Turn on Capalino BID Matching to see ONLY RFPs that match your business’ profile. " +
                            "Turn it off to search manually using the fields below.\n\n" +
                            "Star ratings reflect that the RFPs match the following elements of your profile: \n" +
                            "★★★: Capability and Geographic Coverage\n" +
                            "★★:  Industry and Geographic Coverage\n" +
                            "★★:  Capability\n" +
                            "★:    Industry")
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
                        if (!response.equalsIgnoreCase("[]")) {

                            JSONArray jsonArray = new JSONArray(response);
                            jsonObject = new JSONObject();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                Data.Actual_id_list_geographic.add(Integer.valueOf(jsonObject.getString("ActualTagID")));

                            }
                        }
                                try {
                                    //d.	1 star: Construction                                                        category
                                    Cursor cursor = null;
                                    ArrayList<ListData_RFP> list = new ArrayList<>();
                                    for (int i = 0; i < list_pref.size(); i++) {
                                        if (list_pref.get(i).getSettingTypeID() != 1) {
                                            cursor = dataBaseHelper.getDataFromProcurementMaster(list_pref.get(i).getSettingTypeID());
                                            list_1star = fillList(cursor,list);
                                        }
                                    }



                                    //c part : c.	2 stars: Roofing - Geographic Coverage
                                    //                subcategory - area

                                    for (int i = 0; i < list_pref.size(); i++) {
                                        if (list_pref.get(i).getSettingTypeID() != 1) {
                                            Cursor cursor2 = dataBaseHelper.getDataFromProcurementMasterStar2(
                                                    list_pref.get(i).getActualTagID(),
                                                    list_pref.get(i).getSettingTypeID());
                                            if (cursor2 != null && cursor2.getCount() > 0) {
                                                while (cursor2.moveToNext()) {
                                                    Data.star = 2;
                                                    ListData_RFP listData_rfp = new ListData_RFP(cursor2.getString(4), "Capalino+Company Match", Data.star, cursor2.getString(0), cursor2.getString(1),
                                                            cursor2.getString(3), cursor2.getString(2));
                                                    for (int j = 0; j < list_1star.size(); j++) {
                                                        if (list_1star.get(j).equals(listData_rfp)) {
                                                            list_1star.get(j).setRating(Data.star);
                                                        }
                                                    }

                                                }



                                            }
                                        }

                                    }


                                    //b.	2 stars: Construction + Geographic Coverage       category + area
                                    for (int i = 0; i < list_pref.size(); i++) {
                                        if (list_pref.get(i).getSettingTypeID() == 1) {
                                            for (int j = 0; j < Data.procid.size(); j++) {
                                                Cursor cursor3 = dataBaseHelper.getDataFromProcurementMasterStar2_(
                                                        list_pref.get(i).getActualTagID(), Data.procid.get(j));
                                                if (cursor3 != null && cursor3.getCount() > 0) {
                                                    while (cursor3.moveToNext()) {
                                                        Data.star = 2;
                                                        ListData_RFP listData_rfp = new ListData_RFP(cursor3.getString(4), "Capalino+Company Match", Data.star, cursor3.getString(0), cursor3.getString(1),
                                                                cursor3.getString(3), cursor3.getString(2));

                                                        geographiclist.add(listData_rfp);
                                                        for (int k = 0; k < list_1star.size(); k++) {
                                                            if (list_1star.get(k).equals(listData_rfp)) {
                                                                list_1star.get(k).setRating(Data.star);
                                                            }
                                                        }

                                                    }



                                                }
                                            }


                                        }
                                    }

                                    //a.	3 stars: Roofing + Geographic Coverage                 subcategory + area
                                    for (int i = 0; i < list_pref.size(); i++) {
                                        if (list_pref.get(i).getSettingTypeID() != 1) {
                                            Cursor cursor4 = dataBaseHelper.getDataFromProcurementMasterStar3(
                                                    list_pref.get(i).getActualTagID(),
                                                    list_pref.get(i).getSettingTypeID());
                                            if (cursor4 != null && cursor4.getCount() > 0) {
                                                while (cursor4.moveToNext()) {
                                                    ListData_RFP listData_rfp = new ListData_RFP(cursor4.getString(4), "Capalino+Company Match", Data.star, cursor4.getString(0), cursor4.getString(1),
                                                            cursor4.getString(3), cursor4.getString(2));
                                                    for (int j = 0; j < geographiclist.size(); j++) {
                                                        if (geographiclist.get(j).equals(listData_rfp)) {
                                                            Data.star = 3;
                                                            geographiclist.get(j).setRating(Data.star);
                                                        }
                                                    }


                                                }


                                            }
                                        }
                                    }

                                    for (ListData_RFP data : geographiclist) {
                                        for (int i = 0; i < list_1star.size(); i++) {
                                            if (list_1star.get(i).equals(data)) {
                                                list_1star.set(i, data);
                                            }
                                        }
                                    }
                                    List<ListData_RFP> list_rfp = new ArrayList<ListData_RFP>();
                                    for (ListData_RFP data_rfp : list_1star) {
                                        if (data_rfp.getRating() == 3.0) {
                                            list_rfp.add(data_rfp);
                                        }
                                    }

                                    for (ListData_RFP data_rfp : list_1star) {
                                        if (data_rfp.getRating() == 2.0) {
                                            list_rfp.add(data_rfp);
                                        }
                                    }

                                    for (ListData_RFP data_rfp : list_1star) {
                                        if (data_rfp.getRating() == 1.0) {
                                            list_rfp.add(data_rfp);
                                        }
                                    }


                                    hidePB();
                                    Intent j = new Intent(BrowseActivity.this, REFListingActivity.class);
                                    j.putExtra("list", (Serializable) list_rfp);
                                    startActivity(j);
                                    finish();
                                }catch (Exception e){
                                    e.printStackTrace();
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
                return jsonObject;
            }


        }.execute(link, "", "");
    }

    private void getGeographicTodays() {
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


                    response = response.replace("\n","");
                    if(response!=null) {
                          if(!response.equalsIgnoreCase("[]")){
                              JSONArray jsonArray = new JSONArray(response);
                              jsonObject = new JSONObject();
                              for (int i = 0; i < jsonArray.length(); i++) {
                                  jsonObject = jsonArray.getJSONObject(i);
                                  Data.Actual_id_list_geographic.add(Integer.valueOf(jsonObject.getString("ActualTagID")));

                              }
                          }

                            try {
                                //d.	1 star: Construction
                                //      category

                                Cursor cursor = null;
                                /*for (int i = 0; i < list_pref.size(); i++) {
                                    if (list_pref.get(i).getSettingTypeID() != 1) {
                                        for(int j=0;j<list_procurement.size(); j++){
                                            cursor = dataBaseHelper.
                                                    getDataFromProcurementMasterByProcurementID(
                                                            list_pref.get(i).getSettingTypeID(),
                                                            list_procurement.get(j));
                                            list_1star = fillListTodays(cursor);
                                        }

                                    }
                                }*/

                                for (int i = 0; i < list_procurement.size(); i++) {
                                    cursor = dataBaseHelper.getDataFromProcurementMasterByProcurementID(
                                            list_procurement.get(i));
                                    fillListTodays(cursor);


                                }



                                ArrayList<ListData_RFP> list = new ArrayList<>();
                                for (int i = 0; i < list_pref.size(); i++) {
                                    if (list_pref.get(i).getSettingTypeID() != 1) {
                                        cursor = dataBaseHelper.getDataFromProcurementMaster(list_pref.get(i).getSettingTypeID());
                                        list_1star = fillList(cursor, list);
                                    }
                                }

                                cursor.close();

                                Log.d("list_todays", String.valueOf(list_todays.size()));

                                //c part : c.	2 stars: Roofing - Geographic Coverage
                                //                subcategory - area

                                for (int i = 0; i < list_pref.size(); i++) {
                                    if (list_pref.get(i).getSettingTypeID() != 1) {
                                        Cursor cursor2 = dataBaseHelper.getDataFromProcurementMasterStar2(
                                                list_pref.get(i).getActualTagID(),
                                                list_pref.get(i).getSettingTypeID());
                                        if (cursor2 != null && cursor2.getCount() > 0) {
                                            while (cursor2.moveToNext()) {
                                                Data.star = 2;
                                                ListData_RFP listData_rfp = new ListData_RFP(cursor2.getString(4), "Capalino+Company Match", Data.star, cursor2.getString(0), cursor2.getString(1),
                                                        cursor2.getString(3), cursor2.getString(2));

                                                for (int j = 0; j < list_1star.size(); j++) {
                                                    if (list_1star.get(j).equals(listData_rfp)) {
                                                        list_1star.get(j).setRating(Data.star);

                                                    }
                                                }

                                            }



                                        }
                                    }

                                }


                                //b.	2 stars: Construction + Geographic Coverage       category + area
                                for (int i = 0; i < list_pref.size(); i++) {
                                    if (list_pref.get(i).getSettingTypeID() == 1) {
                                        for (int j = 0; j < Data.procid.size(); j++) {
                                            Cursor cursor3 = dataBaseHelper.getDataFromProcurementMasterStar2_(
                                                    list_pref.get(i).getActualTagID(), Data.procid.get(j));
                                            if (cursor3 != null && cursor3.getCount() > 0) {
                                                while (cursor3.moveToNext()) {
                                                    Data.star = 2;
                                                    ListData_RFP listData_rfp = new ListData_RFP(cursor3.getString(4), "Capalino+Company Match", Data.star, cursor3.getString(0), cursor3.getString(1),
                                                            cursor3.getString(3), cursor3.getString(2));
                                                    Log.d("obj", String.valueOf(listData_rfp.
                                                            getTitle()));
                                                    geographiclist.add(listData_rfp);
                                                    for (int k = 0; k < list_1star.size(); k++) {
                                                        if (list_1star.get(k).equals(listData_rfp)) {
                                                            list_1star.get(k).setRating(Data.star);
                                                        }
                                                    }

                                                }



                                            }
                                        }


                                    }
                                }

                                //a.	3 stars: Roofing + Geographic Coverage                 subcategory + area
                                for (int i = 0; i < list_pref.size(); i++) {
                                    if (list_pref.get(i).getSettingTypeID() != 1) {
                                        Cursor cursor4 = dataBaseHelper.getDataFromProcurementMasterStar3(
                                                list_pref.get(i).getActualTagID(),
                                                list_pref.get(i).getSettingTypeID());
                                        if (cursor4 != null && cursor4.getCount() > 0) {
                                            while (cursor4.moveToNext()) {
                                                ListData_RFP listData_rfp = new ListData_RFP(cursor4.getString(4), "Capalino+Company Match", Data.star, cursor4.getString(0), cursor4.getString(1),
                                                        cursor4.getString(3), cursor4.getString(2));
                                                Log.d("obj_star3", String.valueOf(listData_rfp.
                                                        getTitle()));
                                                Log.d("geog_list", String.valueOf(geographiclist
                                                        .size()));

                                                for (int j = 0; j < geographiclist.size(); j++) {
                                                    if (geographiclist.get(j).equals(listData_rfp)) {
                                                        Data.star = 3;
                                                        geographiclist.get(j).setRating(Data.star);

                                                    }
                                                }


                                            }


                                        }
                                    }
                                }

                                for (ListData_RFP data : geographiclist) {
                                    for (int i = 0; i < list_1star.size(); i++) {
                                        if (list_1star.get(i).equals(data)) {
                                            list_1star.set(i, data);
                                        }
                                    }
                                }



                                List<ListData_RFP> list_rfp = new ArrayList<ListData_RFP>();
                                for (ListData_RFP data_rfp : list_1star) {
                                    if (data_rfp.getRating() == 3.0) {
                                        list_rfp.add(data_rfp);
                                    }
                                }

                                for (ListData_RFP data_rfp : list_1star) {
                                    if (data_rfp.getRating() == 2.0) {
                                        list_rfp.add(data_rfp);
                                    }
                                }

                                for (ListData_RFP data_rfp : list_1star) {
                                    if (data_rfp.getRating() == 1.0) {
                                        list_rfp.add(data_rfp);
                                    }
                                }

                                Log.d("list_rfp", list_rfp.size()+"");

                                for (ListData_RFP data: list_rfp){
                                    for (ListData_RFP data_rfp : list_todays){
                                        if (data.equals(data_rfp)){
                                            data_rfp.setRating(1);
                                        }
                                    }
                                }






                                hidePB();
                                Intent j = new Intent(BrowseActivity.this, REFListingActivity.class);
                                j.putExtra("list", (Serializable) list_todays);
                                j.putExtra("rating","hide");
                                startActivity(j);
                                finish();
                            }catch (Exception e){
                                e.printStackTrace();
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
                return jsonObject;
            }


        }.execute(link, "", "");
    }

    private void getCapabilitiesCount() {
        Data.SettingTypeID_capab_search.clear();
        //showPB("Loading...");
        Utils utils = new Utils(context);
        if (Utils.isConnected(this)) {
            String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/" +
                    "apis/getUserAllPreferences.php?UserID=" + utils.getdata("Userid");

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
                                j.putExtra("list", (Serializable) list_1star);
                                startActivity(j);
                                finish();
                            } else {
                                jsonArray = new JSONArray(response);
                                JSONObject jsonObject = null;
                                list_pref.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    String settingid = jsonObject.get("SettingTypeID").toString();
                                    String ActualTagID = jsonObject.get("ActualTagID").toString();
                                        //Data.SettingTypeID_capab_search.add(Integer.valueOf(settingid));
                                        list_pref.add(new PreferenceModel(Integer.
                                                valueOf(settingid),Integer.valueOf(ActualTagID)));
                                }

                                getGeographic();

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

    private void getCapabilitiesCountTodays() {
        Data.SettingTypeID_capab_search.clear();
        //showPB("Loading...");
        Utils utils = new Utils(context);
        if (Utils.isConnected(this)) {
            String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/" +
                    "apis/getUserAllPreferences.php?UserID=" + utils.getdata("Userid");

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
                                j.putExtra("list", (Serializable) list_1star);
                                startActivity(j);
                                finish();
                            } else {
                                jsonArray = new JSONArray(response);
                                JSONObject jsonObject = null;
                                list_pref.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    String settingid = jsonObject.get("SettingTypeID").toString();
                                    String ActualTagID = jsonObject.get("ActualTagID").toString();
                                    //Data.SettingTypeID_capab_search.add(Integer.valueOf(settingid));
                                    list_pref.add(new PreferenceModel(Integer.
                                            valueOf(settingid),Integer.valueOf(ActualTagID)));



                                }



                                getGeographicTodays();

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

    private void getTodaysProcurementID() {
        //showPB("Loading...");
        list_procurement = new ArrayList<>();
        Utils utils = new Utils(context);
        if (Utils.isConnected(this)) {
            String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/" +
                    "apis/getTodaysTaggedRFPs_J.php";

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
                        JSONArray jsonArray = new JSONArray();
                        if (response != null) {
                            if (response.equalsIgnoreCase("No RFP Found.")) {
                                hidePB();
                                Intent j = new Intent(BrowseActivity.this, REFListingActivity.class);
                                j.putExtra("list", (Serializable) list_1star);
                                startActivity(j);
                                finish();

                            } else {
                                jsonArray = new JSONArray(response);
                                JSONObject jsonObject = null;
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonObject = jsonArray.getJSONObject(i);
                                    String ProcurementID = jsonObject.
                                            get("ProcurementID").toString();
                                    list_procurement.add(Integer.valueOf(ProcurementID));


                                }

                            Log.d("list_size", list_procurement.size()+"");
                                getCapabilitiesCountTodays();

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

    private ArrayList<ListData_RFP> fillList(Cursor cursor, ArrayList<ListData_RFP> list) {
        Data.procid.clear();

        if(!DataBaseHelper.sqliteDataBase.isOpen()){
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            try {
                dataBaseHelper.createDataBase();
                dataBaseHelper.openDataBase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor!=null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                this.list.add(new ListData_RFP(cursor.getString(4),"Capalino+Company Match", Data.star, cursor.getString(0), cursor.getString(1),
                        cursor.getString(3), cursor.getString(2)));
                if(Data.star==1) {
                    Data.procid.add(cursor.getInt(4));
                }
            }

            cursor.close();
        }

        ArrayList<ListData_RFP> capablitylist = new ArrayList<ListData_RFP>();// unique
        for (ListData_RFP element : this.list) {
            if (!capablitylist.contains(element)) {
                capablitylist.add(element);
            }
        }

        return capablitylist;
    }

    private ArrayList<ListData_RFP> fillListTodays(Cursor cursor) {

        ArrayList<ListData_RFP> capablitylist = new ArrayList<ListData_RFP>();// unique

        if(!DataBaseHelper.sqliteDataBase.isOpen()){
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            try {
                dataBaseHelper.createDataBase();
                dataBaseHelper.openDataBase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (cursor!=null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                    if(list_procurement.contains(cursor.getInt(4))) {
                    list_todays.add(new ListData_RFP(cursor.getString(4), "Capalino+Company Match", 0, cursor.getString(0), cursor.getString(1),
                            cursor.getString(3), cursor.getString(2)));
                    if (Data.star == 1) {
                        Data.procid.add(cursor.getInt(4));
                        Log.d("procurementID", cursor.getInt(4) + "");
                    }
                }


                for (ListData_RFP element : list_todays) {
                    if (!capablitylist.contains(element)) {
                        capablitylist.add(element);
                    }
                }

                return capablitylist;




            }

            cursor.close();
        }

        return null;
    }

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

            }else {


                if(utils.getdata("BusinessName")!=null &&
                        !utils.getdata("BusinessName").equalsIgnoreCase("NA")) {
                    showPB("Matching opportunities for " + utils.getdata("BusinessName"));
                }else{
                    showPB("Matching opportunities...");
                }

                if(Data.isOpen) {

                    getCapabilitiesCount();
                }
                else {
                    hidePB();
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

    private void getTagedRFP(final String lastupdatedate) {
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(BrowseActivity.this);
                    dataBaseHelper.createDataBase();
                    dataBaseHelper.openDataBase();


                    /*Cursor cursor = dataBaseHelper.DBRecord("Select LastUpdateDate from ProcurementRFPPreferences");
                    if(cursor.getCount()>0){
                        while (cursor.moveToNext()){
                            lastupdatedate = cursor.getString(0);



                        }
                    }*/

                    HttpClient httpclient = new DefaultHttpClient();

                    SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                    /*String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getTaggedRFPUpdatedForStore.php?lastUpdateSql=" +lastupdatedate
                            +"&CurrentDate="+format.format(new Date());*/
                    String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/" +
                            "getTaggedRFPUpdatedForStore_J.php?CurrentDate="+format.format(new Date()) +
                            "&UserID=" + utils.getdata("Userid") + "&ContentLastUpdateDate=" + lastupdatedate;
                    link = link.replace(" ","%20");
                    HttpPost httppost = new HttpPost(link);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    String response = httpclient.execute(httppost,
                            responseHandler);


                    Log.i("Response", "Response : " + response);
                    if(!response.equalsIgnoreCase("No new content available.") && !response.equalsIgnoreCase("")) {
                        JSONObject jsonObject_root = new JSONObject(response);
                        String result = jsonObject_root.getString("results");
                        String datastatus = jsonObject_root.getString("datastatus");

                        if (datastatus.equalsIgnoreCase("full")){
                            showPB("Loading new City and State RFPs. Please wait, this may take up to a minute. Thanks for your patience.");
                            dataBaseHelper.delete("ProcurementRFPPreferences");
                        }

                        JSONArray jsonarray = new JSONArray(result);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobj = jsonarray.getJSONObject(i);
                            String PreferenceID = jsonobj.getString("PreferenceID");
                            String ProcurementID = jsonobj.getString("ProcurementID");
                            String SettingTypeID = jsonobj.getString("SettingTypeID");
                            String ActualTagID = jsonobj.getString("ActualTagID");
                            String AddedDateTime = jsonobj.getString("AddedDateTime");
                            String lastupdate = jsonobj.getString("LASTUPDATE");
                            String Status = jsonobj.getString("Status");

                            TaggedRFP been = new TaggedRFP(Integer.valueOf(PreferenceID), Integer.valueOf(ProcurementID), Integer.valueOf(SettingTypeID),
                                    Integer.valueOf(ActualTagID), AddedDateTime, lastupdate, Status);

                            //list_data.add(new ListData(image, contentShortDescription, ContentRelevantDateTime));
                            if (datastatus.equalsIgnoreCase("full")){

                                dataBaseHelper.InsertinRFPPrefence(been);
                            }else {
                                if (Status.equalsIgnoreCase("1")){
                                    dataBaseHelper.InsertinRFPPrefence(been);
                                }

                                if (Status.equalsIgnoreCase("0")){
                                    dataBaseHelper.deleteRFPPrefrence(been.getPreferenceID());
                                }
                            }

                            utils.savedata("RFPUpdatedDate","dba");


                        }

                    }

                    hidePB();
                } catch (Exception e) {
                    hidePB();
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();



    }

    void showPB(final String message) {
        try {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if(pb == null) {
                        pb = new ProgressDialog(BrowseActivity.this);
                        pb.setCancelable(false);

                    }
                    pb.setMessage(message);
                    pb.show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();

        }

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

    public void todaysRFPClick(View view) {


        showPB("Please wait...");
        getTodaysProcurementID();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hidePB();
    }
}
