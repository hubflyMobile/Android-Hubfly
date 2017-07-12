package com.hubfly.ctq.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by vis-1544 on 6/6/2016.
 */
public class CustomTypeface {

    private static CustomTypeface instance = new CustomTypeface();

    private CustomTypeface() {
    }

    private Typeface mTypeFace = null;

    public static CustomTypeface getInstance() {
        return instance;
    }

    public Typeface getTypeface(Context context) {
        if (mTypeFace == null) {
            mTypeFace = Typeface.createFromAsset(context.getAssets(), "fonts/robotoregular.ttf");
        }
        return mTypeFace;
    }
}
