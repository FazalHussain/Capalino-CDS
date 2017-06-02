package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.MWBE.Connects.NY.AppConstants.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.MWBE.Connects.NY.CustomViews.CustomEditText_Book;
import com.MWBE.Connects.NY.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class ActivationActivity extends Activity {

    private CustomEditText_Book email_et;
    private CustomEditText_Book activation_et;
    private Context context = this;
    private ProgressDialog pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        init();
    }

    private void init() {
        View view = findViewById(R.id.activationdialog);
        view.getBackground().setAlpha(200);
        if(getIntent().getStringExtra("email")!=null)
        email_et = (CustomEditText_Book)findViewById(R.id.email);
        activation_et = (CustomEditText_Book) findViewById(R.id.activation_code);
        if(getIntent().getStringExtra("email")!=null) {
            email_et.setText(getIntent().getStringExtra("email"));
            activation_et.requestFocus();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    public void SubmitClick(View view){
        try{
            check_activationcode();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void check_activationcode() {
        Thread check_thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    showPB("Loading...");
                    final String email = getIntent().getStringExtra("email");
                    final String password = getIntent().getStringExtra("pass");
                    String pass = "";
                    if(password.contains("#") || (password.contains("%") && !password.contains("%23"))){

                    for(char c: password.toCharArray()) {
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
                    }else {
                        pass = password;
                    }
                    String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getActivationCode.php?UserEmailAddress=" + email + "&UserPassword=" + pass;

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(url);
                    httppost.setHeader("Content-type", "application/json;charset=UTF-8");

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    JSONArray jsonarray = new JSONArray(response);
                    JSONObject jsonobj = jsonarray.getJSONObject(0);
                    String activation_code = jsonobj.getString("ActivationCode");
                    if (activation_code.equalsIgnoreCase(activation_et.getText().toString())) {
                        final String finalPass = pass;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateRecord(email, finalPass);
                            }
                        });
                    }else {
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               hidePB();
                               new AlertDialog.Builder(context)
                                       .setTitle("Alert!")
                                       .setMessage("Wrong Activation Code!\nPlease try again.")
                                       .setIcon(android.R.drawable.ic_dialog_alert)
                                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                           }
                                       })
                                       .show();
                           }
                       });
                    }

                } catch (Exception e) {
                    hidePB();
                    e.printStackTrace();
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           new android.app.AlertDialog.Builder(context)
                                   .setTitle("Alert!")
                                   .setMessage("There is a problem when geting response from server. Check your internet connection, Please try again.")
                                   .setIcon(android.R.drawable.ic_dialog_alert)
                                   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                       }
                                   })
                                   .show();
                       }
                   });
                }

            }
        });
        check_thread.start();
    }

    private void updateRecord(String email, String password) {
        final Utils utils = new Utils(ActivationActivity.this);
        final String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/updateStatusCode.php?UserEmailAddress="+email+"&UserPassword="+password;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidePB();
                new android.app.AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setMessage("Your Capalino account is now activated. Please sign in to access MWBE Connect NY.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Intent i = new Intent(ActivationActivity.this, LoginActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.putExtra("newuser", "yes");
                                i.putExtra("email",utils.getdata("email"));
                                //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(i);
                            }
                        })
                        .show();

             /* AlertDialog dialog =  new AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setMessage("This Agreement and any operating rules for Capalino's website established  by Capalino" +
                                " constitute the entire agreement of the parties with respect to the subject matter hereof," +
                                " and supersede all previous written or oral agreements between the parties with respect to such subject matter." +
                                " This Agreement shall be construed in accordance with the laws of the State of New York," +
                                " without regard to its conflict of laws rules.")
                        .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                //updateRecord(email,password);


                            }
                        })

                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                Typeface face=Typeface.createFromAsset(getAssets(), "gotham_book.otf");
                textView.setTypeface(face);*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidePB();
                error.printStackTrace();
                new android.app.AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setMessage("Check your internet connection, Please try again")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);

    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(ActivationActivity.this);
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
