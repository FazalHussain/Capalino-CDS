package com.MWBE.Connects.NY.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.MWBE.Connects.NY.AppConstants.Constants;
import com.MWBE.Connects.NY.CapalinoServices.CapabilitiesService;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.JavaBeen.CapabilitiesMaster;
import com.MWBE.Connects.NY.Storage.Storage;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.CustomViews.CustomEditText_Book;
import com.MWBE.Connects.NY.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class CapablitiesActivity extends FragmentActivity {

    private int count;
    private int settingTypeID;
    private Utils utils;
    private int count1;
    private int count2;
    private int count3;
    private int count4;
    private int count5;
    private int count6;
    private int count7;
    private int count8;
    private int count9;
    private int count10;
    private ProgressDialog pb;
    private Context context = this;
    private boolean isShow;
    private int finalsettingid;
    private int totalcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capablities);
        showPB("Loading....");
        //getCount();
        init();
        utils = new Utils(this);
        getDataFromServer();
        for(int i=100;i<1100;i+=100)
        getCapabilitiesCount1(i);
        //getSettingTypeID();
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
                                ((CustomEditText_Book) findViewById(R.id.Advertising_)).setText(jsonArray.length() + " Selected");
                                else
                                    ((CustomEditText_Book) findViewById(R.id.Advertising_)).setText("");
                                break;
                            }

                            case 200: {
                                if(jsonArray.length()>0)
                                ((CustomEditText_Book) findViewById(R.id.Architectural)).setText(jsonArray.length() + " Selected");
                                else
                                    ((CustomEditText_Book) findViewById(R.id.Architectural)).setText("");
                                break;
                            }

                            case 300: {
                                if(jsonArray.length()>0)
                                    ((CustomEditText_Book) findViewById(R.id.hconstruction)).setText(jsonArray.length() + " Selected");
                                else
                                    ((CustomEditText_Book) findViewById(R.id.hconstruction)).setText("");
                                break;
                            }

                            case 400: {
                                if(jsonArray.length()>0)
                                    ((CustomEditText_Book) findViewById(R.id.Enviornmental)).setText(jsonArray.length() + " Selected");
                                else
                                    ((CustomEditText_Book) findViewById(R.id.Enviornmental)).setText("");
                                break;
                            }

                            case 500: {
                                if(jsonArray.length()>0)
                                    ((CustomEditText_Book) findViewById(R.id.General_maintenance)).setText(jsonArray.length() + " Selected");
                                else
                                    ((CustomEditText_Book) findViewById(R.id.General_maintenance)).setText("");
                                break;
                            }

                            case 600: {
                                if(jsonArray.length()>0)
                                    ((CustomEditText_Book) findViewById(R.id.Maintainance)).setText(jsonArray.length() + " Selected");
                                else
                                    ((CustomEditText_Book) findViewById(R.id.Maintainance)).setText("");
                                break;
                            }

                            case 700: {
                                if(jsonArray.length()>0)
                                    ((CustomEditText_Book) findViewById(R.id.security)).setText(jsonArray.length() + " Selected");
                                else
                                    ((CustomEditText_Book) findViewById(R.id.security)).setText("");
                                break;
                            }

                            case 800: {
                                if(jsonArray.length()>0)
                                    ((CustomEditText_Book) findViewById(R.id.Information_technology)).setText(jsonArray.length() + " Selected");
                                else
                                    ((CustomEditText_Book) findViewById(R.id.Information_technology)).setText("");
                                break;
                            }

                            case 900: {
                                if(jsonArray.length()>0)
                                    ((CustomEditText_Book) findViewById(R.id.humanservices)).setText(jsonArray.length() + " Selected");
                                else
                                    ((CustomEditText_Book) findViewById(R.id.humanservices)).setText("");
                                break;
                            }

                            case 1000: {
                                if(jsonArray.length()>0)
                                    ((CustomEditText_Book) findViewById(R.id.others)).setText(jsonArray.length() + " Selected");
                                else
                                    ((CustomEditText_Book) findViewById(R.id.others)).setText("");
                                hidePB();
                                break;
                            }
                        }
                    }

                }
            }.execute(link, "", "");
        }
    }

    private void getDataFromServer() {
        try{
         new AsyncTask<String,Void,Void>(){

             @Override
             protected Void doInBackground(String... params) {
                 try {
                     int count = 0;
                     DataBaseHelper db = new DataBaseHelper(CapablitiesActivity.this);
                     db.createDataBase();
                     db.openDataBase();
                     Cursor cursor = db.getDataFromQuery("Select count(TagValueTitle) as TotalRecords from CapabilitiesMaster");
                     if (cursor.getCount() > 0) {
                         while (cursor.moveToNext()) {
                             count = cursor.getInt(0);
                         }
                     }
                     //System.setProperty("http.keepAlive", "false");
                     HttpClient httpclient1 = new DefaultHttpClient();


                     String link = params[0] + count;
                     link = link.replace(" ", "%20");
                     HttpGet httppost = new HttpGet(link);


                     ResponseHandler<String> responseHandler = new BasicResponseHandler();

                     String response = httpclient1.execute(httppost,
                             responseHandler);


                     Log.d("Response", "Response : " + response);
                     if (!response.contains("Both are equal")) {
                         if (!response.contains("Both are equal")) {
                             db.delete("CapabilitiesMaster");
                             JSONArray jsonarray = new JSONArray(response);
                             for (int i = 0; i < jsonarray.length(); i++) {
                                 JSONObject jsonobj = jsonarray.getJSONObject(i);
                                 String TagID = jsonobj.getString("TagID");
                                 String CapTagID = jsonobj.getString("CapTagID");
                                 String TagValueID = jsonobj.getString("TagValueID");
                                 String TagValueTitle = jsonobj.getString("TagValueTitle");

                                 //list_data.add(new ListData(image, contentShortDescription, ContentRelevantDateTime));
                                 CapabilitiesMaster been = new CapabilitiesMaster(Integer.valueOf(TagID), Integer.valueOf(CapTagID), Integer.valueOf(TagValueID),
                                         TagValueTitle);
                                 final boolean isinserted = db.InsertinCapabilitiesMaster(been);

                                 runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {

                                         if (isinserted) {
                                             //Toast.makeText(HomeActivity.this, "Hello", Toast.LENGTH_LONG).show();
                                             Log.d("CapabilitiesMaster", "Added");
                                         }
                                     }
                                 });
                             }
                             hidePB();
                         } else {
                             hidePB();
                         }
                     }
                 }catch (Exception e) {
                     hidePB();
                     e.printStackTrace();
                 }
                 return null;
             }
         }.execute("http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getCapabalitiesData.php?Records=");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getCount() {
        if(Storage.count1>0){
            count1 = Storage.count1;
        }

        if(Storage.count2>0){
            count2 = Storage.count2;
        }
        if(Storage.count3>0){
            count3 = Storage.count3;
        }
        if(Storage.count4>0){
            count4 = Storage.count4;
        }

        if(Storage.count5>0){
            count5 = Storage.count5;
        }

        if(Storage.count6>0){
            count6 = Storage.count6;
        }

        if(Storage.count7>0){
            count7 = Storage.count7;
        }

        if(Storage.count8>0){
            count8 = Storage.count8;
        }

        if(Storage.count9>0){
            count9 = Storage.count9;
        }

        if(Storage.count10>0){
            count10 = Storage.count10;
        }



    }

    private void init() {
        if(count1>0)
        ((CustomEditText_Book) findViewById(R.id.Advertising_)).setText(count1 + " Selected");
        if(count2>0)
        ((CustomEditText_Book) findViewById(R.id.Architectural)).setText(count2 + " Selected");
        if(count3>0)
        ((CustomEditText_Book) findViewById(R.id.hconstruction)).setText(count3 + " Selected");
        if(count4>0)
        ((CustomEditText_Book) findViewById(R.id.Enviornmental)).setText(count4 + " Selected");
        if(count5>0)
        ((CustomEditText_Book) findViewById(R.id.General_maintenance)).setText(count5 + " Selected");
        if(count6>0)
        ((CustomEditText_Book) findViewById(R.id.Maintainance)).setText(count6 + " Selected");
        if(count7>0)
        ((CustomEditText_Book) findViewById(R.id.security)).setText(count7 + " Selected");
        if(count8>0)
        ((CustomEditText_Book) findViewById(R.id.Information_technology)).setText(count8 + " Selected");
        if(count9>0)
        ((CustomEditText_Book) findViewById(R.id.humanservices)).setText(count9 + " Selected");
        if(count10>0)
        ((CustomEditText_Book) findViewById(R.id.others)).setText(count10 + " Selected");

        count = 0;
        if(((CustomEditText_Book) findViewById(R.id.Advertising_)).getText().length()>0){
            count++;
        }

        if(((CustomEditText_Book) findViewById(R.id.Architectural)).getText().length()>0){
            count++;
        }

        if(((CustomEditText_Book) findViewById(R.id.hconstruction)).getText().length()>0){
            count++;
        }

        if(((CustomEditText_Book) findViewById(R.id.Enviornmental)).getText().length()>0){
            count++;
        }

        if(((CustomEditText_Book) findViewById(R.id.Maintainance)).getText().length()>0){
            count++;
        }

        if(((CustomEditText_Book) findViewById(R.id.humanservices)).getText().length()>0){
            count++;
        }

        if(((CustomEditText_Book) findViewById(R.id.Information_technology)).getText().length()>0){
            count++;
        }

        if(((CustomEditText_Book) findViewById(R.id.General_maintenance)).getText().length()>0){
            count++;
        }

        if(((CustomEditText_Book) findViewById(R.id.security)).getText().length()>0){
            count++;
        }

        if(((CustomEditText_Book) findViewById(R.id.others)).getText().length()>0){
            count++;
        }




    }

    private void getCapabilitiesCount() {
        Utils utils = new Utils(CapablitiesActivity.this);
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
                        response = response.replace("\n","");
                        if(response.equalsIgnoreCase("[]")){
                            Data.clearlist(settingTypeID);
                            SetText(settingTypeID);
                            if(settingTypeID==1000){
                                hidePB();
                            }
                        }
                        if(settingTypeID==finalsettingid){
                            hidePB();
                        }
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
                protected void onPostExecute(JSONArray s) {
                    super.onPostExecute(s);
                    if (s != null) {
                        if (s.length() > 0) {
                            JSONObject jsonObject = null;
                            for (int i = 0; i < s.length(); i++) {
                                try {
                                    jsonObject = s.getJSONObject(i);
                                    Data.jsonObject.add(jsonObject);
                                    String settingid = jsonObject.get("SettingTypeID").toString();
                                    if (settingid.length() > 0)
                                        settingTypeID = Integer.valueOf(settingid);

                                    SetupActualIDList(jsonObject);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            switch (settingTypeID) {
                                case 100: {
                                    ((CustomEditText_Book) findViewById(R.id.Advertising_)).setText(count1 + " Selected");

                                    break;
                                }

                                case 200: {
                                    ((CustomEditText_Book) findViewById(R.id.Architectural)).setText(count2 + " Selected");
                                    break;
                                }

                                case 300: {
                                    ((CustomEditText_Book) findViewById(R.id.hconstruction)).setText(count3 + " Selected");
                                    break;
                                }

                                case 400: {
                                    ((CustomEditText_Book) findViewById(R.id.Enviornmental)).setText(count4 + " Selected");
                                    break;
                                }

                                case 500: {
                                    ((CustomEditText_Book) findViewById(R.id.General_maintenance)).setText(count5 + " Selected");
                                    break;
                                }

                                case 600: {
                                    ((CustomEditText_Book) findViewById(R.id.Maintainance)).setText(count6 + " Selected");
                                    break;
                                }

                                case 700: {
                                    ((CustomEditText_Book) findViewById(R.id.security)).setText(count7 + " Selected");
                                    break;
                                }

                                case 800: {
                                    ((CustomEditText_Book) findViewById(R.id.Information_technology)).setText(count8 + " Selected");
                                    break;
                                }

                                case 900: {
                                    ((CustomEditText_Book) findViewById(R.id.humanservices)).setText(count9 + " Selected");
                                    break;
                                }

                                case 1000: {
                                    ((CustomEditText_Book) findViewById(R.id.others)).setText(count10 + " Selected");
                                    hidePB();
                                    break;
                                }
                            }

                            init();

                        } else {
                            if(finalsettingid == 1000){
                                //hidePB();
                                finalsettingid = 0;
                            }
                        }
                    } else {
                        /*if (!isShow)
                            new AlertDialog.Builder(context)
                                    .setTitle("Alert!")
                                    .setMessage("Server not able to respond, check your internet connection and please try again.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            isShow = true;
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();*/

                        switch (settingTypeID) {
                            case 100: {
                                ((CustomEditText_Book) findViewById(R.id.Advertising_)).setText("");

                                break;
                            }

                            case 200: {
                                ((CustomEditText_Book) findViewById(R.id.Architectural)).setText("");
                                break;
                            }

                            case 300: {
                                ((CustomEditText_Book) findViewById(R.id.hconstruction)).setText("");
                                break;
                            }

                            case 400: {
                                ((CustomEditText_Book) findViewById(R.id.Enviornmental)).setText("");
                                break;
                            }

                            case 500: {
                                ((CustomEditText_Book) findViewById(R.id.General_maintenance)).setText("");
                                break;
                            }

                            case 600: {
                                ((CustomEditText_Book) findViewById(R.id.Maintainance)).setText("");
                                break;
                            }

                            case 700: {
                                ((CustomEditText_Book) findViewById(R.id.security)).setText("");
                                break;
                            }

                            case 800: {
                                ((CustomEditText_Book) findViewById(R.id.Information_technology)).setText("");
                                break;
                            }

                            case 900: {
                                ((CustomEditText_Book) findViewById(R.id.humanservices)).setText("");
                                break;
                            }

                            case 1000: {
                                ((CustomEditText_Book) findViewById(R.id.others)).setText("");
                                hidePB();
                                break;
                            }
                        }
                    }
                }
            }.execute(link, "", "");
        }else {
            hidePB();
            /*if(!isShow)
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("Server not able to respond, check your internet connection and please try again.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isShow = true;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();*/
        }
    }

    private void SetText(int settingTypeID) {
        switch (settingTypeID) {
            case 100: {
                ((CustomEditText_Book) findViewById(R.id.Advertising_)).setText("");

                break;
            }

            case 200: {
                ((CustomEditText_Book) findViewById(R.id.Architectural)).setText("");
                break;
            }

            case 300: {
                ((CustomEditText_Book) findViewById(R.id.hconstruction)).setText("");
                break;
            }

            case 400: {
                ((CustomEditText_Book) findViewById(R.id.Enviornmental)).setText("");
                break;
            }

            case 500: {
                ((CustomEditText_Book) findViewById(R.id.General_maintenance)).setText("");
                break;
            }

            case 600: {
                ((CustomEditText_Book) findViewById(R.id.Maintainance)).setText("");
                break;
            }

            case 700: {
                ((CustomEditText_Book) findViewById(R.id.security)).setText("");
                break;
            }

            case 800: {
                ((CustomEditText_Book) findViewById(R.id.Information_technology)).setText("");
                break;
            }

            case 900: {
                ((CustomEditText_Book) findViewById(R.id.humanservices)).setText("");
                break;
            }

            case 1000: {
                ((CustomEditText_Book) findViewById(R.id.others)).setText("");
                hidePB();
                break;
            }
        }
    }

    public void SetupActualIDList(JSONObject jsonObject) {
        try{


            String settingTypeID = jsonObject.getString("SettingTypeID");
            //Data.ActualID_list_advertising.clear();
            switch (settingTypeID){
                case "100":{

                    if(Data.ActualID_list_advertising.size()>0){
                        for(int i=0;i<Data.ActualID_list_advertising.size();i++){
                            if(!Data.ActualID_list_advertising.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_advertising.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                count1++;
                            }
                        }

                    }else {

                        count1 = 1;
                        Data.ActualID_list_advertising.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }

                    break;
                }

                case "200":{
                    if(Data.ActualID_list_architectural.size()>0){
                        for(int i=0;i<Data.ActualID_list_architectural.size();i++){
                            if(!Data.ActualID_list_architectural.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_architectural.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                count2++;
                            }
                        }

                    }else {
                        count2 =1;
                        Data.ActualID_list_architectural.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }
                    break;
                }

                case "300":{

                    if(Data.ActualID_list_construction.size()>0){
                        for(int i=0;i<Data.ActualID_list_construction.size();i++){
                            if(!Data.ActualID_list_construction.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_construction.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                count3++;
                            }
                        }

                    }else {
                        count3 = 1;
                        Data.ActualID_list_construction.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }


                    break;
                }

                case "400":{

                    if(Data.ActualID_list_envoirnmental.size()>0){
                        for(int i=0;i<Data.ActualID_list_envoirnmental.size();i++){
                            if(!Data.ActualID_list_envoirnmental.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_envoirnmental.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                count4++;
                            }
                        }

                    }else {
                        count4 = 1;
                        Data.ActualID_list_envoirnmental.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }


                    break;
                }

                case "500":{

                    if(Data.ActualID_list_solidwaste.size()>0){
                        for(int i=0;i<Data.ActualID_list_solidwaste.size();i++){
                            if(!Data.ActualID_list_solidwaste.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_solidwaste.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                count5++;
                            }
                        }

                    }else {
                        count5 = 1;
                        Data.ActualID_list_solidwaste.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }

                    break;
                }

                case "600":{

                    if(Data.ActualID_list_facilities.size()>0){
                        for(int i=0;i<Data.ActualID_list_facilities.size();i++){
                            if(!Data.ActualID_list_facilities.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_facilities.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                count6++;
                            }
                        }

                    }else {
                        count6 = 1;
                        Data.ActualID_list_facilities.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }

                    break;
                }

                case "700":{

                    if(Data.ActualID_list_safety.size()>0){
                        for(int i=0;i<Data.ActualID_list_safety.size();i++){
                            if(!Data.ActualID_list_safety.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_safety.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                count7++;
                            }
                        }

                    }else {
                        count7 = 1;
                        Data.ActualID_list_safety.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }

                    break;
                }

                case "800":{

                    if(Data.ActualID_list_it.size()>0){
                        for(int i=0;i<Data.ActualID_list_it.size();i++){
                            if(!Data.ActualID_list_it.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_it.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                count8++;
                            }
                        }

                    }else {
                        count8 = 1;
                        Data.ActualID_list_it.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }

                    break;
                }

                case "900":{

                    if(Data.ActualID_list_humanservice.size()>0){
                        for(int i=0;i<Data.ActualID_list_humanservice.size();i++){
                            if(!Data.ActualID_list_humanservice.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_humanservice.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                count9++;
                            }
                        }

                    }else {
                        count9 = 1;
                        Data.ActualID_list_humanservice.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }

                    break;
                }

                case "1000":{
                    if(Data.ActualID_list_others.size()>0){
                        for(int i=0;i<Data.ActualID_list_others.size();i++){
                            if(!Data.ActualID_list_others.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_others.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                count10++;
                            }
                        }

                    }else {
                        count10 = 1;
                        Data.ActualID_list_others.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }

                    break;
                }
            }
            }catch (Exception e){
                e.printStackTrace();
            }

    }

   /* private void getSettingTypeID() {
        showPB("Loading...");
        for (int i = 1; i < 11; i++) {
            switch (i) {
                case 1: {
                    settingTypeID = 100;
                    getCapabilitiesCount();
                    break;
                }

                case 2: {
                    settingTypeID = 200;
                    getCapabilitiesCount();
                    break;
                }

                case 3: {
                    settingTypeID = 300;
                    getCapabilitiesCount();
                    break;
                }

                case 4: {
                    settingTypeID = 400;
                    getCapabilitiesCount();
                    break;
                }

                case 5: {
                    settingTypeID = 500;
                    getCapabilitiesCount();
                    break;
                }

                case 6: {
                    settingTypeID = 600;
                    getCapabilitiesCount();
                    break;
                }

                case 7: {
                    settingTypeID = 700;
                    getCapabilitiesCount();
                    break;
                }

                case 8: {
                    settingTypeID = 800;
                    getCapabilitiesCount();
                    break;
                }

                case 9: {
                    settingTypeID = 900;
                    getCapabilitiesCount();
                    break;
                }

                case 10: {
                    settingTypeID = 1000;
                    finalsettingid = settingTypeID;
                    getCapabilitiesCount();
                    break;
                }
            }
        }
    }*/

    public void AddOthersClick(View view){
        Intent i = new Intent(CapablitiesActivity.this,SetupActivity.class);
        i.putExtra("status","addothers");
        startActivity(i);
    }

    //BackClick Event
    public void BackClick(View view) {
        Intent intentback = new Intent();
        intentback.putExtra("count", count);
        setResult(Constants.request_capabilites, intentback);
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

        Intent j = new Intent(CapablitiesActivity.this, CapabilitiesService.class);
        j.putExtra("Userid", utils.getdata("Userid"));
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            startForegroundService(j);
        }else {
            startService(j);
        }

        if(getIntent().getStringExtra("status1")!=null){
            startActivity(new Intent(this,SettingsActivity.class));
        }else {
            finish();
        }
    }

    //Edit Text Click Listener
    public void AdvertisingClick(View view) {
        Intent i = new Intent(CapablitiesActivity.this, CapablitiesSearchActivity.class);
        i.putExtra("status", "Advertising");
        utils.savedata("status","Advertising");
        i.putExtra("header", "Advertising, Graphic Arts and Marketing");
        startActivityForResult(i, Constants.request_advertising);
    }

    public void HConstructionClick(View view) {
        Intent i = new Intent(CapablitiesActivity.this, CapablitiesSearchActivity.class);
        i.putExtra("status", "HVConstruction");
        utils.savedata("status", "HVConstruction");
        i.putExtra("header", "Construction (Horizontal and Vertical)");
        startActivityForResult(i, Constants.request_horizontal_constructions);
    }

    public void ArchitecturalClick(View view) {
        Intent i = new Intent(CapablitiesActivity.this, CapablitiesSearchActivity.class);
        i.putExtra("status", "Architecture");
        utils.savedata("status", "Architecture");
        i.putExtra("header", "Architectural, Engineering and Surveying");
        startActivityForResult(i, Constants.request_architectural);
    }

    public void EnvoirnmentalClick(View view) {
        Intent i = new Intent(CapablitiesActivity.this, CapablitiesSearchActivity.class);
        i.putExtra("status", "Envoirnmental");
        utils.savedata("status", "Envoirnmental");
        i.putExtra("header", "Environmental");
        startActivityForResult(i, Constants.request_envoirnmental);
    }

    public void FacilitiesClick(View view) {
        Intent i = new Intent(CapablitiesActivity.this, CapablitiesSearchActivity.class);
        i.putExtra("status", "Facilities");
        utils.savedata("status", "Facilities");
        i.putExtra("header", "Facilities Maintenance and Building Ops");
        startActivityForResult(i, Constants.request_facilities);
    }

    public void GeneralMaintainanceClick(View view) {
        Intent i = new Intent(CapablitiesActivity.this, CapablitiesSearchActivity.class);
        i.putExtra("status", "GeneralMaintainance");
        utils.savedata("status", "GeneralMaintainance");
        i.putExtra("header", "Solid Waste Removal");
        startActivityForResult(i, Constants.request_generalmaintainance);
    }

    public void SecurityClick(View view) {
        Intent i = new Intent(CapablitiesActivity.this, CapablitiesSearchActivity.class);
        i.putExtra("status", "Security");
        utils.savedata("status", "Security");
        i.putExtra("header", "Safety and Security");
        startActivityForResult(i, Constants.request_security);
    }

    public void ITClick(View view) {
        Intent i = new Intent(CapablitiesActivity.this, CapablitiesSearchActivity.class);
        i.putExtra("status", "IT");
        utils.savedata("status", "IT");
        i.putExtra("header", "Information Technology (IT)");
        startActivityForResult(i, Constants.request_IT);
    }

    public void OthersClick(View view) {
        Intent i = new Intent(CapablitiesActivity.this, CapablitiesSearchActivity.class);
        i.putExtra("status", "Others");
        utils.savedata("status", "Others");
        i.putExtra("header", "Other Professional Services");
        startActivityForResult(i, Constants.request_others);
    }

    public void HumanServiceClick(View view) {
        Intent i = new Intent(CapablitiesActivity.this, CapablitiesSearchActivity.class);
        i.putExtra("status", "humanservices");
        utils.savedata("status", "humanservices");
        i.putExtra("header", "Human Services");
        startActivityForResult(i, Constants.request_human);
    }

    //Footer Tab Click Events

    public void HomeClick(View view) {

        try {
            Intent i = new Intent(CapablitiesActivity.this, HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void BrowseClick(View view) {
        try {
            Intent i = new Intent(CapablitiesActivity.this, BrowseActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void TrackClick(View view) {

        try {
            Intent i = new Intent(CapablitiesActivity.this, TrackList.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ResourceClick(View view) {
        Intent i = new Intent(CapablitiesActivity.this, ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case Constants.request_advertising: {
                    if (data.getStringExtra("count") != null) {
                        settingTypeID = 100;
                        getCapabilitiesCount();
                        //((CustomEditText_Book) findViewById(R.id.Advertising_)).setText(data.getStringExtra("count"));
                        //count++;

                        break;
                    }
                }

                case Constants.request_horizontal_constructions: {
                    if (data.getStringExtra("count") != null) {
                        settingTypeID = 300;

                        getCapabilitiesCount();
                        //((CustomEditText_Book) findViewById(R.id.hconstruction)).setText(data.getStringExtra("count"));
                        //count++;
                        break;
                    }
                }

                case Constants.request_architectural: {
                    if (data.getStringExtra("count") != null) {
                        settingTypeID = 200;
                        getCapabilitiesCount();
                        //((CustomEditText_Book) findViewById(R.id.Architectural)).setText(data.getStringExtra("count"));
                        //count++;
                        break;
                    }
                }

                case Constants.request_envoirnmental: {
                    if (data.getStringExtra("count") != null) {
                        settingTypeID = 400;
                        getCapabilitiesCount();
                        //((CustomEditText_Book) findViewById(R.id.Enviornmental)).setText(data.getStringExtra("count"));
                        //count++;
                        break;
                    }
                }

                case Constants.request_facilities: {
                    if (data.getStringExtra("count") != null) {
                        settingTypeID = 600;
                        getCapabilitiesCount();
                        //((CustomEditText_Book) findViewById(R.id.Maintainance)).setText(data.getStringExtra("count"));
                        //count++;
                        break;
                    }
                }

                case Constants.request_generalmaintainance: {
                    if (data.getStringExtra("count") != null) {
                        settingTypeID = 500;
                        getCapabilitiesCount();
                        //((CustomEditText_Book) findViewById(R.id.General_maintenance)).setText(data.getStringExtra("count"));
                        //count++;
                        break;
                    }
                }

                case Constants.request_security: {
                    if (data.getStringExtra("count") != null) {
                        settingTypeID = 700;
                        getCapabilitiesCount();
                        //((CustomEditText_Book) findViewById(R.id.security)).setText(data.getStringExtra("count"));
                        //count++;
                        break;
                    }
                }

                case Constants.request_IT: {
                    if (data.getStringExtra("count") != null) {
                        settingTypeID = 800;
                        getCapabilitiesCount();
                        //((CustomEditText_Book) findViewById(R.id.Information_technology)).setText(data.getStringExtra("count"));
                        //count++;
                        break;
                    }
                }

                case Constants.request_human: {
                    if (data.getStringExtra("count") != null) {
                        settingTypeID = 900;
                        getCapabilitiesCount();
                        //((CustomEditText_Book) findViewById(R.id.humanservices)).setText(data.getStringExtra("count"));
                        //count++;
                        break;
                    }
                }

                case Constants.request_others: {
                    if (data.getStringExtra("count") != null) {
                        settingTypeID = 1000;
                        getCapabilitiesCount();
                        //((CustomEditText_Book) findViewById(R.id.others)).setText(data.getStringExtra("count"));
                        //count++;
                        break;
                    }
                }
            }
        }
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        Intent j = new Intent(CapablitiesActivity.this, CapabilitiesService.class);
        j.putExtra("Userid", utils.getdata("Userid"));
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            startForegroundService(j);
        }else {
            startService(j);
        }


        if(getIntent().getStringExtra("status1")!=null){
            startActivity(new Intent(this,SettingsActivity.class));
        }else
            finish();

    }


    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(CapablitiesActivity.this);
                pb.setMessage(message);
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

    @Override
    protected void onResume() {
        super.onResume();
        for(int i=100;i<1100;i+=100)
        getCapabilitiesCount1(i);
    }
}
