package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.MWBE.Connects.NY.CheckBoxAdapter.ItemListAdapterGeographic;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.JavaBeen.ViewHolder_Agency;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.MWBE.Connects.NY.AppConstants.Constants;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.JavaBeen.ListData_Agency;
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
import java.util.List;

public class GeographicCoverageActivity extends Activity {

    private ListView lv;
    private ArrayList<ListData_Agency> list;
    private ArrayList<Boolean> list_check;
    private int count;
    private boolean[] list_check_restore;
    private ArrayList<Integer> positionArray = new ArrayList<>();
    private String[] val;
    private String[] key;
    private Utils utils;
    private Context context = this;
    private String responsefromserver;
    private ArrayList<String> tagidlist;
    private boolean isShow;
    private ProgressDialog pb;
    private String TagID;
    private boolean allchecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geographic_coverage);
        init();
    }

    public void BackClick(View view) {
        Intent intentback = new Intent();
        utils = new Utils(GeographicCoverageActivity.this);
        intentback.putExtra("geographic_count", count + " selected");
        intentback.putIntegerArrayListExtra("geographic_pos_array", positionArray);
        setResult(Constants.request_geographic_coverage, intentback);
        finish();
    }

    public void UpdateClick(View view) {
        try {
            showPB("Geographic Coverage is being updated, please wait");
            count = 0;
            isShow = false;
            utils = new Utils(this);
            DeleteGeographic(utils.getdata("Userid"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DeleteGeographic(final String userid) {
        try {

            Thread thread_update = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Activity activity = (Activity)context;
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                        dataBaseHelper.createDataBase();
                        dataBaseHelper.openDataBase();
                        HttpClient httpclient = new DefaultHttpClient();
                        //showPB("Loading....");

                        String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/deleteUserGeoTags.php?UserID="+userid;
                        HttpPost httppost = new HttpPost(link);

                        ResponseHandler<String> responseHandler = new BasicResponseHandler();

                        String response = httpclient.execute(httppost,
                                responseHandler);


                        Log.i("Response", "Response : " + response);
                        response = response.replace("\n","");
                        if(response.equalsIgnoreCase("Record Deleted.")){
                           updateData();
                        }

                        //list_data.add(new ListData(image, contentShortDescription, ContentRelevantDateTime));

                        //isinserted = dataBaseHelper.InsertUserProcurmentTracking(been);




                    } catch (Exception e) {
                        hidePB();
                        e.printStackTrace();
                    }


                }
            });
            thread_update.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateData() {

        if(Data.AllNYC){
            updateData(1111);
        }

        allchecked = checkBoolean(list_check);
        if(!allchecked) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder dialog1 = new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Thank you for updating your geographic coverage!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intentback = new Intent();

                                    utils = new Utils(GeographicCoverageActivity.this);
                                    intentback.putExtra("geographic_count", count + " selected");
                                    intentback.putIntegerArrayListExtra("geographic_pos_array", positionArray);
                                    setResult(Constants.request_geographic_coverage, intentback);
                                    finish();
                                }
                            });

                    dialog1.show();
                }
            });
        }

        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String date = dateformat.format(new Date());
        for (int i = 0; i < list_check.size(); i++) {
            if (list_check.get(i)) {

                if(i==0){continue;}

                getDataID(i);

                /*int j = i;
                if(list_check.get(1)){
                    j=64;
                    list_check.set(1,false);
                    count++;
                }*/


                //Data.Actual_id_list_geographic.add(positionArray.get(i));
                String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addUserPreferences.php?UserID=" + utils.getdata("Userid") + "&SettingTypeID=" + getIntent().getIntExtra("SettingID", 0) + "&ActualTagID=" + (TagID) + "&AddedDateTime=" + date;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        responsefromserver = response;
                        if(!isShow)
                            checkResponse();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                Volley.newRequestQueue(this).add(stringRequest);
            }else {

                if(i==list_check.size()-1){
                    hidePB();
                }
            }
        }



        for (int i = 0; i < list_check.size(); i++) {
            if (list_check.get(i)) {
                if(i==0){continue;}
                count++;
            }
        }



        Data.poss_array = positionArray;
    }

    private boolean checkBoolean(ArrayList<Boolean> list_check) {
        for(boolean checked : list_check){
            if(checked){
                return true;
            }
        }

        return false;
    }


    private void getDataID(int position) {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(GeographicCoverageActivity.this);
        try{
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();

            Cursor cursor = dataBaseHelper.getDataFromDB("TagTitle", Data.list_geographic.get(position).getTitle(),"GeopraphyTags",true);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    TagID = cursor.getString(0);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateData(int j) {
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String date = dateformat.format(new Date());

        String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addUserPreferences.php?UserID=" + utils.getdata("Userid") + "&SettingTypeID=" + getIntent().getIntExtra("SettingID", 0) + "&ActualTagID=" + j + "&AddedDateTime=" + date;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                hidePB();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void checkResponse() {
        hidePB();
        isShow = true;
        if (!allchecked || responsefromserver.equalsIgnoreCase("Records Added.")) {
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("Thank you for updating your geographic coverage!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intentback = new Intent();

                            utils = new Utils(GeographicCoverageActivity.this);
                                intentback.putExtra("geographic_count", count + " selected");
                            intentback.putIntegerArrayListExtra("geographic_pos_array", positionArray);
                            setResult(Constants.request_geographic_coverage, intentback);
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        } else {
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("Your geographic coverage not been updated, please try again.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }



    private void init() {
        try {
            Data.count_geographic = 0;
            lv = (ListView) findViewById(R.id.list_geographic_coverage_lv);
            populatelist();
            positionArray = Data.poss_array;
            if(positionArray.size()==0) {
                for (int i = 0; i < list_check.size(); i++) {
                    positionArray.add(-1);
                }
            }

            ((Button)findViewById(R.id.updatebtn)).getBackground().setAlpha(200);
            ((Button)findViewById(R.id.updatebtn)).setEnabled(false);

            list_check = Data.list_check;




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populatelist() {
        try {
            list = new ArrayList<ListData_Agency>();
            list_check = new ArrayList<Boolean>();
            final ArrayList<String> tagid_list = new ArrayList<>();

            DataBaseHelper dataBaseHelper = new DataBaseHelper(GeographicCoverageActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            getGeographicSelection();
            list.add(new ListData_Agency("All NYC"));
            Cursor cursor = dataBaseHelper.getDataFromDB("", "", "GeopraphyTags", false,true);
            tagid_list.add("");
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    list.add(new ListData_Agency(cursor.getString(2)));
                    tagid_list.add(cursor.getInt(0)+"");
                }
                Data.tagidlist_db = tagid_list;
            }

            for (int i = 0; i < list.size(); i++) {
                list_check.add(i, false);
            }

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        ((Button)findViewById(R.id.updatebtn)).getBackground().setAlpha(255);
                        ((Button)findViewById(R.id.updatebtn)).setEnabled(true);

                        list_check.set(position, !list_check.get(position));
                        if(!list_check.get(position)){
                            getDataID(position);
                            Data.tagidlist.remove(TagID);
                        }

                        if(position==0) {
                            if (list_check.get(position)) {
                                Data.AllNYC = true;
                                list_check.set(4, true);
                                list_check.set(25, true);
                                list_check.set(42, true);
                                list_check.set(44, true);
                                list_check.set(32, true);

                                Data.tagidlist.add("0");
                                Data.tagidlist.add("3");
                                Data.tagidlist.add("24");
                                Data.tagidlist.add("41");
                                Data.tagidlist.add("43");
                                Data.tagidlist.add("31");

                                Data.list_check = list_check;


                                ItemListAdapterGeographic adapter = new ItemListAdapterGeographic(GeographicCoverageActivity.this,list);
                                lv.setAdapter(adapter);


                            } else {
                                Data.AllNYC = false;
                                list_check.set(4, false);
                                list_check.set(25, false);
                                list_check.set(42, false);
                                list_check.set(44, false);
                                list_check.set(32, false);

                                Data.tagidlist.remove("0");
                                Data.tagidlist.remove("3");
                                Data.tagidlist.remove("24");
                                Data.tagidlist.remove("41");
                                Data.tagidlist.remove("43");
                                Data.tagidlist.remove("31");

                                Data.list_check = list_check;

                                ItemListAdapterGeographic adapter = new ItemListAdapterGeographic(GeographicCoverageActivity.this,list);
                                lv.setAdapter(adapter);
                                //adapter.notifyDataSetChanged();
                            }

                        }else {
                            positionArray.set(position,position);


                            //list_check.set(position, !list_check.get(position));
                        }







                        //positionArray.set(position,position);
                        //list_check.set(position, !list_check.get(position));
                        if(!list_check.get(position)){
                            if(tagid_list.get(position).length()>0){
                                //tagid_list.remove(position);
                                String idtoremove = tagid_list.get(position);
                                for(int i=0;i<Data.tagidlist.size();i++){
                                    if(position == Integer.valueOf(idtoremove)){
                                        Data.tagidlist.remove(idtoremove);
                                    }
                                }
                            }
                        }


                        count = Data.count_geographic;

                        //CustomListAdapter adapter = new CustomListAdapter(GeographicCoverageActivity.this, R.layout.activity_agency, list);
                        //lv.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            //CustomListAdapter adapter = new CustomListAdapter(GeographicCoverageActivity.this, R.layout.activity_agency, list);
            //lv.setAdapter(adapter);

            Data.list_check = list_check;
            Data.list_geographic = list;


            /*ItemListAdapterGeographic adapter = new ItemListAdapterGeographic(GeographicCoverageActivity.this,list);
            lv.setAdapter(adapter);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lv.setItemsCanFocus(false);*/

            //getGeographicSelection();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getGeographicSelection() {
        showPB("Loading...");
        tagidlist = new ArrayList<>();
        Utils utils = new Utils(GeographicCoverageActivity.this);

        final String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getUserGeoTags.php?UserID="+utils.getdata("Userid");
        new AsyncTask<String, Void, ArrayList>() {
            @Override
            protected ArrayList doInBackground(String... params) {
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");

                    HttpPost httppost = new HttpPost(params[0]);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    response = response.replace("\n","");
                    if(response.equalsIgnoreCase("[]")){
                        tagidlist.clear();
                    }
                    if(response!=null) {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if(jsonObject.getString("ActualTagID").equalsIgnoreCase("1111")){
                                tagidlist.add("0");
                                positionArray.set(0,0);
                            }else {
                                tagidlist.add(jsonObject.getString("ActualTagID"));
                                positionArray.set(Integer.valueOf(jsonObject.getString("ActualTagID")),Integer.valueOf(jsonObject.getString("ActualTagID")));
                            }
                        }
                        return tagidlist;
                    }


                    return null;


                } catch (Exception e) {
                    e.printStackTrace();
                    hidePB();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList arrayList) {
                if(arrayList!=null)
                if(arrayList.size()>0){
                    Data.tagidlist = arrayList;
                }else {
                    Data.tagidlist.clear();
                }

                ItemListAdapterGeographic adapter = new ItemListAdapterGeographic(GeographicCoverageActivity.this,list);
                lv.setAdapter(adapter);
                lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                lv.setItemsCanFocus(true);
                hidePB();


            }
        }.execute(link, "", "");
    }




    //Footer tab Click Listener
    public void HomeClick(View view) {
        Intent i = new Intent(GeographicCoverageActivity.this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void BrowseClick(View view) {
        Intent i = new Intent(GeographicCoverageActivity.this, BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view) {
        Intent i = new Intent(GeographicCoverageActivity.this, ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view) {
        Intent i = new Intent(GeographicCoverageActivity.this, TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
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
                    positionArray.set(position, position);
                } else {
                    viewHolder.checkbox.setVisibility(View.GONE);
                    //list_tick.remove(position);

                }

            } catch (Exception e) {
                hidePB();
                e.printStackTrace();
            }
            return convertView;
        }
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        Intent intentback = new Intent();

        utils = new Utils(GeographicCoverageActivity.this);
        if(count>0)
            intentback.putExtra("geographic_count", count + " selected");
        intentback.putIntegerArrayListExtra("geographic_pos_array", positionArray);
        setResult(Constants.request_geographic_coverage, intentback);
        finish();

    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(GeographicCoverageActivity.this);
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
}
