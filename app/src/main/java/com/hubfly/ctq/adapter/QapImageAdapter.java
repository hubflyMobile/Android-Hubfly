package com.hubfly.ctq.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.hubfly.ctq.util.GlideUtil;

import java.util.ArrayList;

/**
 * Created by Admin on 09-07-2017.
 */

public class QapImageAdapter extends BaseAdapter {
    private Activity mContext;
    ArrayList<String> mAlImage;
    GlideUtil mGlideUtil;

    // Constructor
    public QapImageAdapter(Activity mContext, ArrayList<String> mAlImage) {
        this.mContext = mContext;
        this.mAlImage = mAlImage;
        mGlideUtil = new GlideUtil(mContext);
    }

    public int getCount() {
        return mAlImage.size();
    }

    @Override
    public Object getItem(int position) {
        return mAlImage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        mGlideUtil.LoadImages(imageView, 0, "file://" + mAlImage.get(position), true, 1.5f, "file://" + mAlImage.get(position));


        return imageView;
    }


}
