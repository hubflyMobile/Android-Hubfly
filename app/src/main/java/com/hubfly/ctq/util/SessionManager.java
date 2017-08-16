package com.hubfly.ctq.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;


public class SessionManager {

    // Shared Preferences
    SharedPreferences mSharedPreferences;

    // Editor for Shared preferences
    Editor mEditor;

    // Context
    Context mContext;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "JS_Auto";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_TOKEN = "auth_token";

    // Email address (make variable public to access from outside)
    public static final String KEY_ID = "auth_id";

    private static SessionManager sInstance = null;

    public static SessionManager getInstance() {
        if (sInstance == null) {
            sInstance = new SessionManager();
        }
        return sInstance;
    }

    public SessionManager() {

    }

    // Constructor
    public SessionManager(Context mContext) {
        this.mContext = mContext;
        mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String authToken, String authId) {
        mEditor.putBoolean(IS_LOGIN, true);
        mEditor.putString(KEY_TOKEN, authToken);
        mEditor.putString(KEY_ID, authId);
        mEditor.commit();
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_TOKEN, mSharedPreferences.getString(KEY_TOKEN, null));
        user.put(KEY_ID, mSharedPreferences.getString(KEY_ID, null));
        return user;
    }

    public HashMap<String, String> getDeviceId() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("device_id", mSharedPreferences.getString("device_id", null));
        return map;
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return mSharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void ClearUser() {
        // Clearing all data from Shared Preferences
        mEditor.clear();
        mEditor.commit();
    }

    public void ClearSharepointId() {
        // Clearing all data from Shared Preferences
        mEditor.remove("Digest");
        mEditor.remove("share_point_id");
        mEditor.remove("auth_id");
        mEditor.remove("auth_token");
        mEditor.commit();
    }

    public void setProfileDetails(String response) {
        mEditor.putString("response", response);
        mEditor.commit();
    }

    public HashMap<String, String> getProfileDetails() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("response", mSharedPreferences.getString("response", null));
        return map;
    }

    public void setProfilePicture(String response) {
        mEditor.putString("profile_picture_response", response);
        mEditor.commit();
    }

    public HashMap<String, String> getProfilePicture() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("profile_picture_response", mSharedPreferences.getString("profile_picture_response", null));
        return map;
    }

}