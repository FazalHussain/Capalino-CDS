package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.MWBE.Connects.NY.JavaBeen.ViewHolder_Agency;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.MWBE.Connects.NY.AppConstants.Constants;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.CheckBoxAdapter.ItemListAdapterCertification;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
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

public class CertificationActivity extends Activity {

    private ListView lv;
    private ArrayList<ListData_Agency> list;
    private ArrayList<Boolean> list_check;
    private int count;
    private ArrayList<Integer> position_Array_Certification = new ArrayList<>();
    private Context context = this;
    private String responsefromserver;
    private Utils utils;
    private AlertDialog levelDialog;
    private int inputSelection = 0;
    private ArrayList<String> tagidlist_certification;
    private boolean isShow;
    private ProgressDialog pb;
    private boolean allchecked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification);
        init();
       // populatepopup();
    }

    private void populatepopup() {
        final CharSequence[] items = { " NEW YORK CITY ", " NEW YORK STATE " };

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Input Type");

        builder.setSingleChoiceItems(items, inputSelection,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        switch (item) {
                            case 0:
                                inputSelection = 0;
                                break;
                            case 1:
                                inputSelection = 1;
                                break;

                        }


                    }
                });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                populatelist();
                levelDialog.dismiss();
            }
        });
        levelDialog = builder.create();
        levelDialog.show();

        Data.selectioninput = inputSelection;
    }


    public void BackClick(View view){
        Intent intentback = new Intent();
        if(count>0)
            intentback.putExtra("certification_count", count + " selected");
        intentback.putIntegerArrayListExtra("Certification_pos_array", position_Array_Certification);
        setResult(Constants.request_certification, intentback);
        finish();
    }

    public void UpdateClick(View view) {
        try {
            showPB("Certification is being updated, please wait");
            count = 0;
            isShow = false;
            utils = new Utils(this);
            DeleteCertification(utils.getdata("Userid"));
        }catch (Exception e){
            hidePB();
            e.printStackTrace();
        }
    }

    private void checkResponse() {
        hidePB();
        isShow = true;
        if (responsefromserver.equalsIgnoreCase("Records Added.")) {
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("Thank you for updating your certifications!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intentback = new Intent();
                            if(count>0)
                                intentback.putExtra("certification_count", count + " selected");
                            intentback.putIntegerArrayListExtra("Certification_pos_array", position_Array_Certification);
                            setResult(Constants.request_certification, intentback);
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("Your certifications not been updated, please try again.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
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

    private void init() {
        lv = (ListView) findViewById(R.id.list_certifications_lv);
        populatelist();
        position_Array_Certification = Data.poss_array_certification;
            if(position_Array_Certification.size()==0)
            for(int i=0;i<list_check.size();i++){
                position_Array_Certification.add(-1);
            }

        ((Button)findViewById(R.id.updatebtn)).getBackground().setAlpha(200);
        ((Button)findViewById(R.id.updatebtn)).setEnabled(false);

        list_check = Data.list_check_certification;

    }

    private void DeleteCertification(final String userid) {
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

                        String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/deleteUserCertificationsTags.php?UserID="+userid;
                        HttpPost httppost = new HttpPost(link);

                        ResponseHandler<String> responseHandler = new BasicResponseHandler();

                        final String response = httpclient.execute(httppost,
                                responseHandler);


                        Log.i("Response", "Response : " + response);

                        if(response.contains("Record Deleted.")){
                            UpdateData();
                        }

                        //list_data.add(new ListData(image, contentShortDescription, ContentRelevantDateTime));

                        //isinserted = dataBaseHelper.InsertUserProcurmentTracking(been);




                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
            thread_update.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void UpdateData() {
        try{
            allchecked = checkBoolean(list_check);
            if(!allchecked){
                hidePB();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder dialog1 = new AlertDialog.Builder(context)
                                .setTitle("Alert!")
                                .setMessage("Thank you for updating your certification!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intentback = new Intent();
                                        if(count>0)
                                            intentback.putExtra("certification_count", count + " selected");
                                        intentback.putIntegerArrayListExtra("Certification_pos_array", position_Array_Certification);
                                        setResult(Constants.request_certification, intentback);
                                        finish();
                                    }
                                });
                        dialog1.show();
                    }
                });


            }else {

                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                String date = dateformat.format(new Date());
                String url = "";
                for (int i = 0; i < list_check.size(); i++) {
                    if (list_check.get(i)) {
                        url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addUserPreferences.php?UserID=" + utils.getdata("Userid") + "&SettingTypeID=" + getIntent().getIntExtra("SettingID", 0) + "&ActualTagID=" + (position_Array_Certification.get(i) + 1) + "&AddedDateTime=" + date;
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                responsefromserver = response;
                                if (!isShow)
                                    checkResponse();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });

                        Volley.newRequestQueue(this).add(stringRequest);
                    }
                }

                for (int i = 0; i < list_check.size(); i++) {
                    if (list_check.get(i)) {
                        count++;
                    }
                }

                Data.poss_array_certification = position_Array_Certification;

            }
    } catch (Exception e) {
        e.printStackTrace();
    }

    }


    private void populatelist() {
        try {
            list = new ArrayList<ListData_Agency>();
            list_check = new ArrayList<Boolean>();
            final ArrayList<String> tagid_list = new ArrayList<>();
            DataBaseHelper dataBaseHelper = new DataBaseHelper(CertificationActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            getCertificationSelection();
            Cursor cursor = null;
            cursor = dataBaseHelper.getDataFromDB("","","CertificationTypeTags",false);

            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    if(list.size()>=9){
                        list.add(new ListData_Agency("New York State - "+cursor.getString(2)));
                    }else
                        list.add(new ListData_Agency("New York City - "+cursor.getString(2)));
                    tagid_list.add(cursor.getInt(0)+"");
                }

                Data.tagidlistcertification_db = tagid_list;
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
                        position_Array_Certification.set(position,position);
                        list_check.set(position, !list_check.get(position));
                        if(!list_check.get(position)){
                            if(tagid_list.get(position).length()>0){
                                tagid_list.remove(position);
                            }
                        }
                        //CustomListAdapter adapter = new CustomListAdapter(CertificationActivity.this, R.layout.activity_agency, list);
                        //lv.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            Data.list_check_certification = list_check;

            /*ItemListAdapterCertification adapter = new ItemListAdapterCertification(CertificationActivity.this,list);
            lv.setAdapter(adapter);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lv.setItemsCanFocus(false);*/


            //getCertificationSelection();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getCertificationSelection() {
        showPB("Loading...");
        tagidlist_certification = new ArrayList<>();
        Utils utils = new Utils(CertificationActivity.this);

        String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getUserCertificationTags.php?UserID="+utils.getdata("Userid");
        new AsyncTask<String, Void, ArrayList>() {
            @Override
            protected ArrayList doInBackground(String... params) {
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");

                    HttpPost httppost = new HttpPost(params[0]);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    if(response!=null) {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            tagidlist_certification.add(jsonObject.getString("ActualTagID"));
                            position_Array_Certification.set(Integer.valueOf(jsonObject.getString("ActualTagID")) - 1, Integer.valueOf(jsonObject.getString("ActualTagID")) - 1);
                        }
                        return tagidlist_certification;
                    }
                    return null;

                } catch (Exception e) {
                    hidePB();
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList arrayList) {
                //super.onPostExecute(arrayList);

                if(arrayList!=null && arrayList.size()>0){
                    Data.tagidlistcertification = arrayList;
                }
                ItemListAdapterCertification adapter = new ItemListAdapterCertification(CertificationActivity.this,list);
                lv.setAdapter(adapter);
                lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                lv.setItemsCanFocus(false);
                hidePB();
            }
        }.execute(link, "", "");
    }


    //Footer tab Click Listener
    public void HomeClick(View view){
        Intent i = new Intent(CertificationActivity.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void BrowseClick(View view){
        Intent i = new Intent(CertificationActivity.this,BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(CertificationActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view){
        Intent i = new Intent(CertificationActivity.this,TrackList.class);
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
            try{

                ViewHolder_Agency viewHolder;
                if(convertView==null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_agency_row, null);
                    viewHolder = new ViewHolder_Agency(convertView);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder_Agency) convertView.getTag();
                }

                ListData_Agency data = getItem(position);
                viewHolder.title.setText(data.getTitle());

                if(position_Array_Certification.get(position)!=-1){
                    if(!list_check.get(position)){
                        position_Array_Certification.set(position,-1);
                    }else
                        list_check.set(position,true);
                }

                if(list_check.get(position)==true) {
                    viewHolder.checkbox.setVisibility(View.VISIBLE);
                    position_Array_Certification.set(position,position);
                }else {
                    viewHolder.checkbox.setVisibility(View.GONE);
                    //list_tick.remove(position);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        Intent intentback = new Intent();
        if(count>0)
            intentback.putExtra("certification_count", count + " selected");
        intentback.putIntegerArrayListExtra("Certification_pos_array", position_Array_Certification);
        setResult(Constants.request_certification, intentback);
        finish();

    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(CertificationActivity.this);
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
