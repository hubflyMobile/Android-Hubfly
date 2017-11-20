package com.hubfly.ctq.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hubfly.ctq.R;
import com.hubfly.ctq.util.Config;
import com.hubfly.ctq.util.SessionManager;
import com.hubfly.ctq.util.Utility;

/**
 * Created by Admin on 03-07-2017.
 */

public class Login extends Activity {

    String FedAuth_Value = "";
    Boolean LoadUrl = Boolean.valueOf(false);
    String RTFA_Value = "";
    SessionManager mSessionManager;
    Utility mUtility;
    WebView mWebView;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Initialization();

        if (Utility.isInternetConnected(Login.this)) {
            InitializationViews();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Login.this.mWebView.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    /**
     * Session Manager Initialization
     */
    void Initialization() {
        mSessionManager = new SessionManager(Login.this);
        this.mUtility = new Utility(this);
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setMessage("Loading...");
        }
    }


    /**
     * 0
     * Views  Initialization
     */
    void InitializationViews() {
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.clearCache(true);
        CookieManager.getInstance().removeSessionCookie();
        if (!LoadUrl) {
            mWebView.loadUrl(Config.LoginUrl);
        }
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String Cookies = CookieManager.getInstance().getCookie(Config.LoginUrl + "/SitePages/Home.aspx?AjaxDelta=1");
                if (Cookies != null && Cookies.contains("rtFa")) {
                    String[] seperated = Cookies.split(";");
                    for (int i = 0; i <= seperated.length - 1; i++) {
                        if (seperated[i].contains("rtFa")) {
                            Login.this.RTFA_Value = seperated[i].replace("rtFa=", "");
                            Config.Rtfa = Login.this.RTFA_Value;
                        }
                        if (seperated[i].contains("FedAuth")) {
                            Login.this.FedAuth_Value = seperated[i].substring(9);
                            Config.FedAuth = Login.this.FedAuth_Value;
                        }
                        Login.this.LoadUrl = Boolean.valueOf(true);
                    }
                    Login.this.mSessionManager.createLoginSession(Login.this.FedAuth_Value, Login.this.RTFA_Value);
                }
                if (!(Login.this.FedAuth_Value == null || Login.this.FedAuth_Value.equals("") || Login.this.RTFA_Value == null || Login.this.RTFA_Value.equals(""))) {
                    if (!Login.this.LoadUrl.booleanValue()) {
                        Login.this.progressDialog.show();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (Login.this.progressDialog.isShowing()) {
                                Login.this.progressDialog.dismiss();
                                Login.this.progressDialog = null;
                            }
                            Login.this.mSessionManager.SetCurrentTimeStamp(Login.this.mUtility.setCurrentTimeStamp());
                            try {
                                Login.this.startActivity(new Intent(Login.this.getApplicationContext(), DummyActivity.class));
                                Login.this.finish();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    }, 6000);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }


}
