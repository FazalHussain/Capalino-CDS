package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import net.authorize.sampleapplication.*;

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
            SendEmail();

        } catch (Exception e) {
            hidePB();
            e.printStackTrace();
        }
    }

    private void SendEmail() {
        showPB("Loading...");
        //http://hivelet.com/emailCapalinoForGotPasswordFormated.php?toemail=
        final String email = email_et.getText().toString();

        if (!isValidEmail(email)){
            hidePB();
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("Please enter a valid email address.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        String url = "http://hivelet.com/resetPassword.php?toemail="+ email;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("Success")) {
                    hidePB();
                    new AlertDialog.Builder(context)
                            .setTitle("Reset Password!")
                            .setMessage("An email is sent to " + email + " with instructions to reset password. ")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(ForgotPasswordActivity.this,
                                            LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .show();

                } else {
                    hidePB();
                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("This email doesn't exist in our system. Kindly recheck the email address or create a new account.")
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

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
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
