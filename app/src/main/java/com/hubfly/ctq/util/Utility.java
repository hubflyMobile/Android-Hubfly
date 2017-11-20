package com.hubfly.ctq.util;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
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

import com.google.gson.Gson;
import com.hubfly.ctq.Model.ActivityModel;
import com.hubfly.ctq.Model.CustomerModel;
import com.hubfly.ctq.Model.OpenCtqModel;
import com.hubfly.ctq.Model.PartModel;
import com.hubfly.ctq.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static com.hubfly.ctq.util.Config.mAlCustomer;
import static com.hubfly.ctq.util.Config.mAlParts;

/**
 * Created by Admin on 03-07-2017.
 */

public class Utility {

    Activity mActivity;
    public static int SCREENWIDTH = 0;
    public static int SCREENHEIGHT = 0;
    public static String UPLOAD_IMAGE_DIRECTORY = "";

    public Utility() {

    }

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
        System.out.println(message);
    }

    public Boolean validation(Context mContext, EditText mEditText1, String option) {

        Boolean status = true;
        String Value = null;
        if (mEditText1 != null) {
            Value = mEditText1.getText().toString();
        }

        if (option.equals("CTQ")) {
            if (Value.length() == 0) {
                showToast(mContext, "Please Enter CTQ Values", "0");//OQ
                status = false;
            }
            if (Value.equals(".")) {
                showToast(mContext, "Please Enter NumericDigit Values", "0");//OQ
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

    public static ProgressDialog showLoading(Activity context) {
        ProgressDialog pDialog = null;
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

   /* public static ProgressDialog showLoading(Activity context) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        return pDialog;
    }*/


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
        mRecyclerView.setLayoutManager(mLayoutManager);
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
        lp.width = GetWidth(c) / 2 - PxToDp(c, 24);
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

    public int PxToDp(Context context, float px) {
        int dip = (int) (0.5f + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, context.getResources().getDisplayMetrics()));
        return (int) Math.ceil(dip);
    }

    public static boolean sdIsPresent() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static void PrepareFolder() {
        try {
            if (sdIsPresent()) {
                String Main_Path = Environment.getExternalStorageDirectory() + "/"
                        + "Hubfly/";
                UPLOAD_IMAGE_DIRECTORY = Main_Path + "Documents" + "/" + "Upload";

                File tempdir = new File(UPLOAD_IMAGE_DIRECTORY);
                CreateFolder(tempdir);

                tempdir = new File(UPLOAD_IMAGE_DIRECTORY + "/" + ".nomedia");
                tempdir.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void CreateFolder(File tempdir) {
        try {
            if (!tempdir.exists()) {
                tempdir.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject SendParams(Activity mActivity, String Option, String JsonInput, Integer ID) {
        SessionManager mSessionManager = new SessionManager(mActivity);
        JSONObject mJsonObject = null;
        try {
            HashMap<String, String> mHashId = mSessionManager.getUserDetails();
            mJsonObject = new JSONObject();
            mJsonObject.put("HostUrl", Config.LoginUrl);
            mJsonObject.put("IsMobileRequest", true);
            mJsonObject.put("Rtfa", mHashId.get("auth_id"));
            mJsonObject.put("FedAuth", mHashId.get("auth_token"));

            if (ID != null) {
                mJsonObject.put("ItemID", ID);
            }

            if (Option != null && Option.equals("0")) {
                Gson gson = new Gson();
                String UserDetails = gson.toJson(Config.mUserDetailsModel);
                JSONObject jsonObject = new JSONObject(UserDetails);
                mJsonObject.put("UserDetails", jsonObject);
            }

            if (JsonInput != null) {
                mJsonObject.put("JsonInput", JsonInput);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mJsonObject;
    }

    public void OpenDrawer(Activity mActivity) {
        DrawerLayout drawerLayout = (DrawerLayout) mActivity
                .findViewById(R.id.drawer_layout);
        LinearLayout drawerList = (LinearLayout) mActivity
                .findViewById(R.id.testing);
        drawerLayout.openDrawer(drawerList);
    }

    public OpenCtqModel SetOpenCtqData(int PartID, String mStrHeatNo, String CustomerName, String JobCode
            , String PartName, String CtqStatus, String QapStatus) {
        OpenCtqModel mOpenCtqModel = new OpenCtqModel();
        mOpenCtqModel.setPartID(PartID);
        mOpenCtqModel.setHeatNo(mStrHeatNo);
        mOpenCtqModel.setCustname(CustomerName);
        mOpenCtqModel.setJobCode(JobCode);
        mOpenCtqModel.setPartname(PartName);
        mOpenCtqModel.setCTQStatus(CtqStatus);
        mOpenCtqModel.setQAPStatus(QapStatus);
        return mOpenCtqModel;
    }


    public ActivityModel SetActivityData(int ActivityID, int JobID, int PartID, String HeatNo, Boolean Verified
            , String ActivityName) {
        ActivityModel mActivityModel = new ActivityModel();
        mActivityModel.setID(ActivityID);
        mActivityModel.setJobIDHF(JobID);
        mActivityModel.setPartIDHF(PartID);
        mActivityModel.setHeatNoHF(HeatNo);
        mActivityModel.setVerifiedHF(Verified);
        mActivityModel.setActivityNameHF(ActivityName);
        return mActivityModel;
    }

    //To check for the internet connection
    public static boolean isInternetConnected(final Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int length = 0; length < info.length; length++) {
                    if (info[length].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast mToast = Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
            }
        });
        return false;
    }


    public void setPartDetails(JSONArray mJsonArray) {
        mAlParts.clear();
        try {
            if (mJsonArray != null) {
                for (int i = 0; i < mJsonArray.length(); i++) {
                    JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                    PartModel mPartModel = new PartModel();
                    mPartModel.setID(mJsonObject.getInt("ID"));
                    mPartModel.setCustomerIDHF(mJsonObject.getString("CustomerIDHF"));
                    mPartModel.setPartNameHF(mJsonObject.getString("PartNameHF"));
                    mPartModel.setPartNoHF(mJsonObject.getString("PartNoHF"));
                    mPartModel.setSpecTypeHF(mJsonObject.getString("SpecTypeHF"));
                    mPartModel.setPartJobCodeHF(mJsonObject.getString("PartJobCodeHF"));
                    mAlParts.add(mPartModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setClientDetails(JSONArray mJsonArray) {
        mAlCustomer.clear();
        try {
            if (mJsonArray != null) {
                for (int i = 0; i < mJsonArray.length(); i++) {
                    JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                    CustomerModel customerModel = new CustomerModel();
                    customerModel.setID(mJsonObject.getInt("ID"));
                    customerModel.setClientName(mJsonObject.getString("ClientNameHF"));
                    mAlCustomer.add(customerModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendAutoMail(Activity mActivity, String Response) {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"hari.murugesan@hubfly.com", "jayakumar.b@hubfly.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Api Response");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Response);
        mActivity.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
    public String setCurrentTimeStamp() {
        return Long.valueOf(System.currentTimeMillis() / 1000).toString();
    }

    public String GetCurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        return new SimpleDateFormat("dd-MM-yyyy , HH:mm").format(c.getTime());
    }
}
