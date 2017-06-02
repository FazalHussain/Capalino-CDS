package com.MWBE.Connects.NY.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.MWBE.Connects.NY.JavaBeen.ViewHolder_Agency;
import com.MWBE.Connects.NY.AppConstants.Constants;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.JavaBeen.ListData_Agency;
import com.MWBE.Connects.NY.R;

import java.util.ArrayList;
import java.util.List;

public class AgencyActivity extends Activity {

    private ListView lv;
    private ArrayList<ListData_Agency> list;
    private ArrayList<Boolean> list_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency);
        init();
    }

    //BackClick Event
    public void BackClick(View view){
        finish();
    }

    private void init() {
        lv = (ListView) findViewById(R.id.list_agency_lv);
        populatelist();
    }

    private void populatelist() {
        try {
            list = new ArrayList<ListData_Agency>();
            list_check = new ArrayList<Boolean>();

            DataBaseHelper dataBaseHelper = new DataBaseHelper(AgencyActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            list.add(new ListData_Agency("All Agencies"));
            Cursor cursor = dataBaseHelper.getDataFromDBAgency(true);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    String title = cursor.getString(0).replace("\\u0027","'");
                    //String descrip = cursor.getString(1);
                    list.add(new ListData_Agency(title));
                }
            }

            for (int i = 0; i < list.size(); i++) {
                list_check.add(i, false);
            }

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        for (int i = 0; i < list.size(); i++) {
                            list_check.set(i, false);
                        }
                        list_check.set(position, !list_check.get(position));
                        CustomListAdapter adapter = new CustomListAdapter(AgencyActivity.this, R.layout.activity_agency, list);
                        lv.setAdapter(adapter);

                        Intent intentback = new Intent();
                        intentback.putExtra("agency", list.get(position).getTitle());
                        setResult(Constants.request_agency, intentback);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            CustomListAdapter adapter = new CustomListAdapter(AgencyActivity.this, R.layout.activity_agency, list);
            lv.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //Footer tab Click Listener
    public void HomeClick(View view){
        try{
            Intent i = new Intent(AgencyActivity.this,HomeActivity.class);
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
            Intent i = new Intent(AgencyActivity.this,SettingsMain.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void TrackClick(View view){
        try{
            Intent i = new Intent(AgencyActivity.this,TrackList.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void ResourceClick(View view){
        Intent i = new Intent(AgencyActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public class CustomListAdapter extends ArrayAdapter<ListData_Agency> {

        public CustomListAdapter(Context context, int resource, List<ListData_Agency> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try{

                ViewHolder_Agency viewHolder;
                if(convertView==null) {
                    convertView = getLayoutInflater().inflate(R.layout.list_agency_row, null);
                    viewHolder = new ViewHolder_Agency(convertView);
                    convertView.setTag(viewHolder);
                }else {
                    viewHolder = (ViewHolder_Agency) convertView.getTag();
                }

                ListData_Agency data = getItem(position);
                viewHolder.title.setText(data.getTitle());
                if(list_check.get(position)==true) {
                    viewHolder.checkbox.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.checkbox.setVisibility(View.GONE);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
       finish();

    }
}
