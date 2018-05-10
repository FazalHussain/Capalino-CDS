package com.MWBE.Connects.NY.Activities;

import net.authorize.aim.Result;
import net.authorize.sampleapplication.IContractor;
import net.authorize.sampleapplication.LoginActivity;
import net.authorize.sampleapplication.NavigationActivity;
import net.authorize.sampleapplication.TransactionResultActivity;
import net.authorize.sampleapplication.models.ContractorModel;
import net.authorize.sampleapplication.models.StaticData;

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
import android.widget.Toast;

import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.PurchaseEvent;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Random;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class SubscribtionActivity extends Activity implements IContractor {

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
    private boolean isMonthly;
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
        if((isannual || isMonthly) ){
            if(promocode_et.length()>0){
                getDiscountFromServer(promocode_et.getText().toString());
            } else {
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
        } else {
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
        if (isannual || isMonthly){
            ContractorModel.getInstance().sendDataPayment("Payment Failed");
        }
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
        isMonthly = false;

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
        paymentStatus = "Monthly";
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        quarterdate = cal.getTime();
        expiry = dateformat.format(quarterdate);
        isannual = false;
        isMonthly = true;

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
                                    else if(PromoType.equalsIgnoreCase("$"))
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
                                    }else {
                                        if(PromoType!=null){

                                            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                                            int month = Integer.parseInt(PromoType);
                                            Calendar cal = Calendar.getInstance();
                                            cal.add(Calendar.MONTH, month);
                                            quarterdate = cal.getTime();
                                            expiry = dateformat.format(quarterdate);


                                        }
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
                /*if(isannual){
                    if (promoType.equalsIgnoreCase("%") ){
                        float discount = Float.valueOf(600) * (Float.valueOf(DiscountPercentage))/100;
                        float totalammount =  (Float.valueOf(600) - discount);
                        String result = String.format("%.02f",totalammount);
                        ((TextView)findViewById(R.id.note_promocode)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.note_promocode)).setText("Discounted Price: $"+String.valueOf(result));
                        //annual_et.setText(String.valueOf(totalammount));
                    }else if(promoType.equalsIgnoreCase("$")) {
                        float totalammount =  Float.valueOf(600) - Float.valueOf(DiscountPercentage);
                        String result = String.format("%.02f",totalammount);
                        ((TextView)findViewById(R.id.note_promocode)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.note_promocode)).setText("Discounted Price: $"+String.valueOf(result));
                        //annual_et.setText(String.valueOf(totalammount));

                    }
                }*/

                if(isannual || isMonthly){
                    float total_temp = isannual ? 99f : 9.99f;
                    if (promoType.equalsIgnoreCase("%") ){

                        float discount = total_temp * (Float.valueOf(DiscountPercentage))/100;
                        float totalammount =  (total_temp - discount);

                        totalammount = totalammount <= 0 ? 0.0f : totalammount;
                        String result = String.format("%.02f",totalammount);

                        ((TextView)findViewById(R.id.note_promocode)).setVisibility(View.VISIBLE);
                        ((TextView)findViewById(R.id.note_promocode)).setText("Discounted Price: $"+String.valueOf(result));

                        //quarter_et.setText(String.valueOf(totalammount));
                        final SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                        final Date date = new Date();
                        SendPromoCodeData(PromoCodeID,utils.getdata("Userid"),StaticData.TotalAmount,dateformat.format(date));
                    }else if(promoType.equalsIgnoreCase("$")) {

                        float totalammount =  total_temp - Float.valueOf(DiscountPercentage);
                        totalammount = totalammount <= 0 ? 0.0f : totalammount;

                        String result = String.format("%.02f",totalammount);
                        ((TextView)findViewById(R.id.note_promocode)).setVisibility(View.VISIBLE);

                        ((TextView)findViewById(R.id.note_promocode)).setText("Discounted Price: $"+String.valueOf(result));

                        //quarter_et.setText(String.valueOf(totalammount));
                        final SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                        final Date date = new Date();
                        SendPromoCodeData(PromoCodeID,utils.getdata("Userid"),StaticData.TotalAmount,dateformat.format(date));

                    }else {
                        float totalamount = 0.0f;
                        String result = String.format("%.02f",totalamount);
                        //((TextView)findViewById(R.id.note_promocode)).setVisibility(View.VISIBLE);
                        //((TextView)findViewById(R.id.note_promocode)).setText("Discounted Price: "+String.valueOf(result));
                        String msg = Integer.valueOf(promoType) > 1 ?
                                "You have been given " + PromoType + " months free access. Enjoy all the premium features." :
                                "You have been given " + PromoType + " month free access. Enjoy all the premium features.";
                        new AlertDialog.Builder(SubscribtionActivity.this)
                                .setTitle("Yay!")
                                .setMessage(msg)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SendData();
                                    }
                                })
                        .show();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }

    public void PayClick(View view) {
        Pay();

    }

    private void Pay() {
        if(isannual || isMonthly){
            Intent i = new Intent(this, LoginActivity.class);
            i.putExtra("isannual",String.valueOf(isannual));
            i.putExtra("isquarter",String.valueOf(isMonthly));
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
            ContractorModel.getInstance().sendDataPayment("Payment Success!!");

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
                            /*Calendar cal = Calendar.getInstance();
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
                            ((TextView)findViewById(R.id.annual_expiry_date)).setText("will expire on "+sdf.format(cal.getTime()));*/
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


    }

    public void SendData(){

            showPB("Loading...");
            final SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            final Date date = new Date();
            String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/updateCustomerDataApp.php?UserID="+utils.getdata("Userid")
                    +"&Email="+utils.getdata("email")+"&PaymentStatus="+paymentStatus+"&PaymentDate="+dateformat.format(date)
                    +"&ExpirationDate="+expiry;
            url = url.replace(" ","%20");

        new AsyncTask<String,Void,String>(){

            @Override
            protected String doInBackground(String... strings) {
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");
                    HttpPost httppost = new HttpPost(strings[0]);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    if(response.equalsIgnoreCase("Record Updated.")){
                        if(PromoCodeID!=null && !PromoCodeID.equalsIgnoreCase(""))
                            SendPromoCodeData(PromoCodeID,utils.getdata("Userid"),StaticData.TotalAmount,dateformat.format(date));
                    }

                } catch (Exception e) {
                    hidePB();
                    e.printStackTrace();
                }

                return null;
            }
        }.execute(url);




           /* StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("Record Updated.")) {
                        if(PromoCodeID!=null && !PromoCodeID.equalsIgnoreCase(""))
                            SendPromoCodeData(StaticData.PromoCodeID,StaticData.Userid,StaticData.TotalAmount,dateformat.format(date));
                        else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                }
                            });

                        }


                    } else {
                        //hidePB();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    hidePB();

                    //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                }
            });

            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });

            Volley.newRequestQueue(SubscribtionActivity.this).add(stringRequest);*/

    }

    private void SendPromoCodeData(String promoCodeID, String userid, String totalAmount, String format) {
        new android.os.AsyncTask<String,Void,String>(){
            String res;
            @Override
            protected String doInBackground(String... params) {
                params[0] = params[0].replace(" ","%20");

                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");
                    HttpPost httppost = new HttpPost(params[0]);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    hidePB();
                    if(response.contains("Record added.")){
                        if(PromoCodeID!=null && !PromoCodeID.equalsIgnoreCase("")){
                            finish();

                        }
                    }

                } catch (Exception e) {
                    hidePB();
                    e.printStackTrace();
                }

                /*StringRequest stringRequest = new StringRequest(Request.Method.POST, params[0], new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.replace("\r","");
                        response = response.replace("\n","");
                        if (response.equalsIgnoreCase("Record Added. ")) {

                            res= response;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hidePB();
                                    finish();
                                }
                            });
                        } else {
                            //hidePB();

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    }
                });

                Volley.newRequestQueue(SubscribtionActivity.this).add(stringRequest);*/

                return res;
            }


        }.execute("http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addUserPromoCode.php?PromoCodeID="+promoCodeID+"&UserID="+userid+"&Amount="+totalAmount+"&AddedDateTime="+format);
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
                try {
                    pb = new ProgressDialog(SubscribtionActivity.this);
                    pb.setMessage(message);
                    pb.setCancelable(false);
                    pb.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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


    @Override
    public void sendDataPurchase(String subscribtionType, double price) {
        Random rand = new Random();
        int random = rand.nextInt(100);
        Answers.getInstance().logPurchase(new PurchaseEvent()
                .putItemPrice(BigDecimal.valueOf(price))
                .putCurrency(Currency.getInstance("USD"))
                .putItemName("subscribe")
                .putItemType(subscribtionType)
                .putItemId("Capalino-Subscribe-" + random)
                .putSuccess(true));
    }
}
