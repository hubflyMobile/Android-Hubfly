package com.hubfly.ctq.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.hubfly.ctq.R;
import com.hubfly.ctq.util.SessionManager;
import com.hubfly.ctq.util.Utility;

import java.util.HashMap;

/**
 * Created by Admin on 10-03-2017.
 */

public class Splash extends AppCompatActivity {

    Utility mUtility;
    SessionManager mSessionManager;
    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mUtility = new Utility(Splash.this);
        mSessionManager = new SessionManager(Splash.this);

        HashMap<String, String> mHashMap = mSessionManager.getDeviceId();
        if (mHashMap.get("device_id") != null && !mHashMap.get("device_id").equals("")) {
            Utility.logging("DEVICE_ID" + mHashMap.get("device_id"));
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
                        Intent mIntent = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(mIntent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
