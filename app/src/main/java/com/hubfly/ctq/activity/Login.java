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

    String RTFA_Value = "";
    String FedAuth_Value = "";
    ProgressDialog progressDialog;
    WebView mWebView;
    Boolean LoadUrl = false;
    SessionManager mSessionManager;


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
                mWebView.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    /**
     * Session Manager Initialization
     */
    void Initialization() {
        mSessionManager = new SessionManager(Login.this);

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
                CookieManager cookieManager = CookieManager.getInstance();
                String Cookies = cookieManager.getCookie(Config.LoginUrl + "/SitePages/Home.aspx?AjaxDelta=1");
                if (Cookies != null && Cookies.contains("rtFa")) {
                    String[] seperated = Cookies.split(";");
                    for (int i = 0; i <= seperated.length - 1; i++) {
                        if (seperated[i].contains("rtFa")) {
                            RTFA_Value = seperated[i].replace("rtFa=", "");
                            Config.Rtfa = RTFA_Value;
                        }
                        if (seperated[i].contains("FedAuth")) {
                            FedAuth_Value = seperated[i].substring(9);
                            Config.FedAuth = FedAuth_Value;
                        }
                        LoadUrl = true;
                    }
                    mSessionManager.createLoginSession(FedAuth_Value, RTFA_Value);
                }

                if (FedAuth_Value != null && !FedAuth_Value.equals("") && RTFA_Value != null && !RTFA_Value.equals("")) {

                    if (!LoadUrl) {
                        progressDialog.show();
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            try {
                                Intent mIntent = new Intent(getApplicationContext(), DummyActivity.class);
                                startActivity(mIntent);
                                finish();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                    }, 3000);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

        });
    }


}
