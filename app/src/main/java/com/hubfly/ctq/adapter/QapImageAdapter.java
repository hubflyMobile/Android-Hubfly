package com.hubfly.ctq.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hubfly.ctq.Model.ImageModel;
import com.hubfly.ctq.R;
import com.hubfly.ctq.activity.NewCTQ;
import com.hubfly.ctq.util.GlideUtil;
import com.hubfly.ctq.util.Utility;

import java.util.ArrayList;

/**
 * Created by Admin on 09-07-2017.
 */

public class QapImageAdapter extends BaseAdapter {

    Activity mContext;
    ArrayList<ImageModel> mAlImage;
    String option;
    GlideUtil mGlideUtil;
    NewCTQ mNewCTQ;
    boolean isClicked = false;

    // Constructor
    public QapImageAdapter(Activity mContext, ArrayList<ImageModel> mAlImage, String option) {
        this.mContext = mContext;
        this.mAlImage = mAlImage;
        this.option = option;
        mGlideUtil = new GlideUtil(mContext);
        mNewCTQ = (NewCTQ) mContext;
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

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder VH;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.gridview_layout, null);
            VH = new ViewHolder(convertView);
            VH.mImgDelete.setTag(position);
            convertView.setTag(VH);
        } else {
            VH = (ViewHolder) convertView.getTag();
        }

        if (mAlImage.get(position).getServer()) {
            VH.mImgDelete.setVisibility(View.GONE);
        } else {
            VH.mImgDelete.setVisibility(View.VISIBLE);
        }

        if (option.equals("0")) {
            Glide.with(mContext).load(mAlImage.get(position).getBaseImage())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(VH.mImgDisplay);
        } else {
            VH.mImgDelete.setVisibility(View.GONE);
            mGlideUtil.LoadImages(VH.mImgDisplay, 0, mAlImage.get(position).getBaseImage(), true, 1.5f, mAlImage.get(position).getBaseImage());
        }


        VH.mImgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isClicked) {
                    isClicked = true;
                    String code = v.getTag().toString();
                    Utility.logging("" + code);
                    mAlImage.remove(Integer.parseInt(code));
                    mNewCTQ.mAlBase.remove(Integer.parseInt(code));
                    notifyDataSetChanged();
                    ChangeClickedStatus();
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView mImgDisplay, mImgDelete;

        ViewHolder(final View convertView) {
            mImgDisplay = (ImageView) convertView.findViewById(R.id.img_display);
            mImgDelete = (ImageView) convertView.findViewById(R.id.img_delete);
        }
    }


    public void ChangeClickedStatus() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isClicked = false;
            }
        }, 300);
    }

}
