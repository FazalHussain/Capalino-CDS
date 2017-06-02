package com.MWBE.Connects.NY.CustomViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;

/**
 * Created by Fazal on 5/13/2016.
 */
public class CustomEditText_Book extends EditText {

    CustomEditText_Book editText_book;

    public CustomEditText_Book(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        editText_book.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        init();
    }

    public CustomEditText_Book(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText_Book(Context context) {
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
