package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.CustomViews.CustomEditText_Book;
import com.MWBE.Connects.NY.PasswordValidator;
import com.MWBE.Connects.NY.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Random;

public class RegisterActivity extends Activity {

    private CustomEditText_Book email_et;
    private boolean ischecked = false;
    private ProgressDialog pb;
    private CustomEditText_Book fname_et;
    private CustomEditText_Book lastname_et;
    private CustomEditText_Book company_et;
    private CustomEditText_Book password_et;
    private String email;
    private int activation_code;
    private Context context = this;
    private String password;
    private CustomEditText_Book retype_pass;
    private Utils utils;

    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    private TextView textViewhint;
    private String Emailtext;
    private boolean validpass;
    private boolean emailValid;
    private boolean retypepassValid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        getWindow().setBackgroundDrawableResource(R.drawable.background1) ;
        init();
    }

    private void init() {
        utils = new Utils(RegisterActivity.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        View view = findViewById(R.id.registerdialog);
        view.getBackground().setAlpha(200);
        fname_et = (CustomEditText_Book) findViewById(R.id.name);
        lastname_et = (CustomEditText_Book) findViewById(R.id.lastname);
        email_et = (CustomEditText_Book) findViewById(R.id.email);
        company_et = (CustomEditText_Book) findViewById(R.id.company_name);
        password_et = (CustomEditText_Book) findViewById(R.id.password);

        retype_pass = (CustomEditText_Book) findViewById(R.id.repassword);

        Emailtext = email_et.getText().toString();
        //validation sudden
        ValidateEmail();

        CheckPasswordValidationWithRetypePassword();

        password_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (password_et.getEditableText() == s) {
                    PasswordValidator passwordValidator = new PasswordValidator();
                    if (!passwordValidator.validate(password_et.getText().toString())) {
                        validpass = false;
                        /*String first = "Password ";
                        String second = "<font color='#F4131A'><b>!</b></font>";
                        ((TextView) findViewById(R.id.passwordlbl)).setText(Html.fromHtml(first + second));*/
                    } else {
                        // password_et.setError("Your password should be a minimum of 8 letters, and should contain at least one upper case letter, one number and one special character.");
                        String first = "Password ";
                        validpass = true;
                        //   String second = "<font color='#F4131A'><b>!</b></font>";
                        //  ((TextView) findViewById(R.id.passwordlbl)).setText(Html.fromHtml(first + second));
                        ((TextView) findViewById(R.id.passwordlbl)).setText(Html.fromHtml(first));

                    }
                }
            }
        });
        password_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textViewhint = (TextView) findViewById(R.id.passhint);
                    if (textViewhint.getVisibility() == View.GONE) {
                        textViewhint.setVisibility(View.VISIBLE);
                    }
                } else if (!validpass) {
                    String first = "Password ";
                    String second = "<font color='#F4131A'><b>!</b></font>";
                    ((TextView) findViewById(R.id.passwordlbl)).setText(Html.fromHtml(first + second));
                    password_et.setError("Password doesn't follow the above criteria.");
                    if (textViewhint.getVisibility() == View.VISIBLE) {
                        textViewhint.setVisibility(View.GONE);
                    }
                    //  textViewhint.setVisibility(View.GONE);
                }

            }
        });



    }

    private void CheckPasswordValidationWithRetypePassword() {
        retype_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (retype_pass.getEditableText() == s)
                    if (password_et.getText().toString().equalsIgnoreCase(retype_pass.getText().toString())) {
                        String first = "Repeat Password ";
                        retypepassValid = true;
                        ((TextView) findViewById(R.id.repasswordlbl)).setText(Html.fromHtml(first));
                    } else {
                        String first = "Repeat Password ";
                        String second = "<font color='#F4131A'><b>!</b></font>";
                        //((TextView) findViewById(R.id.repasswordlbl)).setText(Html.fromHtml(first + second));
                        //retype_pass.setError("Password does not match.");
                        retypepassValid = false;
                    }
            }
        });

        retype_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (textViewhint.getVisibility() == View.VISIBLE) {
                        textViewhint.setVisibility(View.GONE);
                    }
                } else if (!retypepassValid) {
                    String first = "Repeat Password ";
                    String second = "<font color='#F4131A'><b>!</b></font>";
                    ((TextView) findViewById(R.id.repasswordlbl)).setText(Html.fromHtml(first + second));
                    retype_pass.setError("Password does not match.");
                }

            }
        });
    }

    private void ValidateEmail() {
        email_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isValidEmail(email_et.getText().toString())){
                    ((TextView)findViewById(R.id.emaillbl)).setText("Email Address");
                    emailValid = true;
                }else {
                    String first = "Email Address ";
                    String second =  "<font color='#F4131A'><b>!</b></font>";
                   // email_et.setError("Invalid Email");
                    emailValid = false;
                    //((TextView)findViewById(R.id.emaillbl)).setText(Html.fromHtml(first+second));
                }
            }
        });



        email_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                }
                else if (!emailValid){
                        email_et.setError("Invalid Email");
                    }
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private boolean validate() {
        try {
            if (fname_et.length() == 0 || lastname_et.length() == 0 || email_et.length() == 0 ||
                     password_et.length() == 0 || retype_pass.length() == 0) {
                new AlertDialog.Builder(context)
                        .setTitle("Alert!")
                        .setMessage("Please provide all the required information.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return false;
            } else {
                if (!password_et.getText().toString().equalsIgnoreCase(retype_pass.getText().toString())) {
                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Password does not match.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                    return false;
                }
                return true;
            }
        }catch (Exception e){
            hidePB();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.AlertInternetConnection(RegisterActivity.this);
                }
            });
            e.printStackTrace();
            return false;
        }
    }

    public void checkEmail(){
        Thread thread_check_email = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    String email = email_et.getText().toString().trim();
                    utils.savedata("fullname",fname_et.getText().toString());
                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");

                    String link = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getEmail.php?UserEmailAddress="+email;
                    HttpPost httppost = new HttpPost(link);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    JSONArray jsonarray = new JSONArray(response);
                    JSONObject jsonobj = jsonarray.getJSONObject(0);
                    if(jsonobj.getString("Number").equalsIgnoreCase("1")) {
                        ischecked = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hidePB();
                                new AlertDialog.Builder(context)
                                        .setTitle("Alert!")
                                        .setMessage("Email Already Exists, Try With Different Email.")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                return;
                                            }
                                        })
                                        .show();
                            }
                        });
                    }else{
                      registration();
                    }



                    //hidePB();
                } catch (Exception e) {
                    hidePB();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.AlertInternetConnection(RegisterActivity.this);
                        }
                    });
                    e.printStackTrace();
                }
            }
        });
        thread_check_email.start();
    }

    private void registration() {

        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(retype_pass.getWindowToken(), 0);
        String fname = fname_et.getText().toString();
        String lname = lastname_et.getText().toString();
        email = email_et.getText().toString();
        //String company = company_et.getText().toString();
        String company = "NA";
        password = password_et.getText().toString();
        if(password.contains("#") || password.contains("%"))
        password = URLEncoder.encode(password);
        Random rnd = new Random();
        activation_code = 1000000 + rnd.nextInt(9000000);
        utils.savedata("fname", fname);
        utils.savedata("lname", lname);
        utils.savedata("email", email);
        utils.savedata("company", company);
        utils.savedata("password", password);
        utils.savedata("activationcode", String.valueOf(activation_code));

        startActivity(new Intent(RegisterActivity.this,PdfViewActivity.class));

        hidePB();


    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(RegisterActivity.this);
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

    //BackClick Event
    public void BackClick(View view){
        finish();
    }

    public void ActivateClick(View view){
        try{
            if(validate()) {
                PasswordValidator passwordValidator = new PasswordValidator();
                if(isValidEmail(email_et.getText().toString()) && passwordValidator.validate(password_et.getText().toString())) {
                    showPB("Loading");
                    checkEmail();
                }

                if(!passwordValidator.validate(password_et.getText().toString()))
                {
                    hidePB();
                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Password must contain atleast 6 character including uppercase, lowercase, number and special characters ")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }

                if(!isValidEmail(email_et.getText().toString())){
                    hidePB();
                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("Invalid Email!")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
        }catch (Exception e){
            hidePB();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.AlertInternetConnection(RegisterActivity.this);
                }
            });
            e.printStackTrace();
        }
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        finish();
    }
}
