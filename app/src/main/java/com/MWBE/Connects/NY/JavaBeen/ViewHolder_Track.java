package com.MWBE.Connects.NY.JavaBeen;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.MWBE.Connects.NY.CustomViews.CustomTextView_Book;
import com.MWBE.Connects.NY.R;

/**
 * Created by Fazal on 5/30/2016.
 */
public class ViewHolder_Track {
    public final ImageView calender_image;
    public RatingBar ratingbar;
    public CustomTextView_Book title;
    public CustomTextView_Book Agency;
    public CustomTextView_Book track_started_date;

    public ViewHolder_Track(View view) {

        title = (CustomTextView_Book) view.findViewById(R.id.title_content);
        Agency = (CustomTextView_Book) view.findViewById(R.id.agency_content);
        ratingbar = (RatingBar) view.findViewById(R.id.rating_bar);
        track_started_date = (CustomTextView_Book) view.findViewById(R.id.trackdate_content);
        calender_image = (ImageView) view.findViewById(R.id.calender);
    }
}
