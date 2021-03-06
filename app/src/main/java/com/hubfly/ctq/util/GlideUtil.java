package com.hubfly.ctq.util;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.TypedValue;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.hubfly.ctq.R;

import java.io.File;
import java.util.HashMap;


/**
 * Created by vis-1544 on 8/5/2016.
 */
public class GlideUtil {
    Activity context;
    Utility mUtility;
    float mRadius = 1.75f;
    SessionManager mSessionManager;
    String RTFA = "", FedAuth = "";
    HashMap<String, String> mHashMap;

    public GlideUtil(Activity c) {
        this.context = c;
        mUtility = new Utility(c);
        mSessionManager = new SessionManager(c);
    }

    /**
     * Send local path like this Uri.parse("file://" + localpath)
     *
     * @param view
     * @param option
     * @param image_url
     * @param enable_animation
     */
    public void LoadImages(final ImageView view, Integer option, Object image_url,
                           boolean enable_animation, Float radius, final String imageUrl) {
        if (getObjectType(image_url) != null) {
            int drawable = getDefaultImage(option);
            if (image_url instanceof String) {
                if (((String) image_url).contains("/images/email_template_images/")) {
                    option = 3;
                }
            }

            if (radius != null) {
                mRadius = radius;
            } else {
                mRadius = 1.75f;
            }
            Glide.clear(view);
            mHashMap = mSessionManager.getUserDetails();
            RTFA = mHashMap.get("auth_id");
            FedAuth = mHashMap.get("auth_token");
            GlideUrl glideUrl = new GlideUrl(imageUrl, new LazyHeaders.Builder().addHeader("Cookie", "rtFa=" + RTFA + "; FedAuth=" + FedAuth).build());
            switch (option) {
                case 0://Load Feed page Profile Image with default ic_user_profile placeholder
                    if (enable_animation) {
                        Glide.with(context).load(glideUrl)
                                .placeholder(drawable)
                                .error(drawable)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .thumbnail(0.5f)
                                .crossFade()
                                .into(view);
                    } else {
                        Glide.with(context).load(getObjectType(image_url))
                                .placeholder(drawable)
                                .error(drawable)
                                .thumbnail(0.5f)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .dontAnimate()
                                .into(view);
                    }
                    break;
                case 1://Load Settings fragment Profile image with default placeholder
                    if (enable_animation) {
                        Glide.with(context).load(glideUrl)
                                .placeholder(drawable)
                                .error(drawable)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .thumbnail(0.5f)
                                .bitmapTransform(new CropCircleTransformation(context, PxToDp(context, mRadius), "#FDFBFA"))
                                .crossFade()
                                .into(view);
                    } else {
                        Glide.with(context).load(getObjectType(image_url))
                                .placeholder(drawable)
                                .error(drawable)
                                .thumbnail(0.5f)
                                .fitCenter()
                                .bitmapTransform(new CropCircleTransformation(context, PxToDp(context, mRadius), "#FDFBFA"))
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .dontAnimate()
                                .into(view);
                    }
                    break;
                case 2://Load Settings fragment Profile image with default placeholder
                    if (enable_animation) {
                        Glide.with(context).load(glideUrl)
                                .placeholder(drawable)
                                .error(drawable)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .thumbnail(0.5f)
                                .crossFade()
                                .into(view);
                    } else {
                        Glide.with(context).load(getObjectType(image_url))
                                .placeholder(drawable)
                                .error(drawable)
                                .thumbnail(0.5f)
                                .fitCenter()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .dontAnimate()
                                .into(view);
                    }
                    break;
                case 7:
                    break;
                case 8:
                    break;
            }

        }
    }

    public int PxToDp(Context context, float px) {
        int dip = (int) (0.5f + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics()));
        return (int) Math.ceil(dip);
    }

    Object getObjectType(Object o) {
        if (o instanceof String) {
            return (String) o;
        } else if (o instanceof Integer) {
            return (Integer) o;
        } else if (o instanceof File) {
            return (File) o;
        } else if (o instanceof Uri) {
            return (Uri) o;
        } else {
            return null;
        }
    }

    int getDefaultImage(int option) {
        int drawable = R.drawable.ic_user_profile;
        switch (option) {
            case 0:
                drawable = R.drawable.ic_user_profile;
                break;
            case 1:
                drawable = R.drawable.ic_user_profile;
                break;
        }
        return drawable;
    }


}
