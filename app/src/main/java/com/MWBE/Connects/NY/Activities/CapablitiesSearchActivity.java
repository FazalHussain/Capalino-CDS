package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.MWBE.Connects.NY.CheckBoxAdapter.ItemListAdapter;
import com.MWBE.Connects.NY.JavaBeen.ViewHolder_Agency;
import com.MWBE.Connects.NY.Storage.Storage;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.MWBE.Connects.NY.AppConstants.Constants;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.JavaBeen.ListData_Agency;
import com.MWBE.Connects.NY.R;
import com.stericson.RootTools.RootTools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CapablitiesSearchActivity extends Activity {

    private ListView lv;
    private ArrayList<ListData_Agency> list;
    private ArrayList<Boolean> list_check;
    private int count;
    private ArrayList<Integer> positionArray;
    private TextView header;
    private Context context = this;
    private int settingTypeID;
    private ArrayList<Integer> list_actualid = new ArrayList<>();
    private boolean isShow;
    private ProgressDialog pb;
    private Button updatebtn;
    private int TagID;
    private boolean allchecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capablities_search);
        init();
    }

    //BackClick Event
    public void BackClick(View view) {
        /*Intent intentback = new Intent();
        intentback.putExtra("count", count + " selected");
        SendDataBack(intentback);
        finish();*/

        ClearListByID(settingTypeID);
        clearcount(settingTypeID);
        Intent intentback = new Intent();
        intentback.putExtra("count", count + " selected");
        SendDataBack(intentback);
        finish();
    }

    private void UpdateCapalbilities() {

        final Thread update_thread = new Thread(new Runnable() {
            @Override
            public void run() {

                allchecked = checkBoolean(list_check);
                if(!allchecked) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder dialog1 = new AlertDialog.Builder(context)
                                    .setTitle("Alert!")
                                    .setMessage("Thank you for updating your your capabilities!")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ClearListByID(settingTypeID);
                                            Intent intentback = new Intent();
                                            clearcount(settingTypeID);
                                            intentback.putExtra("count", count + " selected");
                                            SendDataBack(intentback);
                                            finish();
                                        }
                                    });

                            dialog1.show();
                        }
                    });
                }

                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                Date date = new Date();
                final Utils utils = new Utils(CapablitiesSearchActivity.this);
                SparseBooleanArray checked = lv.getCheckedItemPositions();
                for(int i=0;i<lv.getAdapter().getCount();i++) {
                    if (checked.get(i)) {
                        //s = UpdateSettingTypeID(list_actualid.get(i));
                        String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addUserPreferences.php?UserID=" + utils.getdata("Userid") +
                                "&SettingTypeID=" + list.get(i).getSettingTypeID() + "&ActualTagID=" + list.get(i).getTagID() + "&AddedDateTime=" + dateformat.format(date);
                        url = url.replace(" ", "%20");

                        switch (list.get(i).getSettingTypeID()+"") {
                            case "100": {
                                Data.ActualID_list_advertising.add(list.get(i).getTagID());
                                break;
                            }

                            case "200": {
                                Data.ActualID_list_architectural.add(list.get(i).getTagID());
                                break;
                            }

                            case "300": {
                                Data.ActualID_list_construction.add(list.get(i).getTagID());
                                break;
                            }

                            case "400": {
                                Data.ActualID_list_envoirnmental.add(list.get(i).getTagID());
                                break;
                            }

                            case "500": {
                                Data.ActualID_list_solidwaste.add(list.get(i).getTagID());
                                break;
                            }

                            case "600": {
                                Data.ActualID_list_facilities.add(list.get(i).getTagID());
                                break;
                            }

                            case "700": {
                                Data.ActualID_list_safety.add(list.get(i).getTagID());
                                break;
                            }

                            case "800": {
                                Data.ActualID_list_it.add(list.get(i).getTagID());
                                break;
                            }

                            case "900": {
                                Data.ActualID_list_humanservice.add(list.get(i).getTagID());
                                break;
                            }

                            case "1000": {
                                Data.ActualID_list_others.add(list.get(i).getTagID());
                                break;
                            }

                        }

                        //SetupList(list_actualid.get(i));
                        final int finalI = i;
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equalsIgnoreCase("Records Added.")) {
                                    hidePB();
                                    if (!isShow) {
                                        isShow = true;
                                        new AlertDialog.Builder(context)
                                                .setTitle("Alert!")
                                                .setMessage("Thank you for updating your capabilities!")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        ClearListByID(settingTypeID);

                                                        Intent intentback = new Intent();
                                                            intentback.putExtra("count", count + " selected");
                                                        SendDataBack(intentback);
                                                        finish();
                                                    }
                                                })
                                                .setCancelable(false)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }

                                } else {
                                    hidePB();
                                    new AlertDialog.Builder(CapablitiesSearchActivity.this)
                                            .setTitle("Alert!")
                                            .setMessage("Profile not updated, please try again.")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                hidePB();
                                error.printStackTrace();
                                new AlertDialog.Builder(CapablitiesSearchActivity.this)
                                        .setTitle("Alert!")
                                        .setMessage("Check your Internet Connection")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                            }
                        });

                        Volley.newRequestQueue(CapablitiesSearchActivity.this).add(stringRequest);
                    }



                    if (i == list_check.size() - 1) {
                        hidePB();
                        break;
                    }

                    try {
                        RootTools.debugMode = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   // Thread.yield();
                }
            }
        });

        Thread.yield();
        update_thread.start();
        //DeleteCapabilities();


    }

    private void clearcount(int settingTypeID) {
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

    private boolean checkBoolean(ArrayList<Boolean> list_check) {
        for(boolean checked : list_check){
            if(checked){
                return true;
            }
        }

        return false;
    }

    private void ClearListByID(int settingTypeID) {
        switch (settingTypeID){
            case 100:{
                Data.ActualID_list_advertising.clear();
                break;
            }

            case 200:{
                Data.ActualID_list_architectural.clear();
                break;
            }

            case 300:{
                Data.ActualID_list_construction.clear();
                break;
            }

            case 400:{
                Data.ActualID_list_envoirnmental.clear();
                break;
            }

            case 500:{
                Data.ActualID_list_solidwaste.clear();
                break;
            }

            case 600:{
                Data.ActualID_list_facilities.clear();
                break;
            }

            case 700:{
                Data.ActualID_list_safety.clear();
                break;
            }

            case 800:{
                Data.ActualID_list_it.clear();
                break;
            }

            case 900:{
                Data.ActualID_list_humanservice.clear();
                break;
            }

            case 1000:{
                Data.ActualID_list_others.clear();
                break;
            }
        }
    }

    private void SetupList(Integer integer) {
        switch (String.valueOf(settingTypeID)){

            case "100":{
                Data.ActualID_list_advertising.add(integer);
                break;
            }

            case "200":{
                Data.ActualID_list_architectural.add(integer);
                break;
            }

            case "300":{
                Data.ActualID_list_construction.add(integer);
                break;
            }

            case "400":{
                Data.ActualID_list_envoirnmental.add(integer);
                break;
            }

            case "500":{
                Data.ActualID_list_solidwaste.add(integer);
                break;
            }

            case "600":{
                Data.ActualID_list_facilities.add(integer);
                break;
            }

            case "700":{
                Data.ActualID_list_safety.add(integer);
                break;
            }

            case "800":{
                Data.ActualID_list_it.add(integer);
                break;
            }

            case "900":{
                Data.ActualID_list_humanservice.add(integer);
                break;
            }

            case "1000":{
                Data.ActualID_list_others.add(integer);
                break;
            }
        }
    }

    private void init() {
        updatebtn = (Button) findViewById(R.id.updatebtn);
        updatebtn.setEnabled(false);
        updatebtn.getBackground().setAlpha(200);
        header = (TextView) findViewById(R.id.headertext);
        if(getIntent().getStringExtra("header")!=null)
        header.setText(getIntent().getStringExtra("header"));
        lv = (ListView) findViewById(R.id.list_capabilities_search_lv);

        populatelist();
    }

    private void populatelist() {
        try {
            list = new ArrayList<ListData_Agency>();
            list_check = new ArrayList<Boolean>();


            setupList();
            positionArray = new ArrayList<>();
            list_check.clear();
            positionArray.clear();
                for (int i = 0; i < list.size();i++){
                    list_check.add(i, false);
                    positionArray.add(-1);
                }

                for (int i = 0; i < list.size(); i++) {
                    list_check.set(i, false);
                }


            /*for (int i = 0; i < list.size();i++){
                positionArray.add(-1);
            }*/

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    updatebtn.setEnabled(true);
                    updatebtn.getBackground().setAlpha(255);
                    positionArray.set(position, position);
                    list_check.set(position, !list_check.get(position));

                    switch (getIntent().getStringExtra("status")) {
                        case "Advertising": {
                            RemoveIndex(Data.ActualID_list_advertising,position);
                            break;
                        }

                        case "HVConstruction": {
                            Data.list_check_capabilities_cons = list_check;
                            RemoveIndex(Data.ActualID_list_construction,position);
                            break;
                        }

                        case "Architecture": {
                            Data.list_check_capabilities_arc = list_check;
                            RemoveIndex(Data.ActualID_list_architectural,position);
                            break;
                        }

                        case "Envoirnmental": {
                            Data.list_check_capabilities_envoirnmental = list_check;
                            RemoveIndex(Data.ActualID_list_envoirnmental,position);
                            break;
                        }

                        case "Facilities": {
                            Data.list_check_capabilities_facility = list_check;
                            RemoveIndex(Data.ActualID_list_facilities,position);
                            break;
                        }

                        case "GeneralMaintainance": {
                            Data.list_check_capabilities_adv = list_check;
                            RemoveIndex(Data.ActualID_list_solidwaste,position);
                            break;
                        }

                        case "Security": {
                            Data.list_check_capabilities_safety = list_check;
                            RemoveIndex(Data.ActualID_list_safety,position);
                            break;
                        }

                        case "IT": {
                            Data.list_check_capabilities_it = list_check;
                            RemoveIndex(Data.ActualID_list_it,position);
                            break;
                        }

                        case "humanservices": {
                            Data.list_check_capabilities_hservices = list_check;
                            RemoveIndex(Data.ActualID_list_humanservice,position);
                            break;
                        }

                        case "Others": {
                            Data.list_check_capabilities_others = list_check;
                            RemoveIndex(Data.ActualID_list_others,position);
                            break;
                        }

                    }

                }
            });

            /*lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        updatebtn.setEnabled(true);
                        updatebtn.getBackground().setAlpha(255);
                        positionArray.set(position, position);
                        list_check.set(position, !list_check.get(position));

                        switch (getIntent().getStringExtra("status")) {
                            case "Advertising": {
                                Data.list_check_capabilities_adv = list_check;
                                RemoveIndex(Data.ActualID_list_advertising,position);
                                break;
                            }

                            case "HVConstruction": {
                                Data.list_check_capabilities_cons = list_check;
                                RemoveIndex(Data.ActualID_list_construction,position);
                                break;
                            }

                            case "Architecture": {
                                Data.list_check_capabilities_arc = list_check;
                                RemoveIndex(Data.ActualID_list_architectural,position);
                                break;
                            }

                            case "Envoirnmental": {
                                Data.list_check_capabilities_envoirnmental = list_check;
                                RemoveIndex(Data.ActualID_list_envoirnmental,position);
                                break;
                            }

                            case "Facilities": {
                                Data.list_check_capabilities_facility = list_check;
                                RemoveIndex(Data.ActualID_list_facilities,position);
                                break;
                            }

                            case "GeneralMaintainance": {
                                Data.list_check_capabilities_adv = list_check;
                                RemoveIndex(Data.ActualID_list_solidwaste,position);
                                break;
                            }

                            case "Security": {
                                Data.list_check_capabilities_safety = list_check;
                                RemoveIndex(Data.ActualID_list_safety,position);
                                break;
                            }

                            case "IT": {
                                Data.list_check_capabilities_it = list_check;
                                RemoveIndex(Data.ActualID_list_it,position);
                                break;
                            }

                            case "humanservices": {
                                Data.list_check_capabilities_hservices = list_check;
                                RemoveIndex(Data.ActualID_list_humanservice,position);
                                break;
                            }

                            case "Others": {
                                Data.list_check_capabilities_others = list_check;
                                RemoveIndex(Data.ActualID_list_others,position);
                                break;
                            }

                        }

                        //finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });*/

            //CustomListAdapter adapter = new CustomListAdapter(CapablitiesSearchActivity.this, R.layout.activity_agency, list);
            //lv.setAdapter(adapter);



            ItemListAdapter adapter = new ItemListAdapter(this,list,list_check);
            lv.setItemsCanFocus(false);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lv.setAdapter(adapter);


            switch (getIntent().getStringExtra("status")) {
                case "Advertising": {
                    if (Data.ActualID_list_advertising.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < Data.ActualID_list_advertising.size(); j++) {
                                if (list.get(i).getTagID() == Data.ActualID_list_advertising.get(j)) {
                                    lv.setItemChecked(i, true);
                                }

                            }
                        }
                    }
                    break;
                }

                case "Architecture": {
                    if (Data.ActualID_list_architectural.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < Data.ActualID_list_architectural.size(); j++) {
                                if (list.get(i).getTagID() == Data.ActualID_list_architectural.get(j)) {
                                    lv.setItemChecked(i, true);
                                    //Data.list_check_capabilities_cons.set(i, true);
                                }

                            }
                        }
                    }
                    break;
                }

                case "HVConstruction": {
                    if (Data.ActualID_list_construction.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < Data.ActualID_list_construction.size(); j++) {
                                if (list.get(i).getTagID() == Data.ActualID_list_construction.get(j)) {
                                    lv.setItemChecked(i, true);
                                    //Data.list_check_capabilities_cons.set(i, true);
                                }

                            }
                        }
                    }
                    break;
                }

                case "Envoirnmental": {
                    if (Data.ActualID_list_envoirnmental.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < Data.ActualID_list_envoirnmental.size(); j++) {
                                if (list.get(i).getTagID() == Data.ActualID_list_envoirnmental.get(j)) {
                                    lv.setItemChecked(i, true);
                                    //Data.list_check_capabilities_cons.set(i, true);
                                }

                            }
                        }
                    }
                    break;
                }

                case "Facilities": {
                    if (Data.ActualID_list_facilities.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < Data.ActualID_list_facilities.size(); j++) {
                                if (list.get(i).getTagID() == Data.ActualID_list_facilities.get(j)) {
                                    lv.setItemChecked(i, true);
                                    //Data.list_check_capabilities_cons.set(i, true);
                                }

                            }
                        }
                    }
                    break;
                }

                case "GeneralMaintainance": {
                    if (Data.ActualID_list_solidwaste.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < Data.ActualID_list_solidwaste.size(); j++) {
                                if (list.get(i).getTagID() == Data.ActualID_list_solidwaste.get(j)) {
                                    lv.setItemChecked(i, true);
                                    //Data.list_check_capabilities_cons.set(i, true);
                                }

                            }
                        }
                    }
                    break;
                }

                case "Security": {
                    if (Data.ActualID_list_safety.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < Data.ActualID_list_safety.size(); j++) {
                                if (list.get(i).getTagID() == Data.ActualID_list_safety.get(j)) {
                                    lv.setItemChecked(i, true);
                                    //Data.list_check_capabilities_cons.set(i, true);
                                }

                            }
                        }
                    }
                    break;
                }

                case "IT": {
                    if (Data.ActualID_list_it.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < Data.ActualID_list_it.size(); j++) {
                                if (list.get(i).getTagID() == Data.ActualID_list_it.get(j)) {
                                    lv.setItemChecked(i, true);
                                    //Data.list_check_capabilities_cons.set(i, true);
                                }

                            }
                        }
                    }
                    break;
                }

                case "humanservices": {
                    if (Data.ActualID_list_humanservice.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < Data.ActualID_list_humanservice.size(); j++) {
                                if (list.get(i).getTagID() == Data.ActualID_list_humanservice.get(j)) {
                                    lv.setItemChecked(i, true);
                                    //Data.list_check_capabilities_cons.set(i, true);
                                }

                            }
                        }
                    }
                    break;
                }

                case "Others": {
                    if (Data.ActualID_list_others.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < Data.ActualID_list_others.size(); j++) {
                                if (list.get(i).getTagID() == Data.ActualID_list_others.get(j)) {
                                    lv.setItemChecked(i, true);
                                    //Data.list_check_capabilities_cons.set(i, true);
                                }

                            }
                        }
                    }
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDataID(int position) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        try{
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();

            Cursor cursor = dataBaseHelper.getDataFromDB("TagTitle", list.get(position).getTitle(), "GeopraphyTags", true);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    TagID = cursor.getInt(0);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void RemoveIndex(ArrayList<Integer> list,int position) {
        try {
            if (!list_check.get(position) && list.size() > 0) {
                getDataID(position);
                list.remove(TagID);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setupList() {
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(CapablitiesSearchActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();

            switch (getIntent().getStringExtra("status")) {
                case "Advertising": {
                    list_check = new ArrayList<>(Data.list_check_capabilities_adv);
                    Cursor cursor = dataBaseHelper.getDataFromQuery("Select * from " +
                            "CapabilitiesMaster ORDER BY TagValueTitle");
                    if (cursor.getCount() > 0) {
                        Data.ActualID_list_advertising_db.clear();
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 100) {
                                settingTypeID = 100;
                                list.add(new ListData_Agency(cursor.getString(3), settingTypeID,
                                        cursor.getInt(2)));
                                Data.ActualID_list_advertising_db.add(cursor.getInt(2));
                            }
                            list_actualid.add(cursor.getInt(2));
                        }
                    }
                    break;
                }

                case "HVConstruction": {
                    Cursor cursor = dataBaseHelper.getDataFromQuery("Select * from " +
                            "CapabilitiesMaster ORDER BY TagValueTitle");
                    if (cursor.getCount() > 0) {
                        Data.ActualID_list_construction_db.clear();
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 300) {
                                settingTypeID = 300;
                                list.add(new ListData_Agency(cursor.getString(3), settingTypeID,
                                        cursor.getInt(2)));
                                Data.ActualID_list_construction_db.add(cursor.getInt(2));
                            }
                            list_actualid.add(cursor.getInt(2));
                        }
                    }

                    break;
                }

                case "Architecture": {
                    Cursor cursor = dataBaseHelper.getDataFromQuery("Select * from " +
                            "CapabilitiesMaster ORDER BY TagValueTitle");
                    if (cursor.getCount() > 0) {
                        Data.ActualID_list_architectural_db.clear();
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 200) {
                                settingTypeID = 200;
                                list.add(new ListData_Agency(cursor.getString(3), settingTypeID,
                                        cursor.getInt(2)));
                                Data.ActualID_list_architectural_db.add(cursor.getInt(2));
                            }
                            list_actualid.add(cursor.getInt(2));
                        }
                    }
                    break;
                }

                case "Envoirnmental": {
                    Cursor cursor = dataBaseHelper.getDataFromQuery("Select * from " +
                            "CapabilitiesMaster ORDER BY TagValueTitle");
                    if (cursor.getCount() > 0) {
                        Data.ActualID_list_envoirnmental_db.clear();
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 400){
                                settingTypeID = 400;
                                list.add(new ListData_Agency(cursor.getString(3), settingTypeID,
                                        cursor.getInt(2)));
                                Data.ActualID_list_envoirnmental_db.add(cursor.getInt(2));

                            }
                            list_actualid.add(cursor.getInt(2));
                        }
                    }
                    break;
                }

                case "Facilities": {
                    Cursor cursor = dataBaseHelper.getDataFromQuery("Select * from " +
                            "CapabilitiesMaster ORDER BY TagValueTitle");
                    if (cursor.getCount() > 0) {
                        Data.ActualID_list_facilities_db.clear();
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 600){
                                settingTypeID = 600;
                                list.add(new ListData_Agency(cursor.getString(3), settingTypeID,
                                        cursor.getInt(2)));
                                Data.ActualID_list_facilities_db.add(cursor.getInt(2));
                            }

                            list_actualid.add(cursor.getInt(2));
                        }
                    }
                    break;
                }

                case "GeneralMaintainance": {
                    Cursor cursor = dataBaseHelper.getDataFromQuery("Select * from " +
                            "CapabilitiesMaster ORDER BY TagValueTitle");
                    if (cursor.getCount() > 0) {
                        Data.ActualID_list_solidwaste_db.clear();
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 500){
                                settingTypeID = 500;
                                list.add(new ListData_Agency(cursor.getString(3), settingTypeID,
                                        cursor.getInt(2)));
                                Data.ActualID_list_solidwaste_db.add(cursor.getInt(2));
                            }
                            list_actualid.add(cursor.getInt(2));
                        }
                    }



                    break;
                }

                case "Security": {
                    Cursor cursor = dataBaseHelper.getDataFromQuery("Select * from " +
                            "CapabilitiesMaster ORDER BY TagValueTitle");
                    if (cursor.getCount() > 0) {
                        Data.ActualID_list_safety_db.clear();
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 700){
                                settingTypeID = 700;
                                list.add(new ListData_Agency(cursor.getString(3), settingTypeID,
                                        cursor.getInt(2)));
                                Data.ActualID_list_safety_db.add(cursor.getInt(2));
                            }

                            list_actualid.add(cursor.getInt(2));
                        }
                    }
                    break;
                }

                case "IT": {
                    Cursor cursor = dataBaseHelper.getDataFromQuery("Select * from " +
                            "CapabilitiesMaster ORDER BY TagValueTitle");
                    if (cursor.getCount() > 0) {
                        Data.ActualID_list_it_db.clear();
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 800){
                                settingTypeID = 800;
                                list.add(new ListData_Agency(cursor.getString(3), settingTypeID,
                                        cursor.getInt(2)));
                                Data.ActualID_list_it_db.add(cursor.getInt(2));
                            }

                            list_actualid.add(cursor.getInt(2));
                        }
                    }
                    break;
                }

                case "humanservices": {
                    Cursor cursor = dataBaseHelper.getDataFromQuery("Select * from " +
                            "CapabilitiesMaster ORDER BY TagValueTitle");
                    if (cursor.getCount() > 0) {
                        Data.ActualID_list_humanservice_db.clear();
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 900){
                                settingTypeID = 900;
                                list.add(new ListData_Agency(cursor.getString(3), settingTypeID,
                                        cursor.getInt(2)));
                                Data.ActualID_list_humanservice_db.add(cursor.getInt(2));
                            }

                            list_actualid.add(cursor.getInt(2));
                        }
                    }
                    break;
                }

                case "Others": {
                    Cursor cursor = dataBaseHelper.getDataFromQuery("Select * from " +
                            "CapabilitiesMaster ORDER BY TagValueTitle");
                    if (cursor.getCount() > 0) {
                        Data.ActualID_list_others_db.clear();
                        while (cursor.moveToNext()) {
                            if (cursor.getInt(1) == 1000)
                            {
                                settingTypeID = 1000;
                                list.add(new ListData_Agency(cursor.getString(3), settingTypeID,
                                        cursor.getInt(2)));
                                Data.ActualID_list_others_db.add(cursor.getInt(2));
                            }

                            list_actualid.add(cursor.getInt(2));
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateClick(View view) {
        showPB("Loading...");
        DeleteCapabilities();

        count = 0;
        for (int i = 0; i < list_check.size(); i++) {
            if (list_check.get(i)) {
                count++;
            }
        }

    }

    private void DeleteCapabilities() {
        Utils utils = new Utils(CapablitiesSearchActivity.this);
            if(settingTypeID>0) {
                String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/deleteUserCababilities.php?UserID=" + utils.getdata("Userid") +
                        "&SettingTypeID=" + settingTypeID;
                url = url.replace(" ", "%20");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("Record Deleted.")) {
                            UpdateCapalbilities();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePB();
                        error.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    }
                });

                Volley.newRequestQueue(CapablitiesSearchActivity.this).add(stringRequest);
            }
        }



    private void SendDataBack(Intent intentback) {
        try {
            switch (getIntent().getStringExtra("status")) {
                case "Advertising": {
                    //intentback.putExtra("status", "adv");
                    setResult(Constants.request_advertising, intentback);
                    break;
                }

                case "HVConstruction": {
                    //intentback.putExtra("HConstructionvalues", list.get(position).getTitle());
                    setResult(Constants.request_horizontal_constructions, intentback);
                    break;
                }

                case "Architecture": {
                    //intentback.putExtra("Architecturevalues", list.get(position).getTitle());
                    setResult(Constants.request_architectural, intentback);
                    break;
                }

                case "VConstruction": {
                    //intentback.putExtra("VConstructionvalues", list.get(position).getTitle());
                    setResult(Constants.request_vertical_constructions, intentback);
                    break;
                }

                case "Envoirnmental": {
                    //intentback.putExtra("Envoirnmentalvalues", list.get(position).getTitle());
                    setResult(Constants.request_envoirnmental, intentback);
                    break;
                }

                case "Facilities": {
                    //intentback.putExtra("Facilitiesvalues", list.get(position).getTitle());
                    setResult(Constants.request_facilities, intentback);
                    break;
                }

                case "GeneralMaintainance": {
                    //intentback.putExtra("GeneralMaintainancevalues", list.get(position).getTitle());
                    setResult(Constants.request_generalmaintainance, intentback);
                    break;
                }

                case "Security": {
                    //intentback.putExtra("Securityvalues", list.get(position).getTitle());
                    setResult(Constants.request_security, intentback);
                    break;
                }

                case "IT": {
                    //intentback.putExtra("ITvalues", list.get(position).getTitle());
                    setResult(Constants.request_IT, intentback);
                    break;
                }

                case "humanservices": {
                    //intentback.putExtra("Securityvalues", list.get(position).getTitle());
                    setResult(Constants.request_human, intentback);
                    break;
                }

                case "Others": {
                    //intentback.putExtra("ITvalues", list.get(position).getTitle());
                    setResult(Constants.request_others, intentback);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class CustomListAdapter extends ArrayAdapter<ListData_Agency> {

        public CustomListAdapter(Context context, int resource, List<ListData_Agency> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {

                ViewHolder_Agency viewHolder;
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_agency_row, null);
                    viewHolder = new ViewHolder_Agency(convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder_Agency) convertView.getTag();
                }

                ListData_Agency data = getItem(position);
                viewHolder.title.setText(data.getTitle());

                if (positionArray.get(position) != -1) {
                    if (!list_check.get(position)) {
                        positionArray.set(position, -1);
                    } else
                        list_check.set(position, true);
                }


                if (list_check.get(position) == true) {
                    viewHolder.checkbox.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.checkbox.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    //Footer tab Click Listener
    public void HomeClick(View view) {
        try {
            Intent i = new Intent(CapablitiesSearchActivity.this, HomeActivity.class);
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
            Intent i = new Intent(CapablitiesSearchActivity.this, BrowseActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void ResourceClick(View view) {
        Intent i = new Intent(CapablitiesSearchActivity.this, ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view) {
        Intent i = new Intent(CapablitiesSearchActivity.this, TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        Intent intentback = new Intent();
        intentback.putExtra("count", count + " selected");
        SendDataBack(intentback);
        finish();

    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(CapablitiesSearchActivity.this);
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
