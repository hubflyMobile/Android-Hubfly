package com.hubfly.ctq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.hubfly.ctq.R;
import com.hubfly.ctq.util.Config;
import com.hubfly.ctq.util.SessionManager;

import java.util.HashMap;

/**
 * Created by Admin on 10-03-2017.
 */

public class Splash extends AppCompatActivity {

    SessionManager mSessionManager;
    private static int SPLASH_TIME_OUT = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSessionManager = new SessionManager(Splash.this);

        HashMap<String, String> mHashMap = mSessionManager.getDeviceId();
        if (mHashMap.get("device_id") != null && !mHashMap.get("device_id").equals("")) {
            MoveToNextScreen();
        }else {
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


    public void MoveAnotherActivity(Activity mActivity,Class mClass){
        Intent intent = new Intent(mActivity, mClass);
        startActivity(intent);
        finish();
    }
}
