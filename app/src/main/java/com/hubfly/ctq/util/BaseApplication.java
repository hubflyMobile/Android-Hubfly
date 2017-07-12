package com.hubfly.ctq.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.multidex.MultiDexApplication;

import com.hubfly.ctq.R;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by vis-1544 on 1/8/2016.
 */
@ReportsCrashes(
        mode = ReportingInteractionMode.DIALOG,
        formKey = "",
        resDialogText = R.string.app_name,
        resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
        resDialogTitle = R.string.app_name, // optional. default is your application name
        resDialogCommentPrompt = R.string.app_name, // optional. When defined, adds a user text field input with this text resource as a label
        mailTo = "hari.murugesan@hubfly.com")
public class BaseApplication extends MultiDexApplication {


    @Override
    public void onCreate() {
        super.onCreate();

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    ACRA.init(BaseApplication.this);
                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
                    if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        Config.IS_ONLINE = true;
                    }
                    if (netInfo != null && netInfo.isConnected()) {
                        Config.IS_ONLINE = true;
                    } else {
                        Config.IS_ONLINE = false;
                    }
//                    FontStyle.setDefaultFont(getApplicationContext(), "SERIF", "fonts/BebasNeueRegular.ttf");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        }.execute();
    }

}
