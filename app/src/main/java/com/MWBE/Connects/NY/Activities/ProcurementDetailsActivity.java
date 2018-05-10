package com.MWBE.Connects.NY.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.AppConstants.Constants;
import com.MWBE.Connects.NY.R;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by Fazal on 8/2/2016.
 */
public class ProcurementDetailsActivity extends FragmentActivity {

    private String pdfPath;
    private static String procid;
    private String longdesc;
    private String agency;
    private String deadline;
    private String starteddate;
    private String agencyurl;
    private String refrenceUrl1;
    private String refrenceUrl2;
    private String refrenceUrl3;
    private String refrenceUrl4;
    private String refrenceUrl5;
    private TextView refrenceUrl1_tv;
    private TextView refrenceUrl2_tv;
    private TextView refrenceUrl3_tv;
    private TextView refrenceUrl4_tv;
    private TextView refrenceUrl5_tv;
    private PopupWindow popWindow;
    private Button send_btn;
    private Utils utils;
    private ProgressDialog pb;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.procurement_details_activity);
        init();
    }

    public void BackClick(View v){
        if(getIntent().getStringExtra("notif_status")!=null){
            Log.d("NavTrack","yes");
            Intent i = new Intent(this, TrackList.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }else
        finish();
    }

    public static String getprocid() {
        return procid;
    }

    public void MessagingClick(View view){
       startActivity(new Intent(this,MessagingFragments.class));
    }

    public void popupWindow(){
        try{

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            final LayoutInflater li = LayoutInflater.from(this);
            View customView = li.inflate(R.layout.popup_email, null);
            alertDialogBuilder.setView(customView);

            alertDialogBuilder.setTitle("Email RFP Details");



            final EditText email_et = customView.findViewById(R.id.email_et);
            final EditText note_et = customView.findViewById(R.id.note_et);

            send_btn = customView.findViewById(R.id.sendbtn);


            send_btn.setEnabled(false);
            send_btn.getBackground().setAlpha(200);

            email_et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length()> 0){
                        send_btn.setEnabled(true);
                        send_btn.getBackground().setAlpha(255);
                    }else{
                        send_btn.setEnabled(false);
                        send_btn.getBackground().setAlpha(200);
                    }
                }
            });


            send_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("send_btn", "button click!");

                    if (!isValidEmail(email_et.getText().toString())){
                        new AlertDialog.Builder(ProcurementDetailsActivity.this)
                                .setTitle("Alert!")
                                .setMessage("Please enter valid email address.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        return;
                    }



                    sendEmail(email_et.getText().toString(), note_et.getText().toString());
                }
            });


            alertDialog = alertDialogBuilder.create();
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimation;
            alertDialog.show();


            /*LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            // Inflate the custom layout/view
            View customView = inflater.inflate(R.layout.popup_email,null);

            // get device size
            Display display = getWindowManager().getDefaultDisplay();
            final Point size = new Point();
            display.getSize(size);
//        mDeviceHeight = size.y;
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;


            // set height depends on the device size
            popWindow = new PopupWindow(customView, width, height - 50, true);
            // set a background drawable with rounders corners
            //popWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_bg));
            popWindow.setBackgroundDrawable(new BitmapDrawable());
            popWindow.setOutsideTouchable(true);

            popWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

            popWindow.setAnimationStyle(R.style.PopupAnimation);

            // show the popup at bottom of the screen and set some margin at bottom ie,
            popWindow.showAtLocation(customView, Gravity.CENTER, 0, 100);



            final EditText email_et = customView.findViewById(R.id.email_et);
            final EditText note_et = customView.findViewById(R.id.note_et);



            send_btn = customView.findViewById(R.id.sendbtn);


            send_btn.setEnabled(false);
            send_btn.getBackground().setAlpha(200);

            email_et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length()> 0){
                        send_btn.setEnabled(true);
                        send_btn.getBackground().setAlpha(255);
                    }else{
                        send_btn.setEnabled(false);
                        send_btn.getBackground().setAlpha(200);
                    }
                }
            });


            send_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("send_btn", "button click!");

                    if (!isValidEmail(email_et.getText().toString())){
                        new AlertDialog.Builder(ProcurementDetailsActivity.this)
                                .setTitle("Alert!")
                                .setMessage("Please enter valid email address.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        return;
                    }



                    sendEmail(email_et.getText().toString(), note_et.getText().toString());
                }
            });
*/

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void sendEmail(final String email, final String note) {

        Thread thread_email = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://hivelet.com/sendEmailToThirdUser.php?FirstName=" + utils.getdata("fname") +
                            "&EmailAddress=" + utils.getdata("email") + "&ProcurementID=" + procid +
                            "&toemail=" + URLEncoder.encode(email) + "&Notes=" + URLEncoder.encode(note);

                    showPB("Please wait...");

                    HttpClient httpclient = new DefaultHttpClient();
                    //showPB("Loading....");
                    HttpPost httppost = new HttpPost(url);

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response = httpclient.execute(httppost,
                            responseHandler);

                    Log.i("Response", "Response : " + response);
                    if (response.equalsIgnoreCase("Success")){
                        hidePB();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                new AlertDialog.Builder(ProcurementDetailsActivity.this)
                                        .setTitle("Alert!")
                                        .setMessage("RFP details are sent to " + email)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                alertDialog.dismiss();
                                                dialog.dismiss();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        });


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        thread_email.start();
    }

    public void EmailClick(View v){
        try{
            String title = ((TextView)findViewById(R.id.text_header)).getText().toString();

            title = title.replace("(","");
            title = title.replace(")","");
            title = title.replace("'","");

            try{
                 DataBaseHelper dataBaseHelper = new DataBaseHelper(ProcurementDetailsActivity.this);
                 dataBaseHelper.createDataBase();
                 dataBaseHelper.openDataBase();

                 Cursor cursor = dataBaseHelper.getDataFromDB("ProcurementTitle",title,"ProcurementMaster",true);
                 if(cursor.getCount()>0){
                     while (cursor.moveToNext()){
                         pdfPath = cursor.getString(21);
                     }
                 }
                }catch (Exception e){
                    e.printStackTrace();
                }

            /*title = title.replace("\\u0027","'");
            agency = agency.replace("\\u0027","'");
            longdesc = longdesc.replace("\\u0027","'");*/

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT, title);
            //<a href=\"" + link_val + "\">
            /*intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(new StringBuilder()
                    .append("<p><b>Please find the link below to download detailed info. Thanks.</b></p>")
                    .append("<b><p>LINK:</b><p/>")
                    .append("<a><p>" + pdfPath + "<p/></a>")
                    .append("<p>© 2016 Capalino+Company, New York, NY.</p>")
                    .append("<a><p>http://www.capalino.com/</p></a>")
                    .toString()));*/

            Typeface face = Typeface.createFromAsset(getAssets(), "opensans_regular.ttf");
            SpannableStringBuilder SS = new SpannableStringBuilder(new StringBuilder()
                    .append("<p>Check out this RFP I found on MWBE Connect NY:</p><br/>")
                    .append("<p><b>Title: </b>"+title+"</p>")
                    .append("<b><p>Agency: </b>"+agency+"<p/>")
                    .append("<b><p> Description: </b>" + longdesc + "</b><p/>")
                    .append("<b><p> Published: </b>" + starteddate + "</b><p/>")
                    .append("<b><p> Deadline: </b>" + deadline + "</b><p/>")
                    .append("<b><p> More info: </b>" + agencyurl + "</b><p/>")
                    .append("<p>© 2016 Capalino+Company, New York, NY.</p>")
                    .append("<p>For more information on Capalino+Company’s MWBE services, including RFP response strategy and development, please email us at mwbeconnectny@capalino.com.</p>")
                    .append("<a><p>http://www.capalino.com/</p></a>")
                    .toString());

            SS.setSpan(face, 0, SS.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(SS.toString()));

            intent.setType("text/html");
            intent.setType("message/rfc822");

            popupWindow();

            //startActivityForResult(Intent.createChooser(intent, "Send Email"), Constants.Content_email_Constants);
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    private void init() {
        utils = new Utils(this);
        if(getIntent().getStringExtra("title")!=null){
            ((TextView)findViewById(R.id.text_header)).setText(getIntent().getStringExtra("title"));
        }

        if(getIntent().getStringExtra("fromproc")!=null){
            ((ImageView)findViewById(R.id.messaging)).setVisibility(View.GONE);
        }

        refrenceUrl1_tv = (TextView) findViewById(R.id.ref1_link);
        refrenceUrl2_tv = (TextView) findViewById(R.id.ref2_link);
        refrenceUrl3_tv = (TextView) findViewById(R.id.ref3_link);
        refrenceUrl4_tv = (TextView) findViewById(R.id.ref4_link);
        refrenceUrl5_tv = (TextView) findViewById(R.id.ref5_link);

       getData();

    }

    private void getData() {
        try{
            DataBaseHelper dataBaseHelper = new DataBaseHelper(ProcurementDetailsActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            String title = getIntent().getStringExtra("title").replace("'","''");
            Cursor cursor = dataBaseHelper.getDataFromDB("ProcurementTitle",title,"ProcurementMaster",true);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    procid = cursor.getString(0);
                    longdesc = cursor.getString(7).replace("\\u0027","'");
                    agency = cursor.getString(3);
                    agencyurl = cursor.getString(11);
                    starteddate = cursor.getString(17);
                    deadline = cursor.getString(8);
                    refrenceUrl1 = cursor.getString(12);
                    refrenceUrl2= cursor.getString(13);
                    refrenceUrl3 = cursor.getString(14);
                    refrenceUrl4 = cursor.getString(15);
                    refrenceUrl5 = cursor.getString(16);
                    if(refrenceUrl1!=null && !refrenceUrl1.equalsIgnoreCase("NA")){

                        refrenceUrl1_tv.setVisibility(View.VISIBLE);
                    }

                    if(refrenceUrl2!=null && !refrenceUrl2.equalsIgnoreCase("NA")){

                        refrenceUrl2_tv.setVisibility(View.VISIBLE);
                    }

                    if(refrenceUrl3!=null && !refrenceUrl3.equalsIgnoreCase("NA")){

                        refrenceUrl3_tv.setVisibility(View.VISIBLE);
                    }

                    if(refrenceUrl4!=null && !refrenceUrl4.equalsIgnoreCase("NA")){

                        refrenceUrl4_tv.setVisibility(View.VISIBLE);
                    }

                    if(refrenceUrl5!=null && !refrenceUrl5.equalsIgnoreCase("NA")){

                        refrenceUrl5_tv.setVisibility(View.VISIBLE);
                    }

                    ReferenceCick();

                    ((TextView)findViewById(R.id.longdescrip)).setText(longdesc);

                }
            }
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    private void ReferenceCick() {

        ((TextView)findViewById(R.id.link_content)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agencyurl!=null)
                NavigateWeb(agencyurl);
            }
        });

        refrenceUrl1_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateWeb(refrenceUrl1);
            }
        });

        refrenceUrl2_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateWeb(refrenceUrl2);
            }
        });

        refrenceUrl3_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateWeb(refrenceUrl3);
            }
        });

        refrenceUrl4_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateWeb(refrenceUrl4);
            }
        });

        refrenceUrl5_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateWeb(refrenceUrl5);
            }
        });
    }

    private void NavigateWeb(final String refrenceUrl1) {

        new AlertDialog.Builder(ProcurementDetailsActivity.this)
                .setTitle("Alert!")
                .setMessage("You are being redirected to an external site.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri webpage = null;
                        //String url = ((CustomTextView_Book) findViewById(R.id.link_content)).getText().toString();
                        if(refrenceUrl1.contains(".pdf")) {
                            webpage = Uri.parse("https://docs.google.com/viewer?url=" + refrenceUrl1);
                        }else {
                            webpage = Uri.parse(refrenceUrl1);
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }




    public void HomeClick(View view){

        try{
            Intent i = new Intent(ProcurementDetailsActivity.this,HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void SettingsClick(View view){

        try{
            Intent i = new Intent(ProcurementDetailsActivity.this,SettingsMain.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void BrowseClick(View view) {
        Intent i = new Intent(ProcurementDetailsActivity.this, BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view){

        try{
            Intent i = new Intent(ProcurementDetailsActivity.this,TrackList.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void ResourceClick(View view){
        Intent i = new Intent(ProcurementDetailsActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if(getIntent().getStringExtra("notif_status")!=null){
            Intent i = new Intent(this, TrackList.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }else
            finish();
    }

    void showPB(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pb = new ProgressDialog(ProcurementDetailsActivity.this);
                pb.setMessage(message);
                pb.setCancelable(true);
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
