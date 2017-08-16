package com.hubfly.ctq.util;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import org.json.JSONObject;

/**
 * Created by Admin on 15-06-2017.
 */

public class CommonService extends IntentService {
    Context mContext;

    public CommonService() {
        super(CommonService.class.getName());
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mContext = this;
        try {
            if (intent != null) {
                String TYPE = "", Url = "", Request, Response;
                Url = intent.getStringExtra("URL");
                Request = intent.getStringExtra("Request");
                Response = intent.getStringExtra("Response");
                JSONObject mJsonObject = new JSONObject();
                mJsonObject.put("LogType", "Response");
                mJsonObject.put("TenantName", Config.LoginUrl);
                mJsonObject.put("AppName", "QAC");
                mJsonObject.put("UserRequest", Request);
                mJsonObject.put("Message", Response);
                mJsonObject.put("Trace", "Checking");
                Utility.logging(mJsonObject + "REQUEST");
                ApiClient.sendErrorReport(Url, mJsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
