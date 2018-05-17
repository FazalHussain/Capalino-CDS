package com.MWBE.Connects.NY.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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

    private ImageView sort_btn;
    private String sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflisting);

        init();
    }

    private void init() {

        lv = (ListView) findViewById(R.id.list_rfp_lv);
        sort_btn = (ImageView) findViewById(R.id.sort_btn);
        populatelist();
    }

    private void populatelist() {
        try {

            Data.Geographic_list.clear();
            Data.Contract_list.clear();
            final DataBaseHelper dataBaseHelper = new DataBaseHelper(REFListingActivity.this);
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

                if (getIntent().getSerializableExtra("list") != null){
                    list = (ArrayList<ListData_RFP>) getIntent().getSerializableExtra("list");

                    ((TextView) findViewById(R.id.rfpfound)).setText(list.size() + " RFPs Found");
                    adapter = new CustomListAdapter(REFListingActivity.this, R.layout.activity_reflisting, list);
                    lv.setAdapter(adapter);

                    if(list.size() == 0){
                        new AlertDialog.Builder(context)
                                .setTitle("Alert!")
                                .setMessage("Sorry, no results were found. Modify your search criteria and try again.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onBackPressed();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        return;
                    }
                }

                if (getIntent().getStringExtra("contact_value") != null) {

                    ((TextView) findViewById(R.id.rfpfound)).setText(list.size() + " RFPs Found");

                    if(list.size()>0){
                        sort_btn.setVisibility(View.VISIBLE);
                        Collections.sort(list, new Comparator<ListData_RFP>() {
                            DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
                            @Override
                            public int compare(ListData_RFP o1, ListData_RFP o2) {
                                try {
                                    return f.parse(o2.getPublic_date()).compareTo(f.parse(o1.getPublic_date()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                return 0;
                            }
                        });
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

                list = (ArrayList<ListData_RFP>) getIntent().getSerializableExtra("list");

                ((TextView) findViewById(R.id.rfpfound)).setText(list.size() + " RFPs Found");
                adapter = new CustomListAdapter(REFListingActivity.this, R.layout.activity_reflisting, list);
                lv.setAdapter(adapter);

                sort_btn.setVisibility(View.VISIBLE);

                if(list.size() == 0){
                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Sorry, no results were found. Modify your search criteria and try again.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onBackPressed();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return;
                }



                //lv.setAdapter(adapter);
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


    //Footer Tab Event Listener

    private ArrayList<ListData_RFP> fillList(Cursor cursor) {
        Data.procid.clear();
        ArrayList<ListData_RFP> list = new ArrayList<>();
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

            while (cursor.moveToNext()) {

                    list.add(new ListData_RFP(cursor.getString(4),"Capalino+Company Match", Data.star, cursor.getString(0), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3)));
                if(Data.star==1) {
                    Data.procid.add(cursor.getInt(4));
                }
            }
        }

        ArrayList<ListData_RFP> capablitylist = new ArrayList<ListData_RFP>();// unique
        for (ListData_RFP element : list) {
            if (!capablitylist.contains(element)) {
                capablitylist.add(element);
            }
        }

        return capablitylist;
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
        onBackPressed();
    }

    public void sortClick(View view) {
        try{
            ArrayList<String> list_sortby =new ArrayList<>();
            if(Data.isOpen) {
                list_sortby.add("Star Rating");
                list_sortby.add("Proposal Deadline");
                popup_dropdown(list_sortby);
            }else {
                list_sortby.add("Post Date");
                list_sortby.add("Proposal Deadline");
                popup_dropdown_normal(list_sortby);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void popup_dropdown(ArrayList<String> listsort) {
        try{

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            final LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.sort_popup, null);
            alertDialogBuilder.setView(promptsView);



            alertDialogBuilder.setTitle("Sort By");


            final AlertDialog alertDialog = alertDialogBuilder.create();

            final ListView lv_spinner= (ListView) promptsView
                    .findViewById(R.id.lv_dropdown);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    listsort);
            lv_spinner.setAdapter(adapter);
            lv_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:{
                            CustomListAdapter adapter = new CustomListAdapter(REFListingActivity.this, R.layout.activity_reflisting, list);
                            lv.setAdapter(adapter);
                            alertDialog.dismiss();
                            break;
                        }

                        case 1:{
                            List<ListData_RFP> sorted_list_proposed_deadline = new ArrayList<ListData_RFP>(list);
                            Collections.sort(sorted_list_proposed_deadline, new Comparator<ListData_RFP>() {
                                DateFormat f = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                                @Override
                                public int compare(ListData_RFP o1, ListData_RFP o2) {
                                    try {
                                        return f.parse(o1.getDue_date()).compareTo(f.parse(o2.getDue_date()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    return 0;
                                }
                            });

                            CustomListAdapter adapter = new CustomListAdapter(REFListingActivity.this, R.layout.activity_reflisting, sorted_list_proposed_deadline);
                            lv.setAdapter(adapter);
                            alertDialog.dismiss();
                            break;
                        }


                    }
                }
            });


            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void popup_dropdown_normal(ArrayList<String> listsort) {
        try{

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            final LayoutInflater li = LayoutInflater.from(this);
            View promptsView = li.inflate(R.layout.sort_popup, null);
            alertDialogBuilder.setView(promptsView);



            alertDialogBuilder.setTitle("Sort By");


            final AlertDialog alertDialog = alertDialogBuilder.create();

            final ListView lv_spinner= (ListView) promptsView
                    .findViewById(R.id.lv_dropdown);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    listsort);
            lv_spinner.setAdapter(adapter);
            lv_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:{
                            List<ListData_RFP> sorted_list_posted_date = new ArrayList<ListData_RFP>(list);
                            Collections.sort(sorted_list_posted_date, new Comparator<ListData_RFP>() {
                                DateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
                                @Override
                                public int compare(ListData_RFP o1, ListData_RFP o2) {
                                    try {
                                        return f.parse(o2.getPublic_date()).compareTo(f.parse(o1.getPublic_date()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    return 0;
                                }
                            });

                            CustomListAdapter adapter = new CustomListAdapter(REFListingActivity.this, R.layout.activity_reflisting, sorted_list_posted_date);
                            lv.setAdapter(adapter);
                            alertDialog.dismiss();
                            break;
                        }

                        case 1:{
                            List<ListData_RFP> sorted_list_proposed_deadline = new ArrayList<ListData_RFP>(list);
                            Collections.sort(sorted_list_proposed_deadline, new Comparator<ListData_RFP>() {
                                DateFormat f = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                                @Override
                                public int compare(ListData_RFP o1, ListData_RFP o2) {
                                    try {
                                        return f.parse(o1.getDue_date()).compareTo(f.parse(o2.getDue_date()));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    return 0;
                                }
                            });

                            CustomListAdapter adapter = new CustomListAdapter(REFListingActivity.this, R.layout.activity_reflisting, sorted_list_proposed_deadline);
                            lv.setAdapter(adapter);
                            alertDialog.dismiss();
                            break;
                        }


                    }
                }
            });


            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
        }catch (Exception e){
            e.printStackTrace();
        }
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
                            String title = data_rfp.getTitle().replace("'","''");
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
                                            String title = data_rfp.getTitle().replace("'","''");
                                            String agency = data_rfp.getAgency().replace("'","''");
                                           /* boolean isInserted = dataBaseHelper.InsertUserProcurmentTracking(new TrackingData(title, agency, data_rfp.getPublic_date(),
                                                    data_rfp.getDue_date(), utils.getdata("Userid"), data_rfp.getRating(),data_rfp.getId()));
*/
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


            if(getIntent().getStringExtra("rating") != null){
                viewHolder.ratingbar.setVisibility(View.GONE);
                sort_btn.setVisibility(View.GONE);
                if(data.getRating() < 1) {
                    viewHolder.match_tagged.setVisibility(View.INVISIBLE);
                }else {
                    viewHolder.match_tagged.setVisibility(View.VISIBLE);
                }
            }
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
