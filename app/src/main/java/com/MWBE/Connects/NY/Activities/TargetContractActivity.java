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

import com.MWBE.Connects.NY.CheckBoxAdapter.ItemListAdapterContractValue;
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

public class TargetContractActivity extends Activity {

    private ListView lv;
    private ArrayList<ListData_Agency> list;
    private ArrayList<Boolean> list_check;
    private DataBaseHelper databasehelper;

    private ArrayList<Integer> positionArray = new ArrayList<>();
    private int count;
    private Utils utils;
    private String responsefromserver;
    private Context context = this;
    private ArrayList<String> tagidlist;
    private boolean isShow;
    private ProgressDialog pb;
    private boolean allchecked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_contract);
        init();
    }

    public void BackClick(View view){
        Intent intentback = new Intent();

        utils = new Utils(TargetContractActivity.this);
        intentback.putExtra("contract_count", count + " selected");
        intentback.putIntegerArrayListExtra("contract_pos_array", positionArray);
        setResult(Constants.request_target_contract_value, intentback);
        finish();
    }

    public void UpdateClick(View view) {
        try {
            showPB("Target Contract Value is being updated, please wait");
            count = 0;
            isShow = false;
            utils = new Utils(this);
            DeleteTargetContract(utils.getdata("Userid"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DeleteTargetContract(final String userid) {
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

                        String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/deleteUserContractTags.php?UserID="+userid;
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

        allchecked = checkBoolean(list_check);
        if(!allchecked) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder dialog1 = new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Thank you for updating your contract value!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intentback = new Intent();

                                    utils = new Utils(TargetContractActivity.this);
                                    intentback.putExtra("contract_count", count + " selected");
                                    intentback.putIntegerArrayListExtra("contract_pos_array", positionArray);
                                    setResult(Constants.request_target_contract_value, intentback);
                                    finish();
                                }
                            });

                    dialog1.show();
                }
            });
        }

        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String date = dateformat.format(new Date());
        for (int i = 0; i < positionArray.size(); i++) {
            if (list_check.get(i)) {
                String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addUserPreferences.php?UserID=" + utils.getdata("Userid") + "&SettingTypeID=" + getIntent().getIntExtra("SettingID", 0) + "&ActualTagID=" + (positionArray.get(i) + 1) + "&AddedDateTime=" + date;
                url = url.replace(" ","%20");
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
                count++;
            }
        }


        Data.poss_array_target = positionArray;
    }

    private boolean checkBoolean(ArrayList<Boolean> list_check) {
        for(boolean checked : list_check){
            if(checked){
                return true;
            }
        }

        return false;
    }

    private void checkResponse() {
        hidePB();
        isShow = true;
        if (responsefromserver.equalsIgnoreCase("Records Added.")) {
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("Thank you for updating your contract value!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intentback = new Intent();

                            utils = new Utils(TargetContractActivity.this);
                                intentback.putExtra("contract_count", count + " selected");
                            intentback.putIntegerArrayListExtra("contract_pos_array", positionArray);
                            setResult(Constants.request_target_contract_value, intentback);
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("Your contract value not been updated, please try again.")
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
        lv = (ListView) findViewById(R.id.list_TARGET_CONTACT_lv);
        databasehelper = new DataBaseHelper(TargetContractActivity.this);

        ((Button)findViewById(R.id.updatebtn)).getBackground().setAlpha(200);
        ((Button)findViewById(R.id.updatebtn)).setEnabled(false);
        try {
            databasehelper.createDataBase();
            databasehelper.openDataBase();
        populatelist();
            positionArray = Data.poss_array_target;
            if(positionArray.size()==0) {
                for (int i = 0; i < list_check.size(); i++) {
                    positionArray.add(-1);
                }
            }

            list_check = Data.list_check_target_contract;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void populatelist() {
        try {
            list = new ArrayList<ListData_Agency>();
            list_check = new ArrayList<Boolean>();
            final ArrayList<String> tagid_list = new ArrayList<>();
            getTargetContractSelection();
            Cursor cursor = databasehelper.getDataFromDB("","","ContractValueTags",false);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    list.add(new ListData_Agency(cursor.getString(2)));
                    tagid_list.add(cursor.getInt(0)+"");
                }
                Data.tagidlistcontractvalue_db = tagid_list;
            }


            for (int i = 0; i < list.size(); i++) {
                list_check.add(i, false);
            }

            /*if(getIntent().getStringExtra("target_contract_value")!=null){
                for(int i=0;i<list_check.size();i++){
                    if(getIntent().getStringExtra("target_contract_value").equalsIgnoreCase(list.get(i).getTitle())){
                        list_check.set(i,true);
                    }
                }
            }*/

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                       /* for (int i = 0; i < list.size(); i++) {
                        list_check.set(i, false);
                    }*/

                        ((Button)findViewById(R.id.updatebtn)).getBackground().setAlpha(255);
                        ((Button)findViewById(R.id.updatebtn)).setEnabled(true);
                        positionArray.set(position,position);
                        list_check.set(position, !list_check.get(position));

                        if(!list_check.get(position)){
                            if(tagid_list.get(position).length()>0){
                                tagid_list.remove(position);
                            }
                        }

                        //CustomListAdapter adapter = new CustomListAdapter(TargetContractActivity.this, R.layout.activity_agency, list);
                        //lv.setAdapter(adapter);

                        /*Intent intentback = new Intent();
                        intentback.putExtra("target_contract_value", list.get(position).getTitle());
                        setResult(Constants.request_target_contract_value, intentback);
                        finish();*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            //CustomListAdapter adapter = new CustomListAdapter(TargetContractActivity.this, R.layout.activity_agency, list);
            //lv.setAdapter(adapter);

            Data.list_check_target_contract = list_check;

            /*ItemListAdapterContractValue adapter = new ItemListAdapterContractValue(TargetContractActivity.this,list);
            lv.setAdapter(adapter);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lv.setItemsCanFocus(false);

            getTargetContractSelection();*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getTargetContractSelection() {
        showPB("Loading...");
        tagidlist = new ArrayList<>();
        Utils utils = new Utils(TargetContractActivity.this);

        String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getUserContractTags.php?UserID="+utils.getdata("Userid");
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
                            tagidlist.add(jsonObject.getString("ActualTagID"));
                            positionArray.set(Integer.valueOf(jsonObject.getString("ActualTagID")) - 1, Integer.valueOf(jsonObject.getString("ActualTagID")) - 1);
                        }
                        return tagidlist;
                    }
                    return null;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList arrayList) {
                if(arrayList!=null)
                if(arrayList.size()>0){
                    Data.tagidlistcontractvalue = arrayList;
                }else {
                    Data.tagidlistcontractvalue.clear();
                }
                ItemListAdapterContractValue adapter = new ItemListAdapterContractValue(TargetContractActivity.this,list);
                lv.setAdapter(adapter);
                lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                lv.setItemsCanFocus(false);
                hidePB();
            }
        }.execute(link, "", "");
    }


    //Footer tab Click Listener
    public void HomeClick(View view){
        Intent i = new Intent(TargetContractActivity.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void BrowseClick(View view){
        Intent i = new Intent(TargetContractActivity.this,BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(TargetContractActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view){
        Intent i = new Intent(TargetContractActivity.this,TrackList.class);
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
                /*if(list_check.get(position)==true) {
                    viewHolder.checkbox.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.checkbox.setVisibility(View.GONE);
                }*/

                if(positionArray.get(position)!=-1){
                    if(!list_check.get(position)){
                        positionArray.set(position,-1);
                    }else
                        list_check.set(position,true);
                }

                if(list_check.get(position)==true) {
                    viewHolder.checkbox.setVisibility(View.VISIBLE);
                    positionArray.set(position,position);
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

        utils = new Utils(TargetContractActivity.this);
        if(count>0)
            intentback.putExtra("contract_count", count + " selected");
        intentback.putIntegerArrayListExtra("contract_pos_array", positionArray);
        setResult(Constants.request_target_contract_value, intentback);
        finish();

    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(TargetContractActivity.this);
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
