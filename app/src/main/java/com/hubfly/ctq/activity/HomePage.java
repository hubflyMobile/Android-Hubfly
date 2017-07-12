package com.hubfly.ctq.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hubfly.ctq.Model.ActivityModel;
import com.hubfly.ctq.Model.CustomerModel;
import com.hubfly.ctq.Model.NavigationModel;
import com.hubfly.ctq.Model.PartModel;
import com.hubfly.ctq.Model.UserDetailsModel;
import com.hubfly.ctq.R;
import com.hubfly.ctq.adapter.NavigationAdapter;
import com.hubfly.ctq.fragement.OpenCtQ;
import com.hubfly.ctq.util.Config;
import com.hubfly.ctq.util.HttpApi;
import com.hubfly.ctq.util.OnResponseCallback;
import com.hubfly.ctq.util.SessionManager;
import com.hubfly.ctq.util.Utility;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.hubfly.ctq.util.Config.mCustomerModel;

/**
 * Created by Admin on 04-07-2017.
 */

public class HomePage extends FragmentActivity {

    //View Elements
    DrawerLayout drawerLayout;
    private LinearLayout mLlDrawerList;
    public FrameLayout commonLayout = null;
    ListView mLvNavigation;
    SessionManager mSessionManager;
    Utility mUtility;
    NavigationAdapter mAdapter;
    NavigationModel[] drawerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Initialization();
        InitializationViews();
        SetAdapter();
        getData();
        getUserProffile("1");
        SetClickEvents();

