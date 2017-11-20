package com.hubfly.ctq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.hubfly.ctq.Model.UserDetailsModel;
import com.hubfly.ctq.R;
import com.hubfly.ctq.util.CommonService;
import com.hubfly.ctq.util.Config;
import com.hubfly.ctq.util.HttpApi;
import com.hubfly.ctq.util.OnResponseCallback;
import com.hubfly.ctq.util.SessionManager;
import com.hubfly.ctq.util.Utility;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 12-07-2017.
 */

public class DummyActivity extends Activity {
    Utility mUtility;
    SessionManager mSessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        Initialization();
        getUserProfile();
    }


    void Initialization() {
        mUtility = new Utility(DummyActivity.this);
        mSessionManager = new SessionManager(DummyActivity.this);
    }


    void getUserProfile() {
        try {
            if (Utility.isInternetConnected(DummyActivity.this)) {
                final JSONObject mJsonObject = mUtility.SendParams(DummyActivity.this, null, null, null);
                HttpApi api = new HttpApi(DummyActivity.this, false, new OnResponseCallback() {
                    @Override
                    public void responseCallBack(Activity activity, String responseString) {
                        if (responseString != null && !responseString.equals("")) {
                            mSessionManager.setProfileDetails(responseString);
                            ParseProfileData(responseString);
//                            StartService(mJsonObject.toString(), responseString);
                        }
                    }
                }, Config.Baseurl + Config.GetUserDetails, "POST", mJsonObject);
                api.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OnResponseCallback mOnResponseCallbackPart = new OnResponseCallback() {
        @Override
        public void responseCallBack(Activity activity, String responseString) {
            try {
                if (responseString != null && !responseString.equals("")) {
                    if (responseString != null && !responseString.equals("")) {
                        JSONObject mJsonObject = new JSONObject(responseString);
                        if (mJsonObject.has("Clients")) {
                            mUtility.setClientDetails(mJsonObject.getJSONArray("Clients"));
                        }
                        if (mJsonObject.has("Parts")) {
                            mUtility.setPartDetails(mJsonObject.getJSONArray("Parts"));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Intent mIntent = new Intent(DummyActivity.this, HomePage.class);
                mIntent.putExtra("Details", true);
                startActivity(mIntent);
                finish();
            }
        }
    };


    public void ParseProfileData(final String responseString) {
        String department = "", userId = "";
        try {
            if (!responseString.equals("")) {
                JSONObject mJsonObject = new JSONObject(responseString);
                UserDetailsModel userDetailsModel = new UserDetailsModel();
                Config.UserName = mJsonObject.getString("Title");
                userDetailsModel.setTitle(mJsonObject.getString("Title"));
                if (mJsonObject.has("Department")) {
                    userDetailsModel.setDepartment(mJsonObject.getString("Department"));
                    department = mJsonObject.getString("Department");
//                    Config.Department = "melting";
                    Config.Department = department;
                }
                if (mJsonObject.has("UserId")) {
                    userDetailsModel.setUserId(mJsonObject.getInt("UserId"));
                    userId = mJsonObject.getString("UserId");
                }
                if (mJsonObject.has("Email")) {
                    Config.Email = mJsonObject.getString("Email");
                }
                Config.mUserDetailsModel = userDetailsModel;
                if (department == null || department.equals("") || userId == null || userId.equals("")) {
                    getUserProfile();
                } else {
                    getPartDetails(userDetailsModel);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void StartService(String request, String response) {
        Intent mIntent = new Intent(DummyActivity.this, CommonService.class);
        mIntent.putExtra("URL", Config.ErrorLog);
        if (response != null && !response.equals("")) {
            mIntent.putExtra("Response", response);
        }
        mIntent.putExtra("Request", request);
        startService(mIntent);
    }


    public void getPartDetails(UserDetailsModel userDetailsModel) {
        if (Utility.isInternetConnected(DummyActivity.this)) {
            try {
                JSONObject jsonObject = mUtility.SendParams(DummyActivity.this, null, null, null);
                Gson gson = new Gson();
                String UserDetails = gson.toJson(userDetailsModel);
                JSONObject jsonObj = new JSONObject(UserDetails);
                jsonObject.put("UserDetails", jsonObj);
                HttpApi api = new HttpApi(DummyActivity.this, false, mOnResponseCallbackPart, Config.Baseurl + Config.GetPartsDeptWise, "POST", jsonObject);
//                StartService(jsonObject.toString(), "");
                api.execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
