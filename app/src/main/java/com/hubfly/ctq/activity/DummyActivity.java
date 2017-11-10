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
import org.json.XML;

import static com.hubfly.ctq.util.Config.mUserDetailsModel;

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
        getUserProfile("1");
    }


    void Initialization() {
        mUtility = new Utility(DummyActivity.this);
        mSessionManager = new SessionManager(DummyActivity.this);
    }


    void getUserProfile(String option) {
        try {
            if (Utility.isInternetConnected(DummyActivity.this)) {
                final JSONObject mJsonObject = mUtility.SendParams(DummyActivity.this, null, null, null);
                HttpApi api = null;
                if (option.equals("1")) {
                    api = new HttpApi(DummyActivity.this, false, new OnResponseCallback() {
                        @Override
                        public void responseCallBack(Activity activity, String responseString) {
                            if (responseString != null && !responseString.equals("")) {
                                mSessionManager.setProfileDetails(responseString);
                                ParseProfileData(responseString);
                                StartService(mJsonObject.toString(), responseString);
                            }
                        }
                    }, Config.Baseurl + Config.GetUserDetails, "POST", mJsonObject);
                } else {
                    api = new HttpApi(DummyActivity.this,  true, mUserProfilePictureCallBack, Config.ImageUrl + Config.UserProfile, "GET", mJsonObject);
                }
                api.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public OnResponseCallback mUserProfilePictureCallBack = new OnResponseCallback() {
        @Override
        public void responseCallBack(Activity activity, String responseString) {
            try {
                if (responseString != null && !responseString.equals("")) {
                    mSessionManager.setProfilePicture(responseString);
                    ParseProfilePicture(responseString);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                Intent mIntent = new Intent(DummyActivity.this, HomePage.class);
                startActivity(mIntent);
                finish();
            }
        }
    };


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
                getUserProfile("2");
            }
        }
    };


    public void ParseProfileData(final String responseString) {
        String department = "", userId = "";
        try {
            if (responseString != null && !responseString.equals("")) {
                UserDetailsModel userDetailsModel = null;
                JSONObject mJsonObject = new JSONObject(responseString);
                userDetailsModel = new UserDetailsModel();
                Config.UserName = mJsonObject.getString("Title");
                Config.Department = mJsonObject.getString("Department");
                userDetailsModel.setTitle(mJsonObject.getString("Title"));
                if (mJsonObject.has("Department")) {
                    userDetailsModel.setDepartment(mJsonObject.getString("Department"));
                    department = mJsonObject.getString("Department");
                }
                if (mJsonObject.has("UserId")) {
                    userDetailsModel.setUserId(mJsonObject.getInt("UserId"));
                    userId = mJsonObject.getString("UserId");
                }
                mUserDetailsModel = userDetailsModel;
                if (department != null && !department.equals("") && userId != null && !userId.equals("")) {
                    getPartDetails(userDetailsModel);
                } else {
                    getUserProfile("1");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void ParseProfilePicture(String responseString) {
        try {
            if (responseString != null && !responseString.equals("")) {
                JSONObject mJsonObject = XML.toJSONObject(responseString);
                if (mJsonObject.has("d:PictureUrl")) {
                    if (mJsonObject.getJSONObject("d:PictureUrl").has("content")) {
                        Config.PictureUrl = mJsonObject.getJSONObject("d:PictureUrl").getString("content");
                    }
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
                StartService(jsonObject.toString(), "");
                api.execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
