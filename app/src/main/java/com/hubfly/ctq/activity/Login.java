package com.hubfly.ctq.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hubfly.ctq.R;
import com.hubfly.ctq.util.Config;
import com.hubfly.ctq.util.SessionManager;

/**
 * Created by Admin on 03-07-2017.
 */

public class Login extends Activity {

    SessionManager mSessionManager;
    String RTFA_Value = "";
    String FedAuth_Value = "";
    boolean RTFA = false;
    boolean FedAuth = false;
    ProgressDialog progressDialog;
    WebView mWebView;
    Boolean LoadUrl = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Initialization();
        InitializationViews();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mWebView.setVisibility(View.VISIBLE);
            }
        },3000);
    }

    /**
     * Session Manager Initialization
     */
    void Initialization() {
        mSessionManager = new SessionManager(Login.this);
    }


    /**0
     * Views  Initialization
     */
    void InitializationViews() {
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.clearCache(true);
        CookieManager.getInstance().removeSessionCookie();
        if (!LoadUrl) {
            mWebView.loadUrl(Config.Baseurl);
        }
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                CookieManager cookieManager = CookieManager.getInstance();
                String Cookies = cookieManager.getCookie(Config.Baseurl + "/SitePages/Home.aspx?AjaxDelta=1");
                if (Cookies != null && Cookies.contains("rtFa")) {
                    String[] seperated = Cookies.split(";");

                    for (int i = 0; i <= seperated.length - 1; i++) {
                        if (seperated[i].contains("rtFa") && RTFA != true) {
                            SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(Login.this);
                            SharedPreferences.Editor editor = shared.edit();
                            RTFA_Value = seperated[i].replace("rtFa=","");
                            editor.putString("rtFa", RTFA_Value);
                            editor.commit();
                            RTFA = true;
                        }
                        if (seperated[i].contains("FedAuth") && FedAuth != true) {
                            SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(Login.this);
                            SharedPreferences.Editor editor = shared.edit();
                            FedAuth_Value = seperated[i].substring(9);
                            editor.putString("FedAuth", FedAuth_Value);
                            editor.commit();
                            FedAuth = true;
                        }
                        LoadUrl = true;
                    }
                    mSessionManager.createLoginSession(FedAuth_Value, RTFA_Value);
                }

                if (FedAuth_Value != null && !FedAuth_Value.equals("") && RTFA_Value != null && !RTFA_Value.equals("")) {

                    if (progressDialog == null) {
                        progressDialog = new ProgressDialog(Login.this);
                        progressDialog.setMessage("Loading...");
                        if (!LoadUrl) {
                            progressDialog.show();
                        }
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                            try {
                                if (mSessionManager.isLoggedIn()) {
                                    Intent mIntent = new Intent(getApplicationContext(), HomePage.class);
                                    startActivity(mIntent);
                                    finish();
                                }
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
