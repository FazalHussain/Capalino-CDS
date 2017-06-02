package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.MWBE.Connects.NY.AppConstants.Constants;
import com.MWBE.Connects.NY.JavaBeen.ListData_Resource;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.JavaBeen.ViewHolder_Resource;
import com.MWBE.Connects.NY.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ResourceActivity extends Activity {

    private ListView lv;
    private CustomListAdapter adapter;
    private File videofiles;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        init();
    }

    private void init() {

        lv = (ListView) findViewById(R.id.list_resource_lv);
        populatelist();
       /* try {
            saveResourceToFile();
            //copyAssets();
            //WriteFiles();

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        Data.ActualID_list_advertising.clear();
        Data.ActualID_list_architectural.clear();
        Data.ActualID_list_construction.clear();
        Data.ActualID_list_envoirnmental.clear();
        Data.ActualID_list_solidwaste.clear();
        Data.ActualID_list_facilities.clear();
        Data.ActualID_list_safety.clear();
        Data.ActualID_list_it.clear();
        Data.ActualID_list_humanservice.clear();
        Data.ActualID_list_others.clear();

        if(Data.DateExpire){
            ((ImageView)findViewById(R.id.browsebtn)).setEnabled(false);
            ((ImageView)findViewById(R.id.browsebtn)).setAlpha(0.5f);
            ((ImageView)findViewById(R.id.trackbtn)).setEnabled(false);
            ((ImageView)findViewById(R.id.trackbtn)).setAlpha(0.5f);
        }


    }

    private void populatelist() {
        ArrayList<ListData_Resource> list = new ArrayList<>();
        list.add(new ListData_Resource("About Capalino+Company",R.drawable.post));
        list.add(new ListData_Resource("Capalino+Company MWBE Team",R.drawable.post));
        list.add(new ListData_Resource("NYC and NYS Certification Fact Sheet",R.drawable.pdf));
        list.add(new ListData_Resource("About the App Video",R.drawable.video));
        list.add(new ListData_Resource("Share MWBE Connect NY With a Friend ",R.drawable.post));
        //list.add(new ListData_Resource("Legal Disclaimer",0));
        list.add(new ListData_Resource("Terms, Conditions and Privacy Policy",R.drawable.pdf));
        adapter = new CustomListAdapter(ResourceActivity.this,R.layout.activity_resource,list);
        lv.setAdapter(adapter);
        lvclick(lv,list);
        adapter.notifyDataSetChanged();
    }

    private void lvclick(ListView lv, final ArrayList<ListData_Resource> list) {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Intent i = new Intent(ResourceActivity.this, ShowPDFActivity.class);
                    switch (position) {
                        case 0: {
                            i.putExtra("url", "http://www.capalino.com/services/");
                            i.putExtra("status", "url");
                            startActivity(i);
                            break;
                        }

                        case 1: {
                            i.putExtra("url", "http://www.capalino.com/services/mwbe-consulting/");
                            i.putExtra("status", "url");
                            startActivity(i);
                            break;
                        }

                        case 2: {
                            i.putExtra("url", "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/MWBEEligFormatted.pdf");
                            i.putExtra("status", "pdf");
                            startActivity(i);
                            break;
                        }



                        case 3: {

                            //String url = Environment.getExternalStorageDirectory() + "/Downloadnyclobbyingfirmscapalinocompany.mp4";
                            //File file = new File(url);
                            i = new Intent(Intent.ACTION_VIEW);
                            i.setDataAndType(Uri.parse("http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/mwbevideo.mp4"), "video/*");
                            startActivity(i);
                            break;
                        }

                        case 4:{
                            EmailClick();
                            //i.putExtra("url", "http://freecs13.hostei.com/celeritas-solutions/Capalino/MWBE%20Elig%20Formatted.pdf");
                            //i.putExtra("status", "pdf");
                            break;
                        }

                        case 5:{
                            //i.putExtra("url", "http://celeritas-solutions.com/cs/pah_brd_v1/capalino_android_v3/old/PrivacyStatement.pdf");
                            i.putExtra("url", "http://ec2-52-4-106-227.compute-1.amazonaws.com/capalinoappaws/Terms.pdf");
                            i.putExtra("status", "pdf");
                            startActivity(i);
                            break;
                        }
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void EmailClick(){
        try{


            /*String agency = getIntent().getStringExtra("agencyname");
            String public_date = getIntent().getStringExtra("proposeddeadline");
            String due_date = getIntent().getStringExtra("duedate");*/



            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT, "MWBE Connect NY");
            //<a href=\"" + link_val + "\">
            /*intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(new StringBuilder()
                    .append("<p><b>Please find the link below to download detailed info. Thanks.</b></p>")
                    .append("<b><p>LINK:</b><p/>")
                    .append("<a><p>" + pdfPath + "<p/></a>")
                    .append("<p>© 2016 Capalino+Company, New York, NY.</p>")
                    .append("<a><p>http://www.capalino.com/</p></a>")
                    .toString()));*/

            //Uri itunesuri = Uri.parse("http://itunes.apple.com/us/app/mwbeconnectny/id1186587404?ls=1&mt=8");
            //Uri androiduri = Uri.parse("http://play.google.com/store/apps/details?id=com.MWBE.Connects.NY");
            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(new StringBuilder()
                    .append("<html><body><p>Hi!</p><br/>")
                    .append("<p>I’ve been using MWBE Connect NY to find and automatically match with City and State RFPs that I’m eligible for. You should check it out, I think you would love it! <p/>")
                    .append("<p>Download the app <p/>")
                    .append("<p>iPhone: http://itunes.apple.com/us/app/mwbeconnectny/id1186587404?ls=1&mt=8<p/>")
                    .append("<p>Android: http://play.google.com/store/apps/details?id=com.MWBE.Connects.NY<p/>")
                    .append("<p>Learn more at www.mwbeconnectny.com.<p/><br/><br/>")
                    .append("<p>Capalino+Company is the #1 ranked lobbying firm in New York City.<p/></body></html>")
                    .toString()));


            //String body = "<html><body><a href=\"" + itunesuri + "\">Here</a></body></html>";

            //intent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(body));

            intent.setType("text/html");
            intent.setType("message/rfc822");
            startActivityForResult(Intent.createChooser(intent, "Send Email"), Constants.Content_email_Constants);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveResourceToFile() throws IOException {
        InputStream in = null;
        FileOutputStream fout = null;
        try {
            in = getResources().openRawResource(R.raw.nyclobbyingfirmscapalinocompany);
            String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            String filename = "nyclobbyingfirmscapalinocompany.mp4";
            fout = new FileOutputStream(new File(downloadsDirectoryPath + filename));

            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }



    //Fotter Tab Click Event

    public void BrowseClick(View view){
        Intent i = new Intent(ResourceActivity.this,BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void SettingsClick(View view){
        Intent i = new Intent(ResourceActivity.this,SettingsMain.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view){
        Intent i = new Intent(ResourceActivity.this,TrackList.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void HomeClick(View view){

        try{
            Intent i = new Intent(ResourceActivity.this,HomeActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public class CustomListAdapter extends ArrayAdapter<ListData_Resource> {

        public CustomListAdapter(Context context, int resource, List<ListData_Resource> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try{

                ViewHolder_Resource viewHolder;
                if(convertView==null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_resource_row, null);

                    viewHolder = new ViewHolder_Resource(convertView);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder_Resource) convertView.getTag();
                }

                ListData_Resource data = getItem(position);
                viewHolder.text.setText(data.getText());
                viewHolder.image.setImageResource(data.getImage());
            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }


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
}
