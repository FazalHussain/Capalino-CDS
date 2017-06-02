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

public class ProcurementTypeActivity extends Activity {

    private ListView lv;
    private ArrayList<ListData_Agency> list;
    private ArrayList<Boolean> list_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurement_type);
        init();

    }

    //BackClick Event
    public void BackClick(View view){
        finish();
    }

    private void init() {
        lv = (ListView) findViewById(R.id.list_procurement_lv);
        populatelist();
    }

    private void populatelist() {
        try {
            list = new ArrayList<ListData_Agency>();
            list_check = new ArrayList<Boolean>();
            /*list.add(new ListData_Agency("Constructions"));
            list.add(new ListData_Agency("Goods"));
            list.add(new ListData_Agency("Standard Services"));
            list.add(new ListData_Agency("Professional Services"));*/
            final ArrayList<String> capabilityID = new ArrayList<>();
            DataBaseHelper dataBaseHelper = new DataBaseHelper(ProcurementTypeActivity.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            Cursor cursor = dataBaseHelper.getDataFromDB("", "", "CapabilitiesTypeTags", false);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    list.add(new ListData_Agency(cursor.getString(2)));
                    capabilityID.add(cursor.getString(1));
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
                        CustomListAdapter adapter = new CustomListAdapter(ProcurementTypeActivity.this, R.layout.activity_agency, list);
                        lv.setAdapter(adapter);

                        Intent intentback = new Intent();
                        intentback.putExtra("capabilitytype", list.get(position).getTitle());
                        intentback.putExtra("capabilityID",capabilityID.get(position));
                        setResult(Constants.request_procurement_type, intentback);
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            CustomListAdapter adapter = new CustomListAdapter(ProcurementTypeActivity.this, R.layout.activity_agency, list);
            lv.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
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

    //Footer tab Click Listener
    public void HomeClick(View view){
        Intent i = new Intent(ProcurementTypeActivity.this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void SettingsClick(View view){
        Intent i = new Intent(ProcurementTypeActivity.this,SettingsMain.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void ResourceClick(View view){
        Intent i = new Intent(ProcurementTypeActivity.this,ResourceActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
    }

    public void TrackClick(View view){
        Intent i = new Intent(ProcurementTypeActivity.this,TrackList.class);
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

