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

import com.MWBE.Connects.NY.JavaBeen.ListData_Agency;
import com.MWBE.Connects.NY.Storage.Storage;
import com.MWBE.Connects.NY.R;

import java.util.List;


/**
 * Adapter that allows us to render a list of items
 *
 * @author marvinlabs
 */
public class ItemListAdapterContractValueFragment extends ArrayAdapter<ListData_Agency> {

    private int pos;

    /**
     * Constructor from a list of items
     */
    public ItemListAdapterContractValueFragment(Context context, List<ListData_Agency> items) {
        super(context, 0, items);
        li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            convertView = li.inflate(R.layout.list_row_agency1, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.holder, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.holder);
        }

        // Set some view properties
        holder.title.setText(item.getTitle());
        // Restore the checked state properly
        final ListView lv = (ListView) parent;

        try {
            if(Storage.list_check_target_contract.size()>0)
                if(Storage.list_check_target_contract.get(position)){
                    lv.setItemChecked(position,true);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.layout.setChecked(lv.isItemChecked(position));


        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public int getPos() {
        return pos;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private LayoutInflater li;

    private static class ViewHolder {
        public ViewHolder(View root) {
            title = (TextView) root.findViewById(R.id.title);
            layout = (CheckableRelativeLayout) root.findViewById(R.id.layout);
        }

        public TextView title;
        public CheckableRelativeLayout layout;
    }
}
