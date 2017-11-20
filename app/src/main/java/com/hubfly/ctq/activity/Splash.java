package com.hubfly.ctq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hubfly.ctq.R;
import com.hubfly.ctq.util.Config;
import com.hubfly.ctq.util.SessionManager;
import com.hubfly.ctq.util.Utility;

import java.util.HashMap;

/**
 * Created by Admin on 10-03-2017.
 */

public class Splash extends Activity {

    SessionManager mSessionManager;
    private static int SPLASH_TIME_OUT = 2500;
    Utility mUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSessionManager = new SessionManager(getApplicationContext());
        mUtility = new Utility(Splash.this);
        HashMap<String, String> mHashMap = this.mSessionManager.getDeviceId();
        int TotalTimeStamp = 0;
        if (!(this.mSessionManager.getCurrentTimeStamp() == null || this.mSessionManager.getCurrentTimeStamp().equals(""))) {
            TotalTimeStamp = Integer.parseInt(this.mUtility.setCurrentTimeStamp()) - Integer.parseInt(this.mSessionManager.getCurrentTimeStamp());
        }
        if (TotalTimeStamp > 259200) {
            this.mSessionManager.ClearUser();
            MoveToNextScreen();
        } else if (mHashMap.get("device_id") == null || ((String) mHashMap.get("device_id")).equals("")) {
            MoveToNextScreen();
        } else {
            MoveToNextScreen();
        }
    }


    void MoveToNextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> mHashMap = mSessionManager.getUserDetails();
                if (mHashMap.get("auth_token") != null && !mHashMap.get("auth_token").equals("") && mHashMap.get("auth_id") != null && mHashMap.get("auth_id") != null) {
                    if (mSessionManager.isLoggedIn()) {
                        MoveAnotherActivity(Splash.this, DummyActivity.class);
                    } else {
                        MoveAnotherActivity(Splash.this, Login.class);
                    }
                    Config.Rtfa = mHashMap.get("auth_id");
                    Config.FedAuth = mHashMap.get("auth_token");
                } else {
                    MoveAnotherActivity(Splash.this, Login.class);
                }
            }
        }, SPLASH_TIME_OUT);
    }


    public void MoveAnotherActivity(Activity mActivity, Class mClass) {
        Intent intent = new Intent(mActivity, mClass);
        startActivity(intent);
        finish();
    }
}

