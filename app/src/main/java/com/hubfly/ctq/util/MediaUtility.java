package com.hubfly.ctq.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.hubfly.ctq.R;

import java.io.File;

/**
 * Created by vis-1544 on 2/12/2016.
 */
public class MediaUtility {
    Activity mContext;
    public final int SELECT_PICTURE = 1,REQUEST_CAMERA = 3;

    public void startCameraActivity(Activity mActivity) {
        mContext = mActivity;
        try {
            Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mIntent.putExtra("return-data", true);
            if (mIntent.resolveActivity(mActivity.getPackageManager()) != null) {
                mActivity.startActivityForResult(mIntent, REQUEST_CAMERA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GalleryActivity(Activity mActivity) {
        mContext = mActivity;
        Intent mIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mIntent.setType("image");
        mIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        mIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        mActivity.startActivityForResult(Intent.createChooser(mIntent, mContext.getResources().getString(R.string.media)), SELECT_PICTURE);

    }



    public String UriToPath(Activity mActivity, Uri uri) {
        mContext = mActivity;
        String filePath = "";
        try {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = mActivity.getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();
            }
            Utility.logging("filePath " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filePath == null)
            filePath = "";
        return filePath;
    }

    public String UriToPath(Context c, Uri uri) {
        String filePath = "";
        try {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = c.getContentResolver().query(uri, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();
            }
            Utility.logging("filePath " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (filePath == null)
            filePath = "";
        return filePath;
    }
    public boolean isFile(final Activity mActivity, String path) {
        boolean isFile = false;
        mContext = mActivity;
        boolean isFilePresent = false;
        if (path != null && !path.equals("null") && !path.equals("")) {
            File mFile = new File(path);
            if (mFile != null && mFile.exists()) {
                isFilePresent = true;
                {
                    isFile = true;
                }
            }
        }
        return isFile;
    }

    public  String getImageRealPath(Context context, Uri uri) {
        String filepath ="";
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = context.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        Utility.logging(filepath);
        cursor.close();

        return filepath;
    }
}