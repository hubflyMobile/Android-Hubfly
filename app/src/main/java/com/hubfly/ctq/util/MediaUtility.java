package com.hubfly.ctq.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by vis-1544 on 2/12/2016.
 */
public class MediaUtility {
    Activity mContext;
    public final int REQUEST_CAMERA = 3;

    public void startCameraActivity(Activity mActivity,Uri mMediaCaptureUri) {
        mContext = mActivity;
        Utility.PrepareFolder();
        try {
            Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaCaptureUri);
            mIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", false);
            mIntent.putExtra("return-data", true);
            if (mIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                mActivity.startActivityForResult(mIntent, REQUEST_CAMERA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}