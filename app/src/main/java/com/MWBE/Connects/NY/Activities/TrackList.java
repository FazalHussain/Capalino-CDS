package com.MWBE.Connects.NY.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.CustomViews.CustomTextView_Bold;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.JavaBeen.ListData_Track;
import com.MWBE.Connects.NY.JavaBeen.ViewHolder_Track;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.MWBE.Connects.NY.JavaBeen.ListData_RFP;
import com.MWBE.Connects.NY.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TrackList extends FragmentActivity {

    private ListView lv_track_list;
    private Context context = this;
    private ArrayList<ListData_Track> list_track;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);
        init();
    }



    private void init() {
        lv_track_list = (ListView) findViewById(R.id.list_tracking_item);
        list_track = new ArrayList<ListData_Track>();
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
        populateList();
    }

    private void populateList() {

      /*  if(Data.listData_rfp.size()>0) {
            for(int i=0;i<Data.listData_rfp.size();i++) {
                list_track.add(new ListData_Track("Capalino+Company Match", Data.listData_rfp.get(i).getRating(),
                        Data.listData_rfp.get(i).getTitle(), Data.listData_rfp.get(i).getAgency(),  Data.listData_rfp.get(i).getPublic_date()));
            }*/

            //label show
            getTrackeddata();



        CustomListAdapter adapter = new CustomListAdapter(TrackList.this, R.layout.activity_track_list, list_track);
        lv_track_list.setAdapter(adapter);
        ((TextView)findViewById(R.id.rfpfound)).setText(list_track.size()+" RFPs Found");
//            lvClick();
        clickActionList();

    }

    private void getTrackeddata() {
        try{
            DataBaseHelper dataBaseHelper = new DataBaseHelper(TrackList.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            Utils utils = new Utils(TrackList.this);
            Cursor cursor = dataBaseHelper.getTrackedData(utils.getdata("Userid"),true);
            list_track.clear();
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    String title = cursor.getString(0).replace("\\u0027","'");
                    String agency = cursor.getString(1).replace("\\u0027","'");
                    list_track.add(new ListData_Track(cursor.getString(6),"Capalino+Company Match",cursor.getDouble(3),title,
                            agency,cursor.getString(4)));
                }
            }else {
                ((TextView)findViewById(R.id.rfpfound)).setVisibility(View.GONE);
                ((CustomTextView_Bold) findViewById(R.id.label)).setVisibility(View.VISIBLE);
            }




        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void clickActionList() {
        try{

            if(getIntent().getStringExtra("notif_status")!=null){
                if(getIntent().getStringExtra("notif_status").equalsIgnoreCase("RFP Updated")){
                    Intent i = new Intent(TrackList.this, ProcurementDetailsActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra("title",getIntent().getStringExtra("title"));
                    i.putExtra("notif_status","notif");
                    startActivity(i);
                }
            }

             lv_track_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 @Override
                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                     Intent i = new Intent(TrackList.this, ProcurementDetailsActivity.class);
                     i.putExtra("title",list_track.get(position).getTitle());
                     startActivity(i);
                 }
             });
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    //BackClick Event
    public void BackClick(View view){
        finish();
        startActivity(new Intent(this,BrowseActivity.class));
    }


    public class CustomListAdapter extends ArrayAdapter<ListData_Track> {

        ArrayList<ListData_RFP> list_track = new ArrayList<ListData_RFP>();
        Utils utils = new Utils(TrackList.this);

        public CustomListAdapter(Context context, int resource, List<ListData_Track> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            try {
                ViewHolder_Track viewHolder;
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_track_row, null);
                    viewHolder = new ViewHolder_Track(convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder_Track) convertView.getTag();
                }

                ListData_Track data = getItem(position);
                setdata(viewHolder, data);
                AddEventToCalender(viewHolder,data);

                DeleteRFPClick(position,convertView,data);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        private void AddEventToCalender(ViewHolder_Track viewHolder, final ListData_Track data) {
            try{
                viewHolder.calender_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            Log.d("click","yes");

                            int year = 0, monthOfYear = 0, dayOfMonth = 0;
                            int hours = 0, minutes = 0;
                            Calendar myCalendar = Calendar.getInstance();
                            Intent intent = new Intent(Intent.ACTION_INSERT);
                            intent.setType("vnd.android.cursor.item/event");
                            Log.d("beginTime",myCalendar.getTime().toString());
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm");
                            myCalendar.setTime(sdf.parse(data.getTrack_started_date()));
                            String df = data.getTrack_started_date();
                            String[] d_ampm_array = df.split(" ");
                            String[] d_array = df.split("-");
                            dayOfMonth = Integer.parseInt(d_array[0]);
                            Log.d("day",d_array[0]);
                            Log.d("count", String.valueOf(d_array.length));
                            try {


                                monthOfYear = myCalendar.get(Calendar.MONTH) + 1;
                                //dayOfMonth = Integer.parseInt(d_array[0]);
                                d_array = d_array[2].split(" ");
                                year = Integer.parseInt(d_array[0]);
                                String[] time_array = d_array[1].split(":");
                                hours = Integer.parseInt(time_array[0]);
                                minutes = Integer.parseInt(time_array[1]);

                                Log.d("month", String.valueOf(monthOfYear));
                                Log.d("year",d_array[0]);

                                Log.d("hh",time_array[0]);
                                Log.d("mm",time_array[1]);
                                for(int i=0;i<time_array.length;i++){
                                    Log.d("tt",time_array[i]);
                                }

                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear-1);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                myCalendar.set(Calendar.HOUR_OF_DAY, hours);
                                myCalendar.set(Calendar.MINUTE, minutes);
                                if(d_ampm_array[2].equalsIgnoreCase("AM"))
                                    myCalendar.set( Calendar.AM_PM, Calendar.AM );
                                else
                                    myCalendar.set( Calendar.AM_PM, Calendar.PM );
                            }catch (NumberFormatException e){
                                e.printStackTrace();
                            }


                            Log.d("endtime", myCalendar.getTime().toString());
                            intent.putExtra("allDay", false);
                            //intent.putExtra("rrule", "FREQ=YEARLY");
                            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, myCalendar.getTimeInMillis());
                            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, myCalendar.getTimeInMillis());

                            intent.putExtra(CalendarContract.Events.DESCRIPTION, getData(data.getTitle()));
                            intent.putExtra("title", data.getTitle());
                            intent.putExtra(CalendarContract.Events.HAS_ALARM, 1);
                            startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private String getData(String title) {
            try{
                DataBaseHelper dataBaseHelper = new DataBaseHelper(TrackList.this);
                dataBaseHelper.createDataBase();
                dataBaseHelper.openDataBase();
                title =  title.replace("'","\\u0027");
                Cursor cursor = dataBaseHelper.getDataFromDB("ProcurementTitle",title,"ProcurementMaster",true);
                if(cursor.getCount()>0){
                    while (cursor.moveToNext()){
                       return cursor.getString(7).replace("\\u0027","'");

                        //((TextView)findViewById(R.id.longdescrip)).setText(longdesc);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            return "";
        }

        private void DeleteRFPClick(final int position, View convertView, final ListData_Track data) {
            try{

                //http://celeritas-solutions.com/cds/capalinoapp/apis/unTrackRFP.php?ProcurementTitle=%@&UserID=%@
                ((ImageView)convertView.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        new AlertDialog.Builder(context)
                                .setTitle("Alert!")
                                .setMessage("Are you sure you want to untrack? You will no longer receive updates for this RFP.")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/unTrackRFPUpdated.php?ProcurementTitle=" + data.getId() + "&UserID=" + utils.getdata("Userid");
                                        url = url.replace(" ","%20");
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.contains("Record Deleted.")) {
                                                    try {
                                                        final DataBaseHelper dataBaseHelper = new DataBaseHelper(TrackList.this);
                                                        dataBaseHelper.createDataBase();
                                                        dataBaseHelper.openDataBase();

                                                        String title = data.getTitle().replace("'","\\u0027");
                                                        dataBaseHelper.deleteRecord("'"+title+"'");
                                                        populateList();


                                                        new AlertDialog.Builder(context)
                                                                .setTitle("Alert!")
                                                                .setMessage("You have successfully untracked RFP.")
                                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                    }
                                                                })
                                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                                .show();

                                                       /* new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                dialog_alert.dismiss();
                                                            }
                                                        }, 3000);*/
                                                    } catch (Exception e) {
                                                        //hidePB();
                                                        e.printStackTrace();
                                                    }


                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                error.printStackTrace();
                                                new AlertDialog.Builder(context)
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

                                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


                                        Volley.newRequestQueue(TrackList.this).add(stringRequest);


                                    }
                                })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });

                }catch (Exception e){
                    e.printStackTrace();
                }
        }

        private void setdata(ViewHolder_Track viewHolder,ListData_Track data) {
            //viewHolder.header.setText(data.getHeader());
            viewHolder.title.setText(data.getTitle());
            viewHolder.Agency.setText(data.getAgency());
            viewHolder.track_started_date.setText(data.getTrack_started_date());

            viewHolder.ratingbar.setRating((float) data.getRating());

        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }
    }

        public void BrowseClick(View view){
        Intent i = new Intent(TrackList.this,BrowseActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void SettingsClick(View view){
        Intent i = new Intent(TrackList.this,SettingsMain.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(TrackList.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void HomeClick(View view){
        Intent i = new Intent(TrackList.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);

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
