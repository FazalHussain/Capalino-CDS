package com.MWBE.Connects.NY.Activities;

import net.authorize.sampleapplication.LoginActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class SubscribtionActivity extends Activity {

    private EditText quarter_et;
    private EditText annual_et;
    private EditText promocode_et;
    private Utils utils;
    private String paymentStatus;
    private Date quarterdate;
    private Date annualdate;
    private String expiry;
    private Context context = this;
    private String PaymentStatus;
    private String ExpirationDate;
    private boolean isquarter;
    private boolean isannual;
    private String DiscountPercentage;
    private String PromoType;
    private Button promocode_btn;
    private ProgressDialog pb;
    private String pass;
    private String PromoCodeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribtion);
        init();
    }

    private void init() {
        utils = new Utils(this);
        quarter_et = (EditText) findViewById(R.id.quarterly_subscribtion_et);
        annual_et = (EditText) findViewById(R.id.annual_subscription_et);
        promocode_et = (EditText) findViewById(R.id.promocode);
        promocode_btn = (Button) findViewById(R.id.promocode_btn);
        //HideKeyboard();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getData();
        getLoginDetails();
    }

    private void HideKeyboard() {
        promocode_et.clearFocus();
        InputMethodManager in = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(promocode_et.getWindowToken(), 0);
    }

    public void PromoCodeClick(View view){
        HideKeyboard();
        if((isannual || isquarter) ){
            if(promocode_et.length()>0)
            getDiscountFromServer(promocode_et.getText().toString());
            else {
                new AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setMessage("Please enter a promo code.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }else {
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("Please select payment type to proceed further.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    public void BackClick(View view){
        onBackPressed();
    }

    private void getLoginDetails() {
        final Thread getLogin_thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    char[] chars = utils.getdata("pass").toCharArray();
                    pass = "";
                    for (char c : chars) {
                        if (c == '#') {
                            pass += "%23";
                        } else if (c == '^') {
                            pass += "%5E";
                        } else if (c == '%') {
                            pass += "%25";
                        } else {
                            pass += c;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getLogin.php?UserEmailAddress=" + utils.getdata("email")
                        + "&UserPassword=" + pass;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");
                    HttpPost httppost = new HttpPost(url);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    JSONArray jsonarray = new JSONArray(response);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonObj = jsonarray.getJSONObject(i);
                        utils.savedata("fname", jsonObj.getString("UserFirstName"));
                        utils.savedata("lname", jsonObj.getString("UserLastName"));
                        utils.savedata("city", jsonObj.getString("UserCity"));
                        utils.savedata("state", jsonObj.getString("UserState"));
                        utils.savedata("Address", jsonObj.getString("UserAddressLine1"));
                    }

                } catch (Exception e) {
                    hidePB();
                    e.printStackTrace();
                }
            }
        });
        getLogin_thread.start();
    }

    public void AnnualSubscriptionClick(View view){
        //startActivity(new Intent(this, LoginActivity.class));
        /*Intent intent = new Intent();
        intent.setComponent(new ComponentName("net.authorize.sampleapplication", "net.authorize.sampleapplication.LoginActivity"));
        startActivity(intent);*/

        Drawable img = this.getResources().getDrawable( R.drawable.checked );
        annual_et.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);
        quarter_et.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0, 0);
        paymentStatus = "Annually";
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        annualdate = cal.getTime();
        expiry = dateformat.format(annualdate);
        isannual = true;
        isquarter = false;

        promocode_et.setEnabled(true);
        promocode_et.setFocusable(true);
        promocode_et.setFocusableInTouchMode(true);
        promocode_btn.setEnabled(true);

        if(promocode_et.getText().length()>0)
        UpdateDiscountTextView(PromoType);

    }

    public void QuarterSubscriptionClick(View view){
        Drawable img = this.getResources().getDrawable( R.drawable.checked );
        quarter_et.setCompoundDrawablesWithIntrinsicBounds( null, null, img, null);
        annual_et.setCompoundDrawablesWithIntrinsicBounds(0 , 0, 0, 0);
        paymentStatus = "Quarterly";
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 3);
        quarterdate = cal.getTime();
        expiry = dateformat.format(quarterdate);
        isannual = false;
        isquarter = true;

        promocode_et.setEnabled(true);
        promocode_et.setFocusable(true);
        promocode_et.setFocusableInTouchMode(true);
        promocode_btn.setEnabled(true);
        if(promocode_et.getText().length()>0)
        UpdateDiscountTextView(PromoType);

        //Log.d("date",dateformat.format(new Date()));
        //SendPromoCodeData("1","846","0.01",dateformat.format(new Date()));



        //startActivity(new Intent(this, LoginActivity.class));
    }




    public void getDiscountFromServer(String promocode){
        new AsyncTask<String,Void,Void>(){

            @Override
            protected Void doInBackground(String... params) {
                try{
                    HttpClient httpclient1 = new DefaultHttpClient();



                    String link = params[0];
                    link = link.replace(" ","%20");
                    HttpGet httppost = new HttpGet(link);



                    ResponseHandler<String> responseHandler = new BasicResponseHandler();

                    String response = httpclient1.execute(httppost,
                            responseHandler);


                    Log.d("Response", "Response : " + response);
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                        if(jsonObject.has("PromoCode_status")){
                            if(jsonObject.getString("PromoCode_status").equalsIgnoreCase("Invalid")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DiscountPercentage = "";

                                        new AlertDialog.Builder(context)
                                                .setTitle("Alert!")
                                                .setMessage("This is incorrect code. Please enter correct promo code. Thanks.")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .setCancelable(false)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();

                                        ((TextView)findViewById(R.id.note_promocode)).setVisibility(View.INVISIBLE);
                                        //((TextView)findViewById(R.id.note_promocode)).setText("This is incorrect code. Please enter correct promo code. Thanks.");
                                    }
                                });
                            }

                            if(jsonObject.getString("PromoCode_status").equalsIgnoreCase("Expired")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DiscountPercentage = "";
                                        ((TextView)findViewById(R.id.note_promocode)).setVisibility(View.INVISIBLE);
                                        //((TextView)findViewById(R.id.note_promocode)).setText("Your Promo code's validity has expired");


                                        new AlertDialog.Builder(context)
                                                .setTitle("Alert!")
                                                .setMessage("Your Promo code's validity has expired.")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .setCancelable(false)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }
                                });
                            }
                        }

                        final String Status = jsonObject.getString("Status");
                        PromoType = jsonObject.getString("PromoType");
                        PromoCodeID = jsonObject.getString("PromoCodeValue");
                        DiscountPercentage = jsonObject.getString("DiscountPercentage");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(Status.equalsIgnoreCase("Active")){
                                    //((TextView)findViewById(R.id.note_promocode)).setVisibility(View.VISIBLE);
                                    if(PromoType.equalsIgnoreCase("%")){
                                        //((TextView)findViewById(R.id.note_promocode)).setText(DiscountPercentage+"% discount has been applied. Please tap PAY to securely make your payment.");
                                        new AlertDialog.Builder(context)
                                                .setTitle("Alert!")
                                                .setMessage("Discount has been applied. Please tap PAY to securely make your payment.")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .setCancelable(false)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();

                                    }
                                    else
                                    {
                                        new AlertDialog.Builder(context)
                                                .setTitle("Alert!")
                                                .setMessage("Discount has been applied. Please tap PAY to securely make your payment.")
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .setCancelable(false)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }
                                        //((TextView)findViewById(R.id.note_promocode)).setText("$"+DiscountPercentage+" discount has been applied. Please tap PAY to securely make your payment.");
                                   UpdateDiscountTextView(PromoType);
                                }else {
                                    DiscountPercentage = "";
                                    //((TextView)findViewById(R.id.note_promocode)).setVisibility(View.VISIBLE);
                                    //((TextView)findViewById(R.id.note_promocode)).setText("Your Promo code's validity has expired");
                                }
                            }


                        });


                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        }.execute("http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/checkPromo.php?PromoCodeValue="+promocode);
    }

    private void UpdateDiscountTextView(String promoType) {
        if(promoType!=null && !promoType.equalsIgnoreCase("")){
            try{
                if(isannual){
                    if (promoType.equalsIgnoreCase("%") ){
                        float discount = Float.valueOf(600) * (Float.valueOf(DiscountPercentage))/100;
                        float totalammount =  (Float.valueOf(600) - discount);
                        String result = String.format("%.02f",totalammount);
                        ((TextView)findViewById(R.id.note_promocode)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.note_promocode)).setText("Discounted Price: $"+String.valueOf(result));
                        //annual_et.setText(String.valueOf(totalammount));
                    }else {
                        float totalammount =  Float.valueOf(600) - Float.valueOf(DiscountPercentage);
                        String result = String.format("%.02f",totalammount);
                        ((TextView)findViewById(R.id.note_promocode)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.note_promocode)).setText("Discounted Price: $"+String.valueOf(result));
                        //annual_et.setText(String.valueOf(totalammount));

                    }
                }

                if(isquarter){
                    if (promoType.equalsIgnoreCase("%") ){
                        float discount = Float.valueOf(225) * (Float.valueOf(DiscountPercentage))/100;
                        float totalammount =  (Float.valueOf(225) - discount);
                        String result = String.format("%.02f",totalammount);
                        ((TextView)findViewById(R.id.note_promocode)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.note_promocode)).setText("Discounted Price: $"+String.valueOf(result));
                        //quarter_et.setText(String.valueOf(totalammount));
                    }else {
                        float totalammount =  Integer.valueOf(225) - Float.valueOf(DiscountPercentage);
                        String result = String.format("%.02f",totalammount);
                        ((TextView)findViewById(R.id.note_promocode)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.note_promocode)).setText("Discounted Price: $"+String.valueOf(result));
                        //quarter_et.setText(String.valueOf(totalammount));

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    public void PayClick(View view) {


        if(isannual || isquarter){
            Intent i = new Intent(this, LoginActivity.class);
            i.putExtra("isannual",String.valueOf(isannual));
            i.putExtra("isquarter",String.valueOf(isquarter));
            i.putExtra("paymentStatus",paymentStatus);
            i.putExtra("Userid",utils.getdata("Userid"));
            i.putExtra("email",utils.getdata("email"));
            i.putExtra("fname",utils.getdata("fname"));
            i.putExtra("lname",utils.getdata("lname"));
            i.putExtra("city",utils.getdata("city"));
            i.putExtra("Address",utils.getdata("Address"));
            i.putExtra("state",utils.getdata("state"));
            i.putExtra("expiry",expiry);
            i.putExtra("PromoType",PromoType);
            i.putExtra("PromoCodeID",PromoCodeID);
            i.putExtra("discount",DiscountPercentage);


            startActivity(i);

        }else {
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("Please select payment type for proceed further.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }

    private void getData() {

        final Thread getData_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                showPB("Loading...");
                //String url = "https://celeritas-solutions.com/cds/capalinoapp/apis/getPaymentStatus.php?UserID=" +utils.getdata("Userid");
                  String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getPaymentStatus.php?UserID=" +utils.getdata("Userid");
                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");
                    HttpPost httppost = new HttpPost(url);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    JSONArray jsonarray = new JSONArray(response);
                    if(jsonarray.length()==0){
                        hidePB();
                        return;
                    }
                    //hidePB();
                    for(int i=0;i<jsonarray.length();i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        PaymentStatus = jsonobj.getString("PaymentStatus");
                        ExpirationDate = jsonobj.getString("ExpirationDate");
                        }
                    if(ExpirationDate!=null)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(PaymentStatus.equalsIgnoreCase("Trial")){
                                ((TextView)findViewById(R.id.currentversion_title)).setText("You are currently using trial version which will expire on: "+ExpirationDate);
                            }else {
                                ((TextView)findViewById(R.id.currentversion_title)).setText("You are currently using paid version which will expire on: "+ExpirationDate);
                            }
                            hidePB();
                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                            try {
                                cal.setTime(sdf.parse(ExpirationDate));
                                cal.add(Calendar.MONTH, 3);
                            } catch (ParseException e) {
                                hidePB();
                                e.printStackTrace();
                            }
                            ((TextView)findViewById(R.id.quarter_expiry_date)).setText("will expire on "+sdf.format(cal.getTime()));
                            cal.add(Calendar.MONTH, 9);
                            ((TextView)findViewById(R.id.annual_expiry_date)).setText("will expire on "+sdf.format(cal.getTime()));

                            if(Data.DateExpire){
                                ((TextView)findViewById(R.id.currentversion_title)).setText("Your trial/paid subscription has expired.");
                            }



                        }
                    });

                }catch (Exception e){
                    hidePB();
                    e.printStackTrace();
                }
            }
        });
        getData_thread.start();

/*
        String url = "http://celeritas-solutions.com/cds/capalinoapp/apis/getUserRequests.php?UserID=" +utils.getdata("Userid");
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");
                    HttpPost httppost = new HttpPost(params[0]);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    String RequestText = null;
                    JSONArray jsonarray = new JSONArray(response);
                    for(int i=0;i<jsonarray.length();i++) {
                        JSONObject jsonobj = jsonarray.getJSONObject(i);
                        RequestText = jsonobj.getString("RequestText");
                        list_cmnt.add(new ListData_track_comnt(RequestText, R.drawable.person));
                    }

                    return RequestText;
                }catch (Exception e){
                    e.printStackTrace();
                    return "";
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(!s.equalsIgnoreCase("")){
                    CustomListAdapterComment adaptercomment = new CustomListAdapterComment(getActivity(),R.layout.list_track_row,list_cmnt);
                    lv.setAdapter(adaptercomment);
                }
            }
        }.execute(url,"","");
*/
    }

    //Footer Click Listener
    public void HomeClick(View view) {
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void BrowseClick(View view) {
        Intent i = new Intent(this, BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view) {
        Intent i = new Intent(this, TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view) {
        Intent i = new Intent(this, ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(SubscribtionActivity.this);
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
    }


}
