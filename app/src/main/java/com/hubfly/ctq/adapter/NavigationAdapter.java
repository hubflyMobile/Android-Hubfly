package com.hubfly.ctq.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hubfly.ctq.Model.NavigationModel;
import com.hubfly.ctq.R;


/**
 * Created by Admin on 08-06-2017.
 */

public class NavigationAdapter extends ArrayAdapter<NavigationModel> {

    Context mContext;
    int layoutResourceId;
    NavigationModel data[] = null;
    public TextView textViewName;
    public ImageView mImgSlide;
    public LinearLayout mLlNavView;

    public NavigationAdapter(Context mContext, int layoutResourceId, NavigationModel[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        textViewName = (TextView) listItem.findViewById(R.id.txt_name);
        mImgSlide = (ImageView) listItem.findViewById(R.id.img_slide);
        mLlNavView = (LinearLayout) listItem.findViewById(R.id.ll_nav_view);

        NavigationModel folder = data[position];
        textViewName.setText(folder.name);
        mImgSlide.setImageDrawable(ContextCompat.getDrawable(mContext, folder.getIcon()));

        return listItem;
    }
}