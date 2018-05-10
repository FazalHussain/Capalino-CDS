package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.MWBE.Connects.NY.CustomViews.CustomTextView_Bold;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.CustomViews.CustomTextView_Book;
import com.MWBE.Connects.NY.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeItem_ViewActivity extends Activity {

    private CustomTextView_Bold header;
    private ImageView icon;
    private Context context = this;
    private String url ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_item__view);
        init();
    }

    //BackClick Event
    public void BackClick(View view){
        finish();
    }



    private void init() {
        try{
            header = (CustomTextView_Bold) findViewById(R.id.headertext);
            icon = (ImageView) findViewById(R.id.icon_img);

            header.setText(getIntent().getStringExtra("headertext"));
            icon.setImageResource(getIntent().getIntExtra("image", 0));

            ((TextView)findViewById(R.id.link_content)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Alert!")
                            .setMessage("You are being redirected to an external site.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //String url = ((CustomTextView_Book) findViewById(R.id.link_content)).getText().toString();
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
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
            });

            setdata();
            CalenderClick();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void CalenderClick() {
        ((ImageView)findViewById(R.id.calender)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Calendar myCalendar = Calendar.getInstance();
                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setType("vnd.android.cursor.item/event");
                    Log.d("beginTime",myCalendar.getTime().toString());
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
                    String startdate = ((CustomTextView_Book)findViewById(R.id.startdate)).getText().toString();
                    myCalendar.setTime(sdf.parse(startdate));
                    setCalender(startdate,myCalendar);

                    intent.putExtra("allDay", false);
                    //intent.putExtra("rrule", "FREQ=YEARLY");
                    intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, myCalendar.getTimeInMillis());
                    String enddate = ((CustomTextView_Book)findViewById(R.id.enddate)).getText().toString();
                    setCalender(enddate,myCalendar);
                    String descrip = ((CustomTextView_Book) findViewById(R.id.longdescrip)).getText().toString();
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, descrip);
                    String location = ((CustomTextView_Book) findViewById(R.id.location)).getText().toString();
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);
                    String title = ((CustomTextView_Bold)findViewById(R.id.text_header)).getText().toString();
                    intent.putExtra("title", title);

                    intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, myCalendar.getTimeInMillis());
                    intent.putExtra(CalendarContract.Events.HAS_ALARM, 1);
                    startActivity(intent);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void setCalender(String startdate, Calendar myCalendar) {
        try{
            String[] start_date_time = startdate.split(" ");
            String[] date = start_date_time[0].split("-");
            int day = Integer.parseInt(date[0]);
            int month = myCalendar.get(Calendar.MONTH) + 1;
            int year = Integer.parseInt(date[2]);

            String[] time = start_date_time[1].split(":");

            int hours = Integer.parseInt(time[0]);
            int minutes = Integer.parseInt(time[1]);

            Log.d("day", String.valueOf(day));
            Log.d("month", String.valueOf(month));
            Log.d("year", String.valueOf(year));
            Log.d("AMPM",start_date_time[2]);
            Log.d("hh", String.valueOf(hours));
            Log.d("mm", String.valueOf(minutes));

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month-1);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            myCalendar.set(Calendar.HOUR_OF_DAY, hours);
            myCalendar.set(Calendar.MINUTE, minutes);
            Log.d("time",myCalendar.getTime().toString());
            if(start_date_time[2].equalsIgnoreCase("AM"))
                myCalendar.set( Calendar.AM_PM, Calendar.AM );
            else
                myCalendar.set( Calendar.AM_PM, Calendar.PM );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setdata() {
        try{
            DataBaseHelper dataBaseHelper = new DataBaseHelper(HomeItem_ViewActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            Cursor cursor = dataBaseHelper.getDataFromDB("", "", "ContentMasterUpdated", false);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    String contentTitle = cursor.getString(1).replace("\\u0027","'");
                    String ContentDescription = cursor.getString(2).replace("\\u0027","'");
                    if(contentTitle.equalsIgnoreCase(getIntent().getStringExtra("Title"))) {
                        ((CustomTextView_Bold)findViewById(R.id.text_header)).setText(contentTitle);
                        if(!cursor.getString(8).equalsIgnoreCase("NA") && !cursor.getString(8).equalsIgnoreCase(""))
                        ((CustomTextView_Book)findViewById(R.id.time_duration)).setText(cursor.getString(8));
                        ((CustomTextView_Book) findViewById(R.id.longdescrip)).setText(ContentDescription);
                        if(header.getText().toString().equalsIgnoreCase("EVENT")){
                            ((ImageView)findViewById(R.id.calender)).setVisibility(View.VISIBLE);
                            View view = findViewById(R.id.subheader);
                            view.setVisibility(View.VISIBLE);
                            ((CustomTextView_Book)findViewById(R.id.time_duration)).setText(cursor.getString(3));
                            ((CustomTextView_Book)findViewById(R.id.startdate)).setText(cursor.getString(3));
                            ((CustomTextView_Book)findViewById(R.id.enddate)).setText(cursor.getString(4));
                            ((CustomTextView_Book) findViewById(R.id.cost)).setText(cursor.getString(6));
                            ((CustomTextView_Book) findViewById(R.id.location)).setText(cursor.getString(5));

                        }
                        url = cursor.getString(7);
                        if(!url.equalsIgnoreCase("NA") && url!=null && !url.equalsIgnoreCase("")) {


                            if(!url.startsWith("http")){
                                url = "https://"+url;
                            }

                            SpannableString content = new SpannableString("Click here for more information");
                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                            ((CustomTextView_Book) findViewById(R.id.link_content)).setText(content);
                            ((CustomTextView_Book) findViewById(R.id.longdescrip)).setMovementMethod(new ScrollingMovementMethod());
                        }
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void BrowseClick(View view){
        Intent i = new Intent(HomeItem_ViewActivity.this,BrowseActivity.class);
        startActivity(i);
    }

    public void SettingsClick(View view){
        Intent i = new Intent(HomeItem_ViewActivity.this,SettingsMain.class);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(HomeItem_ViewActivity.this,ResourceActivity.class);
        startActivity(i);
    }

    public void TrackClick(View view){
        Intent i = new Intent(HomeItem_ViewActivity.this,TrackList.class);
        startActivity(i);
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        /*Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);*/
        finish();

    }

}
