package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.CustomViews.CustomButton;
import com.MWBE.Connects.NY.JavaBeen.ListData_track_comnt;
import com.MWBE.Connects.NY.JavaBeen.ViewHolder_TrackComment;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fazal on 6/28/2016.
 */
public class MessagingFragments extends Activity implements TextWatcher {

    private ListView lv;
    private ArrayList<ListData_track_comnt> list_cmnt;
    private Context context;
    private EditText requesttext;
    private Utils utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging_fragment);
        init();
    }

    public void BackClick(View view){
        onBackPressed();
    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = this;
        utils = new Utils(this);
        lv = (ListView) findViewById(R.id.list_tracking_cmnt);
        requesttext = (EditText) findViewById(R.id.request_text_et);

        list_cmnt = new ArrayList<ListData_track_comnt>();
        populatelist();
    }

    private void populatelist() {

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

// This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                list_cmnt.clear();
                getData();
            }
        }, 0, 5, TimeUnit.SECONDS);

        //getData();
        /*list_cmnt.add(new ListData_track_comnt("Do i need to file a Broker certification", R.drawable.person));
        list_cmnt.add(new ListData_track_comnt("Yes - this is required for contract over", R.drawable.bulb));
        list_cmnt.add(new ListData_track_comnt("Thank you!", R.drawable.person));*/
       /* CustomListAdapterComment adaptercomment = new CustomListAdapterComment(getActivity(),R.layout.list_track_row,list_cmnt);
        lv.setAdapter(adaptercomment);*/
        listClick();
        SendClick();
    }

    private void listClick() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(context)
                        .setTitle("Message")
                        .setMessage(list_cmnt.get(position).getTitle())
                        .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_email)
                        .show();
            }
        });
    }

    private void getData() {

        final Thread getData_thread = new Thread(new Runnable() {
            @Override
            public void run() {

                String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getUserRequests.php?UserID=" +utils.getdata("Userid")+
                        "&ProcurementID="+ProcurementDetailsActivity.getprocid();
                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");
                    HttpPost httppost = new HttpPost(url);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    String RequestText = null;
                    JSONArray jsonarray = new JSONArray(response);
                    for(int i=0;i<jsonarray.length();i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        RequestText = jsonobj.getString("RequestText");
                        RequestText = RequestText.replace("u0027","'");
                        if(jsonobj.getString("UniqueID").equalsIgnoreCase("1")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((TextView)findViewById(R.id.message_head)).setVisibility(View.GONE);
                                }
                            });
                            list_cmnt.add(new ListData_track_comnt(RequestText, R.drawable.bulb));
                        }else
                        list_cmnt.add(new ListData_track_comnt(RequestText, R.drawable.person));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CustomListAdapterComment adaptercomment = new CustomListAdapterComment(MessagingFragments.this, R.layout.list_track_row, list_cmnt);
                            lv.setAdapter(adaptercomment);
                            lv.setSelection(adaptercomment.getCount() - 1);
                        }
                    });

                }catch (Exception e){
                        e.printStackTrace();
                    }
            }
        });
        getData_thread.start();


    }

    private void SendClick() {
            ((CustomButton) findViewById(R.id.sendbtn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (requesttext.getText().length() > 0) {
                        utils = new Utils(MessagingFragments.this);
                        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                        Date date = new Date();
                        ProcurementDetailsActivity procurementDetailsActivity = new ProcurementDetailsActivity();
                        String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addUserRequest.php?RequestText=" + requesttext.getText().toString() +
                                "&UserID=" + utils.getdata("Userid") + "&RequestAddedDateTime=" + dateformat.format(date)+"&ProcurementID="+ProcurementDetailsActivity.getprocid();
                        url = url.replace(" ", "%20");
                        url = url.replace("'","''");
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equalsIgnoreCase("Records Added.")) {

                                    list_cmnt.add(new ListData_track_comnt(requesttext.getText().toString(), R.drawable.person));
                                    requesttext.setText("");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            CustomListAdapterComment adaptercomment = new CustomListAdapterComment(MessagingFragments.this, R.layout.list_track_row, list_cmnt);
                                            lv.setAdapter(adaptercomment);
                                        }
                                    });

                            /*new AlertDialog.Builder(context)
                                    .setTitle("Alert!")
                                    .setMessage("Record Added Successfully")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();*/


                                } else {
                                    //hidePB();
                                    new AlertDialog.Builder(context)
                                            .setTitle("Alert!")
                                            .setMessage("No Record Added, Please Try Again.")
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
                                error.printStackTrace();
                                new AlertDialog.Builder(context)
                                        .setTitle("Alert!")
                                        .setMessage("No Record Added, Please Try Again.")
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

                        Volley.newRequestQueue(MessagingFragments.this).add(stringRequest);
                    }else {
                        new AlertDialog.Builder(context)
                                .setTitle("Alert!")
                                .setMessage("At First, enter your comment")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            });
    }


    //Footer Click Listener
    public void BrowseClick(View view){
        Intent i = new Intent(this,BrowseActivity.class);
        startActivity(i);
    }

    public void SettingsClick(View view){
        Intent i = new Intent(this,SettingsActivity.class);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(this,ResourceActivity.class);
        startActivity(i);
    }

    public void HomeClick(View view){
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(requesttext.getEditableText()==editable){
            int pos = 0;
            boolean capitalize = true;
            StringBuilder sb = new StringBuilder(requesttext.getText().toString());
            while (pos < sb.length()) {
                if (sb.charAt(pos) == '.') {
                    capitalize = true;
                } else if (capitalize && !Character.isWhitespace(sb.charAt(pos))) {
                    sb.setCharAt(pos, Character.toUpperCase(sb.charAt(pos)));
                    capitalize = false;
                }
                pos++;
            }

            requesttext.setText(sb.toString());
        }
    }

    public class CustomListAdapterComment extends ArrayAdapter<ListData_track_comnt> {

        public CustomListAdapterComment(Context context, int resource, List<ListData_track_comnt> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            try{
                ViewHolder_TrackComment viewHolder;
                if(convertView==null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_track_cmnt_row, null);
                    viewHolder = new ViewHolder_TrackComment(convertView);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder_TrackComment) convertView.getTag();
                }

                ListData_track_comnt data = getItem(position);
                viewHolder.title.setText(data.getTitle());
                viewHolder.image.setImageResource(data.getImage());

            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }


    }


}
