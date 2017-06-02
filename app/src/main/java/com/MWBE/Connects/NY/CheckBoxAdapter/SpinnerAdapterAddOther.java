package com.MWBE.Connects.NY.CheckBoxAdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.MWBE.Connects.NY.R;

import java.util.List;

/**
 * Created by Fazal on 8/17/2016.
 */
public class SpinnerAdapterAddOther extends ArrayAdapter<String> {

    private Context context;

    public SpinnerAdapterAddOther(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_item_add_other, parent, false);
        TextView make = (TextView) row.findViewById(R.id.spinnertext);
        Typeface myTypeFace = Typeface.createFromAsset(context.getAssets(),
                "gotham_book.otf");
        make.setTypeface(myTypeFace);
        make.setText(getItem(position));
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_item_add_other, parent, false);
        TextView make = (TextView) row.findViewById(R.id.spinnertext);
        Typeface myTypeFace = Typeface.createFromAsset(context.getAssets(),
                "gotham_book.otf");
        make.setTypeface(myTypeFace);
        make.setText(getItem(position));
        return row;
    }
}
