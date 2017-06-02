package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.CheckBoxAdapter.SpinnerAdapter;
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

import java.util.Arrays;

/**
 * Created by Fazal on 7/26/2016.
 */
public class UpdateProfileActivity extends Activity implements TextWatcher {

    private Context context = this;
    private Utils utils;
    private EditText fname_et;
    private EditText lname_et;
    private EditText email_et;
    private EditText phno_et;
    private EditText address_et;
    private EditText city_et;
    private EditText state_et;
    private String responsefromserver;
    private EditText businessname_et;
    private EditText businessweb_et;
    private EditText zipcode_et;
    private Spinner state_spinner;
    private Button updatebtn;
    private ProgressDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile_fragment);
        init();
    }

        public void BackClick(View view){
            finish();
            startActivity(new Intent(this, SettingsMain.class));
        }

    private void getLoginDetails() {
        try {
            showPB("Loading Profile Details");
            String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getLoginDetailWithIDZipCode.php?UserID=" + utils.getdata("Userid");



            new AsyncTask<String, Void, JSONObject>() {
                @Override
                protected JSONObject doInBackground(String... params) {
                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        //showPB("Loading....");
                        HttpPost httppost = new HttpPost(params[0]);

                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        final String response = httpclient.execute(httppost,
                                responseHandler);

                        Log.i("Response", "Response : " + response);
                        JSONArray jsonarray = new JSONArray(response);
                        JSONObject jsonobj = jsonarray.getJSONObject(0);
                        if (jsonobj.getString("UserID") != null) {
                            utils.savedata("city",jsonobj.getString("UserCity"));
                            utils.savedata("state",jsonobj.getString("UserState"));
                            utils.savedata("buisnessname",jsonobj.getString("BusinessName"));
                            utils.savedata("buisnessweb",jsonobj.getString("BusinessWebsite"));
                            return jsonobj;
                        }


                        return null;
                    } catch (Exception e) {
                        e.printStackTrace();
                        hidePB();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.AlertInternetConnection(UpdateProfileActivity.this);
                            }
                        });
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(JSONObject s) {
                    super.onPostExecute(s);
                    try{
                        String fname = s.getString("UserFirstName");
                        String lname = s.getString("UserLastName");
                        String email = s.getString("UserEmailAddress");
                        String phno = s.getString("UserMobilePhone");
                        String city = s.getString("UserCity");
                        String state = s.getString("UserState");
                        String address = s.getString("UserAddressLine1");
                        address = address.replace("%2B","+");
                        address = address.replace("%23","#");
                        String buisnessname = s.getString("BusinessName");
                        buisnessname = buisnessname.replace("%2B","+");
                        buisnessname = buisnessname.replace("%23","#");
                        String buisnessweb = s.getString("BusinessWebsite");
                        String zipcode = s.getString("UserZipCode");
                        fname_et.setText(fname);
                        lname_et.setText(lname);
                        email_et.setText(email);
                        phno_et.setText(phno);
                        address_et.setText(address);
                        city_et.setText(city);
                        //state_et.setText(state);
                        String[] array = getResources().getStringArray(R.array.countries_array);
                        for(int i=0;i<array.length;i++){
                            if(array[i].equalsIgnoreCase(state))
                            state_spinner.setSelection(i);
                        }
                        zipcode_et.setText(zipcode);
                        businessname_et.setText(buisnessname);
                        businessweb_et.setText(buisnessweb);
                        hidePB();
                        updatebtn.setEnabled(false);
                        updatebtn.getBackground().setAlpha(200);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                }
            }.execute(url, "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init() {
        utils = new Utils(this);
        fname_et = (EditText) findViewById(R.id.name);
        lname_et = (EditText) findViewById(R.id.lastname);
        email_et = (EditText) findViewById(R.id.email);
        phno_et = (EditText) findViewById(R.id.phno);
        address_et = (EditText) findViewById(R.id.address);
        city_et = (EditText) findViewById(R.id.city);
        //state_et = (EditText) findViewById(R.id.state);
        state_spinner = (Spinner) findViewById(R.id.state);
        businessname_et = (EditText) findViewById(R.id.businessname);
        businessweb_et = (EditText) findViewById(R.id.businessweb);
        zipcode_et = (EditText) findViewById(R.id.zipcode);
        populateSpinner();

        getLoginDetails();

        zipcode_et.addTextChangedListener(this);
        phno_et.addTextChangedListener(this);
        address_et.addTextChangedListener(this);
        businessname_et.addTextChangedListener(this);
        businessweb_et.addTextChangedListener(this);
        city_et.addTextChangedListener(this);

        updatebtn = (Button) findViewById(R.id.update);

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateClick();
            }
        });


    }

    private void populateSpinner() {
        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

        SpinnerAdapter adapter = new SpinnerAdapter(UpdateProfileActivity.this,R.layout.update_profile_fragment,
                Arrays.asList(getResources().getStringArray(R.array.countries_array)));

// Apply the adapter to the spinner
        state_spinner.setAdapter(adapter);

        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updatebtn.setEnabled(true);
                updatebtn.getBackground().setAlpha(255);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void UpdateClick() {
        showPB("Loading...");
        if (fname_et.getText().length() > 0 && lname_et.getText().length() > 0 && email_et.getText().length() > 0) {
            String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/updateUserProfileZipCode.php?UserEmailAddress=" + email_et.getText().toString() +
                    "&UserFirstName=" + fname_et.getText().toString() + "&UserLastName=" + lname_et.getText().toString() +
                    "&UserMobilePhone=" + phno_et.getText().toString() + "&UserAddressLine1=" + address_et.getText().toString() +
                    "&UserCity=" + city_et.getText().toString() + "&UserState=" + state_spinner.getSelectedItem() + "&BusinessWebsite="
                    +businessweb_et.getText().toString()+"&BusinessName="+businessname_et.getText().toString()+"&UserZipCode="+zipcode_et.getText().toString();
            url = url.replace("+","%2B");
            url = url.replace("#","%23");
            url = url.replace(" ","%20");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equalsIgnoreCase("Records Updated.")) {
                                hidePB();
                                new AlertDialog.Builder(context)
                                        .setTitle("Alert!")
                                        .setMessage("Thank You. Your profile has been updated.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                                startActivity(new Intent(UpdateProfileActivity.this, SettingsMain.class));
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            } else {
                                hidePB();
                                new AlertDialog.Builder(context)
                                        .setTitle("Alert!")
                                        .setMessage("There is a problem with getting response from server,please try again")
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
                            /*new AlertDialog.Builder(context)
                                    .setTitle("Alert!")
                                    .setMessage("Check your Internet Connection")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();*/
                            Utils.AlertInternetConnection(UpdateProfileActivity.this);
                            //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                        }
                    });

                    Volley.newRequestQueue(UpdateProfileActivity.this).add(stringRequest);




        }else {
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("All the Field required to proceed further")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    //Footer Click Listener
    public void HomeClick(View view){
        Intent i = new Intent(UpdateProfileActivity.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void BrowseClick(View view){
        Intent i = new Intent(UpdateProfileActivity.this,BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view){
        Intent i = new Intent(UpdateProfileActivity.this,TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(UpdateProfileActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        updatebtn.setEnabled(true);
        updatebtn.getBackground().setAlpha(255);
    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(UpdateProfileActivity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, SettingsMain.class));
    }
}
