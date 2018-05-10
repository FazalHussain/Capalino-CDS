package net.authorize.sampleapplication;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.authorize.mobile.Result;
import net.authorize.sampleapplication.fragments.LoginActivityRetainedFragment;
import net.authorize.sampleapplication.models.ContractorModel;
import net.authorize.sampleapplication.models.StaticData;
import net.authorize.sampleapplication.services.AnetIntentService;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Allows the user to login into Authorize.Net sandbox account using valid
 * login and password credentials
 */
public class LoginActivity extends AnetBaseActivity implements
        LoginActivityRetainedFragment.OnFragmentInteractionListener{

    public static final String LOGIN_ID_TAG = "LOGIN_ID";
    public static final String PASSWORD_TAG = "PASSWORD";
    private static final String AUTHENTICATE_USER_FRAGMENT = "authenticate_user_fragment";
    private EditText loginIdEditText;
    private EditText passwordEditText;
    private TextView loginIdErrorMessageTextView;
    private TextView passwordErrorMessageTextView;
    private Drawable loginIdIcon;
    private Drawable passwordIcon;
    private ProgressBar loginProgressBar;
    private Button loginButton;
    private CoordinatorLayout coordinatorLayout;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getPassword();
        SetupData();
        setupClickableUI(findViewById(R.id.login_page));
        setupViews();
        //attemptLogin();
        setupErrorMessages();

    }

    private void getPassword() {
        String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/getOnlyPassword.php";
        StringRequest request = new StringRequest(url , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Pass:", response);
                pass = response;

                attemptLogin();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pass = "";
                //Log.e(TAG, "Error: " + getMessage(error, context));
                //Toast.makeText(context, getMessage(error, context), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(LoginActivity.this).add(request);

    }

    private void SetupData() {
        StaticData.isannual = getIntent().getStringExtra("isannual");
        StaticData.isquarter = getIntent().getStringExtra("isquarter");
        StaticData.paymentStatus = getIntent().getStringExtra("paymentStatus");
        StaticData.Userid = getIntent().getStringExtra("Userid");
        StaticData.email = getIntent().getStringExtra("email");
        StaticData.fname = getIntent().getStringExtra("fname");
        StaticData.lname = getIntent().getStringExtra("lname");
        StaticData.city = getIntent().getStringExtra("city");
        StaticData.state = getIntent().getStringExtra("state");
        StaticData.Address = getIntent().getStringExtra("Address");
        StaticData.expiry = getIntent().getStringExtra("expiry");
        if(getIntent().getStringExtra("PromoType")!=null && !getIntent().getStringExtra("PromoType").equalsIgnoreCase("")){
            StaticData.PromoType = getIntent().getStringExtra("PromoType");
            StaticData.PromoCodeID = getIntent().getStringExtra("PromoCodeID");
            StaticData.discount = getIntent().getStringExtra("discount");
        }
        //SendData();
       // SendPromoCodeData(StaticData.PromoCodeID,StaticData.Userid,StaticData.TotalAmount,StaticData.expiry);
    }

    private void SendPromoCodeData(String promoCodeID, String userid, String totalAmount, String format) {
        new AsyncTask<String,Void,String>(){
            String res;
            @Override
            protected String doInBackground(String... params) {
                params[0] = params[0].replace(" ","%20");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, params[0], new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.replace("\r","");
                        response = response.replace("\n","");
                        if (response.equalsIgnoreCase("Record Added. ")) {
                            res= response;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    /*dismissIndeterminateProgressDialog();
                                    Intent intent = new Intent(NavigationActivity.this, TransactionResultActivity.class);
                                    intent.putExtra(TransactionResultActivity.TRANSACTION_RESULT, transactionResult);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();*/
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

                Volley.newRequestQueue(LoginActivity.this).add(stringRequest);

                return res;
            }


        }.execute("http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/addUserPromoCode.php?PromoCodeID="+promoCodeID+"&UserID="+userid+"&Amount="+totalAmount+"&AddedDateTime="+format);
    }




    /**
     * Sets variables with their views in the layout XML
     */
    private void setupViews() {
        loginIdEditText = (EditText) findViewById(R.id.loginId);
        passwordEditText = (EditText) findViewById(R.id.password);
        loginIdErrorMessageTextView = (TextView) findViewById(R.id.loginId_error_message);
        passwordErrorMessageTextView = (TextView) findViewById(R.id.password_error_message);
        //loginIdIcon = ((ImageView) findViewById(R.id.loginId_icon)).getDrawable();
        //passwordIcon = ((ImageView) findViewById(R.id.password_icon)).getDrawable();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.snackbarPosition);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.bottom_section);
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return attemptLoginOnEnter(keyCode);
            }
        });
        loginButton = (Button) findViewById(R.id.login_button1);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //attemptLogin();
            }
        });
    }


    /**
     * Allows the user to login when an enter is clicked on the keyboard
     * @param keyCode the code for the physical key that was pressed
     * @return
     */
    public boolean attemptLoginOnEnter(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            hideSoftKeyboard(this);
            //attemptLogin();
            return true;
        } else {
            return false;
        }
    }


    /**
     * Sends the credentials (username and password) from the edit texts
     * to a retained fragment for backend authentication.
     */
    public void attemptLogin() {

        loginProgressBar = (ProgressBar) findViewById(R.id.login_progress_bar);
        loginProgressBar.setVisibility(View.VISIBLE);
        /*String loginId = loginIdEditText.getText().toString();
        String password = passwordEditText.getText().toString();*/
        String loginId = "capalino";
        String password = pass;
        boolean credentialsVerified = verifyCredentials(loginId, password);
        if (!isNetworkAvailable()) {
            displaySnackbar(coordinatorLayout, "", R.string.snackbar_text_no_network_connection, R.string.snackbar_action_retry);
        } else if (!credentialsVerified) {
            displayOkDialog(getResources().getString(R.string.dialog_title_login_error), getResources().getString(R.string.dialog_message_login_error));
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            LoginActivityRetainedFragment retainedFragment = (LoginActivityRetainedFragment) fragmentManager.
                    findFragmentByTag(AUTHENTICATE_USER_FRAGMENT);
            if (retainedFragment == null) {
                retainedFragment = new LoginActivityRetainedFragment();
                Bundle credentials = new Bundle();
                credentials.putString(LOGIN_ID_TAG, loginId);
                credentials.putString(PASSWORD_TAG, password);
                retainedFragment.setArguments(credentials);
                fragmentManager.beginTransaction().add(retainedFragment,
                        AUTHENTICATE_USER_FRAGMENT).commit();
            } else {
                retainedFragment.startServiceAuthenticateUser(loginId, password);
            }

            loginProgressBar = (ProgressBar) findViewById(R.id.login_progress_bar);
            //loginProgressBar.setVisibility(View.VISIBLE);

        }
    }


    /**
     * Checks if either login field is empty
     * @param loginId text entered in the login edit text
     * @param password text entered in the password edit text
     * @return
     */
    private boolean verifyCredentials(String loginId, String password) {
        if (loginId == null || loginId.trim().length() == 0)
            return false;
        if (password == null || password.trim().length() == 0)
            return false;
        return true;
    }


    /**
     * Sets up custom error messages for the login fields.
     */
    private void setupErrorMessages() {
        loginIdEditText.addTextChangedListener(new LoginTextWatcher
                (loginIdErrorMessageTextView, loginIdIcon, loginButton));
        passwordEditText.addTextChangedListener(new LoginTextWatcher
                (passwordErrorMessageTextView, passwordIcon, loginButton));
    }


    /**
     * Callback method that receives the results of the user authentication
     * from the intent service
     * @param resultData the result of the authentication
     */
    public void onReceiveAuthenticateUserResult(Bundle resultData) {
        Result loginResult = (Result) resultData.getSerializable
                (AnetIntentService.AUTHENTICATE_USER_STATUS);
        loginProgressBar.setVisibility(View.INVISIBLE);
        if (loginResult.isResponseOk()) {
            Intent intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);
            finish();
        } else if (loginResult.getMessages().size() != 0){
            displayOkDialog(getResources().getString(R.string.dialog_title_login_error),
                    getResources().getString(R.string.dialog_message_login_error));
        } else {
            displayOkDialog(getResources().getString(R.string.dialog_title_login_error),
                    getResources().getString(R.string.dialog_message_unknown_error));
        }
    }



    /**
     * Class used to display custom error messages when the
     * login fields are empty
     */
    private class LoginTextWatcher implements TextWatcher {
        private TextView textView;
        private Drawable icon;
        private Button loginButton;

        public LoginTextWatcher(TextView textView, Drawable icon, Button loginButton) {
            this.textView = textView;
            this.icon = icon;
            this.loginButton = loginButton;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                textView.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    icon.setTint(getResources().getColor(R.color.ErrorMessageColor));
                loginButton.setEnabled(false);
            } else {  // restores layout
                textView.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    icon.setTint(getResources().getColor(R.color.ThemeColor));
                loginButton.setEnabled(true);
            }
        }
    }
}
