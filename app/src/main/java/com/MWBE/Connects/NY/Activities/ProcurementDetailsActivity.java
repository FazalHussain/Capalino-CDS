package com.MWBE.Connects.NY.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.AppConstants.Constants;
import com.MWBE.Connects.NY.R;

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

    public void EmailClick(View v){
        try{
            String title = ((TextView)findViewById(R.id.text_header)).getText().toString();

            title = title.replace("(","");
            title = title.replace(")","");
            title = title.replace("'","");

            /*String agency = getIntent().getStringExtra("agencyname");
            String public_date = getIntent().getStringExtra("proposeddeadline");
            String due_date = getIntent().getStringExtra("duedate");*/

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

            title = title.replace("\\u0027","'");
            agency = agency.replace("\\u0027","'");
            longdesc = longdesc.replace("\\u0027","'");

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


            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(new StringBuilder()
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
                    .toString()));

            intent.setType("text/html");
            intent.setType("message/rfc822");
            startActivityForResult(Intent.createChooser(intent, "Send Email"), Constants.Content_email_Constants);
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    private void init() {
        if(getIntent().getStringExtra("title")!=null){
            ((TextView)findViewById(R.id.text_header)).setText(getIntent().getStringExtra("title"));
        }

        if(getIntent().getStringExtra("fromproc")!=null){
            ((ImageView)findViewById(R.id.messaging)).setVisibility(View.GONE);
        }

       getData();
    }

    private void getData() {
        try{
            DataBaseHelper dataBaseHelper = new DataBaseHelper(ProcurementDetailsActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            String title = getIntent().getStringExtra("title").replace("'","\\u0027");
            Cursor cursor = dataBaseHelper.getDataFromDB("ProcurementTitle",title,"ProcurementMaster",true);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    procid = cursor.getString(0);
                    longdesc = cursor.getString(7).replace("\\u0027","'");
                    agency = cursor.getString(3);
                    agencyurl = cursor.getString(11);
                    starteddate = cursor.getString(17);
                    deadline = cursor.getString(8);

                    ((TextView)findViewById(R.id.longdescrip)).setText(longdesc);
                }
            }
            }catch (Exception e){
                e.printStackTrace();
            }
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
}
