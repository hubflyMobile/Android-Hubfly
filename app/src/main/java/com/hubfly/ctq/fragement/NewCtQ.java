package com.hubfly.ctq.fragement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hubfly.ctq.Model.ActivityModel;
import com.hubfly.ctq.Model.JobWiseModel;
import com.hubfly.ctq.Model.OpenCtqModel;
import com.hubfly.ctq.Model.PartJobCodeModel;
import com.hubfly.ctq.R;
import com.hubfly.ctq.activity.NewCTQ;
import com.hubfly.ctq.adapter.NewQacListAdapter;
import com.hubfly.ctq.adapter.NewQacPartListAdapter;
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

public class NewCtQ extends Fragment {

    RippleView mRvImgNavigation;
    TextView mTxtHeading;
    RippleView mRvCustName, mRvPortNo, mRvCreate, mRvCancel, mRvRefresh;
    Spinner mSpCustName, mSpPartNo;
    EditText mEdtHeatNo, mEdtFurnace;
    LinearLayout mLlFurnace;
    Utility mUtility;
    ArrayList<PartJobCodeModel> mAlPartJobCode;
    NewQacListAdapter mAdapterCustName;
    NewQacPartListAdapter mAdapterPortNo;
    Integer mCustomerId, mPartId;
    String mStrJobCode = "";
    ArrayList<OpenCtqModel> mAlOpenCTQ;
    String CTQstatus, QAPStatus, CustomerName, PartName, JobCode;
    ImageView mImgProfile;
    GlideUtil mGlideUtil;
    TextView mTxtUserName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_new_ctq, container, false);

        Initialization();
        InitializationViews(rootView);
        SetAdapter();
        SetClickEvents();
        return rootView;

    }


    void Initialization() {
        mUtility = new Utility(getActivity());
        mAlPartJobCode = new ArrayList<>();
        mAlOpenCTQ = new ArrayList<>();
        mGlideUtil = new GlideUtil(getActivity());
    }


    void InitializationViews(View rootView) {
        mRvImgNavigation = (RippleView) rootView.findViewById(R.id.rv_back);
        mTxtHeading = (TextView) rootView.findViewById(R.id.txt_title);
        mEdtHeatNo = (EditText) rootView.findViewById(R.id.edt_head_no);
        mSpPartNo = (Spinner) rootView.findViewById(R.id.sp_part_no);
        mSpCustName = (Spinner) rootView.findViewById(R.id.sp_cust_name);
        mRvCreate = (RippleView) rootView.findViewById(R.id.rv_submit);
        mRvCustName = (RippleView) rootView.findViewById(R.id.rv_cust_name);
        mRvPortNo = (RippleView) rootView.findViewById(R.id.rv_part_name);
        mRvCancel = (RippleView) rootView.findViewById(R.id.rv_cancel);
        mRvRefresh = (RippleView) rootView.findViewById(R.id.rv_refresh);

        mLlFurnace = (LinearLayout) rootView.findViewById(R.id.ll_furnace);
        mEdtFurnace = (EditText) rootView.findViewById(R.id.edt_furnace);

        mTxtHeading.setText("New Quality Assurance Check");

        if (Config.Department != null && !Config.Department.equals("") && Config.Department.toLowerCase().equalsIgnoreCase("furnace")) {
            mLlFurnace.setVisibility(View.VISIBLE);
        } else {
            mLlFurnace.setVisibility(View.INVISIBLE);
        }

        mImgProfile = (ImageView) rootView.findViewById(R.id.img_profile);
        mTxtUserName = (TextView) rootView.findViewById(R.id.txt_name);
        mTxtUserName.setText(Config.UserName);


        if (Config.PictureUrl != null && !Config.PictureUrl.equals("")) {
            mGlideUtil.LoadImages(mImgProfile, 1, Config.PictureUrl, true, 1.5f, Config.PictureUrl);
        }

    }

    void SetClickEvents() {

        mRvImgNavigation.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                mUtility.OpenDrawer(getActivity());
            }
        });

        mRvCreate.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (mEdtHeatNo.getText().toString().trim().length() > 0) {
                    mUtility.HideShowKeyboard(getActivity(), mEdtHeatNo, "0");
                    if (Config.Department != null && !Config.Department.equals("") && Config.Department.toLowerCase().equalsIgnoreCase("furnace")) {
                        if (mEdtFurnace.getText().toString().trim().length() > 0) {
                            GenerateNewCtq(true);
                        } else {
                            mUtility.showToast(getActivity(), "Please Furnace", "0");
                        }
                    } else {
                        GenerateNewCtq(false);
                    }
                } else {
                    mUtility.showToast(getActivity(), "Please add heat number", "0");
                }
            }
        });

        mEdtHeatNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean ClickedDone = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (mEdtHeatNo.getText().toString().trim().length() > 0) {
                        mUtility.HideShowKeyboard(getActivity(), mEdtHeatNo, "0");
                        if (Config.Department != null && !Config.Department.equals("") && Config.Department.toLowerCase().equalsIgnoreCase("furnace")) {
                            if (mEdtFurnace.getText().toString().trim().length() > 0) {
                                GenerateNewCtq(true);
                            } else {
                                mUtility.showToast(getActivity(), "Please Furnace", "0");
                            }
                        } else {
                            GenerateNewCtq(false);
                        }
                    } else {
                        mUtility.showToast(getActivity(), "Please add heat number", "0");
                    }
                    ClickedDone = true;
                }
                return ClickedDone;
            }
        });

        mEdtFurnace.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                mUtility.HideShowKeyboard(getActivity(), mEdtHeatNo, "0");
                return false;
            }
        });

        mRvCancel.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                mEdtHeatNo.setText("");
                mSpCustName.setSelection(0);
                mSpPartNo.setSelection(0);
            }
        });


        mRvCustName.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                mSpCustName.performClick();
            }
        });

        mRvPortNo.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                mSpPartNo.performClick();
            }
        });


        mSpCustName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSpCustName.getAdapter() != null && mSpCustName.getAdapter().getCount() != 0) {
                    mCustomerId = Config.mAlCustomer.get(position).getID();
                    getPartDetails(mCustomerId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mSpPartNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPartId = null;
                if (mSpPartNo.getAdapter() != null && mSpPartNo.getAdapter().getCount() != 0) {
                    mPartId = mAlPartJobCode.get(position).getID();
                    mStrJobCode = mAlPartJobCode.get(position).getPartJobCodeHF();
                    Utility.logging("mPartId" + mPartId);
                    Utility.logging("mStrJobCode" + mStrJobCode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mRvRefresh.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                getJobDetails();
            }
        });
    }


    void GenerateNewCtq(Boolean IsFurnace) {
        try {
            if (Utility.isInternetConnected(getActivity())) {
                JobWiseModel mJobWiseModel = new JobWiseModel();
                mJobWiseModel.setPartIDHF(mPartId);
                mJobWiseModel.setCustomerIDHF(mCustomerId);
                mJobWiseModel.setHeatNumberHF(mEdtHeatNo.getText().toString());
                if (IsFurnace) {
                    mJobWiseModel.setFurnaceNameHF(mEdtFurnace.getText().toString());
                }
                Gson mGson = new Gson();
                String mJobData = mGson.toJson(mJobWiseModel);
                JSONObject mJsonObject = mUtility.SendParams(getActivity(), "0", mJobData, null);
                HttpApi api = new HttpApi(getActivity(), true, mGenerateOnResponseCallback, Config.Baseurl + Config.CreateNewQAC, "POST", mJsonObject);
                api.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    OnResponseCallback mGenerateOnResponseCallback = new OnResponseCallback() {
        @Override
        public void responseCallBack(Activity activity, String responseString) {
            Utility.logging("GenerateCallBack" + responseString);
            ArrayList<ActivityModel> mAlQap = new ArrayList<>();
            ArrayList<ActivityModel> mAlCtq = new ArrayList<>();
            try {
                if (responseString == null || responseString.equals("[]\n" + "200")) {
                    mUtility.showToast(getActivity(), "This heat number already exist", "0");
                    mEdtHeatNo.setText("");
                } else {
                    JSONArray mJsonArray = new JSONArray(responseString);
                    for (int i = 0; i < mJsonArray.length(); i++) {
                        JSONObject mJsonObject = mJsonArray.getJSONObject(i);
                        OpenCtqModel mOpenCtqModel = mUtility.SetOpenCtqData(mJsonObject.getInt("PartID"), mJsonObject.getString("HeatNumber"), mJsonObject.getString("CustomerName"), mJsonObject.getString("JobCode"), mJsonObject.getString("PartName"), mJsonObject.getString("CTQStatus"), mJsonObject.getString("QAPStatus"));
                        CustomerName = mJsonObject.getString("CustomerName");
                        JobCode = mJsonObject.getString("JobCode");
                        PartName = mJsonObject.getString("PartName");
                        CTQstatus = mJsonObject.getString("CTQStatus");
                        QAPStatus = mJsonObject.getString("QAPStatus");

                        if (mJsonObject.has("CTQJobs")) {
                            JSONArray mCtqJson = mJsonObject.getJSONArray("CTQJobs");
                            for (int j = 0; j < mCtqJson.length(); j++) {
                                JSONObject mObject = mCtqJson.getJSONObject(j);
                                ActivityModel mActivityModel = mUtility.SetActivityData(mObject.getInt("ID"), mObject.getInt("JobIDHF"), mObject.getInt("PartIDHF"), mObject.getString("HeatNoHF"), mObject.getBoolean("VerifiedHF"), mObject.getString("ActivityNameHF"));
                                if (mObject.has("CTQMinValueHF")) {
                                    mActivityModel.setCTQMinValueHF(mObject.getDouble("CTQMinValueHF"));
                                }
                                if (mObject.has("CTQMaxValueHF")) {
                                    mActivityModel.setCTQMaxValueHF(mObject.getDouble("CTQMaxValueHF"));
                                }
                                if (mObject.has("QACJobIDHF")) {
                                    mActivityModel.setQACJobIDHF(mObject.getInt("QACJobIDHF"));
                                }
                                mAlCtq.add(mActivityModel);
                            }
                            mOpenCtqModel.setmAlCtq(mAlCtq);
                        }

                        if (mJsonObject.has("QAPJobs")) {
                            JSONArray mCtqJson = mJsonObject.getJSONArray("QAPJobs");
                            for (int j = 0; j < mCtqJson.length(); j++) {
                                JSONObject mObject = mCtqJson.getJSONObject(j);
                                ActivityModel mActivityModel = mUtility.SetActivityData(mObject.getInt("ID"), mObject.getInt("JobIDHF"), mObject.getInt("PartIDHF"), mObject.getString("HeatNoHF"), mObject.getBoolean("VerifiedHF"), mObject.getString("ActivityNameHF"));
                                if (mObject.has("CTQMinValueHF")) {
                                    mActivityModel.setCTQMinValueHF(mObject.getDouble("CTQMinValueHF"));
                                }
                                if (mObject.has("CTQMaxValueHF")) {
                                    mActivityModel.setCTQMaxValueHF(mObject.getDouble("CTQMaxValueHF"));
                                }
                                if (mObject.has("QACJobIDHF")) {
                                    mActivityModel.setQACJobIDHF(mObject.getInt("QACJobIDHF"));
                                }
                                mAlQap.add(mActivityModel);
                            }
                            mOpenCtqModel.setmAlCtq(mAlQap);
                        }
                        mAlOpenCTQ.add(mOpenCtqModel);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (mAlOpenCTQ != null && mAlOpenCTQ.size() > 0) {
                    Intent mIntent = new Intent(getActivity(), NewCTQ.class);
                    Gson gson = new Gson();
                    String jsonCtq = gson.toJson(mAlCtq);
                    String jsonQap = gson.toJson(mAlQap);
                    mIntent.putExtra("CTQ", jsonCtq);
                    mIntent.putExtra("QAP", jsonQap);
                    mIntent.putExtra("CUSTOMERNAME", CustomerName);
                    mIntent.putExtra("JOBCODE", JobCode);
                    mIntent.putExtra("PARTNAME", PartName);
                    mIntent.putExtra("CTQSTATUS", CTQstatus);
                    mIntent.putExtra("QAPSTATUS", QAPStatus);
                    startActivity(mIntent);
                }
            }
        }
    };


    void SetAdapter() {
        mAdapterCustName = new NewQacListAdapter(getActivity(), Config.mAlCustomer);
        mSpCustName.setAdapter(mAdapterCustName);

        mAdapterPortNo = new NewQacPartListAdapter(getActivity(), mAlPartJobCode);
        mSpPartNo.setAdapter(mAdapterPortNo);
    }


    void getPartDetails(Integer ID) {
        mAlPartJobCode.clear();
        for (int j = 0; j < Config.mAlParts.size(); j++) {
            if (ID == Integer.parseInt(Config.mAlParts.get(j).getCustomerIDHF()) && Config.mAlParts.get(j).getSpecTypeHF().equals("JS")) {
                PartJobCodeModel mJobCodeModel = new PartJobCodeModel();
                mJobCodeModel.setPartJobCodeHF(Config.mAlParts.get(j).getPartJobCodeHF());
                mJobCodeModel.setID(Config.mAlParts.get(j).getID());
                mAlPartJobCode.add(mJobCodeModel);
            }
        }
        mAdapterPortNo = new NewQacPartListAdapter(getActivity(), mAlPartJobCode);
        mSpPartNo.setAdapter(mAdapterPortNo);
    }


    void getJobDetails() {
        try {
            if (Utility.isInternetConnected(getActivity())) {
                JSONObject mJsonObject = mUtility.SendParams(getActivity(), null, null, null);
                Utility.logging(mJsonObject.toString());
                JSONObject jsonObject = mUtility.SendParams(getActivity(), "0", null, null);
                HttpApi api = new HttpApi(getActivity(), true, new OnResponseCallback() {
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, Config.Baseurl + Config.GetPartsDeptWise, "POST", jsonObject);
                api.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mEdtHeatNo.setText("");
            mSpCustName.setSelection(0);
            mSpPartNo.setSelection(0);

            SetAdapter();
        }
    }

}
