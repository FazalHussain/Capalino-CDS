package com.MWBE.Connects.NY.CapalinoServices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.Storage.Storage;

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
public class CapabilitiesService extends Service {
    private Context context = this;
    private String userid;
    private int settingTypeID;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            NotificationCompat.Builder b=new NotificationCompat.Builder(this);
            startForeground(1, Utils.buildForegroundNotification(b));
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            //Bundle extras = intent.getExtras();
            //userid = extras.getString("Userid");
            Data.SettingTypeID_capab.clear();
            getSettingTypeID();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception e){
            e.printStackTrace();
            }

        return Service.START_STICKY;
    }

    private void getCapabilitiesCount() {

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
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(JSONArray s) {
                    /*if(settingTypeID==1000){
                        Data.isCompleted = true;
                    }*/
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
                                    //Data.SettingTypeID_capab.add(settingTypeID);



                                    SetupActualIDList(jsonObject);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            /*switch (settingTypeID) {
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
                            }*/

                            //init();

                        }


                    }else {
                        switch (settingTypeID){
                            case 100:{
                                Storage.count1 = 0;
                                break;
                            }

                            case 200:{
                                Storage.count2 = 0;
                                break;
                            }

                            case 300:{
                                Storage.count3 = 0;
                                break;
                            }

                            case 400:{
                                Storage.count4 = 0;
                                break;
                            }

                            case 500:{
                                Storage.count5 = 0;
                                break;
                            }

                            case 600:{
                                Storage.count6 = 0;
                                break;
                            }

                            case 700:{
                                Storage.count7 = 0;
                                break;
                            }

                            case 800:{
                                Storage.count8 = 0;
                                break;
                            }

                            case 900:{
                                Storage.count9 = 0;
                                break;
                            }

                            case 1000:{
                                Storage.count10 = 0;
                                break;
                            }
                        }
                    }
                }
            }.execute(link, "", "");
        }
    }

    public void SetupActualIDList(JSONObject jsonObject) {
        try{
            String settingTypeID = jsonObject.getString("SettingTypeID");
            Log.d("SettingTypeID", settingTypeID);
            //Data.ActualID_list_advertising.clear();

            switch (settingTypeID){
                case "100":{

                    if(Data.ActualID_list_advertising.size()>0){
                        for(int i=0;i<Data.ActualID_list_advertising.size();i++){
                            if(!Data.ActualID_list_advertising.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_advertising.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                Storage.count1++;
                            }
                        }

                    }else {

                        Storage.count1 = 1;
                        Data.ActualID_list_advertising.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }

                    break;
                }

                case "200":{
                    if(Data.ActualID_list_architectural.size()>0){
                        for(int i=0;i<Data.ActualID_list_architectural.size();i++){
                            if(!Data.ActualID_list_architectural.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_architectural.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                Storage.count2++;
                            }
                        }

                    }else {
                        Storage.count2 =1;
                        Data.ActualID_list_architectural.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }
                    break;
                }

                case "300":{

                    if(Data.ActualID_list_construction.size()>0){
                        for(int i=0;i<Data.ActualID_list_construction.size();i++){
                            if(!Data.ActualID_list_construction.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_construction.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                Storage.count3++;
                            }
                        }

                    }else {
                        Storage.count3 = 1;
                        Data.ActualID_list_construction.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }


                    break;
                }

                case "400":{

                    if(Data.ActualID_list_envoirnmental.size()>0){
                        for(int i=0;i<Data.ActualID_list_envoirnmental.size();i++){
                            if(!Data.ActualID_list_envoirnmental.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_envoirnmental.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                Storage.count4++;
                            }
                        }

                    }else {
                        Storage.count4 = 1;
                        Data.ActualID_list_envoirnmental.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }


                    break;
                }

                case "500":{

                    if(Data.ActualID_list_solidwaste.size()>0){
                        for(int i=0;i<Data.ActualID_list_solidwaste.size();i++){
                            if(!Data.ActualID_list_solidwaste.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_solidwaste.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                Storage.count5++;
                            }
                        }

                    }else {
                        Storage.count5 = 1;
                        Data.ActualID_list_solidwaste.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }

                    break;
                }

                case "600":{

                    if(Data.ActualID_list_facilities.size()>0){
                        for(int i=0;i<Data.ActualID_list_facilities.size();i++){
                            if(!Data.ActualID_list_facilities.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_facilities.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                Storage.count6++;
                            }
                        }

                    }else {
                        Storage.count6 = 1;
                        Data.ActualID_list_facilities.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }

                    break;
                }

                case "700":{

                    if(Data.ActualID_list_safety.size()>0){
                        for(int i=0;i<Data.ActualID_list_safety.size();i++){
                            if(!Data.ActualID_list_safety.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_safety.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                Storage.count7++;
                            }
                        }

                    }else {
                        Storage.count7 = 1;
                        Data.ActualID_list_safety.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }

                    break;
                }

                case "800":{

                    if(Data.ActualID_list_it.size()>0){
                        for(int i=0;i<Data.ActualID_list_it.size();i++){
                            if(!Data.ActualID_list_it.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_it.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                Storage.count8++;
                            }
                        }

                    }else {
                        Storage.count8 = 1;
                        Data.ActualID_list_it.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }

                    break;
                }

                case "900":{

                    if(Data.ActualID_list_humanservice.size()>0){
                        for(int i=0;i<Data.ActualID_list_humanservice.size();i++){
                            if(!Data.ActualID_list_humanservice.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_humanservice.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                Storage.count9++;
                            }
                        }

                    }else {
                        Storage.count9 = 1;
                        Data.ActualID_list_humanservice.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }

                    break;
                }

                case "1000":{
                    if(Data.ActualID_list_others.size()>0){
                        for(int i=0;i<Data.ActualID_list_others.size();i++){
                            if(!Data.ActualID_list_others.contains(Integer.valueOf(jsonObject.getString("ActualTagID")))){
                                Data.ActualID_list_others.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                                Storage.count10++;
                            }
                        }

                    }else {
                        Storage.count10 = 1;
                        Data.ActualID_list_others.add(Integer.valueOf(jsonObject.getString("ActualTagID")));
                    }

                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getSettingTypeID() {
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
                    getCapabilitiesCount();
                    break;
                }
            }
        }
    }








}
