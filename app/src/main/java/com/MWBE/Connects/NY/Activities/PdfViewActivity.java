package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.R;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SignUpEvent;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PdfViewActivity extends Activity {

    private ProgressDialog pb;
    private Utils utils;
    private Context context = this;
    private WebView wv;
    private String url;
    private String expiry;
    FirebaseAnalytics mFirebaseAnalytics;
    AppEventsLogger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        init();
    }

    private void init() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        //logger = AppEventsLogger.newLogger(PdfViewActivity.this);
        utils = new Utils(this);
        wv = (WebView) findViewById(R.id.pdfview_wv);
        showpdf();
    }

    private void showpdf() {
        try {
                showPBForPDF("Loading Terms & Conditions");
                url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/terms.html";
                wv.getSettings().setJavaScriptEnabled(true);
                wv.getSettings().setSaveFormData(true);
                //wv.getSettings().setBuiltInZoomControls(false);
                wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
                wv.setBackgroundColor(Color.TRANSPARENT);
                wv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_INSET);
                wv.getSettings().setBuiltInZoomControls(true);
                wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

                wv.getSettings().setJavaScriptEnabled(true);
                wv.getSettings().setPluginState(WebSettings.PluginState.ON);


            wv.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            //wv.loadUrl("https://docs.google.com/viewer?url=" + url);
            wv.loadUrl(url);
        }catch (Exception e){
            hidePB();
            e.printStackTrace();
        }

    }

    public void AgreeClick(View view){
        //SendData();
        register();
    }

    public void BackClick(View view){
        finish();
    }

    public void register(){
        
    Thread thread_activate = new Thread(new Runnable(){
        @Override
        public void run() {
            try {



                showPB("Registration is in progress...");
                String fname = utils.getdata("fname");
                String lname = utils.getdata("lname");
                String email = utils.getdata("email");
                String company = utils.getdata("company");
                String password = utils.getdata("password");
                String activationcode = utils.getdata("activationcode");
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                Date date = new Date();
                String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/registerUser.php?UserFirstName="+fname+
                        "&UserLastName="+lname+"&UserEmailAddress="+email+"&UserCompany="+company+"&UserPassword="+password+
                        "&UserRegisteredDate="+dateformat.format(date)+"&ActivationCode="+activationcode;
                url = url.replace(" ","%20");
                //String urlEncoded = Uri.encode(url, ALLOWED_URI_CHARS);
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                httppost.setHeader("Content-type", "application/json;charset=UTF-8");


                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                final String response = httpclient.execute(httppost,
                        responseHandler);

                if(response.contains("Records Added.Passwords Added.")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            activation();
                        }
                    });
                }

                Log.i("Response", "Response : " + response);

                //hidePB();
            } catch (Exception e) {
                hidePB();
                e.printStackTrace();
            }

        }
    });
    thread_activate.start();
}



    public void activation(){
        Thread thread_activate = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    String message = utils.getdata("activationcode");
                    //String message = "Thank you for registering to use the MWBE Connect NY mobile app. Type the code below into the ACTIVATION screen to activate the app. Activation Code: "+utils.getdata("activationcode");
                    //message = message.replace(" ","%20");
                    //String link = "http://hivelet.com/emailCapalinoH.php?message="+message+"&toemail="+utils.getdata("email");
                    String link = "http://hivelet.com/emailCapalinoH_new.php?message="+message+"&toemail="+utils.getdata("email");
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(link);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    if(response.equalsIgnoreCase("Success")){
                        Bundle params = new Bundle();
                        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "Register");
                        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "Registration Activity");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, params);

                        Answers.getInstance().logSignUp(new SignUpEvent()
                                .putMethod("Sign_up")
                                .putSuccess(true));


                        //logger.logEvent(AppEventsConstants.EVENT_PARAM_REGISTRATION_METHOD);


                        hidePB();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(context)
                                        .setTitle("Alert!")
                                        .setMessage("Thank you for registering! An activation code has been emailed to you. Please use this to activate your account.")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(PdfViewActivity.this, ActivationActivity.class);
                                                i.putExtra("email", utils.getdata("email"));
                                                i.putExtra("pass", utils.getdata("password"));
                                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                startActivity(i);
                                                finish();
                                                finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                        });
                        //Toast.makeText(getApplicationContext(),"Activation code send to your email, Check your email & activate your account",Toast.LENGTH_LONG).show();

                    }


                    //hidePB();
                } catch (Exception e) {
                    hidePB();
                    e.printStackTrace();
                }
            }
        });
        thread_activate.start();



       /* Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(RegisterActivity.this, ActivationActivity.class);
                i.putExtra("email", email);
                i.putExtra("pass", password);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                finish();
            }
        }, 10000);*/


    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(PdfViewActivity.this);
                pb.setMessage(message);
                pb.setCancelable(false);
                pb.show();
            }
        });

    }

    void showPBForPDF(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(PdfViewActivity.this);
                pb.setMessage(message);
                pb.setCancelable(false);
                pb.show();

                new CountDownTimer(3000, 3000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub

                        pb.dismiss();
                    }
                }.start();
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
