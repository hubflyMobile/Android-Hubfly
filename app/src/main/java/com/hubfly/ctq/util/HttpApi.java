package com.hubfly.ctq.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

public class HttpApi extends AsyncTask<String, String, String> {
    private ProgressDialog pDialog;
    Activity activity;
    OnResponseCallback callback;
    Boolean isShowLoader = true;
    String url, method;
    JSONObject mJsonObject;

    public HttpApi(Activity activity, Boolean isShowLoader, OnResponseCallback callback, String url, String method, JSONObject mJsonObject) {
        this.activity = activity;
        this.callback = callback;
        this.isShowLoader = isShowLoader;
        this.url = url;
        this.method = method;
        this.mJsonObject = mJsonObject;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (this.isShowLoader) {
            pDialog = Utility.showLoading(activity);
        }
    }


    @Override
    protected String doInBackground(String... args) {
        String json = null;
        try {
            if (Utility.isInternetConnected(activity)) {
                json = new ApiClient().makeHttpRequest(activity, url, method, mJsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (json != null)
            return json.toString();
        else
            return null;

    }

    protected void onPostExecute(String requestType) {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
                pDialog = null;
            }
            if (requestType != null) {
                Utility.logging("Response Data :" + requestType);
                callback.responseCallBack(activity, requestType);
            } else {
                Utility.logging("Response Data :" + "No Result");
                callback.responseCallBack(activity, requestType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
