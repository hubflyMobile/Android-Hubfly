package com.hubfly.ctq.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import static com.hubfly.ctq.util.Config.IS_ONLINE;

/**
 * Created by Admin on 15-03-2017.
 */

public class NetworkService extends Service {

    private boolean mActive;
    private Handler mTHandler;
    private Thread mThread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public NetworkService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            Bundle mBundle = intent.getExtras();
            if (mBundle != null && mBundle.containsKey("network_changed")) {
                Initialize();
                start();
                return Service.START_STICKY;
            }
        }
        return Service.START_NOT_STICKY;
    }

    public void Initialize() {
        mActive = false;
        mThread = null;
    }

    public void start() {
        if (!mActive) {
            mActive = true;
            if (mThread == null || !mThread.isAlive()) {
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        mTHandler = new Handler();
                        ReConnect();
                        Looper.loop();
                    }

                });
                mThread.start();
            }
        }
    }
    public void ReConnect() {
        try {
            Thread.sleep(500000);
            if (!IS_ONLINE) {
                ReConnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
