package com.MWBE.Connects.NY.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Fazal on 5/10/2016.
 */
public class CustomTextView_Bold extends TextView {
    public CustomTextView_Bold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomTextView_Bold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView_Bold(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "gotham_bold.otf");
            setTypeface(tf);
        }
    }
}
