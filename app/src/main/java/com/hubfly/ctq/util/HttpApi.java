package com.hubfly.ctq.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.List;

public class HttpApi extends AsyncTask<String, String, String> {
    private ProgressDialog pDialog;
    Activity activity;
    OnResponseCallback callback;
    Boolean isShowLoader = true;
    Utility mUtility;
    String url,method,option;
    List<NameValuePair> nameValuePair;
    JSONObject mJsonObject;

    public HttpApi(Activity activity, ProgressDialog mProgressDialog, OnResponseCallback callback) {
        this.activity = activity;
        this.pDialog = mProgressDialog;
        this.callback = callback;
        mUtility = new Utility(activity);
    }

    public HttpApi(Activity activity, ProgressDialog mProgressDialog, Boolean isShowLoader, OnResponseCallback callback, String url, String method, String option, List<NameValuePair> nameValuePair, JSONObject mJsonObject
    ) {
        this.activity = activity;
        this.pDialog = mProgressDialog;
        this.callback = callback;
        mUtility = new Utility(activity);
        this.isShowLoader = isShowLoader;
        this.url = url;
        this.method = method;
        this.option = option;
        this.nameValuePair = nameValuePair;
        this.mJsonObject = mJsonObject;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (this.isShowLoader && activity != null) {
            if (pDialog != null) {
                pDialog.show();
            }
        }
    }


    @Override
    protected String doInBackground(String... args) {
        String json = null;
        // Building Parameters
        try {
            json = new ApiClient().makeHttpRequest(activity,url,method,nameValuePair,option,mJsonObject);
            if (json!=null) {
                Log.d("Create Response", json.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (json!=null)
            return json.toString();
        else
            return null;

    }

    protected void onPostExecute(String requestType) {
        // dismiss the dialog once done
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog = null;
        }
        try {
            if (requestType != null) {
                Log.d("Response Data :", requestType);
                callback.responseCallBack(activity, requestType);
            } else {
                Log.d("RESULT", "no result found");
                callback.responseCallBack(activity, requestType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
