package com.MWBE.Connects.NY.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.JavaBeen.CapabilitiesMaster;
import com.MWBE.Connects.NY.Storage.Storage;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.CheckBoxAdapter.SpinnerAdapter;
import com.MWBE.Connects.NY.JavaBeen.UpdateData;
import com.MWBE.Connects.NY.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Fazal on 7/28/2016.
 */
public class GeneralInformationSetup extends Fragment {

    private Button nextbtn;
    private EditText fname_et;
    private EditText lname_et;
    private EditText email_et;
    private EditText phno_et;
    private EditText address_et;
    private EditText city_et;
    private EditText state_et;
    private Spinner state_spinner;
    private EditText buisnessname_et;
    private EditText buisnessweb_et;
    private EditText zipcode_et;
    private String address = "NA";
    private String phno = "NA";
    private String city = "NA";
    private String spinnertext = "NA";
    private String buisnessname = "NA";
    private String buisnessweb = "NA";
    private String zipcode = "NA";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.general_info_setup_fragment,container,false);
        getDataFromServer();

        popup(rootview);
        init(rootview);
        NextClick(rootview);
        return rootview;
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
                                    //populatelist();
                                }
                            });
                            //hidePB();
                        }else {
                            //hidePB();
                        }
                    } catch (Exception e) {
                        //hidePB();
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute("http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getCapabalitiesData.php?Records=");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void popup(View rootview) {
        if(Storage.showpopup==1) {
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("Alert!")
                    .setMessage("MWBE Connect NY uses a special matching technology to provide you with a list of RFPs that specifically " +
                            "apply to your business. In order to take advantage of the matching capabilities and discover as many " +
                            "RFPs as possible, it is important that you fully complete your profile. " +
                            "The RFPs generated will be based on the information you use to describe your business. " +
                            "Follow this “Getting Started” guide to help you build your profile step-by-step, and to " +
                            "get more tips on how to use other resources within the app!")
                    .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            Storage.showpopup = 0;
            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
            Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "gotham_book.otf");
            textView.setTypeface(face);
        }
    }

    private void init(View rootview) {
        fname_et = (EditText) rootview.findViewById(R.id.fname);
        lname_et = (EditText) rootview.findViewById(R.id.lastname);
        email_et = (EditText) rootview.findViewById(R.id.email);
        phno_et = (EditText) rootview.findViewById(R.id.phno);
        address_et = (EditText) rootview.findViewById(R.id.address);
        city_et = (EditText) rootview.findViewById(R.id.city);
        //state_et = (EditText) rootview.findViewById(R.id.state);
        state_spinner = (Spinner) rootview.findViewById(R.id.state);
        buisnessname_et = (EditText) rootview.findViewById(R.id.businessname);
        buisnessweb_et = (EditText) rootview.findViewById(R.id.businessweb);
        zipcode_et = (EditText) rootview.findViewById(R.id.zipcode);
        nextbtn = (Button) rootview.findViewById(R.id.next);
        Utils utils = new Utils(getActivity());
        fname_et.setText(utils.getdata("fname"));
        lname_et.setText(utils.getdata("lname"));
        email_et.setText(utils.getdata("email"));

        SpinnerFocus();

        populateSpinner();
        if(Storage.updateData!=null) {
            phno_et.setText(Storage.updateData.getPhno());
            buisnessname_et.setText(Storage.updateData.getBuisnessname());
            address_et.setText(Storage.updateData.getBuisnessaddress());
            buisnessweb_et.setText(Storage.updateData.getBuisnessweb());
            //state_et.setText(Storage.updateData.getState());
            String[] array = getResources().getStringArray(R.array.countries_array);
            for(int i=0;i<array.length;i++){
                if(array[i].equalsIgnoreCase(Storage.updateData.getState()))
                    state_spinner.setSelection(i);
            }
            //state_spinner.setSelection(Storage.updateData.getState());
            city_et.setText(Storage.updateData.getCity());
            zipcode_et.setText(Storage.updateData.getZipcode());
        }



    }

    private void SpinnerFocus() {
        city_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(city_et.getWindowToken(), 0);
                    //textView.clearFocus();
                    state_spinner.requestFocus();
                    //state_spinner.performClick();
                }
                return true;
            }
        });
    }

    private void populateSpinner() {
       /* ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

        SpinnerAdapter adapter = new SpinnerAdapter(getActivity(),R.layout.update_profile_fragment,
                Arrays.asList(getResources().getStringArray(R.array.countries_array)));


// Apply the adapter to the spinner
        state_spinner.setAdapter(adapter);
    }

    private void NextClick(View rootview) {
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    setupValues();

                    /*UpdateData updatedata = new UpdateData(fname_et.getText().toString(),lname_et.getText().toString(),email_et.getText().toString(),
                            address_et.getText().toString(),phno_et.getText().toString(),city_et.getText().toString(),state_spinner.getSelectedItem().toString(),
                            buisnessname_et.getText().toString(),buisnessweb_et.getText().toString(),zipcode_et.getText().toString());*/

                    UpdateData updatedata = new UpdateData(fname_et.getText().toString(),lname_et.getText().toString(),email_et.getText().toString(),
                            address,phno,city,spinnertext,buisnessname,buisnessweb,zipcode);
                    Storage.updateData = updatedata;
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.setup,new GeographicCoverageSetup()).addToBackStack("tag").commit();
                }else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Alert!")
                            .setMessage("First and Last Name, Email, Phone & Business Name are required to proceed further.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }

            }
        });
    }

    private boolean validate() {
        if(fname_et.getText().length() == 0 || lname_et.getText().length() == 0
                || email_et.getText().length() == 0 || phno_et.getText().length() == 0
                || buisnessname_et.getText().length() == 0){
            return false;
        }

        return true;
    }

    private void setupValues() {
        if(address_et.length()>0){
            address = address_et.getText().toString();
        }

        if(phno_et.length()>0){
            phno = phno_et.getText().toString();
        }

        if(city_et.length()>0){
            city = city_et.getText().toString();
        }

        if(!state_spinner.getSelectedItem().toString().equalsIgnoreCase("Select State")){
            spinnertext = state_spinner.getSelectedItem().toString();
        }

        if(buisnessname_et.length()>0){
            buisnessname = buisnessname_et.getText().toString();
        }

        if(buisnessweb_et.length()>0){
            buisnessweb = buisnessweb_et.getText().toString();
        }

        if(zipcode_et.length()>0){
            zipcode = zipcode_et.getText().toString();
        }

    }
}
