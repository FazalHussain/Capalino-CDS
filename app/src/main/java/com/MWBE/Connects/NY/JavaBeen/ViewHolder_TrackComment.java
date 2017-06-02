package com.MWBE.Connects.NY.JavaBeen;

import android.view.View;
import android.widget.ImageView;

import com.MWBE.Connects.NY.CustomViews.CustomTextView_Book;
import com.MWBE.Connects.NY.R;

/**
 * Created by Fazal on 5/30/2016.
 */
public class ViewHolder_TrackComment {
    public CustomTextView_Book title;
    public ImageView image;

    public ViewHolder_TrackComment(View view) {
        this.title = (CustomTextView_Book) view.findViewById(R.id.title);
        this.image = (ImageView) view.findViewById(R.id.img_icon);
    }
}
