package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.MWBE.Connects.NY.CapalinoServices.ConnectivityReceiver;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.CustomViews.CustomAutoCompleteTextView;
import com.MWBE.Connects.NY.CustomViews.CustomCheckBox;
import com.MWBE.Connects.NY.CustomViews.CustomEditText_Book;
import com.MWBE.Connects.NY.R;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.fabric.sdk.android.Fabric;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.ArrayList;

public class LoginActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "1GNLnpbRbAtycdP5ZM3Lv0BMw";
    private static final String TWITTER_SECRET = "P7DKECgNkw8ftOjKoJfuPr3pwbBj0ngblYmKfvvOnNYzOnqaOk";


    private static final String TAG = LoginActivity.class.getSimpleName();
    public static final String PACKAGE = "com.MWBE.Connects.NY";
    private CustomEditText_Book password_et;
    private CustomAutoCompleteTextView email_et;
    private Context context = this;
    private CustomCheckBox rememberme;
    private Utils utils;
    private ProgressDialog pb;
    private boolean isactivated;
    private String activationcode;
    private ArrayList<String> listemail;
    private boolean present;
    private String pass="";
    private String statuscode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseApp.initializeApp(context, FirebaseOptions.fromResource(context));

       /* if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }*/


        //TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        //Fabric.with(this, new Twitter(authConfig));
        //generateHashkey();
        setContentView(R.layout.activity_login_capalino);
        init();
        Security.setProperty("networkaddress.cache.negative.ttl", "0");


    }





    private void init() {
        //deleteDatabase("CapalinoDataBase.sqlite");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View mainview = findViewById(R.id.logindialog);
        mainview.getBackground().setAlpha(200);

        password_et = (CustomEditText_Book) findViewById(R.id.password);
        email_et = (CustomAutoCompleteTextView) findViewById(R.id.email);
        rememberme = (CustomCheckBox) findViewById(R.id.remember_check);
        utils = new Utils(LoginActivity.this);
        //rememberme.setChecked(true);
        if(getIntent().getStringExtra("email")!=null) {
            email_et.setText(getIntent().getStringExtra("email"));
            password_et.requestFocus();
        }

        if(utils.getdata("ischecked").equalsIgnoreCase("true")) {
            startActivity(new Intent(LoginActivity.this, Splash.class));
            finish();
        } else if (utils.getdata("logout").equalsIgnoreCase("logout") || utils.getdata("logout") != null) {
            utils.savedata("email", email_et.getText().toString());
            utils.savedata("pass", password_et.getText().toString());
            utils.savedata("ischecked", "false");
            remember_check();
        }

    }

    public void ForgotClick(View view){
        startActivity(new Intent(this,ForgotPasswordActivity.class));
    }

    private void getEmailList() {
        try {
            DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            listemail = new ArrayList<>();
            Cursor cursor = dataBaseHelper.getEmailFromDB("", "", "AutoCompleteEmailList", false);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                        listemail.add(cursor.getString(1));
                }
            }
        } catch (Exception e) {
            hidePB();
            e.printStackTrace();
        }
    }

    private void remember_check() {
        rememberme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rememberme.isChecked()) {
                    utils.savedata("email", email_et.getText().toString());
                    utils.savedata("pass", password_et.getText().toString());
                    utils.savedata("ischecked", "true");
                    System.out.println("Checked");
                } else {
                    utils.savedata("email", "");
                    utils.savedata("pass", "");
                    utils.savedata("ischecked", "");
                }
            }
        });
    }

    public void RegisterClick(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        //startActivity(new Intent(LoginActivity.this, BrowseActivity.class));
    }

    public void LoginClick(View view) {
        Login();
    }

    private void Login() {
        boolean isConnected = ConnectivityReceiver.isConnected(this);
        showPB("Loading...");
        if (email_et.length() > 0 && password_et.length() > 0) {
            try {

                char[] chars = password_et.getText().toString().toCharArray();
                pass = "";
                for(char c: chars){
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

                DataBaseHelper dataBaseHelper = new DataBaseHelper(LoginActivity.this);
                dataBaseHelper.createDataBase();
                dataBaseHelper.openDataBase();
                boolean inserted = dataBaseHelper.InsertEmailList(email_et.getText().toString());
            } catch (Exception e) {
                hidePB();
                e.printStackTrace();
            }
            String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getLogin.php?UserEmailAddress=" + email_et.getText().toString() + "&UserPassword=" + pass;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!response.equalsIgnoreCase("Login Failed")) {
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if (jsonObject.has("SetupStatus")) {
                                statuscode = jsonObject.getString("SetupStatus");
                            }

                            checkAccount();

                            }catch (Exception e){
                            hidePB();
                                e.printStackTrace();
                            }


                    } else {
                        hidePB();
                        new AlertDialog.Builder(context)
                                .setTitle("Alert!")
                                .setMessage("Please try again. Either the email address or password does not match with our records.")
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

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


            Volley.newRequestQueue(this).add(stringRequest);

        } else {
            hidePB();
            new AlertDialog.Builder(context)
                    .setTitle("Alert!")
                    .setMessage("Email & Password required to proceed further...")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }

    }

    private void checkAccount() {
        try {
            String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getLogin.php?UserEmailAddress=" + email_et.getText().toString() + "&UserPassword=" + pass;
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
                        JSONArray jsonarray = new JSONArray(response);
                        JSONObject jsonobj = jsonarray.getJSONObject(0);
                        if (jsonobj.has("UserID")) {
                            if (jsonobj.getString("UserID") != null) {
                                activationcode = jsonobj.getString("UserID");
                                utils.savedata("Userid",activationcode);
                                //Intent j = new Intent(LoginActivity.this, CapabilitiesService.class);
                                //j.putExtra("Userid", utils.getdata("Userid"));
                                //startService(j);
                                utils.savedata("email",jsonobj.getString("UserEmailAddress"));
                                utils.savedata("fname",jsonobj.getString("UserFirstName"));
                                utils.savedata("lname",jsonobj.getString("UserLastName"));
                                return activationcode;
                            }
                        }
                        activationcode = jsonobj.getString("UserStatusCode");

                        return activationcode;
                    } catch (Exception e) {
                        hidePB();
                        e.printStackTrace();
                    }
                    return activationcode;
                }

                @Override
                protected void onPostExecute(String s) {
                    if(s!=null)
                    if (!s.equalsIgnoreCase("0")) {
                        isactivated = true;
                        hidePB();
                        utils.savedata("pass",password_et.getText().toString());
                        if (isactivated) {
                            //deleteDatabase("CapalinoDataBase.sqlite");

                            if (!email_et.getText().toString().equalsIgnoreCase("") && rememberme.isChecked()) {
                                utils.savedata("email", email_et.getText().toString());
                                utils.savedata("pass", password_et.getText().toString());
                                utils.savedata("ischecked", "false1");
                            }

                            Intent i = new Intent(LoginActivity.this, HomeActivity
                                    .class);
                            if(getIntent().getStringExtra("newuser")!=null){
                                if(getIntent().getStringExtra("newuser").equalsIgnoreCase("yes")){
                                    i = new Intent(LoginActivity.this,SetupActivity.class);
                                }
                            }
                            i.putExtra("islogin", "yes");
                            utils.savedata("Userid", s);
                            if(!statuscode.equalsIgnoreCase("Active")){
                                Intent j = new Intent(LoginActivity.this,SetupActivity.class);
                                startActivity(j);
                            }else {
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(i);
                            }
                        }
                    } else {
                        hidePB();
                        new AlertDialog.Builder(context)
                                .setTitle("Alert!")
                                .setMessage("Account is not active. Kindly activate your account using the activation code sent to you email.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(LoginActivity.this,ActivationActivity.class);
                                        i.putExtra("email",email_et.getText().toString());
                                        i.putExtra("pass",password_et.getText().toString());
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
            hidePB();
            e.printStackTrace();
        }
    }



    public void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    PACKAGE,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());


                String hashkey = Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                Log.d("hash",hashkey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, e.getMessage(), e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (utils.getdata("ischecked").equalsIgnoreCase("true"))
            rememberme.setChecked(true);
        else*/
            rememberme.setChecked(true);
        //email_et.setText(utils.getdata("email"));
        //password_et.setText(utils.getdata("pass"));
    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(LoginActivity.this);
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

    @Override
    public void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();

        branch.initSession(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                    // params will be empty if no data found
                    // ... insert custom logic here ...

                    try{
                        //Log.i("MyApp", branchUniversalObject.getShortUrl(getApplicationContext(),linkProperties));
                    }catch (Exception e){
                        e.printStackTrace();
                    }



                } else {
                    Log.i("MyApp", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }
}
