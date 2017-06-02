package com.MWBE.Connects.NY.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.MWBE.Connects.NY.CheckBoxAdapter.ItemListAdapterCertificationFragment;
import com.MWBE.Connects.NY.Database.DataBaseHelper;
import com.MWBE.Connects.NY.JavaBeen.ListData_Agency;
import com.MWBE.Connects.NY.JavaBeen.ViewHolder_Agency;
import com.MWBE.Connects.NY.Storage.Storage;
import com.MWBE.Connects.NY.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fazal on 7/28/2016.
 */
public class CertificationSetup extends Fragment {

    private Button nextbtn;
    private ListView lv;
    private ArrayList<ListData_Agency> list;
    private ArrayList<Boolean> list_check;
    private Button backbtn;
    private ImageView questionimage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.certification_setup,container,false);
        init(rootview);
        NextClick(rootview);
        BackClick(rootview);
        QuestionClick();
        return rootview;
    }

    private void QuestionClick() {
        questionimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    public void showDialog(){
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View dialogview = factory.inflate(
                R.layout.activation_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setView(dialogview);
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        Button btn = (Button)dialogview.findViewById(R.id.okbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void init(View rootview) {
        lv = (ListView) rootview.findViewById(R.id.list_certification);
        nextbtn = (Button) rootview.findViewById(R.id.next);
        backbtn = (Button) rootview.findViewById(R.id.back);
        questionimage = (ImageView) rootview.findViewById(R.id.question);
        populatelist();
    }

    private void populatelist() {
        try {
            list = new ArrayList<ListData_Agency>();
            list_check = new ArrayList<Boolean>();

            DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity());
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
            Cursor cursor = dataBaseHelper.getDataFromDB("", "", "CertificationTypeTags", false);
            if(cursor.getCount()>0){
                while (cursor.moveToNext()){
                    if(list.size()>=9){
                        list.add(new ListData_Agency("New York State - "+cursor.getString(2)));
                    }else
                        list.add(new ListData_Agency("New York City - "+cursor.getString(2)));
                }
            }

            for (int i = 0; i < list.size(); i++) {
                list_check.add(i, false);
            }

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        list_check.set(position, !list_check.get(position));
                        Storage.list_check_certification = list_check;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            if(Storage.list_check_certification.size()>0){
                list_check = Storage.list_check_certification;
            }

            ItemListAdapterCertificationFragment adapter = new ItemListAdapterCertificationFragment(getActivity(),list);
            lv.setAdapter(adapter);
            lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            lv.setItemsCanFocus(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void NextClick(View rootview) {
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.setup, new CapablitiesSetup()).addToBackStack("tag").commit();
            }
        });
    }

    private void BackClick(View rootview) {
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.setup, new TargetContractValueSetup()).commit();
            }
        });
    }

    public class CustomListAdapter extends ArrayAdapter<ListData_Agency> {

        public CustomListAdapter(Context context, int resource, List<ListData_Agency> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {

                ViewHolder_Agency viewHolder;
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.list_agency_row, null);
                    viewHolder = new ViewHolder_Agency(convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder_Agency) convertView.getTag();
                }

                ListData_Agency data = getItem(position);
                viewHolder.title.setText(data.getTitle());


                if (list_check.get(position) == true) {
                    viewHolder.checkbox.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.checkbox.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }
    }
}
