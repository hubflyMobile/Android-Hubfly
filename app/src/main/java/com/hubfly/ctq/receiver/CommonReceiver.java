package com.hubfly.ctq.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hubfly.ctq.util.Config;

import static com.hubfly.ctq.util.Config.IS_ONLINE;
import static com.hubfly.ctq.util.Config.mBroadcastReceived;


/**
 * Created by Admin on 15-03-2017.
 */

public class CommonReceiver extends BroadcastReceiver {

    Boolean firstConnect = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
            if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                try {
                    ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mInfo = manager.getActiveNetworkInfo();
                    if (mInfo != null && mInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        Config.IS_ONLINE = true;
                    }
                    if (mInfo != null && mInfo.isConnected()) {
                        if (firstConnect) {
                            firstConnect = false;
                            IS_ONLINE = true;
                        }
                    } else {
                        firstConnect = true;
                        IS_ONLINE = false;
                    }
                    if (mBroadcastReceived != null) {
                        mBroadcastReceived.onBroadcastReceived(intent);
                    }
                } catch (Exception e) {
                    firstConnect = true;
                    e.printStackTrace();
                }

            }
        }

    }
}
