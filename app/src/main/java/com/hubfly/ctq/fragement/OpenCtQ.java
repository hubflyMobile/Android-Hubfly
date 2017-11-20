package com.hubfly.ctq.fragement;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hubfly.ctq.Model.ActivityModel;
import com.hubfly.ctq.Model.ImageModel;
import com.hubfly.ctq.Model.OpenCtqModel;
import com.hubfly.ctq.R;
import com.hubfly.ctq.adapter.CtoListAdapter;
import com.hubfly.ctq.util.Config;
import com.hubfly.ctq.util.GlideUtil;
import com.hubfly.ctq.util.HttpApi;
import com.hubfly.ctq.util.OnResponseCallback;
import com.hubfly.ctq.util.RippleView;
import com.hubfly.ctq.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 04-07-2017.
 */

public class OpenCtQ extends Fragment {

    Boolean isHeatNo = Boolean.valueOf(false);
    CtoListAdapter mAdapter;
    ArrayList<OpenCtqModel> mAlOpenCtq;
    EditText mEdtSearch;
    GlideUtil mGlideUtil;
    ImageView mImgClear;
    ImageView mImgProfile;
    LinearLayout mLlHeatNoOpen;
    LinearLayout mLlNoData;
    LinearLayout mLlOpenCtq;
    LinearLayout mLlRootList;
    RippleView mRvImgNavigation;
    RecyclerView mRvOpenCTQ;
    TextView mTxtHeading;
    TextView mTxtHeatNoOpen;
    TextView mTxtUserName;
    Utility mUtility;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_open_ctq, container, false);

        Initializaion();
        InitiizationViews(rootView);
        SetClickEvents();
        setAdapter();
        getJobWiseDetails();
        return rootView;
    }

    void Initializaion() {
        mAlOpenCtq = new ArrayList<>();
        mUtility = new Utility(getActivity());
        mGlideUtil = new GlideUtil(getActivity());
    }

    void InitiizationViews(View rootView) {
        this.mRvImgNavigation = (RippleView) rootView.findViewById(R.id.rv_back);
        this.mTxtHeading = (TextView) rootView.findViewById(R.id.txt_title);
        this.mLlOpenCtq = (LinearLayout) rootView.findViewById(R.id.ll_open_ctq);
        this.mLlNoData = (LinearLayout) rootView.findViewById(R.id.ll_no_data);
        this.mLlRootList = (LinearLayout) rootView.findViewById(R.id.ll_root_list);
        this.mEdtSearch = (EditText) rootView.findViewById(R.id.edt_search);
        this.mImgClear = (ImageView) rootView.findViewById(R.id.img_clear);
        this.mRvOpenCTQ = this.mUtility.CustomRecycleView(getActivity(), this.mLlOpenCtq);
        this.mImgProfile = (ImageView) rootView.findViewById(R.id.img_profile);
        this.mTxtUserName = (TextView) rootView.findViewById(R.id.txt_name);
        this.mLlHeatNoOpen = (LinearLayout) rootView.findViewById(R.id.ll_heat_no_open);
        this.mTxtHeatNoOpen = (TextView) rootView.findViewById(R.id.txt_heat_no_open);
        this.mTxtHeading.setText("Open Quality Assurance Check");
        this.mTxtUserName.setText(Config.UserName);
        if (!(Config.Email == null || Config.Email.equals(""))) {
            String Image = Config.UserProfilePhoto + Config.Email;
            this.mGlideUtil.LoadImages(this.mImgProfile, Integer.valueOf(1), Image, true, Float.valueOf(1.75f), Image);
        }
        this.mLlRootList.setVisibility(View.GONE);
        this.mUtility.HideShowKeyboard(getActivity(), this.mEdtSearch, "0");
        HideShowDepartMent();
    }

    void HideShowDepartMent() {
        if (Config.Department != null && !Config.Department.equals("")) {
            if (Config.Department.toLowerCase().equals(Config.CORE_SHOP)) {
                this.isHeatNo = Boolean.valueOf(false);
                this.mLlHeatNoOpen.setVisibility(View.GONE);
                return;
            }
            if (Config.Department.toLowerCase().equals(Config.CORE_SHOP_CS1) || Config.Department.toLowerCase().equals(Config.CORE_SHOP_CS2) || Config.Department.toLowerCase().equals(Config.PATTERN_SHOP) || Config.Department.toLowerCase().equals(Config.CORE_SHOP)) {
                this.mTxtHeatNoOpen.setText("Process Type");
            } else {
                this.mTxtHeatNoOpen.setText("Heat Number");
            }
            this.isHeatNo = Boolean.valueOf(true);
            this.mLlHeatNoOpen.setVisibility(View.VISIBLE);
        }
    }


    void SetClickEvents() {
        mRvImgNavigation.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                mUtility.OpenDrawer(getActivity());
            }
        });

        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = mEdtSearch.getText().toString().trim();
                if (text != null && !text.equals("")) {
                    mAdapter.getFilter().filter(text);
                } else {
                    mAdapter.getFilter().filter(text);
                    mUtility.HideShowKeyboard(getActivity(), mEdtSearch, "0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mImgClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdtSearch.setText("");
                mAdapter.getFilter().filter("");
            }
        });
    }

    void setAdapter() {
        mAdapter = new CtoListAdapter(getActivity(), mAlOpenCtq, mLlNoData, mLlOpenCtq, true);
        mRvOpenCTQ.setAdapter(mAdapter);
    }


    void getJobWiseDetails() {
        try {
            if (Utility.isInternetConnected(getActivity())) {
                JSONObject mJsonObject = mUtility.SendParams(getActivity(), "0", null, null);
                HttpApi api = new HttpApi(getActivity(), true, mOnResponseCallbackActivity, Config.Baseurl + Config.GetOpenQACToday, "POST", mJsonObject);
                api.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public OnResponseCallback mOnResponseCallbackActivity = new OnResponseCallback() {
        @Override
        public void responseCallBack(Activity activity, String responseString) {
            Utility.logging(responseString);
            if (mAlOpenCtq.size() > 0) {
                mAlOpenCtq.clear();
            }
            try {
                if (responseString != null && !responseString.equals("")) {
                    JSONArray mJsonArray = new JSONArray(responseString);
                    for (int i = 0; i < mJsonArray.length(); i++) {
                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                        OpenCtqModel mOpenCtqModel = mUtility.SetOpenCtqData(mJsonObject.getInt("PartID"), mJsonObject.getString("HeatNumber"), mJsonObject.getString("CustomerName"), mJsonObject.getString("JobCode"), mJsonObject.getString("PartName"), mJsonObject.getString("CTQStatus"), mJsonObject.getString("QAPStatus"));
                        if (mJsonObject.has("ProcessTypeHF") && !mJsonObject.getString("ProcessTypeHF").equals("null")) {
                            mOpenCtqModel.setProcessTypeHF(mJsonObject.getInt("ProcessTypeHF"));
                        }
                        if (mJsonObject.has("GroupCodeHF") && !mJsonObject.getString("GroupCodeHF").equals("null")) {
                            mOpenCtqModel.setGroupCodeHF(mJsonObject.getString("ProcessTypeHF"));
                        }
                        ArrayList<ActivityModel> mAlCtq = new ArrayList<>();
                        if (mJsonObject.has("CTQJobs")) {
                            JSONArray mCtqJson = mJsonObject.getJSONArray("CTQJobs");
                            for (int j = 0; j < mCtqJson.length(); j++) {
                                JSONObject mObject = mCtqJson.getJSONObject(j);
                                ActivityModel mActivityModel = mUtility.SetActivityData(mObject.getInt("ID"), mObject.getInt("JobIDHF"), mObject.getInt("PartIDHF"), mObject.getString("HeatNoHF"), mObject.getBoolean("VerifiedHF"), mObject.getString("ActivityNameHF"));
                                mActivityModel.setCTQMinValueHF(mObject.getDouble("CTQMinValueHF"));
                                mActivityModel.setCTQMaxValueHF(mObject.getDouble("CTQMaxValueHF"));
                                if (mObject.getString("CTQValueHF") != null) {
                                    mActivityModel.setCTQValueHF(mObject.getString("CTQValueHF"));
                                }
                                if (mObject.getString("RemarksHF") != null) {
                                    mActivityModel.setRemarksHF(mObject.getString("RemarksHF"));
                                }
                                if (mObject.has("QACJobIDHF")) {
                                    mActivityModel.setQACJobIDHF(mObject.getInt("QACJobIDHF"));
                                }
                                mAlCtq.add(mActivityModel);
                            }
                            mOpenCtqModel.setmAlCtq(mAlCtq);
                        }

                        ArrayList<ActivityModel> mAlQap = new ArrayList<>();
                        if (mJsonObject.has("QAPJobs")) {
                            JSONArray mQapJson = mJsonObject.getJSONArray("QAPJobs");
                            for (int j = 0; j < mQapJson.length(); j++) {
                                JSONObject mObject = mQapJson.getJSONObject(j);
                                ActivityModel mActivityModel = mUtility.SetActivityData(mObject.getInt("ID"), mObject.getInt("JobIDHF"), mObject.getInt("PartIDHF"), mObject.getString("HeatNoHF"), mObject.getBoolean("VerifiedHF"), mObject.getString("ActivityNameHF"));
                                if (mObject.has("RemarksHF") && mObject.getString("RemarksHF") != null) {
                                    mActivityModel.setRemarksHF(mObject.getString("RemarksHF"));
                                }
                                if (mObject.has("CTQMinValueHF")) {
                                    mActivityModel.setCTQMinValueHF(mObject.getDouble("CTQMinValueHF"));
                                }
                                if (mObject.has("CTQMaxValueHF")) {
                                    mActivityModel.setCTQMaxValueHF(mObject.getDouble("CTQMaxValueHF"));
                                }
                                if (mObject.has("QACJobIDHF")) {
                                    mActivityModel.setQACJobIDHF(mObject.getInt("QACJobIDHF"));
                                }
                                ArrayList<ImageModel> mImgAl = new ArrayList<>();
                                if (mObject.has("AttachmentFiles")) {
                                    JSONArray jsonArray = mObject.getJSONArray("AttachmentFiles");
                                    for (int k = 0; k < jsonArray.length(); k++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(k);
                                        ImageModel mImageModel = new ImageModel();
                                        mImageModel.setBaseImage(Config.ImageUrl + jsonObject.getString("ServerRelativeUrl"));
                                        mImageModel.setFileName(jsonObject.getString("FileName"));
                                        mImgAl.add(mImageModel);
                                    }
                                    mActivityModel.setmAlImage(mImgAl);
                                }
                                mAlQap.add(mActivityModel);
                            }
                            mOpenCtqModel.setmAlQap(mAlQap);
                        }
                        mAlOpenCtq.add(mOpenCtqModel);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                mAdapter.notifyDataSetChanged();
                mLlRootList.setVisibility(View.VISIBLE);
                if (mAlOpenCtq != null && mAlOpenCtq.size() > 0) {
                    mLlOpenCtq.setVisibility(View.VISIBLE);
                    mLlNoData.setVisibility(View.GONE);
                } else {
                    mLlOpenCtq.setVisibility(View.GONE);
                    mLlNoData.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }
}
