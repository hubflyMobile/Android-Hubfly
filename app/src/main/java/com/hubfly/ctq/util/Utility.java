package com.hubfly.ctq.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hubfly.ctq.R;

/**
 * Created by Admin on 03-07-2017.
 */

public class Utility {

    Activity mActivity;
    public static int SCREENWIDTH = 0;
    public static int SCREENHEIGHT = 0;

    public Utility(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void showToast(Context mContext, String message, String option) {
        if (mContext != null) {
            Toast mToast = null;
            if (option != null && !option.equals("")) {
                if (option.equals("1")) { //Short
                    mToast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
                } else {
                    mToast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
                }
            }
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        }
    }

    public void HideShowKeyboard(Context mContext, View view, String option) {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (option.equals("1")) { // option =1 -> show key board
                inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            } else { //hide key board
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void logging(String message) {
        System.out.println("Response" + message);
    }

    public Boolean validation(Context mContext, EditText mEditText1, String option) {

        Boolean status = true;
        String Value = null;
        if (mEditText1!=null) {
            Value = mEditText1.getText().toString();
        }

        if (option.equals("CTQ")) {
            if (Value.length() == 0) {
                showToast(mContext, "Please Enter CTO Values", "0");
                status = false;
            }
        } else if (option.equals("Remarks")) {
            if (Value.length() == 0) {
                showToast(mContext, "Please Enter Remarks", "0");
                status = false;
            }
        } else if (option.equals("time")) {
            if (Value.length() == 0) {
                showToast(mContext, "Please Select Time", "0");
                status = false;
            }
        } else if (option.equals("Search")) {
            if (Value.length() == 0) {
                showToast(mContext, "Please Enter Fields", "0");
                status = false;
            }
        } else if (option.equals("Question")) {
            if (Value.length() == 0) {
                showToast(mContext, "Please Enter" + option + " Fields", "0");
                status = false;
            }
        } else if (option.equals("Answer")) {
            if (Value.length() == 0) {
                showToast(mContext, "Please Enter" + option + " Fields", "0");
                status = false;
            }
        }
        return status;
    }

    public interface onBroadcastReceived {
        public void onBroadcastReceived(Intent intent);
    }

    public static ProgressDialog showLoading(Context context){
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

    public RecyclerView CustomRecycleView(Activity mActivity, LinearLayout mLlHeader) {
        RecyclerView mRecyclerView = new RecyclerView(mActivity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLlHeader.addView(mRecyclerView, params);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setClickable(true);
        mRecyclerView.setFocusable(true);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));

        return mRecyclerView;
    }
    public Dialog getDialog(Context context) {
        final Dialog mDialog = new Dialog(context, R.style.AppTheme);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg_only);
        mDialog.setCancelable(true);
        return mDialog;
    }

    public void setDialogFullScreen(Activity c, Dialog mDialog, int option) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mDialog.getWindow();
        lp.copyFrom(window.getAttributes());
//        lp.width = GetWidth(c) - PxToDp(c, 24);
        lp.width = GetWidth(c)/2 - PxToDp(c, 24);
        if (option == 1) {//Set height to flag list in my Profile
            lp.height = (int) (GetHeight(c) / 1.5);
        } else {
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        window.setAttributes(lp);
    }

    public Integer GetWidth(Activity context) {
        if (SCREENWIDTH == 0) {
            DisplayMetrics metrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            SCREENWIDTH = metrics.widthPixels;
            return metrics.widthPixels;
        } else {
            return SCREENWIDTH;
        }
    }

    public Integer GetHeight(Activity context) {
        if (SCREENHEIGHT == 0) {
            DisplayMetrics metrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            SCREENHEIGHT = metrics.heightPixels;
            return metrics.heightPixels;
        } else {
            return SCREENHEIGHT;
        }
    }

    public int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public int PxToDp(Context context, float px) {
        int dip = (int) (0.5f + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics()));
        return (int) Math.ceil(dip);
    }
}
