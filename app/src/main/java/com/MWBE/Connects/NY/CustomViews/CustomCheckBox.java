package com.MWBE.Connects.NY.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by Fazal on 5/9/2016.
 */
public class CustomCheckBox extends CheckBox {

    public CustomCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCheckBox(Context context) {
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
