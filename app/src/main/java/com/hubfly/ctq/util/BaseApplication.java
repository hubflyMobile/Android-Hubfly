package com.hubfly.ctq.util;

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
        resDialogText = R.string.error,
        resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
        resDialogTitle = R.string.app_name, // optional. default is your application name
        resDialogCommentPrompt = R.string.comment, // optional. When defined, adds a user text field input with this text resource as a label
        mailTo = "hari.murugesan@hubfly.com")
public class BaseApplication extends MultiDexApplication {

    SessionManager mSessionManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mSessionManager = new SessionManager(getApplicationContext());

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    ACRA.init(BaseApplication.this);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return "";
            }
        }.execute();
    }

}
