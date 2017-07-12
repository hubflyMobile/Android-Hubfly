package com.hubfly.ctq.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Admin on 23-03-2017.
 */

public class CustomTextView extends TextView {
    Context mContext;

    public CustomTextView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public void init() {
        Typeface face = CustomTypeface.getInstance().getTypeface(mContext);
        if (getTypeface() != null && getTypeface().isBold()) {
            setTypeface(face, Typeface.BOLD);
        } else {
            setTypeface(face);
        }
    }
}
