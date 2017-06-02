package com.MWBE.Connects.NY.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.MWBE.Connects.NY.Activities.CapablitiesActivity;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.CheckBoxAdapter.SpinnerAdapterAddOther;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Fazal on 8/9/2016.
 */
public class AddOthersCapabilitiesFragment extends Fragment {

    private Spinner capabilities_spinner;
    private int settingTypeID;
    private EditText others_editext;
    private ProgressDialog pb;
    Activity activity = getActivity();
    private List<String> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.add_others_capabilities_fragment,container,false);
        init(rootview);
        backClick(rootview);
        AddCapabilitiesClick(rootview);
        return rootview;
    }

    private void backClick(View rootview){
        getActivity().getSupportFragmentManager().beginTransaction().remove(new AddOthersCapabilitiesFragment()).commit();
        ((ImageView)rootview.findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(others_editext.getWindowToken(), 0);
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
                Intent i = new Intent(getActivity(), CapablitiesActivity.class);
                i.putExtra("status1","fromAddothers");
                startActivity(i);
            }
        });
    }

    private void init(View rootview) {
        list = Arrays.asList(getResources().getStringArray(R.array.capabilities_array));
        capabilities_spinner = (Spinner) rootview.findViewById(R.id.capabilities_spinner);
        others_editext = (EditText) rootview.findViewById(R.id.otherstext);
        populatespinner();
    }

    private void populatespinner() {
       /* ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.capabilities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/

        SpinnerAdapterAddOther adapter = new SpinnerAdapterAddOther(getActivity(),R.layout.update_profile_fragment,
                list);

// Apply the adapter to the spinner
        capabilities_spinner.setAdapter(adapter);

        capabilities_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                long item = parent.getItemIdAtPosition(position);
                int ids = (int) item;
                switch (ids) {
                    case 1: {
                        settingTypeID = 100;
                        break;
                    }

                    case 2: {
                        settingTypeID = 200;
                        break;
                    }

                    case 3: {
                        settingTypeID = 300;
                        break;
                    }

                    case 4: {
                        settingTypeID = 400;
                        break;
                    }

                    case 5: {
                        settingTypeID = 500;
                        break;
                    }

                    case 6: {
                        settingTypeID = 600;
                        break;
                    }

                    case 7: {
                        settingTypeID = 700;
                        break;
                    }

                    case 8: {
                        settingTypeID = 800;
                        break;
                    }

                    case 9: {
                        settingTypeID = 900;
                        break;
                    }

                    case 10: {
                        settingTypeID = 1000;
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void AddCapabilitiesClick(final View rootView){
        ((Button)rootView.findViewById(R.id.addcapabilities)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(others_editext.getText().toString()!=null && !others_editext.getText().toString().equalsIgnoreCase("") && settingTypeID>0){
                    AddCapabilities();
                }else {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Alert!")
                            .setMessage("All the fields are required to proceed further.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }
        });
    }

    private void AddCapabilities() {
        try {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(others_editext.getWindowToken(), 0);
            showPB("Loading...");
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            Date date = new Date();
            Utils utils = new Utils(getActivity());
            String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addUserOtherCapabilities.php?UserID=" + utils.getdata("Userid") +
                    "&SettingTypeID="+settingTypeID+"&TagText="+others_editext.getText().toString()+"&AddedDateTime="+dateformat.format(date);
            url=url.replace(" ","%20");
            url = url.replace("\n","");
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        //showPB("Loading....");
                        HttpPost httppost = new HttpPost(params[0]);

                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        final String response = httpclient.execute(httppost,
                                responseHandler);

                        Log.i("Response", "Response : " + response);


                        return response;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    hidePB();
                    if(s!=null)
                    if (s.equalsIgnoreCase("Records Added.")) {
                       new AlertDialog.Builder(getActivity())
                                .setTitle("Congratulations!")
                                .setMessage("Thank you for submitting a new capability. Our team will review and approve your request within the next 24 hours and once approved, the new tag will be available for selection. " +
                                        "Please select “Other” in the same industry category to continue browsing RFPs that match your procurement preferences.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
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
                                        getActivity().getSupportFragmentManager().beginTransaction().remove(new AddOthersCapabilitiesFragment()).commit();
                                        Intent i = new Intent(getActivity(), CapablitiesActivity.class);
                                        i.putExtra("status1","fromAddothers");
                                        startActivity(i);
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }

                    //getLoginDetails();
                }
            }.execute(url, "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void showPB(final String message) {

       getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(getActivity());
                pb.setMessage(message);
                pb.setCancelable(false);
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
}
