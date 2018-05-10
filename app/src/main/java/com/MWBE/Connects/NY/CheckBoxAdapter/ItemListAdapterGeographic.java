/**
 *
 */
package com.MWBE.Connects.NY.CheckBoxAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.JavaBeen.ListData_Agency;
import com.MWBE.Connects.NY.R;

import java.util.List;


/**
 * Adapter that allows us to render a list of items
 *
 * @author marvinlabs
 */
public class ItemListAdapterGeographic extends ArrayAdapter<ListData_Agency> {

    private int pos;

    /**
     * Constructor from a list of items
     */
    public ItemListAdapterGeographic(Context context, List<ListData_Agency> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // The item we want to get the view for
        // --
        final ListData_Agency item = getItem(position);


        // Re-use the view if possible
        // --
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.list_row_agency1, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.holder, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.holder);
        }

        // Set some view properties
        holder.title.setText(item.getTitle());
        // Restore the checked state properly


        final ListView lv = (ListView) parent;

        holder.layout.setChecked(lv.isItemChecked(position));

        return convertView;



            /*if (Data.tagidlist.size() > 0) {
                if(Data.tagidlist.contains("0")){
                    lv.setItemChecked(0, true);
                    Data.list_check.set(0, true);
                }
                for (int i = 0; i < Data.tagidlist.size(); i++) {

                    *//*if(Data.tagidlist.contains("0")){
                        lv.setItemChecked(0, true);
                        Data.list_check.set(0, true);
                    }*//*

                    *//*if(Data.tagidlist.get(i).equalsIgnoreCase("63")){
                        lv.setItemChecked(1, true);
                        Data.list_check.set(1, true);
                    }*//*

                    for (int j = 0; j < Data.tagidlist_db.size(); j++) {
                        if (Data.tagidlist.get(i).equalsIgnoreCase(Data.tagidlist_db.get(j))) {
                            lv.setItemChecked(j, true);
                            Data.list_check.set(j,true);
                        }
                    }
                }
            }


            if(Data.list_check.size()>0)
                if(Data.list_check.get(position)) {
                    lv.setItemChecked(position, true);
                }
*/




    }



    private static class ViewHolder {
        public ViewHolder(View root) {
            title = (TextView) root.findViewById(R.id.title);
            layout = (CheckableRelativeLayout) root.findViewById(R.id.layout);
        }

        public TextView title;
        public CheckableRelativeLayout layout;
    }
}
