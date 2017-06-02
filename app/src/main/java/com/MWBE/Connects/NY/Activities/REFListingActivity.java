package com.MWBE.Connects.NY.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.Database.DatabaseBeen.TrackingData;
import com.MWBE.Connects.NY.JavaBeen.ListData_RFP;
import com.MWBE.Connects.NY.JavaBeen.ViewHolder_RfpList;
import com.MWBE.Connects.NY.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class REFListingActivity extends FragmentActivity {

    private ListView lv;
    private CustomListAdapter adapter;
    private ArrayList<ListData_RFP> list;
    private ArrayList<ListData_RFP> CapabilitiesList = new ArrayList<>();
    private ArrayList<ListData_RFP> GeographicList = new ArrayList<>();
    private ArrayList<ListData_RFP> ContractList = new ArrayList<>();
    private Context context = this;
    private Utils utils;
    private String responsefromserver;
    private String contract_value_tag_id;
    private ProgressDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflisting);

        init();
    }

    private void init() {

        lv = (ListView) findViewById(R.id.list_rfp_lv);
        populatelist();
    }

    private void populatelist() {
        try {
            Data.Geographic_list.clear();
            Data.Contract_list.clear();
            DataBaseHelper dataBaseHelper = new DataBaseHelper(REFListingActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.close();
            dataBaseHelper.openDataBase();
            //list = new ArrayList<ListData_RFP>();
            list = Data.list;
            for(int i=0;i<list.size();i++){
                String title = list.get(i).getTitle().replace("\\u0027","'");
                list.get(i).setTitle(title);
                String agency = list.get(i).getAgency().replace("\\u0027","'");
                list.get(i).setAgency(agency);
                //list.get(i).getTitle().replace("\u0027","'");
            }
            if (!Data.isOpen) {

                if (getIntent().getStringExtra("contact_value") != null) {
                    /*contract_value_tag_id = getIntent().getStringExtra("contracttagid");
                    Cursor cursor = dataBaseHelper.getDataFromProcurementMaster(contract_value_tag_id, Data.agency, Data.search);
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            if (Data.isOpen) {
                                list.add(new ListData_RFP("Capalino+Company Match", 3.0, cursor.getString(2), cursor.getString(1),
                                        cursor.getString(4), cursor.getString(3)));
                            } else {
                                list.add(new ListData_RFP("Capalino+Company Match", 0.0, cursor.getString(2), cursor.getString(1),
                                        cursor.getString(4), cursor.getString(3)));
                            }

                        }*/

                    if(list.size()>0){


                        adapter = new CustomListAdapter(REFListingActivity.this, R.layout.activity_reflisting, list);
                        lv.setAdapter(adapter);
                    } else {
                        new AlertDialog.Builder(context)
                                .setTitle("Alert!")
                                .setMessage("Sorry, no results were found. Modify your search criteria and try again.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }

            } else {
                Cursor cursor = null;
                    for(int i=0;i<Data.SettingTypeID_capab_search.size();i++){
                        cursor = dataBaseHelper.getDataFromProcurementMaster(Data.SettingTypeID_capab_search.get(i));
                        fillList(cursor,CapabilitiesList);
                    }


               for(int i=0;i<Data.procid.size();i++){
                   for(int j=0;j<Data.Actual_id_list_geographic.size();j++){
                       GeographicList = dataBaseHelper.getDataFromProcurementMasterStar2(Data.Actual_id_list_geographic.get(j),Data.procid.get(i));
                       //fillList(cursor,GeographicList);

                       if(GeographicList.size()>0){
                           for(int k=0;k<GeographicList.size();k++)
                           Data.Geographic_list.add(GeographicList.get(k));

                       }else {
                           GeographicList = Data.Geographic_list;
                       }
                   }

               }

                for(int i=0;i<Data.procid.size();i++){
                    for(int j=0;j<Data.Actual_id_list_target.size();j++){
                        ContractList = dataBaseHelper.getDataFromProcurementMasterStar3(Data.Actual_id_list_target.get(j),Data.procid.get(i));
                        //fillList(cursor,ContractList);
                        if(ContractList.size()>0){
                            for(int k=0;k<ContractList.size();k++)
                                Data.Contract_list.add(ContractList.get(k));
                        }else {
                            ContractList = Data.Contract_list;
                        }
                    }

                }

                fillListFinal();
                //lv.setAdapter(adapter);
            }

            ((TextView) findViewById(R.id.rfpfound)).setText(list.size() + " RFPs Found");
            if(list.size()==0){
                new AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setMessage("You have 0 matches.\nIf you don’t see any matches, check back tomorrow! New City and State RFPs are added to MWBE Connect NY every day.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            //adapter = new CustomListAdapter(REFListingActivity.this, R.layout.activity_reflisting, list);
            //lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(REFListingActivity.this, ProcurementDetailsActivity.class);
                    i.putExtra("fromproc","true");
                    i.putExtra("title", list.get(position).getTitle());
                    i.putExtra("agencyname", list.get(position).getAgency());
                    i.putExtra("proposeddeadline", list.get(position).getPublic_date());
                    i.putExtra("duedate", list.get(position).getDue_date());

                    startActivity(i);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void fillListFinal() {
        ArrayList<String> geog_title = new ArrayList<>();
        ArrayList<String> contr_title = new ArrayList<>();
        GeographicList = Data.Geographic_list;
        ContractList = Data.Contract_list;
        for(int i=0;i< CapabilitiesList.size();i++){
            list.add(CapabilitiesList.get(i));
        }

        for(int i=0;i<CapabilitiesList.size();i++){
            for(int j=0;j<GeographicList.size();j++){
                if(CapabilitiesList.get(i).getTitle().equalsIgnoreCase(GeographicList.get(j).getTitle())){
                    GeographicList.get(j).setRating(2);
/*                        list.add(GeographicList.get(i));*/

                }
            }
            //list.add();
        }

        for(int i=0;i<CapabilitiesList.size();i++){
            for(int j=0;j<ContractList.size();j++){
                if(CapabilitiesList.get(i).getTitle().equalsIgnoreCase(ContractList.get(j).getTitle())){
                    ContractList.get(j).setRating(2);
                    //list.add(ContractList.get(i));
                }
            }
            //list.add();
        }

        for(int i=0;i<ContractList.size();i++){
            for(int j=0;j<GeographicList.size();j++){
                if(ContractList.get(i).getTitle().equalsIgnoreCase(GeographicList.get(j).getTitle())){
                    ContractList.get(i).setRating(3);
                        /*list.add(ContractList.get(i));*/

                }
            }
            //list.add();
        }

        for(ListData_RFP geograph_title : GeographicList){
            geog_title.add(geograph_title.getTitle());
        }

        for(ListData_RFP contract_title : ContractList){
            contr_title.add(contract_title.getTitle());
        }

        for(int i=0;i<CapabilitiesList.size();i++){
            if(geog_title.contains(CapabilitiesList.get(i).getTitle()) || contr_title.contains(CapabilitiesList.get(i).getTitle())){
                CapabilitiesList.get(i).setRating(2);
            }
        }

        for(int i=0;i<CapabilitiesList.size();i++){
            if(geog_title.contains(CapabilitiesList.get(i).getTitle()) && contr_title.contains(CapabilitiesList.get(i).getTitle())){
                CapabilitiesList.get(i).setRating(3);
            }
        }

        list.clear();
        for(int i=0;i<CapabilitiesList.size();i++){
            if(CapabilitiesList.get(i).getRating()==3){
                list.add(CapabilitiesList.get(i));
            }
        }

        for(int i=0;i<CapabilitiesList.size();i++){
            if(CapabilitiesList.get(i).getRating()==2){
                list.add(CapabilitiesList.get(i));
            }
        }

        for(int i=0;i<CapabilitiesList.size();i++){
            if(CapabilitiesList.get(i).getRating()==1){
                list.add(CapabilitiesList.get(i));
            }
        }

        /*for(int i=0;i<CapabilitiesList.size();i++){
            if(GeographicList.contains(CapabilitiesList.get(i)) || ContractList.contains(CapabilitiesList.get(i))){
                CapabilitiesList.get(i).setRating(3);
            }
        }

        for(int i=0;i<CapabilitiesList.size();i++){
            if(GeographicList.contains(CapabilitiesList.get(i)) && ContractList.contains(CapabilitiesList.get(i))){
                CapabilitiesList.get(i).setRating(3);
            }
        }*/

        /*if(CapabilitiesList.size()>0 && GeographicList.size()==0 && ContractList.size()==0){

            adapter = new CustomListAdapter(REFListingActivity.this, R.layout.activity_reflisting, list);
        }



        if(GeographicList.size()>0 && ContractList.size()==0){
            adapter = new CustomListAdapter(REFListingActivity.this, R.layout.activity_reflisting, GeographicList);
        }

        if(ContractList.size()>0 && GeographicList.size()==0){
            adapter = new CustomListAdapter(REFListingActivity.this, R.layout.activity_reflisting, ContractList);
        }

        if(ContractList.size()>0 && GeographicList.size()>0){
            adapter = new CustomListAdapter(REFListingActivity.this, R.layout.activity_reflisting, ContractList);
        }
*/
        for(int i=0;i<list.size();i++){
            String title = list.get(i).getTitle().replace("\\u0027","'");
            list.get(i).setTitle(title);
            String agency = list.get(i).getAgency().replace("\\u0027","'");
            list.get(i).setAgency(agency);
            //list.get(i).getTitle().replace("\u0027","'");
        }

        RemoveDublicate();


        adapter = new CustomListAdapter(REFListingActivity.this, R.layout.activity_reflisting, list);
        lv.setAdapter(adapter);

    }

    private void RemoveDublicate() {
       /* Set set = new TreeSet(new Comparator() {
            @Override
            public int compare(ListData_RFP o1, ListData_RFP o) {
                if(o1.getTitle().equalsIgnoreCase(o2.getTitle())){
                    return 0;
                }
                return 1;
            }
        });
        set.addAll(list);

        System.out.println("\n***** After removing duplicates *******\n");

        list = new ArrayList(set);

        *//** Printing original list **//*
        System.out.println(list);*/

        Map<String, ListData_RFP> map = new LinkedHashMap<>();
        for (ListData_RFP ays : list) {
            map.put(ays.getTitle(), ays);
        }
        list.clear();
        list.addAll(map.values());
    }


    //Footer Tab Event Listener

    private void fillList(Cursor cursor,ArrayList<ListData_RFP> list) {
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
        if (cursor.getCount() > 0) {
            //list.clear();
           /* if(CapabilitiesList.size()>0){
                Data.star=1;
            }*/

            /*if(GeographicList.size()>0 || ContractList.size()>0){
                Data.star=2;
            }

            if(CapabilitiesList.size()>0 && GeographicList.size()>0 && ContractList.size()>0){
                Data.star=3;
            }*/
            while (cursor.moveToNext()) {

                    list.add(new ListData_RFP(cursor.getString(4),"Capalino+Company Match", Data.star, cursor.getString(0), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3)));
                if(Data.star==1) {
                    Data.procid.add(cursor.getInt(4));
                }
            }
        }
    }

    public void HomeClick(View view) {
        Intent i = new Intent(REFListingActivity.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void SettingsClick(View view) {
        Intent i = new Intent(REFListingActivity.this, SettingsMain.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view) {
        Intent i = new Intent(REFListingActivity.this, ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view) {
        Intent i = new Intent(REFListingActivity.this, TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void BackClick(View view) {
        finish();
        Data.isOpen = false;
        Intent i = new Intent(this,BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Data.Actual_id_list_geographic.clear();
        Data.Actual_id_list_target.clear();
        Data.SettingTypeID_capab_search.clear();
        startActivity(i);
    }

    public class CustomListAdapter extends ArrayAdapter<ListData_RFP> {

        ArrayList<ListData_RFP> list_track = new ArrayList<ListData_RFP>();
        private boolean ispresent;
        private String title;
        private String publicdate;
        private String duedate;
        private String agency;
        private double rating;
        private String ProcurementID;

        public CustomListAdapter(Context context, int resource, List<ListData_RFP> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            try {
                ViewHolder_RfpList viewHolder;
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_rfp_row, null);
                    viewHolder = new ViewHolder_RfpList(convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder_RfpList) convertView.getTag();
                }

                final ListData_RFP data = getItem(position);
                setdata(viewHolder, data);


                onClick(viewHolder, position);

                viewHolder.arrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(REFListingActivity.this, ProcurementDetailsActivity.class);
                        i.putExtra("fromproc","true");
                        i.putExtra("title", data.getTitle());
                        i.putExtra("agencyname",data.getAgency());
                        i.putExtra("proposeddeadline",data.getPublic_date());
                        i.putExtra("duedate",data.getDue_date());

                        startActivity(i);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }


        private void getTrackeddata() {
            try {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(REFListingActivity.this);
                dataBaseHelper.createDataBase();
                dataBaseHelper.openDataBase();
                utils = new Utils(REFListingActivity.this);
                Cursor cursor = dataBaseHelper.getTrackedData("UserID", utils.getdata("Userid"), "TrackListing", true);
                if (cursor.getCount() > 0) {
                    list_track.clear();
                    while (cursor.moveToNext()) {
                        list_track.add(new ListData_RFP(cursor.getString(7),"Capalino+Company Match", cursor.getDouble(6), cursor.getString(1),
                                cursor.getString(2), cursor.getString(3), cursor.getString(4)));

                    }
                }

                //Data.listData_rfp = list_track;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private void onClick(final ViewHolder_RfpList viewHolder, final int position) {
            viewHolder.track.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        utils = new Utils(REFListingActivity.this);
                        //deleteDatabase("CapalinoDataBase.sqlite");
                        final DataBaseHelper dataBaseHelper = new DataBaseHelper(REFListingActivity.this);
                        dataBaseHelper.createDataBase();
                        dataBaseHelper.openDataBase();

                        getTrackeddata();

                        final ListData_RFP data_rfp = getItem(position);
                        if (list_track.size() > 0) {
                            String title = data_rfp.getTitle().replace("'","\\u0027");
                            for (int i = 0; i < list_track.size(); i++) {
                                if (!list_track.get(i).getTitle().equalsIgnoreCase(title)) {
                                    ispresent = false;

                                } else {
                                    ispresent = true;
                                    break;
                                }
                            }
                        }

                        if (ispresent) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Alert!")
                                    .setMessage("This RFP is already being tracked.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else {
                            new AlertDialog.Builder(context)
                                    .setTitle("Alert!")
                                    .setMessage("Are you sure you want to track this RFP?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            list_track.add(new ListData_RFP(data_rfp.getId(),data_rfp.getHeader(), data_rfp.getRating(), data_rfp.getTitle(),
                                                    data_rfp.getAgency(), data_rfp.getPublic_date(), data_rfp.getDue_date()));
                                            String title = data_rfp.getTitle().replace("'","\\u0027");
                                            String agency = data_rfp.getAgency().replace("'","\\u0027");
                                            boolean isInserted = dataBaseHelper.InsertUserProcurmentTracking(new TrackingData(title, agency, data_rfp.getPublic_date(),
                                                    data_rfp.getDue_date(), utils.getdata("Userid"), data_rfp.getRating(),data_rfp.getId()));

                                            SendDataToTheServer();
                                           final AlertDialog dialog_alert = new AlertDialog.Builder(context)
                                                    .setTitle("Alert!")
                                                    .setMessage("This RFP is now being tracked.")
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                       @Override
                                                       public void onClick(DialogInterface dialog, int which) {

                                                       }
                                                   })
                                                    .show();

                                           /* new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    dialog_alert.dismiss();
                                                }
                                            }, 3000);*/
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();


                        }

                        //String trackdate = viewHolder.getPublic_date().getText().toString();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            viewHolder.advice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(REFListingActivity.this, AdviceActivity.class);
                    i.putExtra("contact_value", contract_value_tag_id);
                    //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                }
            });
        }


        private void SendDataToTheServer() {
            try {
                utils = new Utils(REFListingActivity.this);
                SimpleDateFormat simpleDateFormat = null;
                for (int i = 0; i < list_track.size(); i++) {
                    title = list_track.get(i).getTitle();
                    agency = list_track.get(i).getAgency();
                    publicdate = list_track.get(i).getPublic_date();
                    simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                    duedate = list_track.get(i).getDue_date();
                    rating = list_track.get(i).getRating();
                    ProcurementID = list_track.get(i).getId();
                }

                title = title.replace("#","%23");
                agency = agency.replace("#","%23");
                String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addTrackingRFPUpdated.php?ProcurementTitle=" + title +
                        "&AgencyTitle=" + agency + "&TrackDate=" + simpleDateFormat.format(new Date()) + "&ProposalDeadLine="
                        + duedate + "&UserID=" + utils.getdata("Userid") + "&Rating=" + rating + "&ProcurementID="+ProcurementID;
                url = url.replace(" ", "%20");


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responsefromserver = response;
                        //checkResponse();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                Volley.newRequestQueue(REFListingActivity.this).add(stringRequest);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void checkResponse() {
            if (responsefromserver.equalsIgnoreCase("Records Added.")) {
                new AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setMessage("Data has been sent to the server")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                new AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setMessage("Data has not been sent to the server, please try again.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }


        private void setdata(ViewHolder_RfpList viewHolder, ListData_RFP data) {
            viewHolder.title.setText(data.getTitle());
            viewHolder.Agency.setText(data.getAgency());
            viewHolder.public_date.setText(data.getPublic_date());
            viewHolder.due_date.setText(data.getDue_date());
            viewHolder.ratingbar.setRating((float) data.getRating());
            if(!Data.isOpen)
            viewHolder.ratingbar.setVisibility(View.GONE);

        }
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        finish();
        Data.isOpen = false;
        Intent i = new Intent(this,BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Data.Actual_id_list_geographic.clear();
        Data.Actual_id_list_target.clear();
        Data.SettingTypeID_capab_search.clear();
        startActivity(i);

    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(REFListingActivity.this);
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
