package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.R;

public class ForgotPasswordActivity extends Activity {

    private EditText email_et;
    private Utils utils;
    private Context context = this;
    private ProgressDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();
    }

    private void init() {
        View view = findViewById(R.id.forgotdialog);
        view.getBackground().setAlpha(200);
        utils = new Utils(this);
        email_et = (EditText) findViewById(R.id.email);
    }

    public void ForgotPasswordClick(View view) {
        try {
            if(email_et.getText().length()>0){
                //String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getPassword.php?UserEmailAddress=" + email_et.getText().toString();
                String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/forgotPasswordWithUserCheck.php?UserEmailAddress=" + email_et.getText().toString();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response!=null) {
                            if(response.equalsIgnoreCase("Email does not exist.")){
                                new AlertDialog.Builder(context)
                                        .setTitle("Alert!")
                                        .setMessage("Email does not exist.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                                return;
                            }
                            SendEmail(response);

                        } else {
                            new AlertDialog.Builder(context)
                                    .setTitle("Alert!")
                                    .setMessage("Email is incorrect, please try again.")
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
                                .setMessage("Check your Internet Connection")
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

                Volley.newRequestQueue(this).add(stringRequest);
            }else {
                new AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setMessage("Email field must be filled to proceed further.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

        } catch (Exception e) {
            hidePB();
            e.printStackTrace();
        }
    }

    private void SendEmail(String response) {
        showPB("Loading...");
        response = response.replace("\n","");
        String pass = "";
        char[] resp = response.toCharArray();
        for(char c : resp){
            if(c=='#'){
                pass+="%23";
            }else if(c=='^'){
                pass+="%5E";
            }else if(c=='%'){
                pass+="%25";
            }else {
                pass+=c;
            }
        }
        String message = "Please see below for your current login credentials: Username: "+email_et.getText().toString()+" Password:  "+pass+
                " If you have any questions or comments about the MWBE Assistant Mobile app, please email Safeena Mecklai at safeena@capalino.com.";
        message = message.replace("\n","");
        message = message.replace(" ", "%20");
        //message = getString(R.string.htmlsource);
        //String url = "http://hivelet.com/emailCapalinoH.php?message="+message+"&toemail="+email_et.getText().toString();
        String url = "http://hivelet.com/emailCapalinoForGotPassword.php?password="+pass+"&toemail="+email_et.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Success")) {
                    hidePB();
                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Password has been sent to your email.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } else {
                    hidePB();
                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Email is incorrect, please try again.")
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
                hidePB();
                new AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setMessage("Check your Internet Connection")
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

        Volley.newRequestQueue(this).add(stringRequest);
    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(ForgotPasswordActivity.this);
                pb.setMessage(message);
                pb.setCancelable(false);
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
