package com.MWBE.Connects.NY.SectionAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.MWBE.Connects.NY.R;

import java.util.ArrayList;

/**
 * Created by Fazal on 7/29/2016.
 */
public class EntryAdapter extends ArrayAdapter<Item> {

    private Context context;
    private ArrayList<Item> items;
    private LayoutInflater vi;
    private ArrayList<Boolean> list_check;

    public EntryAdapter(Context context, ArrayList<Item> items, ArrayList<Boolean> list_check) {
        super(context,0, items);
        this.context = context;
        this.items = items;
        this.list_check = list_check;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final Item i = items.get(position);
        if (i != null) {
            if(i.isSection()){
                SectionItem si = (SectionItem)i;
                v = vi.inflate(R.layout.layout_header, null);

                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);

                final TextView sectionView = (TextView) v.findViewById(R.id.section_header);
                sectionView.setText(si.getTitle());

            }else{
                EntryItem ei = (EntryItem)i;


                v = vi.inflate(R.layout.list_agency_row, null);
                final TextView title = (TextView)v.findViewById(R.id.title);
                ImageView checkbox = (ImageView)v.findViewById(R.id.checkbox_);


                if (title != null)
                    title.setText(ei.title);






                if(list_check.size()>0)
                if (list_check.get(position) == true) {
                    checkbox.setVisibility(View.VISIBLE);
                } else {
                    checkbox.setVisibility(View.GONE);
                }
            }
        }
        return v;
    }

}
