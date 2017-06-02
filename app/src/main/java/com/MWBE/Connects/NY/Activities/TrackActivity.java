package com.MWBE.Connects.NY.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.MWBE.Connects.NY.JavaBeen.ListData_Track;
import com.MWBE.Connects.NY.JavaBeen.ListData_track_comnt;
import com.MWBE.Connects.NY.JavaBeen.ViewHolder_Track;
import com.MWBE.Connects.NY.R;

import java.util.ArrayList;
import java.util.List;

public class TrackActivity extends FragmentActivity {

    private ListView lv;
    private ArrayList<ListData_Track> list;
    private ListView lv1;
    private ArrayList<ListData_track_comnt> list_cmnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        init();
    }

    //BackClick Event
    public void BackClick(View view){
        finish();
    }

    private void init() {
        lv = (ListView) findViewById(R.id.list_tracking);
        list = new ArrayList<ListData_Track>();
        populatelist();
    }

    private void populatelist() {


        ListData_Track listData_rfp = (ListData_Track) getIntent().getSerializableExtra("list_data");
        list.add(new ListData_Track(listData_rfp.getId(),listData_rfp.getHeader(), listData_rfp.getRating(), listData_rfp.getTitle(),
                listData_rfp.getAgency(), listData_rfp.getTrack_started_date()));
        CustomListAdapter adapter = new CustomListAdapter(TrackActivity.this,R.layout.list_track_row,list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(TrackActivity.this, ProcurementDetailsActivity.class);
                i.putExtra("title", list.get(position).getTitle());
                startActivity(i);
            }
        });
    }



    public class CustomListAdapter extends ArrayAdapter<ListData_Track> {

        public CustomListAdapter(Context context, int resource, List<ListData_Track> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            try{
                ViewHolder_Track viewHolder;
                if(convertView==null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_track_row, null);
                    viewHolder = new ViewHolder_Track(convertView);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder_Track) convertView.getTag();
                }

                ListData_Track data = getItem(position);
                viewHolder.title.setText(data.getTitle());
                viewHolder.Agency.setText(data.getAgency());
                viewHolder.track_started_date.setText(data.getTrack_started_date());
                viewHolder.ratingbar.setRating((float) data.getRating());

                //((ImageView)findViewById(R.id.arrow)).setVisibility(View.GONE);
                ((ImageView)findViewById(R.id.delete)).setVisibility(View.GONE);

            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }


    }



    //Footer Click Listener
    public void BrowseClick(View view){
        Intent i = new Intent(TrackActivity.this,BrowseActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void SettingsClick(View view){
        Intent i = new Intent(TrackActivity.this,SettingsMain.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(TrackActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void HomeClick(View view){
        Intent i = new Intent(TrackActivity.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);

    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
       finish();

    }
}
