package com.MWBE.Connects.NY.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.PasswordValidator;
import com.MWBE.Connects.NY.R;

import java.net.URLEncoder;

/**
 * Created by Fazal on 8/25/2016.
 */
public class ChangePassword extends Fragment {
    private Button chngebtn;
    private EditText newpass;
    private EditText confirm_pass;
    private ProgressDialog pb;
    private PasswordValidator passwordValidator;
    private boolean validpass;
    private boolean retypepassValid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.change_password,container,false);
        init(rootview);
        BackClick(rootview);

        return rootview;
    }

    private void BackClick(View rootview) {
        ((ImageView)rootview.findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void ChangePasword() {
        showPB("Loading...");
        Utils utils = new Utils(getActivity());
        String pass = URLEncoder.encode(confirm_pass.getText().toString());
        String url = "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/apis/changeUserPassword.php?UserEmailAddress=" + utils.getdata("email") + "&UserPassword=" + pass;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("Password Changed.")) {
                    try{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hidePB();
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Thank You!")
                                        .setMessage("Password changed sucessfully.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                getActivity().getSupportFragmentManager().popBackStack();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();

                            }
                        });

                    }catch (Exception e){
                        hidePB();
                        Utils.AlertInternetConnection(getActivity());
                        e.printStackTrace();
                    }


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                hidePB();
                /*new AlertDialog.Builder(getActivity())
                        .setTitle("Alert!")
                        .setMessage("Check your Internet Connection")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();*/

                Utils.AlertInternetConnection(getActivity());
                //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }

    private boolean Validate() {
        if(newpass.getText().length()>0 && confirm_pass.getText().length()>0 ){
            if (!newpass.getText().toString().equalsIgnoreCase(confirm_pass.getText().toString())) {
                new android.app.AlertDialog.Builder(getActivity())
                        .setTitle("Alert!")
                        .setMessage("Password does not match.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String first = "Confirm Password ";
                                String second = "<font color='#F4131A'><b>!</b></font>";
                                ((TextView) getActivity().findViewById(R.id.confirmpass1)).setText(Html.fromHtml(first + second));
                                confirm_pass.setError("Password does not match.");
                            }
                        })
                        .show();
                return false;
            }
            return true;
        }else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Alert!")
                    .setMessage("All the field required to proceed further")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return false;
    }

    private boolean Checkformat(){
        if(!passwordValidator.validate(confirm_pass.getText().toString()))
        {
            hidePB();
            new android.app.AlertDialog.Builder(getActivity())
                    .setTitle("Alert!")
                    .setMessage("Your password should be a minimum of 8 letters, and should contain at least one upper case letter, one number and one special character.")
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

    private void init(View rootview) {
        passwordValidator = new PasswordValidator();
        chngebtn = (Button) rootview.findViewById(R.id.changepassword);
        newpass = (EditText) rootview.findViewById(R.id.newpass);
        confirm_pass = (EditText) rootview.findViewById(R.id.confirmpass);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ChangePasswordClick();

        //sudden validation
        checkvalidation(rootview);

    }

    private void checkvalidation(final View view) {
        newpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (newpass.getEditableText() == s) {
                    PasswordValidator passwordValidator = new PasswordValidator();
                    if (!passwordValidator.validate(newpass.getText().toString())) {
                        validpass = false;
                    } else {
                        // password_et.setError("Your password should be a minimum of 8 letters, and should contain at least one upper case letter, one number and one special character.");
                        String first = "New Password ";
                        validpass = true;
                        ((TextView) view.findViewById(R.id.newpass1)).setText(Html.fromHtml(first));

                    }
                }
            }
        });

        newpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else if (!validpass) {
                    String first = "New Password ";
                    String second = "<font color='#F4131A'><b>!</b></font>";
                    ((TextView) view.findViewById(R.id.newpass1)).setText(Html.fromHtml(first + second));
                    newpass.setError("Password doesn't follow the above criteria.");
                }
            }
        });


        confirm_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (confirm_pass.getEditableText() == s)
                    if (newpass.getText().toString().equalsIgnoreCase(confirm_pass.getText().toString())) {
                        String first = "Confirm Password ";
                        retypepassValid = true;
                        ((TextView) view.findViewById(R.id.confirmpass1)).setText(Html.fromHtml(first));
                    } else {

                        retypepassValid = false;
                    }
            }
        });

        confirm_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){

                }else if (!retypepassValid){
                    String first = "Confirm Password ";
                    String second = "<font color='#F4131A'><b>!</b></font>";
                    ((TextView) view.findViewById(R.id.confirmpass1)).setText(Html.fromHtml(first + second));
                    confirm_pass.setError("Password does not match.");
                }
            }
        });
    }

    private void ChangePasswordClick() {
        chngebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validate() && Checkformat()) {
                    ChangePasword();
                }
            }
        });
    }

    void showPB(final String message) {

       getActivity().runOnUiThread(new Runnable() {

           @Override
           public void run() {
               pb = new ProgressDialog(getActivity());
               pb.setMessage(message);
               pb.setCancelable(true);
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
    //getActivity().getSupportFragmentManager().popBackStack();
    /*public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            getActivity().finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }*/
}
