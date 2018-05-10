package com.MWBE.Connects.NY.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.MWBE.Connects.NY.Activities.HomeActivity;
import com.MWBE.Connects.NY.Activities.PdfViewActivity;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.JavaBeen.CapabilitiesMaster;
import com.MWBE.Connects.NY.JavaBeen.ViewHolder_Agency;
import com.MWBE.Connects.NY.SectionAdapter.EntryAdapter;
import com.MWBE.Connects.NY.SectionAdapter.EntryItem;
import com.MWBE.Connects.NY.SectionAdapter.Item;
import com.MWBE.Connects.NY.SectionAdapter.SectionItem;
import com.MWBE.Connects.NY.Storage.Storage;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.JavaBeen.ListData_Agency;
import com.MWBE.Connects.NY.JavaBeen.UpdateData;
import com.MWBE.Connects.NY.R;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Fazal on 7/28/2016.
 */
public class CapablitiesSetup extends Fragment {

    private ListView lv,lv1,lv2,lv3,lv4,lv5,lv6,lv7,lv8,lv9;
    private Button submitbtn;
    private Button backbtn;
    private ArrayList<Item> list;
    private ArrayList<Boolean> list_check;
    private ArrayList<Boolean> list_check_search = new ArrayList<>();
    private ArrayList<ListData_Agency> list_constructions;
    private ArrayList<ListData_Agency> list_architectural;
    private ArrayList<ListData_Agency> list_envoirnmental;
    private ArrayList<ListData_Agency> list_solidstate;
    private ArrayList<ListData_Agency> list_facilities;
    private ArrayList<ListData_Agency> list_safetysecurity;
    private ArrayList<ListData_Agency> list_it;
    private ArrayList<ListData_Agency> list_humanservice;
    private ArrayList<ListData_Agency> list_other;
    private ProgressDialog pb;
    private ArrayList<Integer> list_actualid;
    private int ss;
    private boolean isShow;
    private SimpleDateFormat dateformat;
    private Date date;
    private Utils utils;
    private boolean isone;
    private boolean istwo;
    private boolean isseven;
    ArrayList<String> overall_list = new ArrayList<>();
    private DialogInterface dialog_info,dialog_geog,dialog_target,dialog_certif,dialog_capab;
    private boolean havedata;
    private ArrayList<Integer> list_actualid_others = new ArrayList<>();
    private ArrayList<String> list_safetysecurity_title;
    private ToggleButton toggleOnOff;
    private boolean toggleOpen;
    private int ActualId,CapTagID;
    private String title;
    private View rootview;
    private ArrayList<String> searchlist;
    private String TagID;
    private String expiry;
    private Context context = getActivity();
    private String customerid;
    private UpdateData updateData;
    private CustomListAdapter adapter_search;
    private boolean isallcheck;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.capabilities_setup_fragment,container,false);
        init(rootview);
        SubmitClick();
        BackClick(rootview);
        return rootview;
    }

    private void init(View rootview) {
       // getDataFromServer();

        utils = new Utils(getActivity());
        lv = (ListView) rootview.findViewById(R.id.list_certification);
        submitbtn = (Button) rootview.findViewById(R.id.submit);
        backbtn = (Button) rootview.findViewById(R.id.back);
        SetupActualid();
        populatelist();
        setupToggleBtn(rootview);

    }

    private void getDataFromServer() {
        try{
            new AsyncTask<String,Void,Void>(){

                @Override
                protected Void doInBackground(String... params) {
                    try {
                        int count = 0;
                        DataBaseHelper db = new DataBaseHelper(getActivity());
                        db.createDataBase();
                        db.openDataBase();
                        Cursor cursor = db.getDataFromQuery("Select count(TagValueTitle) as TotalRecords from CapabilitiesMaster");
                        if(cursor.getCount()>0){
                            while (cursor.moveToNext()){
                                count = cursor.getInt(0);
                            }
                        }
                        System.setProperty("http.keepAlive", "false");
                        HttpClient httpclient1 = new DefaultHttpClient();



                        String link = params[0] +count;
                        link = link.replace(" ","%20");
                        HttpGet httppost = new HttpGet(link);



                        ResponseHandler<String> responseHandler = new BasicResponseHandler();

                        String response = httpclient1.execute(httppost,
                                responseHandler);


                        Log.d("Response", "Response : " + response);
                        if(!response.contains("Both are equal")) {
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

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        if (isinserted) {
                                            //Toast.makeText(HomeActivity.this, "Hello", Toast.LENGTH_LONG).show();
                                            Log.d("CapabilitiesMaster", "Added");
                                        }
                                    }
                                });
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    populatelist();
                                }
                            });
                            hidePB();
                        }else {
                            hidePB();
                        }
                    } catch (Exception e) {
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

    private void setupToggleBtn(final View view){
        try{
            toggleOnOff = (ToggleButton) view.findViewById(R.id.your_awesome_toggle);

            toggleOnOff.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (toggleOnOff.isChecked()) {
                        toggleOpen = true;
                        ((TextView)rootview.findViewById(R.id.text_hd1)).setVisibility(View.GONE);
                        ((TextView)rootview.findViewById(R.id.text_hd2)).setVisibility(View.GONE);
                        ((EditText)rootview.findViewById(R.id.search_edit_text)).setVisibility(View.VISIBLE);

                        final EditText edit_search = (EditText) rootview.findViewById(R.id.search_edit_text);
                        edit_search.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                if(s.length()>0 && s==edit_search.getEditableText()){
                                    adapter_search = (CustomListAdapter) lv.getAdapter();
                                    adapter_search.getFilter().filter(s);
                                    adapter_search.notifyDataSetChanged();



                                    /*
                                    if(searchlist.size()>0) {
                                        CustomListAdapter adapter_overall = new CustomListAdapter(getActivity(), R.layout.activity_setup, searchlist);
                                        lv.setAdapter(adapter_overall);
                                    }*/
                                    //adapter.notifyDataSetChanged();
                                }else {
                                    CustomListAdapter adapter_overall = new CustomListAdapter(getActivity(), R.layout.activity_setup, overall_list);
                                    lv.setAdapter(adapter_overall);
                                    adapter_search = (CustomListAdapter) lv.getAdapter();
                                    adapter_search.getFilter().filter(s);
                                    adapter_search.notifyDataSetChanged();
                                }
                            }
                        });
                        overall_list.clear();
                        for(int i=0;i<list_check.size();i++){
                            list_check.set(i,false);
                        }
                    } else {
                       toggleOpen = false;
                        ((TextView)rootview.findViewById(R.id.text_hd1)).setVisibility(View.VISIBLE);
                        ((TextView)rootview.findViewById(R.id.text_hd2)).setVisibility(View.VISIBLE);
                        ((EditText)rootview.findViewById(R.id.search_edit_text)).setVisibility(View.GONE);
                        for(int i=0;i<list_check.size();i++){
                            list_check.set(i,false);
                        }
                        overall_list.clear();
                    }

                    if(toggleOpen) {
                        toggleOnOff.setChecked(true);

                        for(Item item : list){
                            if(!item.isSection()){
                                EntryItem entryItem = (EntryItem) item;

                                try{
                                     DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
                                     dataBaseHelper.createDataBase();
                                     dataBaseHelper.openDataBase();

                                    if(entryItem.getTitle().equalsIgnoreCase("General Supplies")) {

                                        Cursor cursor = dataBaseHelper.getDataFromDB("TagValueTitle", entryItem.getTitle(), "CapabilitiesMaster", true);
                                        if (cursor.getCount() > 0) {
                                            while (cursor.moveToNext()) {
                                                switch (cursor.getInt(1)) {
                                                    case 100: {
                                                        if(!overall_list.contains(entryItem.getTitle() + " - Advertising"))
                                                        overall_list.add(entryItem.getTitle() + " - Advertising");
                                                        break;
                                                    }

                                                    case 200: {
                                                        if(!overall_list.contains(entryItem.getTitle() + " - Architectural"))
                                                        overall_list.add(entryItem.getTitle() + " - Architectural");
                                                        break;
                                                    }

                                                    case 300: {
                                                        if(!overall_list.contains(entryItem.getTitle() + " - Construction"))
                                                        overall_list.add(entryItem.getTitle() + " - Construction");
                                                        break;
                                                    }

                                                    case 400: {
                                                        if(!overall_list.contains(entryItem.getTitle() + " - Environmental"))
                                                        overall_list.add(entryItem.getTitle() + " - Environmental");
                                                        break;
                                                    }

                                                    case 500: {
                                                        if(!overall_list.contains(entryItem.getTitle() + " - Solid Waste"))
                                                        overall_list.add(entryItem.getTitle() + " - Solid Waste");
                                                        break;
                                                    }

                                                    case 600: {
                                                        if(!overall_list.contains(entryItem.getTitle() + " - Facilities"))
                                                        overall_list.add(entryItem.getTitle() + " - Facilities");
                                                        break;
                                                    }

                                                    case 700: {
                                                        if(!overall_list.contains(entryItem.getTitle() + " - Safety & Security"))
                                                        overall_list.add(entryItem.getTitle() + " - Safety & Security");
                                                        break;
                                                    }

                                                    case 800: {
                                                        if(!overall_list.contains(entryItem.getTitle() + " - IT"))
                                                        overall_list.add(entryItem.getTitle() + " - IT");
                                                        break;
                                                    }

                                                    case 900: {
                                                        if(!overall_list.contains(entryItem.getTitle() + " - Human Services"))
                                                        overall_list.add(entryItem.getTitle() + " - Human Services");
                                                        break;
                                                    }

                                                    case 1000: {
                                                        if(!overall_list.contains(entryItem.getTitle() + " - Others"))
                                                        overall_list.add(entryItem.getTitle() + " - Others");
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }else {
                                        overall_list.add(entryItem.getTitle());
                                    }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }


                            }
                        }

                        Collections.sort(overall_list);

                        adapter_search = new CustomListAdapter(getActivity(), R.layout.activity_setup, overall_list);
                        lv.setAdapter(adapter_search);
                    }
                    else {
                        toggleOnOff.setChecked(false);
                        EntryAdapter adapter = new EntryAdapter(getActivity(), list,list_check);
                        //listview.setAdapter(adapter);
                        //CustomListAdapter adapter_overall = new CustomListAdapter(getActivity(), R.layout.activity_setup, overall_list);

                        lv.setAdapter(adapter);
                    }

                }
            });
            //checkToggleState();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkToggleState() {
        SharedPreferences yourPrefs = getActivity().getSharedPreferences("pref", 0);
        boolean mON = yourPrefs.getBoolean("mon", true);
        if(mON) {
            toggleOnOff.setChecked(true);
        }
        else {
            toggleOnOff.setChecked(false);
        }
    }

    private void SetupActualid() {
        for(int i=1001;i<=1009;i++){
            list_actualid_others.add(i);
        }
    }

    private void populatelist() {
        try {
            list = new ArrayList<Item>();
            list_architectural = new ArrayList<ListData_Agency>();
            list_constructions = new ArrayList<ListData_Agency>();
            list_envoirnmental = new ArrayList<ListData_Agency>();
            list_solidstate = new ArrayList<ListData_Agency>();
            list_facilities = new ArrayList<ListData_Agency>();
            list_safetysecurity = new ArrayList<ListData_Agency>();
            list_it = new ArrayList<ListData_Agency>();
            list_humanservice = new ArrayList<ListData_Agency>();
            list_other = new ArrayList<ListData_Agency>();
            list_check = new ArrayList<Boolean>();

            DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            list_actualid = new ArrayList<>();
            Cursor cursor = dataBaseHelper.getDataFromQuery("Select * from CapabilitiesMaster ORDER BY CapTagID");
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    setupData(cursor.getInt(1),cursor);
                    list_actualid.add(cursor.getInt(2));
                }
            }

            list_safetysecurity_title = new ArrayList<String>();
            for(ListData_Agency item : list_safetysecurity){
                list_safetysecurity_title.add(item.getTitle());
            }




            //getTotalListData(cursor);

            for (int i = 0; i < list.size(); i++) {
                    list_check.add(false);
                    list_check_search.add(false);

                Storage.list_check_architecture.add(i, false);
                Storage.list_check_advertising.add(i,false);
                Storage.list_check_construction.add(i,false);
                Storage.list_check_solid_state.add(i,false);
                Storage.list_check_envoirnmental.add(i,false);
                Storage.list_check_facility.add(i,false);
                Storage.list_check_safety.add(i,false);
                Storage.list_check_it.add(i,false);
                Storage.list_check_humanservices.add(i,false);
                Storage.list_check_others.add(i, false);
            }

            SortList();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        havedata = !list_check.get(position);

                        Storage.list_check_capab = list_check;

                        if(toggleOpen){
                            if(adapter_search.getCount()>0){
                                for(int i=0;i<overall_list.size();i++){
                                    if(overall_list.get(i).equalsIgnoreCase(adapter_search.getItem(position))){
                                        list_check_search.set(i, !list_check_search.get(i));

                                    }
                                }
                            }else {
                                list_check_search.set(position, !list_check_search.get(position));
                            }

                            //list_check_search.set(position, !list_check.get(position));
                            //Log.d("title", adapter_search.getItem(position));
                        }else {
                            if(!list.get(position).isSection()){
                                //EntryItem entryItem = (EntryItem)list.get(position);
                                list_check.set(position, !list_check.get(position));
                            }
                        }




                        //SetUpCheckedList(position);
                        int currentPosition = lv.getFirstVisiblePosition();
                        //CustomListAdapter adapter = new CustomListAdapter(getActivity(), R.layout.activity_setup, list);
                        if(toggleOpen){

                            if(((EditText)rootview.findViewById(R.id.search_edit_text)).getText().length()>0){
                                searchlist = new ArrayList<String>();
                                for(int i = 0;i<adapter_search.getCount();i++){
                                    searchlist.add(adapter_search.getItem(i));
                                }
                                CustomListAdapter adapter_overall = new CustomListAdapter(getActivity(), R.layout.activity_setup, searchlist);
                                lv.setAdapter(adapter_overall);
                            }else {
                                CustomListAdapter adapter_overall = new CustomListAdapter(getActivity(), R.layout.activity_setup, overall_list);
                                lv.setAdapter(adapter_overall);
                            }



                        }else {
                            EntryAdapter adapter = new EntryAdapter(getActivity(), list,list_check);
                            lv.setAdapter(adapter);

                        }

                        lv.setSelection(currentPosition);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            if(Storage.list_check_capab.size()>0) {
                list_check = Storage.list_check_capab;
            }



            EntryAdapter adapter = new EntryAdapter(getActivity(), list,list_check);
            //listview.setAdapter(adapter);
            //CustomListAdapter adapter_overall = new CustomListAdapter(getActivity(), R.layout.activity_setup, overall_list);

            lv.setAdapter(adapter);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SortList() {

        HashSet<Item> stringSet = new HashSet<Item>();



        Map<String, Item> map = new LinkedHashMap<>();
        for (Item ays : list) {
            if(ays.isSection()) {
                SectionItem sectionItem = (SectionItem) ays;
                map.put(sectionItem.getTitle(), ays);
            }else {
                GeneralSuppliesSort(ays,map);
            }

        }
        list.clear();
        list.addAll(map.values());

        /*EntryAdapter adapter = new EntryAdapter(getActivity(), list,list_check);
        lv.setAdapter(adapter);*/
    }

    private void GeneralSuppliesSort(Item ays, Map<String, Item> map) {
        int i =0;
        EntryItem entryItem = (EntryItem) ays;
        if(entryItem.getTitle().contains("General Supplies")){
            if(entryItem.getTagvalueID()<200){
                map.put("General Supplies Adv",ays);
            }

           else if(entryItem.getTagvalueID()<300){
                map.put("General Supplies Arch",ays);
            }

           else if(entryItem.getTagvalueID()<400){
                map.put("General Supplies Cons",ays);
            }

            else if(entryItem.getTagvalueID()<500){
                map.put("General Supplies Env",ays);
            }

            else if(entryItem.getTagvalueID()<600){
                map.put("General Supplies Solid",ays);
            }

            else if(entryItem.getTagvalueID()<700){
                map.put("General Supplies Facility",ays);
            }

            else if(entryItem.getTagvalueID()<800){
                map.put("General Supplies Safety",ays);
            }

            else if(entryItem.getTagvalueID()<900){
                map.put("General Supplies Human",ays);
            }

            else if(entryItem.getTagvalueID()<1000){
                map.put("General Supplies Others",ays);
            }
        }else {
            map.put(entryItem.getTagvalueID()+"",ays);
            i++;
        }

    }

    private void setupData(int code,Cursor cursor) {
        switch (code){
            case 100:{
                if(list.size()==0)
                    list.add(new SectionItem("Advertising Graph Arts & Marketing"));
                    list.add(new EntryItem(cursor.getString(3),cursor.getInt(2)));
                break;
            }

            case 200:{
                //list_architectural.add(new ListData_Agency(cursor.getString(3)));
                //if(list.size()==9)

                list.add(new SectionItem("Architectural, Engineering & Surveying"));

                list.add(new EntryItem(cursor.getString(3),cursor.getInt(2)));
                break;
            }

            case 300:{
                //list_constructions.add(new ListData_Agency(cursor.getString(3)));
                //if(list.size()==18)
                list.add(new SectionItem("Construction (Horizontal & Vertical)"));
                list.add(new EntryItem(cursor.getString(3),cursor.getInt(2)));
                break;
            }

            case 400:{
                //list_envoirnmental.add(new ListData_Agency(cursor.getString(3)));
                //if(list.size()==38)
                list.add(new SectionItem("Environmental"));
                list.add(new EntryItem(cursor.getString(3),cursor.getInt(2)));
                break;
            }

            case 500:{
                //list_solidstate.add(new ListData_Agency(cursor.getString(3)));
                //if(list.size()==45)
                list.add(new SectionItem("Solid Waste Removal"));
                list.add(new EntryItem(cursor.getString(3),cursor.getInt(2)));
                break;
            }

            case 600:{
                //list_facilities.add(new ListData_Agency(cursor.getString(3)));
                //if(list.size()==48)
                list.add(new SectionItem("Facilities Maintenance and Building Ops"));
                list.add(new EntryItem(cursor.getString(3),cursor.getInt(2)));
                break;
            }

            case 700:{
                //list_safetysecurity.add(new ListData_Agency(cursor.getString(3)));
                //if(list.size()==59)
                list.add(new SectionItem("Safety and Security"));
                list.add(new EntryItem(cursor.getString(3),cursor.getInt(2)));
                break;
            }

            case 800:{
                //list_it.add(new ListData_Agency(cursor.getString(3)));
                //if(list.size()==65)
                list.add(new SectionItem("IT"));
                list.add(new EntryItem(cursor.getString(3),cursor.getInt(2)));
                break;
            }

            case 900:{
                //list_humanservice.add(new ListData_Agency(cursor.getString(3)));
                list.add(new SectionItem("Human Services"));
                list.add(new EntryItem(cursor.getString(3),cursor.getInt(2)));
                break;
            }

            case 1000:{
                //list_other.add(new ListData_Agency(cursor.getString(3)));
                //if(list.size()==85)
                list.add(new SectionItem("Other Professional Services "));
                list.add(new EntryItem(cursor.getString(3),cursor.getInt(2)));
                break;
            }
        }
    }

    private void getTotalListData(Cursor cursor) {
        for(int i=0;i<list_architectural.size();i++){
            if(i==0){
                list.add(new SectionItem("Architectural, Engineering & Surveying"));
            }
            list.add(new EntryItem(list_architectural.get(i).getTitle(),cursor.getInt(2)));
        }

        for(int i=0;i<list_constructions.size();i++){
            if(i==0){
                list.add(new SectionItem("Construction (Horizontal & Vertical)"));
            }
            list.add(new EntryItem(list_constructions.get(i).getTitle(),cursor.getInt(2)));
        }

        for(int i=0;i<list_envoirnmental.size();i++){
            if(i==0){
                list.add(new SectionItem("Environmental"));
            }
                list.add(new EntryItem(list_envoirnmental.get(i).getTitle(),cursor.getInt(2)));
        }

        for(int i=0;i<list_solidstate.size();i++){
            if(i==0){
                list.add(new SectionItem("Solid Waste Removal"));
            }
            list.add(new EntryItem(list_solidstate.get(i).getTitle(),cursor.getInt(2)));
        }

        for(int i=0;i<list_facilities.size();i++){
            if(i==0){
                list.add(new SectionItem("Facilities Maintenance and Building Ops"));
            }
            list.add(new EntryItem(list_facilities.get(i).getTitle(),cursor.getInt(2)));
        }

        for(int i=0;i<list_safetysecurity.size();i++){
            if(i==0){
                list.add(new SectionItem("Safety and Security"));
            }
            list.add(new EntryItem(list_safetysecurity.get(i).getTitle(),cursor.getInt(2)));
        }

        for(int i=0;i<list_it.size();i++){
            if(i==0){
                list.add(new SectionItem("IT"));
            }
            list.add(new EntryItem(list_it.get(i).getTitle(),cursor.getInt(2)));
        }


        for(int i=0;i<list_humanservice.size();i++){
            if(i==0){
                list.add(new SectionItem("Human Services"));
            }
            list.add(new EntryItem(list_humanservice.get(i).getTitle(),cursor.getInt(2)));
        }

        for(int i=0;i<list_other.size();i++){
            if(i==0){
                list.add(new SectionItem("Other Professional Services "));
            }
            list.add(new EntryItem(list_other.get(i).getTitle(),cursor.getInt(2)));
        }


    }

    private void SubmitClick() {
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedata();
                if (Data.AllNYC) {
                    UpdateGeographicPref(1111);
                    Data.AllNYC = false;
                }

                UpdateGeographicPref();
                UpdateTargetContractPref();
                UpdateCertificationPref();
                SendData();
                if (isallchecked(list_check)){
                    UpdateCapalbilities1(100);
                }else {
                    UpdateStatus();
                    HidePB();
                    Intent j = new Intent(getActivity(), HomeActivity.class);
                    j.putExtra("islogin", "yes");
                    j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(j);
                }

                Storage.list_check.clear();
                Storage.list_check_target_contract.clear();
                Storage.list_check_capab.clear();
                Storage.list_check_certification.clear();

            }
        });
    }



    private boolean isallchecked(ArrayList<Boolean> list_check) {

        for (int i=0; i<list_check.size(); i++){
            if (list_check.get(i)){
                return true;
            }
        }

        return false;

    }


    private void updatedata() {
        showPB("Please wait, your profile is being updated...");
        updateData = Storage.updateData;
        String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/updateUserProfileZipCode.php?UserEmailAddress="+ updateData.getEmail() +"&UserFirstName="+updateData.getFname()+
                "&UserLastName="+updateData.getLastname()+"&UserMobilePhone="+updateData.getPhno()+"&UserAddressLine1="+updateData.getBuisnessaddress()+
                "&UserCity="+updateData.getCity()+"&UserState="+updateData.getState()+"&BusinessWebsite="+updateData.getBuisnessweb()+"&BusinessName="+updateData.getBuisnessname()+"&UserZipCode="+updateData.getZipcode();
        url = url.replace("#","%23");
        url = url.replace(" ","%20");
        url = url.replace("+","%2B");


        //url = url.replace(" ","%20");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Records Updated.")) {
                   //UpdateGeographicPref();

                } else {
                    hidePB();
                    new AlertDialog.Builder(getActivity())
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
                new AlertDialog.Builder(getActivity())
                        .setTitle("Alert!")
                        .setMessage("Check your Internet Connection")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog_info = dialog;
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return;
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void UpdateGeographicPref() {
        Utils utils = new Utils(getActivity());
        for(int i=0;i<Storage.list_check.size();i++) {
            if (Storage.list_check.get(i)) {
                if(i==0){continue;}
                getDataID(i);
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                Date date = new Date();
                String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addUserPreferences.php?UserID=" + utils.getdata("Userid") +
                        "&SettingTypeID=1&ActualTagID="+(TagID)+"&AddedDateTime="+dateformat.format(date);
                url = url.replace(" ", "%20");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePB();
                        dialog_info.dismiss();
                        error.printStackTrace();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Alert!")
                                .setMessage("Check your Internet Connection")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog_geog = dialog;
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();

                        return;
                    }
                });

                Volley.newRequestQueue(getActivity()).add(stringRequest);
            }
        }


    }

    private void getDataID(int position) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
        try{
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();

            Cursor cursor = dataBaseHelper.getDataFromDB("TagTitle",Storage.list_geographic.get(position).getTitle(),"GeopraphyTags",true);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    TagID = cursor.getString(0);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void UpdateGeographicPref(int i) {
        Utils utils = new Utils(getActivity());
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                Date date = new Date();
                String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addUserPreferences.php?UserID=" + utils.getdata("Userid") +
                        "&SettingTypeID=1&ActualTagID="+(i)+"&AddedDateTime="+dateformat.format(date);
                url = url.replace(" ", "%20");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       /* if (response.equalsIgnoreCase("Records Added.")) {
                          //UpdateTargetContractPref();

                        } else {
                            hidePB();
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Alert!")
                                    .setMessage("Profile not updated, please try again.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }*/
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePB();
                        dialog_info.dismiss();
                        error.printStackTrace();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Alert!")
                                .setMessage("Check your Internet Connection")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog_geog = dialog;
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();

                        return;
                    }
                });

                Volley.newRequestQueue(getActivity()).add(stringRequest);


    }

    private void UpdateTargetContractPref() {
        Utils utils = new Utils(getActivity());
        for(int i=0;i<Storage.list_check_target_contract.size();i++) {
            if (Storage.list_check_target_contract.get(i)) {
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                Date date = new Date();
                String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addUserPreferences.php?UserID=" + utils.getdata("Userid") +
                        "&SettingTypeID=2&ActualTagID="+(i+1)+"&AddedDateTime="+dateformat.format(date);
                url = url.replace(" ", "%20");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /*if (response.equalsIgnoreCase("Records Added.")) {
                            //UpdateCertificationPref();

                        } else {
                            hidePB();
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Alert!")
                                    .setMessage("Profile not updated, please try again.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }*/
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePB();
                        dialog_info.dismiss();
                        dialog_geog.dismiss();
                        error.printStackTrace();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Alert!")
                                .setMessage("Check your Internet Connection")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog_target = dialog;
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        return;
                        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    }
                });

                Volley.newRequestQueue(getActivity()).add(stringRequest);
            }
        }
    }

    private void UpdateCertificationPref() {

        final Utils utils = new Utils(getActivity());
        for(int i=0;i<Storage.list_check_certification.size();i++) {
            if (Storage.list_check_certification.get(i)) {
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                Date date = new Date();
                String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addUserPreferences.php?UserID=" + utils.getdata("Userid") +
                        "&SettingTypeID=3&ActualTagID="+(i+1)+"&AddedDateTime="+dateformat.format(date);
                url = url.replace(" ", "%20");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /*if (response.equalsIgnoreCase("Records Added.")) {
                            UpdateCapalbilities1(100);
                        } else {
                            hidePB();
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Alert!")
                                    .setMessage("Profile not updated, please try again.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }*/
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePB();
                        dialog_info.dismiss();
                        dialog_geog.dismiss();
                        dialog_target.dismiss();
                        error.printStackTrace();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Alert!")
                                .setMessage("Check your Internet Connection")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog_certif = dialog;
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        return;
                        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    }
                });

                Volley.newRequestQueue(getActivity()).add(stringRequest);
            }
        }
    }

    private void UpdateCapalbilities1(int s) {
        //DeleteCapabilities();
        UpdateStatus();
        dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        date = new Date();
        utils = new Utils(getActivity());

        try{
            DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            isallcheck = checkBoolean(list_check);
            if(!toggleOpen)
            if(!isallcheck){
                if (!havedata) {
                    HidePB();
                    Intent j = new Intent(getActivity(), HomeActivity.class);
                    j.putExtra("islogin", "yes");
                    j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(j);
                }
            }

            for(int i=0;i<list_check.size();i++) {
                if(overall_list.size()==0)
                if(list.get(i).isSection()){
                    list.remove(i);
                    list_check.remove(i);
                }

                if (list_check.get(i)) {
                    /*if(overall_list.size()>0) {
                        //title = overall_list.get(i);
                            EntryItem item = (EntryItem) list.get(i);
                            title =item.getTitle();
                            ActualId = item.getTagvalueID();


                    }else {
                       EntryItem entryitem = (EntryItem) list.get(i);
                        title =entryitem.getTitle();
                        ActualId = entryitem.getTagvalueID();
                    }*/

                    EntryItem entryitem = (EntryItem) list.get(i);
                    title =entryitem.getTitle();
                    ActualId = entryitem.getTagvalueID();
                        Cursor cursor = dataBaseHelper.getDataFromDB("TagValueID",ActualId,"CapabilitiesMaster",true);
                        if(cursor.getCount()>0){
                            while (cursor.moveToNext()){

                                CapTagID = cursor.getInt(1);
                                ActualId = cursor.getInt(2);

                            }
                        }

                        UpdateCapalbilities(CapTagID, ActualId);

                }else {

                    try{
                        if(toggleOpen) {
                            Log.d("IsOpen","Open");
                            if (list_check_search.get(i)) {
                                for (int j = 0; j < list.size(); j++) {
                                    if (!list.get(j).isSection()) {
                                        EntryItem entryitem = (EntryItem) list.get(j);
                                        Log.d("IsOpen","Open");
                                        setupForGenerals(entryitem);
                                        if (overall_list.get(i).equalsIgnoreCase(entryitem.getTitle())) {
                                            title = entryitem.getTitle();
                                            ActualId = entryitem.getTagvalueID();
                                            Log.d("ActualIDD", String.valueOf(ActualId));
                                            Cursor cursor = dataBaseHelper.getDataFromDB("TagValueID", ActualId, "CapabilitiesMaster", true);
                                            if (cursor.getCount() > 0) {
                                                while (cursor.moveToNext()) {

                                                    CapTagID = cursor.getInt(1);
                                                    ActualId = cursor.getInt(2);
                                                    Log.d("CapTagID", String.valueOf(CapTagID));
                                                    Log.d("ActualID", String.valueOf(ActualId));

                                                }
                                            }

                                            UpdateCapalbilities(CapTagID, ActualId);
                                        }
                                    }

                                }
                            }
                        }
                    }catch (Exception e){
                        HidePB();
                        Log.d("error",e.getMessage().toString());
                        e.printStackTrace();
                    }

                    if (!havedata) {
                        HidePB();
                        Intent j = new Intent(getActivity(), HomeActivity.class);
                        j.putExtra("islogin", "yes");
                        j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(j);
                    }
                }
            }
            }catch (Exception e){
                e.printStackTrace();
            }








        /*for(int i=0;i<list_check.size();i++) {
            if(list.get(i).isSection()){
                list.remove(i);
                list_check.remove(i);
            }
            if (list_check.get(i)) {
                //s = UpdateSettingTypeID(list_actualid.get(i));

                if (list_actualid.get(i) > 100 && list_actualid.get(i) < 200) {
                    s = 100;
                    UpdateCapalbilities(s, list_actualid.get(i));
                }

                if (list_actualid.get(i) > 200 && list_actualid.get(i) < 300) {
                    s = 200;
                    UpdateCapalbilities(s, list_actualid.get(i));

                }

                if (list_actualid.get(i) > 300 && list_actualid.get(i) < 400) {
                    s = 300;
                    UpdateCapalbilities(s, list_actualid.get(i));
                }

                if (list_actualid.get(i) > 400 && list_actualid.get(i) < 500) {
                    s = 400;
                    UpdateCapalbilities(s, list_actualid.get(i));
                }

                if (list_actualid.get(i) > 500 && list_actualid.get(i) < 600) {
                    s = 500;
                    UpdateCapalbilities(s, list_actualid.get(i));
                }

                if (list_actualid.get(i) > 600 && list_actualid.get(i) < 700) {
                    s = 600;
                    UpdateCapalbilities(s, list_actualid.get(i));
                }

                if (list_actualid.get(i) > 700 && list_actualid.get(i) < 800) {
                    s = 700;
                    UpdateCapalbilities(s, list_actualid.get(i));
                }

                if (list_actualid.get(i) > 800 && list_actualid.get(i) < 900) {
                    s = 800;
                    UpdateCapalbilities(s, list_actualid.get(i));
                }

                if (list_actualid.get(i) > 900 && list_actualid.get(i) < 1000) {
                    s = 900;
                    UpdateCapalbilities(s, list_actualid.get(i));
                }


                if (list_actualid.get(i) > 1000) {
                    s = 1000;
                    UpdateCapalbilities(s, list_actualid.get(i));
                }

            }else {
                if (!havedata) {
                    hidePB();
                    Intent j = new Intent(getActivity(), HomeActivity.class);
                    j.putExtra("islogin", "yes");
                    j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(j);
                }
            }

        }*/


    }

    private boolean checkBoolean(ArrayList<Boolean> list_check) {
        for(boolean checked : list_check){
            if(checked){
                return true;
            }
        }

        return false;
    }

    private void setupForGenerals(EntryItem entryitem) {
        try{
            Log.d("Setup","SetupForGeneral");
            if(entryitem.getTitle().contains("General Supplies")){

                if(entryitem.getTagvalueID()<200){
                    entryitem.setTitle("General Supplies  - Advertising");
                }

                else if(entryitem.getTagvalueID()<300){
                    entryitem.setTitle("General Supplies  - Architectural");
                }

                else if(entryitem.getTagvalueID()<400){
                    entryitem.setTitle("General Supplies  - Construction");
                }

                else if(entryitem.getTagvalueID()<500){
                    entryitem.setTitle("General Supplies  - Environmental");
                }

                else if(entryitem.getTagvalueID()<600){
                    entryitem.setTitle("General Supplies  - Solid Waste");
                }

                else if(entryitem.getTagvalueID()<700){
                    entryitem.setTitle("General Supplies  - Facilities");
                }

                else if(entryitem.getTagvalueID()<800){
                    entryitem.setTitle("General Supplies  - Safety & Security");
                }

                else if(entryitem.getTagvalueID()<900){
                    entryitem.setTitle("General Supplies  - IT");
                }

                else if(entryitem.getTagvalueID()<1000){
                    entryitem.setTitle("General Supplies  - Human Services");
                }

                else if(entryitem.getTagvalueID()<1100){
                    entryitem.setTitle("General Supplies  - Others");
                }



            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void UpdateStatus() {
        try{
            String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/updateSetupStatus.php?UserID=" + utils.getdata("Userid");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.contains("Record Updated")) {
                        try{
                            Log.d("StatusUpdated","yes");

                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    hidePB();
                    //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


            Volley.newRequestQueue(getActivity()).add(stringRequest);

        }catch (Exception e){
                e.printStackTrace();
            }
    }

    private void UpdateCapalbilities(int s, int actualid) {
        //DeleteCapabilities();

        String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addUserPreferences.php?UserID="+utils.getdata("Userid")+
                "&SettingTypeID="+s+"&ActualTagID="+actualid+"&AddedDateTime="+dateformat.format(date);
        url = url.replace(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Records Added.")) {
                    if(!isShow){
                        Log.d("capab","Success");
                        isShow = true;
                        WelcomeMessageSend();
                    }

                } else {
                    //hidePB();
                    Log.d("capab","Error");
                    HidePB();
                    new AlertDialog.Builder(getActivity())
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
                HidePB();
                dialog_info.dismiss();
                dialog_geog.dismiss();
                dialog_target.dismiss();
                dialog_certif.dismiss();
                error.printStackTrace();
                new AlertDialog.Builder(getActivity())
                        .setTitle("Alert!")
                        .setMessage("Check your Internet Connection")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();

                return;
            }
        });

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    public void SendData(){

        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 14);
        expiry = dateformat.format(cal.getTime());

        SimpleDateFormat dateformat1 = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date();
        customerid = utils.getdata("Userid") + "-" + dateformat1.format(date);
        String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addCustomerData.php?CustomerID="+customerid+"&FirstName="+updateData.getFname()+"&LastName="+updateData.getLastname()+
                "&Address="+updateData.getBuisnessaddress()+"&City="+updateData.getCity()+"&State="+updateData.getState()+
                "&Phone="+updateData.getPhno()+"&Email=" + utils.getdata("email") + "&UserID=" + utils.getdata("Userid") + "&PaymentStatus=Trial&PaymentDate="+dateformat.format(date)+"&ExpirationDate=" + expiry;
        url = url.replace(" ", "%20");
        url = url.replace("#", "%23");
        url = url.replace("+", "%2B");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Record Added.")) {
                    Log.d("Trial","Trial Success");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }

    private void WelcomeMessageSend() {
        try{
            String url = "http://hivelet.com/welcomeEmailCapalino.php?firstName="+utils.getdata("fname")+"&toemail="+utils.getdata("email");
            url = url.replace(" ", "%20");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("Success")) {
                            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                                    .setTitle("Alert!")
                                    .setMessage("Congratulations! You are now ready to use the MWBE Connect NY.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent(getActivity(), HomeActivity.class);
                                            i.putExtra("islogin", "yes");
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(i);
                                        }
                                    })
                                    .setCancelable(false)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    try{
                        HidePB();
                        dialog_info.dismiss();
                        dialog_geog.dismiss();
                        dialog_target.dismiss();
                        dialog_certif.dismiss();
                        error.printStackTrace();
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Alert!")
                                .setMessage("Check your Internet Connection")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    return;
                }
            });

            Volley.newRequestQueue(getActivity()).add(stringRequest);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void BackClick(View rootview) {
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.setup, new CertificationSetup()).commit();
            }
        });
    }

    public class CustomListAdapter extends ArrayAdapter<String> {

        public CustomListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {

                ViewHolder_Agency viewHolder;
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.list_agency_row, null);
                    viewHolder = new ViewHolder_Agency(convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder_Agency) convertView.getTag();
                }

                String data = getItem(position);
                viewHolder.title.setText(data);


                if (list_check_search.get(position)) {
                    viewHolder.checkbox.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.checkbox.setVisibility(View.GONE);
                }

                if(adapter_search.getCount()>0){
                    for(int i=0;i<list_check_search.size();i++){
                        if(list_check_search.get(i)) {
                            if (adapter_search.getItem(position).equalsIgnoreCase(overall_list.get(i))) {
                                viewHolder.checkbox.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }



            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }

    void showPB(final String message) {

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(getActivity());
                pb.setMessage(message);
                pb.show();
            }
        });

    }

    void hidePB() {

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (pb != null && pb.isShowing())
                    pb.dismiss();
            }
        });

    }

    void HidePB(){
        if (pb != null && pb.isShowing())
            pb.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pb != null && pb.isShowing())
            pb.dismiss();

    }
}