        if (savedInstanceState == null) {
            displayView(1);
        }
    }

    void getData() {
        drawerItem[0] = new NavigationModel("Open QAC", R.drawable.ic_menu_open);
    }

    void SetAdapter() {
        mAdapter = new NavigationAdapter(this, R.layout.adapter_navigation_item, drawerItem);
        mLvNavigation.setAdapter(mAdapter);
    }

    void Initialization() {
        mUtility = new Utility(HomePage.this);
        mSessionManager = new SessionManager(HomePage.this);
        drawerItem = new NavigationModel[1];
    }

    void InitializationViews() {
        mLlDrawerList = (LinearLayout) findViewById(R.id.testing);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        commonLayout = (FrameLayout) findViewById(R.id.content_frame);
        mLvNavigation = (ListView) findViewById(R.id.lv_navigation);

        View header = getLayoutInflater().inflate(R.layout.app_common_navigation_header, null);
        mLvNavigation.addHeaderView(header);

    }

    void SetClickEvents() {
        mLvNavigation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                displayView(position);
            }
        });
    }

    public void displayView(int position) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 1:
                fragment = new OpenCtQ();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            drawerLayout.closeDrawer(mLlDrawerList);
        }
    }

    @Override
    public void onBackPressed() {
    }

    void getUserProffile(String option) {
        try {
            HashMap<String, String> mHashId = mSessionManager.getUserDetails();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            JSONObject mJsonObject = new JSONObject();
            mJsonObject.put("HostUrl", "https://hubflysoft.sharepoint.com/sites/dev/");
            mJsonObject.put("IsMobileRequest", true);
            mJsonObject.put("Rtfa", mHashId.get("auth_id"));
            mJsonObject.put("FedAuth", mHashId.get("auth_token"));
            Utility.logging(mJsonObject.toString());
            HttpApi api = null;
            if (option.equals("1")) {
                api = new HttpApi(HomePage.this, mUtility.showLoading(HomePage.this), true, UserProfileCallback, Config.Baseurl + Config.GetUserDetails, "POST", "1", nameValuePairs, mJsonObject);
            } else if (option.equals("2")) {
                api = new HttpApi(HomePage.this, mUtility.showLoading(HomePage.this), false, mOnResponseCallbackPart, Config.Baseurl + Config.GetPartDetails, "POST", "1", nameValuePairs, mJsonObject);
            } else if (option.equals("3")) {
                api = new HttpApi(HomePage.this, mUtility.showLoading(HomePage.this), false, mOnResponseCallbackClient, Config.Baseurl + Config.GetClientDetails, "POST", "1", nameValuePairs, mJsonObject);
            } else {
                api = new HttpApi(HomePage.this, mUtility.showLoading(HomePage.this), false, mOnResponseCallbackActivity, Config.Baseurl + Config.GetActivityDetails, "POST", "1", nameValuePairs, mJsonObject);
            }
            api.execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public OnResponseCallback UserProfileCallback = new OnResponseCallback() {
        @Override
        public void responseCallBack(Activity activity, String responseString) {
            ArrayList<UserDetailsModel> mList = new ArrayList<>();
            try {
                if (responseString != null && !responseString.equals("")) {
                    JSONObject mJsonObject = new JSONObject(responseString);
                    UserDetailsModel mUserDetailsModel = new UserDetailsModel();
                    mUserDetailsModel.setTitle(mJsonObject.getString("Title"));
                    mUserDetailsModel.setDepartment(mJsonObject.getString("Department"));
                    mUserDetailsModel.setDesignation(mJsonObject.getString("Designation"));
                    mUserDetailsModel.setLocation(mJsonObject.getString("Location"));
                    mUserDetailsModel.setManagerTitle(mJsonObject.getString("ManagerTitle"));
                    mUserDetailsModel.setManagerLoginName(mJsonObject.getString("ManagerLoginName"));
                    mUserDetailsModel.setManagerId(mJsonObject.getInt("ManagerId"));
                    mUserDetailsModel.setUserId(mJsonObject.getInt("UserId"));
                    mUserDetailsModel.setSiteAdmin(mJsonObject.getBoolean("IsSiteAdmin"));
                    mUserDetailsModel.setAppAdmin(mJsonObject.getBoolean("IsAppAdmin"));
                    mUserDetailsModel.setLoginName(mJsonObject.getString("LoginName"));
                    if (mJsonObject.has("HiredDate") && mJsonObject.getString("HiredDate") != null) {
                        mUserDetailsModel.setHiredDate(mJsonObject.getString("HiredDate"));
                    }
                    Config.mGetUser = mUserDetailsModel;
                    mList.add(mUserDetailsModel);
                } else {
                    mUtility.showToast(HomePage.this, responseString, "0");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                getUserProffile("2");
            }
        }
    };


    public OnResponseCallback mOnResponseCallbackPart = new OnResponseCallback() {
        @Override
        public void responseCallBack(Activity activity, String responseString) {
            ArrayList<PartModel> mList = new ArrayList<>();
            try {
                if (responseString != null && !responseString.equals("")) {
                    JSONArray mJsonArray = new JSONArray(responseString);
                    for (int i = 0; i < mJsonArray.length(); i++) {
                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                        PartModel mPartModel = new PartModel();
                        mPartModel.setID(mJsonObject.getInt("ID"));
                        mPartModel.setCustomerIDHF(mJsonObject.getString("CustomerIDHF"));
                        mPartModel.setPartNameHF(mJsonObject.getString("PartNameHF"));
                        mPartModel.setPartNoHF(mJsonObject.getString("PartNoHF"));
                        mPartModel.setSpecTypeHF(mJsonObject.getString("SpecTypeHF"));
                        Config.mGetPart = mPartModel;
                        mList.add(mPartModel);
                    }
                } else {
                    mUtility.showToast(HomePage.this, responseString, "0");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                getUserProffile("3");
            }
        }
    };
    public OnResponseCallback mOnResponseCallbackClient = new OnResponseCallback() {
        @Override
        public void responseCallBack(Activity activity, String responseString) {
            ArrayList<CustomerModel> mList = new ArrayList<>();
            try {
                if (responseString != null && !responseString.equals("")) {
                    JSONArray mJsonArray = new JSONArray(responseString);
                    for (int i = 0; i < mJsonArray.length(); i++) {
                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                        CustomerModel customerModel = new CustomerModel();
                        customerModel.setID(mJsonObject.getInt("ID"));
                        customerModel.setClientName(mJsonObject.getString("ClientNameHF"));
                        mCustomerModel = customerModel;
                        mList.add(customerModel);
                    }
                } else {
                    mUtility.showToast(HomePage.this, responseString, "0");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                getUserProffile("4");
            }
        }
    };

    public OnResponseCallback mOnResponseCallbackActivity = new OnResponseCallback() {
        @Override
        public void responseCallBack(Activity activity, String responseString) {
            ArrayList<ActivityModel> mList = new ArrayList<>();
            try {
                if (responseString != null && !responseString.equals("")) {
                    JSONArray mJsonArray = new JSONArray(responseString);
                    for (int i = 0; i < mJsonArray.length(); i++) {
                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                        ActivityModel activityModel = new ActivityModel();
                        activityModel.setID(mJsonObject.getInt("ID"));
                        activityModel.setActivityName(mJsonObject.getString("ActivityNameHF"));
                        Config.mActivityModel = activityModel;
                        mList.add(activityModel);
                    }
                } else {
                    mUtility.showToast(HomePage.this, responseString, "0");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
            }
        }
    };


}
