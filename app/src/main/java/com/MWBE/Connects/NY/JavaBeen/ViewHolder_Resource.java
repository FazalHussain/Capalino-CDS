package com.MWBE.Connects.NY.JavaBeen;

import android.view.View;
import android.widget.ImageView;

import com.MWBE.Connects.NY.CustomViews.CustomTextView_Book;
import com.MWBE.Connects.NY.R;

/**
 * Created by Fazal on 5/27/2016.
 */
public class ViewHolder_Resource {

    public CustomTextView_Book text;
    public ImageView image;

    public ViewHolder_Resource(View view) {
        text = (CustomTextView_Book) view.findViewById(R.id.title);
        image = (ImageView) view.findViewById(R.id.image);
    }


}
