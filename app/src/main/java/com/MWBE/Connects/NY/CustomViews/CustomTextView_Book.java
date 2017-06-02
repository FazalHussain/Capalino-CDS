package com.MWBE.Connects.NY.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Syed Fazal Hussain on 3/11/2016.
 */
public class CustomTextView_Book extends TextView {

    public CustomTextView_Book(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomTextView_Book(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView_Book(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "gotham_book.otf");
            setTypeface(tf);
        }
    }
}
