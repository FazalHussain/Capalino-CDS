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

import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.DataStorage.Data;
import com.MWBE.Connects.NY.JavaBeen.ListData_Agency;
import com.MWBE.Connects.NY.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Adapter that allows us to render a list of items
 * 
 * @author marvinlabs
 */
public class ItemListAdapter extends ArrayAdapter<ListData_Agency> {

	private final Utils utils;
	private int pos;
	private ArrayList<Boolean> list_check;
	private List<ListData_Agency> list;


	/**
	 * Constructor from a list of items
	 */
	public ItemListAdapter(Context context, List<ListData_Agency> items,ArrayList<Boolean> list_check) {
		super(context, 0, items);

		utils = new Utils(context);
		this.list_check = list_check;
		list = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			Context context = parent.getContext();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.list_row_agency1, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(R.id.holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.holder);
		}

		// Set some view properties
		// Restore the checked state properly
		final ListView lv = (ListView) parent;
		ListData_Agency item = getItem(position);
		holder.title.setText(item.getTitle());
		holder.layout.setChecked(lv.isItemChecked(position));
		return convertView;
		//String status = utils.getdata("status");
		/*switch (status){
			case "Advertising": {
				if (Data.ActualID_list_advertising.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						for (int j = 0; j < Data.ActualID_list_advertising.size(); j++) {
							if (list.get(i).getTagID() == Data.ActualID_list_advertising.get(j)) {
								lv.setItemChecked(i, true);
							}

						}
					}
				}

				if(lv.isItemChecked(position)){
					try {
						Data.list_check_capabilities_adv.set(position, true);
					}catch (Exception e){
						e.printStackTrace();
					}
				}
				break;
			}

			case "HVConstruction":{
				if (Data.ActualID_list_construction.size() > 0) {
					for (int i = 0; i < Data.ActualID_list_construction_db.size(); i++) {
						for (int j = 0; j < Data.ActualID_list_construction.size(); j++) {
							if (Data.ActualID_list_construction_db.get(i).toString().equalsIgnoreCase(Data.ActualID_list_construction.get(j).toString())) {
								lv.setItemChecked(i, true);
								//Data.list_check_capabilities_cons.set(i, true);
							}

						}
					}
				}

				if(lv.isItemChecked(position)){
					Data.list_check_capabilities_cons.set(position, true);
				}
				break;
			}

			case "Architecture":{
				if (Data.ActualID_list_architectural.size() > 0) {
					for (int i = 0; i < Data.ActualID_list_architectural_db.size(); i++) {
						for (int j = 0; j < Data.ActualID_list_architectural.size(); j++) {
							if (Data.ActualID_list_architectural_db.get(i).toString().equalsIgnoreCase(Data.ActualID_list_architectural.get(j).toString())) {
								lv.setItemChecked(i, true);
								//Data.list_check_capabilities_arc.set(i, true);
							}

						}
					}
				}

				if(lv.isItemChecked(position)){
					Data.list_check_capabilities_arc.set(position, true);
				}

				break;
			}

			case "Envoirnmental":{
				if (Data.ActualID_list_envoirnmental.size() > 0) {
					for (int i = 0; i < Data.ActualID_list_envoirnmental_db.size(); i++) {
						for (int j = 0; j < Data.ActualID_list_envoirnmental.size(); j++) {
							if (Data.ActualID_list_envoirnmental_db.get(i).toString().equalsIgnoreCase(Data.ActualID_list_envoirnmental.get(j).toString())) {
								lv.setItemChecked(i, true);
								//Data.list_check_capabilities_envoirnmental.set(i, true);
							}

						}
					}
				}

				if(lv.isItemChecked(position)){
					Data.list_check_capabilities_envoirnmental.set(position, true);
				}
				break;
			}

			case "Facilities":{
				if (Data.ActualID_list_facilities.size() > 0) {
					for (int i = 0; i < Data.ActualID_list_facilities_db.size(); i++) {
						for (int j = 0; j < Data.ActualID_list_facilities.size(); j++) {
							if (Data.ActualID_list_facilities_db.get(i).toString().equalsIgnoreCase(Data.ActualID_list_facilities.get(j).toString())) {
								lv.setItemChecked(i, true);
								//Data.list_check_capabilities_facility.set(i, true);
							}

						}
					}
				}

				if(lv.isItemChecked(position)){
					Data.list_check_capabilities_facility.set(position, true);
				}
				break;
			}

			case "GeneralMaintainance":{
				if (Data.ActualID_list_solidwaste.size() > 0) {
					for (int i = 0; i < Data.ActualID_list_solidwaste_db.size(); i++) {
						for (int j = 0; j < Data.ActualID_list_solidwaste.size(); j++) {
							if (Data.ActualID_list_solidwaste_db.get(i).toString().equalsIgnoreCase(Data.ActualID_list_solidwaste.get(j).toString())) {
								lv.setItemChecked(i, true);
								//Data.list_check_capabilities_solidstate.set(i, true);
							}

						}
					}
				}

				if(lv.isItemChecked(position)){
					Data.list_check_capabilities_solidstate.set(position, true);
				}
				break;
			}

			case "Security":{
				if (Data.ActualID_list_safety.size() > 0) {
					for (int i = 0; i < Data.ActualID_list_safety_db.size(); i++) {
						for (int j = 0; j < Data.ActualID_list_safety.size(); j++) {
							if (Data.ActualID_list_safety_db.get(i).toString().equalsIgnoreCase(Data.ActualID_list_safety.get(j).toString())) {
								lv.setItemChecked(i, true);
								//Data.list_check_capabilities_safety.set(i, true);
							}

						}
					}
				}

				if(lv.isItemChecked(position)){
					Data.list_check_capabilities_safety.set(position, true);
				}
				break;
			}

			case "IT":{
				if (Data.ActualID_list_it.size() > 0) {
					for (int i = 0; i < Data.ActualID_list_it_db.size(); i++) {
						for (int j = 0; j < Data.ActualID_list_it.size(); j++) {
							if (Data.ActualID_list_it_db.get(i).toString().equalsIgnoreCase(Data.ActualID_list_it.get(j).toString())) {
								lv.setItemChecked(i, true);
								//Data.list_check_capabilities_it.set(i, true);
							}

						}
					}
				}

				if(lv.isItemChecked(position)){
					Data.list_check_capabilities_it.set(position, true);
				}
				break;
			}

			case "humanservices":{
				if (Data.ActualID_list_humanservice.size() > 0) {
					for (int i = 0; i < Data.ActualID_list_humanservice_db.size(); i++) {
						for (int j = 0; j < Data.ActualID_list_humanservice.size(); j++) {
							if (Data.ActualID_list_humanservice_db.get(i).toString().equalsIgnoreCase(Data.ActualID_list_humanservice.get(j).toString())) {
								lv.setItemChecked(i, true);
								//Data.list_check_capabilities_hservices.set(i, true);
							}

						}
					}
				}

				if(lv.isItemChecked(position)){
					Data.list_check_capabilities_hservices.set(position, true);
				}
				break;
			}

			case "Others":{
				if (Data.ActualID_list_others.size() > 0) {
					for (int i = 0; i < Data.ActualID_list_others_db.size(); i++) {
						for (int j = 0; j < Data.ActualID_list_others.size(); j++) {
							if (Data.ActualID_list_others_db.get(i).toString().equalsIgnoreCase(Data.ActualID_list_others.get(j).toString())) {
								lv.setItemChecked(i, true);
								//Data.list_check_capabilities_others.set(i, true);
							}

						}
					}
				}

				if(lv.isItemChecked(position)){
					Data.list_check_capabilities_others.set(position, true);
				}
				break;
			}


		}*/




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
