package com.hubfly.ctq.fragement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hubfly.ctq.Model.ActivityModel;
import com.hubfly.ctq.Model.CustomerModel;
import com.hubfly.ctq.Model.JobWiseModel;
import com.hubfly.ctq.Model.OpenCtqModel;
import com.hubfly.ctq.Model.PartJobCodeModel;
import com.hubfly.ctq.Model.PartModel;
import com.hubfly.ctq.Model.ProcessType;
import com.hubfly.ctq.R;
import com.hubfly.ctq.activity.NewCTQ;
import com.hubfly.ctq.adapter.CommonAdapter;
import com.hubfly.ctq.adapter.NewQacListAdapter;
import com.hubfly.ctq.adapter.NewQacPartListAdapter;
import com.hubfly.ctq.util.Config;
import com.hubfly.ctq.util.GlideUtil;
import com.hubfly.ctq.util.HttpApi;
import com.hubfly.ctq.util.OnResponseCallback;
import com.hubfly.ctq.util.RippleView;
import com.hubfly.ctq.util.Utility;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import static com.hubfly.ctq.util.Config.mAlCustomer;
import static com.hubfly.ctq.util.Config.mAlParts;

/**
 * Created by Admin on 04-07-2017.
 */

public class NewCtQ extends Fragment {

    String CTQstatus, CustomerName, JobCode, PartName, QAPStatus;
    Boolean isClicked = false, isCoreShooter = false, isFurnace = false, isGrpCode = true, isProcessType = false, isStation = false;
    NewQacListAdapter mAdapterCustName;
    NewQacPartListAdapter mAdapterPortNo;
    ArrayList<String> mAlCommon;
    ArrayList<String> mAlFurnace;
    ArrayList<String> mAlGroupCode;
    ArrayList<String> mAlMachineNo;
    ArrayList<OpenCtqModel> mAlOpenCTQ;
    ArrayList<PartJobCodeModel> mAlPartJobCode;
    ArrayList<String> mAlShift;
    Integer mCustomerId, mPartId;
    EditText mEdtHeatNo;
    GlideUtil mGlideUtil;
    ImageView mImgProfile;
    LinearLayout mLlDupPType, mLlDupStations, mLlCustName, mLlDupCust, mLlFirst, mLlSecond, mLlThird, mLlFourth, mLlCoreShooter, mLlFurnace, mLlGroupCode, mLlHeatFurnace, mLlHeatNo, mLlProcessType, mLlProcessTypeDept, mLlShiftGroup, mLlStations;
    RippleView mRvCancel, mRvDupCust, mRvDupPType, mRvDupStations, mRvCoreShooter, mRvCreate, mRvCustName, mRvFurnace, mRvGrpCode, mRvImgNavigation, mRvPortNo, mRvProcessType, mRvRefresh, mRvShift;
    Spinner mSpCoreShooter, mSpCustName, mSpDupCust, mSpFurnace, mSpGrpCode, mSpPartNo, mSpProcessType, mSpShift, mSpDupPType, mSpDupStations;
    String mStrJobCode = "";
    TextView mTxtDate, mTxtHeading, mTxtUserName, mTxtFirst, mTxtSecond, mTxtThird, mTxtFourth, mTxtDupStations;
    CheckBox mCbFirst, mCbSecond, mCbThird, mCbFourth;
    Utility mUtility;
    Boolean isDupPType = false;


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
        mAlPartJobCode = new ArrayList();
        mAlOpenCTQ = new ArrayList();
        mAlShift = new ArrayList();
        mAlGroupCode = new ArrayList();
        mAlFurnace = new ArrayList();
        mAlCommon = new ArrayList();
        mAlMachineNo = new ArrayList();
        mGlideUtil = new GlideUtil(getActivity());

        mAlShift.add("Please Select Shift");
        mAlShift.add("I");
        mAlShift.add("II");
        mAlShift.add("III");

        mAlGroupCode.add("Please Select Group Code");
        mAlGroupCode.add("A");
        mAlGroupCode.add("B");
        mAlGroupCode.add("C");

        mAlFurnace.add("Please Choose Furnace");
        mAlFurnace.add("A");
        mAlFurnace.add("B");
        mAlFurnace.add("C");
        mAlFurnace.add("D");
        mAlFurnace.add("E");
        mAlFurnace.add("F");
        mAlFurnace.add("G");

        mAlMachineNo.add("Please Choose Machine No");
        mAlMachineNo.add("1");
        mAlMachineNo.add("2");
        mAlMachineNo.add("3");
        mAlMachineNo.add("4");
        mAlMachineNo.add("5");
        mAlMachineNo.add("6");
        mAlMachineNo.add("7");
        mAlMachineNo.add("8");
        mAlMachineNo.add("9");

        if (Config.Department.toLowerCase().equals(Config.CORE_SHOP_CS1)) {
            this.mAlCommon.clear();
            this.mAlCommon.add("Select Core Shop");
            this.mAlCommon.add("Core Sand Mixing");
            this.mAlCommon.add("Core Shooter Parameter");
        } else if (Config.Department.toLowerCase().equals(Config.CORE_SHOP_CS2)) {
            this.mAlCommon.clear();
            this.mAlCommon.add("Select Core Shop");
            this.mAlCommon.add("Coating Mixing");
            this.mAlCommon.add("Core Heating");
            this.mAlCommon.add("core dressing & inspection");
        } else if (Config.Department.toLowerCase().equals(Config.PATTERN_SHOP)) {
            this.mAlCommon.clear();
            this.mAlCommon.add("Select Pattern Shop");
            this.mAlCommon.add("Core Box Inspection");
            this.mAlCommon.add("Pattern Inspection");
        }
    }


    void InitializationViews(View rootView) {

        mRvImgNavigation = (RippleView) rootView.findViewById(R.id.rv_back);
        mTxtHeading = (TextView) rootView.findViewById(R.id.txt_title);
        mEdtHeatNo = (EditText) rootView.findViewById(R.id.edt_head_no);
        mSpPartNo = (Spinner) rootView.findViewById(R.id.sp_part_no);
        mSpCustName = (Spinner) rootView.findViewById(R.id.sp_cust_name);
        mSpShift = (Spinner) rootView.findViewById(R.id.sp_shift);
        mSpGrpCode = (Spinner) rootView.findViewById(R.id.sp_group_code);
        mSpProcessType = (Spinner) rootView.findViewById(R.id.sp_process_type);
        mSpCoreShooter = (Spinner) rootView.findViewById(R.id.sp_core_shooter);
        mSpFurnace = (Spinner) rootView.findViewById(R.id.sp_furnace);
        mRvCreate = (RippleView) rootView.findViewById(R.id.rv_submit);
        mRvCustName = (RippleView) rootView.findViewById(R.id.rv_cust_name);
        mRvPortNo = (RippleView) rootView.findViewById(R.id.rv_part_name);
        mRvCancel = (RippleView) rootView.findViewById(R.id.rv_cancel);
        mRvRefresh = (RippleView) rootView.findViewById(R.id.rv_refresh);
        mRvShift = (RippleView) rootView.findViewById(R.id.rv_shift);
        mRvGrpCode = (RippleView) rootView.findViewById(R.id.rv_group_code);
        mRvProcessType = (RippleView) rootView.findViewById(R.id.rv_process_type);
        mRvCoreShooter = (RippleView) rootView.findViewById(R.id.rv_core_shooter);
        mRvFurnace = (RippleView) rootView.findViewById(R.id.rv_furnace);
        mLlFurnace = (LinearLayout) rootView.findViewById(R.id.ll_furnace_txt);
        mLlShiftGroup = (LinearLayout) rootView.findViewById(R.id.ll_shift_group);
        mLlHeatNo = (LinearLayout) rootView.findViewById(R.id.ll_heat_no);
        mLlGroupCode = (LinearLayout) rootView.findViewById(R.id.ll_group_code);
        mLlHeatFurnace = (LinearLayout) rootView.findViewById(R.id.ll_heat_furnace);
        mLlStations = (LinearLayout) rootView.findViewById(R.id.ll_stations);
        mLlProcessTypeDept = (LinearLayout) rootView.findViewById(R.id.ll_process_type_dept);
        mLlProcessType = (LinearLayout) rootView.findViewById(R.id.ll_process_type);
        mLlCoreShooter = (LinearLayout) rootView.findViewById(R.id.ll_core_shooter);
        mImgProfile = (ImageView) rootView.findViewById(R.id.img_profile);
        mTxtUserName = (TextView) rootView.findViewById(R.id.txt_name);
        mTxtDate = (TextView) rootView.findViewById(R.id.txt_date_time);

        mLlFirst = (LinearLayout) rootView.findViewById(R.id.ll_first);
        mLlSecond = (LinearLayout) rootView.findViewById(R.id.ll_second);
        mLlThird = (LinearLayout) rootView.findViewById(R.id.ll_third);
        mLlFourth = (LinearLayout) rootView.findViewById(R.id.ll_four);

        mLlDupPType = (LinearLayout) rootView.findViewById(R.id.ll_dup_process_type);
        mSpDupPType = (Spinner) rootView.findViewById(R.id.sp_dup_process_type);
        mRvDupPType = (RippleView) rootView.findViewById(R.id.rv_dup_process_type);

        mLlCustName = (LinearLayout) rootView.findViewById(R.id.ll_cust_name);

        mLlDupStations = (LinearLayout) rootView.findViewById(R.id.ll_dup_station);
        mSpDupStations = (Spinner) rootView.findViewById(R.id.sp_dup_station);
        mRvDupStations = (RippleView) rootView.findViewById(R.id.rv_dup_station);
        mTxtDupStations = (TextView) rootView.findViewById(R.id.txt_dup_stations);

        mLlDupCust = (LinearLayout) rootView.findViewById(R.id.ll_dup_customer);
        mSpDupCust = (Spinner) rootView.findViewById(R.id.sp_dup_customer);
        mRvDupCust = (RippleView) rootView.findViewById(R.id.rv_dup_customer);

        mTxtFirst = (TextView) rootView.findViewById(R.id.txt_first);
        mTxtSecond = (TextView) rootView.findViewById(R.id.txt_second);
        mTxtThird = (TextView) rootView.findViewById(R.id.txt_third);
        mTxtFourth = (TextView) rootView.findViewById(R.id.txt_four);

        mCbFirst = (CheckBox) rootView.findViewById(R.id.cb_first);
        mCbSecond = (CheckBox) rootView.findViewById(R.id.cb_second);
        mCbThird = (CheckBox) rootView.findViewById(R.id.cb_third);
        mCbFourth = (CheckBox) rootView.findViewById(R.id.cb_four);

        mTxtDate.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        mTxtDate.setText(this.mUtility.GetCurrentDate());
        mTxtHeading.setText("New Quality Assurance Check");
        mTxtUserName.setText(Config.UserName);

        if (!(Config.Email == null || Config.Email.equals(""))) {
            String Image = Config.UserProfilePhoto + Config.Email;
            this.mGlideUtil.LoadImages(this.mImgProfile, Integer.valueOf(1), Image, true, Float.valueOf(1.75f), Image);
        }

        mLlStations.setVisibility(View.GONE);
        mLlFurnace.setVisibility(View.GONE);
        mLlCoreShooter.setVisibility(View.GONE);
        HideShowDepartMent();
    }


    void HideShowDepartMent() {
        if (Config.Department != null && !Config.Department.equals("")) {
            if (Config.Department.toLowerCase().equals(Config.CORE_SHOP)) {
                mLlShiftGroup.setVisibility(View.VISIBLE);
                mLlGroupCode.setVisibility(View.VISIBLE);
                mLlDupPType.setVisibility(View.GONE);
            } else if (Config.Department.toLowerCase().equals(Config.DISA_MOLDING)) {
                mLlHeatFurnace.setVisibility(View.VISIBLE);
                mLlHeatNo.setVisibility(View.VISIBLE);
                mLlShiftGroup.setVisibility(View.VISIBLE);
                mLlGroupCode.setVisibility(View.VISIBLE);
                mLlDupPType.setVisibility(View.GONE);
            } else if (Config.Department.toLowerCase().equals(Config.CORE_SHOP_CS1) || Config.Department.toLowerCase().equals(Config.CORE_SHOP_CS2)) {
                mLlShiftGroup.setVisibility(View.VISIBLE);
                mLlGroupCode.setVisibility(View.VISIBLE);
                mLlProcessTypeDept.setVisibility(View.VISIBLE);
                mLlProcessType.setVisibility(View.VISIBLE);
                mLlHeatFurnace.setVisibility(View.GONE);
                mLlDupPType.setVisibility(View.GONE);

                if (Config.Department.toLowerCase().equals(Config.CORE_SHOP_CS2)) {
                    mLlCustName.setVisibility(View.GONE);
                    mLlDupStations.setVisibility(View.VISIBLE);
                    mTxtDupStations.setText("Customer Name");
                } else if (Config.Department.toLowerCase().equals(Config.CORE_SHOP_CS1)) {
                    mLlCustName.setVisibility(View.VISIBLE);
                    mLlDupStations.setVisibility(View.GONE);
                }


            } else if (Config.Department.toLowerCase().equals(Config.PATTERN_SHOP)) {
                mLlShiftGroup.setVisibility(View.VISIBLE);
                mLlGroupCode.setVisibility(View.GONE);
                mLlDupPType.setVisibility(View.VISIBLE);
                mLlProcessType.setVisibility(View.GONE);
                mLlProcessTypeDept.setVisibility(View.VISIBLE);
                mLlDupStations.setVisibility(View.GONE);
                isDupPType = true;
                isGrpCode = false;
            } else if (Config.Department.toLowerCase().equals(Config.MELTING)) {
                mLlFurnace.setVisibility(View.VISIBLE);
                mLlShiftGroup.setVisibility(View.VISIBLE);
                mLlHeatFurnace.setVisibility(View.VISIBLE);
                mLlHeatNo.setVisibility(View.VISIBLE);
                mLlGroupCode.setVisibility(View.VISIBLE);
                mLlDupPType.setVisibility(View.GONE);
                isFurnace = true;
            } else {
                mLlHeatFurnace.setVisibility(View.VISIBLE);
                mLlHeatNo.setVisibility(View.VISIBLE);
                mLlShiftGroup.setVisibility(View.VISIBLE);
                mLlGroupCode.setVisibility(View.VISIBLE);
                mLlDupPType.setVisibility(View.GONE);
            }
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
                if (Config.Department.toLowerCase().equalsIgnoreCase(Config.CORE_SHOP)) {
                    if (SpinnerValidation(getActivity(), mSpShift, "shift").booleanValue() && SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpGrpCode, "group").booleanValue()) {
                        NewCtQ.this.GenerateNewCtq();
                    }
                } else if (Config.Department.toLowerCase().equalsIgnoreCase(Config.DISA_MOLDING)) {
                    if (!NewCtQ.this.SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpShift, "shift").booleanValue() || !NewCtQ.this.SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpGrpCode, "group").booleanValue()) {
                        return;
                    }
                    if (NewCtQ.this.mEdtHeatNo.getText().toString() == null || NewCtQ.this.mEdtHeatNo.getText().toString().length() <= 0) {
                        NewCtQ.this.mUtility.showToast(NewCtQ.this.getActivity(), "Please Enter Heat No", "0");
                    } else {
                        NewCtQ.this.GenerateNewCtq();
                    }
                } else if (Config.Department.toLowerCase().equalsIgnoreCase(Config.CORE_SHOP_CS1)) {
                    if (!NewCtQ.this.SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpShift, "shift").booleanValue() || !NewCtQ.this.SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpGrpCode, "group").booleanValue() || !NewCtQ.this.SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpProcessType, "process").booleanValue()) {
                        return;
                    }
                    if (!NewCtQ.this.isClicked.booleanValue()) {
                        NewCtQ.this.GenerateNewCtq();
                    } else if (NewCtQ.this.SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpCoreShooter, "coreshooter").booleanValue() /* && NewCtQ.this.SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpStations, "stations").booleanValue()*/) {
                        if (mCbFirst.isChecked() || mCbSecond.isChecked() || mCbThird.isChecked() || mCbFourth.isChecked()) {
                            NewCtQ.this.GenerateNewCtq();
                        } else {
                            mUtility.showToast(getActivity(), "Please select station no", "0");
                        }
                    }
                } else if (Config.Department.toLowerCase().equalsIgnoreCase(Config.CORE_SHOP_CS2)) {
                    if (NewCtQ.this.SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpShift, "shift").booleanValue() && NewCtQ.this.SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpGrpCode, "group").booleanValue() && NewCtQ.this.SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpProcessType, "process").booleanValue()) {
                        NewCtQ.this.GenerateNewCtq();
                    }
                } else if (Config.Department.toLowerCase().equalsIgnoreCase(Config.PATTERN_SHOP)) {
                    if (SpinnerValidation(getActivity(), mSpShift, "shift") && !isDupPType) {
                        NewCtQ.this.GenerateNewCtq();
                    } else if (SpinnerValidation(getActivity(), mSpShift, "shift") && isDupPType) {
                        NewCtQ.this.GenerateNewCtq();
                    }
                } else if (Config.Department.toLowerCase().equalsIgnoreCase(Config.MELTING)) {
                    if (!NewCtQ.this.SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpShift, "shift").booleanValue() || !NewCtQ.this.SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpGrpCode, "group").booleanValue()) {
                        return;
                    }
                    if (NewCtQ.this.mEdtHeatNo.getText().toString() == null || NewCtQ.this.mEdtHeatNo.getText().toString().length() <= 0) {
                        NewCtQ.this.mUtility.showToast(NewCtQ.this.getActivity(), "Please Enter Heat No", "0");
                    } else if (NewCtQ.this.SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpFurnace, "furnace").booleanValue()) {
                        NewCtQ.this.GenerateNewCtq();
                    }
                } /*else if (!NewCtQ.this.SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpShift, "shift").booleanValue() || !NewCtQ.this.SpinnerValidation(NewCtQ.this.getActivity(), NewCtQ.this.mSpGrpCode, "group").booleanValue()) {

                }*/ else {
                    if (NewCtQ.this.mEdtHeatNo.getText().toString() == null || NewCtQ.this.mEdtHeatNo.getText().toString().length() <= 0) {
                        NewCtQ.this.mUtility.showToast(NewCtQ.this.getActivity(), "Please Enter Heat No", "0");
                    } else {
                        NewCtQ.this.GenerateNewCtq();
                    }
                }
            }
        });

        mEdtHeatNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mUtility.HideShowKeyboard(getActivity(), mEdtHeatNo, "0");
                    return true;
                }
                return false;
            }
        });


        mRvCancel.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                mEdtHeatNo.setText("");
                mSpCustName.setSelection(0);
                mSpPartNo.setSelection(0);
                mSpShift.setSelection(0);
                mSpGrpCode.setSelection(0);
                mSpFurnace.setSelection(0);
                mSpProcessType.setSelection(0);
                mSpCoreShooter.setSelection(0);
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
                    mCustomerId = Integer.valueOf(Config.mAlCustomer.get(position).getID());
                    getPartDetails(NewCtQ.this.mCustomerId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (Config.Department.equals(Config.CORE_SHOP_CS2)) {
            mSpDupStations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mSpDupStations.getAdapter() != null && mSpDupStations.getAdapter().getCount() != 0) {
                        mCustomerId = Integer.valueOf(Config.mAlCustomer.get(position).getID());
                        getPartDetails(NewCtQ.this.mCustomerId);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        if (Config.Department.equals(Config.CORE_SHOP_CS1)) {
            mSpDupCust.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (mSpDupCust.getAdapter() != null && mSpDupCust.getAdapter().getCount() != 0) {
                        mCustomerId = Integer.valueOf(Config.mAlCustomer.get(position).getID());
                        getPartDetails(NewCtQ.this.mCustomerId);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        mSpPartNo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPartId = null;
                if (mSpPartNo.getAdapter() != null && mSpPartNo.getAdapter().getCount() != 0) {
                    mPartId = Integer.valueOf(mAlPartJobCode.get(position).getID());
                    mStrJobCode = mAlPartJobCode.get(position).getPartJobCodeHF();
                    Utility.logging("mPartId" + NewCtQ.this.mPartId);
                    Utility.logging("mStrJobCode" + NewCtQ.this.mStrJobCode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpProcessType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (mSpProcessType.getAdapter() != null && mSpProcessType.getSelectedItemPosition() > 0) {
                    if (mSpProcessType.getSelectedItem().toString().toLowerCase().contains("core shooter parameter")) {
                        mLlCoreShooter.setVisibility(View.VISIBLE);
                        mLlStations.setVisibility(View.GONE);
                        mLlCustName.setVisibility(View.VISIBLE);
                        mLlDupStations.setVisibility(View.GONE);
                        isClicked = Boolean.valueOf(true);
                    } else {
                        mLlCoreShooter.setVisibility(View.GONE);
                        mLlStations.setVisibility(View.GONE);
                        mLlCustName.setVisibility(View.GONE);
                        mLlDupStations.setVisibility(View.VISIBLE);
                        mTxtDupStations.setText("Customer Name");
                        isClicked = Boolean.valueOf(false);
                    }
                    isProcessType = Boolean.valueOf(true);
                } else {
                    mLlCoreShooter.setVisibility(View.GONE);
                    mLlStations.setVisibility(View.GONE);
                    mLlCustName.setVisibility(View.VISIBLE);
                    mLlDupStations.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mRvRefresh.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                getJobDetails();
            }
        });


        this.mSpCoreShooter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (NewCtQ.this.mSpCoreShooter.getAdapter() == null || NewCtQ.this.mSpCoreShooter.getSelectedItemPosition() <= 0) {
                    NewCtQ.this.mLlStations.setVisibility(View.GONE);
                    return;
                }
                if (NewCtQ.this.mSpCoreShooter.getSelectedItem().toString().contains("1") || NewCtQ.this.mSpCoreShooter.getSelectedItem().toString().contains("2")) {
                    StationHideAndShow(5);
                    NewCtQ.this.mLlStations.setVisibility(View.VISIBLE);
                } else if (NewCtQ.this.mSpCoreShooter.getSelectedItem().toString().contains("3") || NewCtQ.this.mSpCoreShooter.getSelectedItem().toString().contains("4") || NewCtQ.this.mSpCoreShooter.getSelectedItem().toString().contains("6")) {
                    StationHideAndShow(3);
                    NewCtQ.this.mLlStations.setVisibility(View.VISIBLE);
                } else if (NewCtQ.this.mSpCoreShooter.getSelectedItem().toString().contains("8") || NewCtQ.this.mSpCoreShooter.getSelectedItem().toString().contains("9")) {
                    StationHideAndShow(4);
                    NewCtQ.this.mLlStations.setVisibility(View.VISIBLE);
                } else if (NewCtQ.this.mSpCoreShooter.getSelectedItem().toString().contains("5") || NewCtQ.this.mSpCoreShooter.getSelectedItem().toString().contains("7")) {
                    StationHideAndShow(2);
                    NewCtQ.this.mLlStations.setVisibility(View.VISIBLE);
                }
                NewCtQ.this.isCoreShooter = true;
                NewCtQ.this.isStation = true;
                mLlCustName.setVisibility(View.GONE);
                mLlDupCust.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    void StationHideAndShow(int option) {

        mLlFirst.setVisibility(View.VISIBLE);
        mLlSecond.setVisibility(View.VISIBLE);
        mLlThird.setVisibility(View.VISIBLE);
        mLlFourth.setVisibility(View.VISIBLE);

        mTxtFirst.setVisibility(View.VISIBLE);
        mTxtFirst.setText("1");

        mTxtSecond.setVisibility(View.VISIBLE);
        mTxtSecond.setText("2");

        mTxtThird.setVisibility(View.VISIBLE);
        mTxtThird.setText("3");

        mTxtFourth.setVisibility(View.VISIBLE);
        mTxtFourth.setText("4");

        if (option == 2) {
            mLlSecond.setVisibility(View.GONE);
            mLlThird.setVisibility(View.GONE);
            mLlFourth.setVisibility(View.GONE);
            mCbFourth.setChecked(false);
            mCbSecond.setChecked(false);
            mCbThird.setChecked(false);
            mCbFirst.setChecked(false);
        } else if (option == 3) {
            mLlThird.setVisibility(View.GONE);
            mLlFourth.setVisibility(View.GONE);

            mCbFourth.setChecked(false);
            mCbSecond.setChecked(false);
            mCbThird.setChecked(false);
            mCbFirst.setChecked(false);
        } else if (option == 4) {
            mLlFourth.setVisibility(View.GONE);
            mCbFourth.setChecked(false);
            mCbSecond.setChecked(false);
            mCbThird.setChecked(false);
            mCbFirst.setChecked(false);
        }
    }

    void GenerateNewCtq() {
        try {
            if (Utility.isInternetConnected(getActivity())) {
                JobWiseModel mJobWiseModel = new JobWiseModel();
                mJobWiseModel.setPartIDHF(mPartId.intValue());
                mJobWiseModel.setCustomerIDHF(mCustomerId.intValue());
                mJobWiseModel.setHeatNumberHF(mEdtHeatNo.getText().toString());
                if (isFurnace) {
                    mJobWiseModel.setFurnaceNameHF(mSpFurnace.getSelectedItem().toString());
                } else {
                    mJobWiseModel.setFurnaceNameHF(null);
                }
                if (isGrpCode.booleanValue()) {
                    mJobWiseModel.setGroupCodeHF(mSpGrpCode.getSelectedItem().toString());
                } else {
                    mJobWiseModel.setGroupCodeHF(null);
                }

                mJobWiseModel.setShiftCodeHF(mSpShift.getSelectedItem().toString());

                if (isCoreShooter.booleanValue()) {
                    mJobWiseModel.setMachineNameHF(mSpCoreShooter.getSelectedItem().toString());
                } else {
                    mJobWiseModel.setMachineNameHF(null);
                }

                if (isStation) {
                    ArrayList<String> mAlStations = new ArrayList<>();
                    mAlStations.clear();

                    if (mCbFirst.isChecked() ? true : false) {
                        mAlStations.add("1");
                    }
                    if (mCbSecond.isChecked() ? true : false) {
                        mAlStations.add("2");
                    }
                    if (mCbThird.isChecked() ? true : false) {
                        mAlStations.add("3");
                    }
                    if (mCbFourth.isChecked() ? true : false) {
                        mAlStations.add("4");
                    }
                    String value = "";
                    for (int i = 0; i < mAlStations.size(); i++) {
                        if (!value.equals("")) {
                            value += " ; " + mAlStations.get(i);
                        } else {
                            value = mAlStations.get(i);
                        }
                    }
                    mJobWiseModel.setStationHF(value);
                } else {
                    mJobWiseModel.setStationHF(null);
                }

                if (this.isProcessType.booleanValue() && !isDupPType) {
                    mJobWiseModel.setProcessTypeHF(ProcessType(mSpProcessType.getSelectedItem().toString()));
                } else if (!this.isProcessType.booleanValue() && isDupPType) {
                    mJobWiseModel.setProcessTypeHF(ProcessType(mSpDupPType.getSelectedItem().toString()));
                } else {
                    mJobWiseModel.setProcessTypeHF(0);
                }

                HttpApi mHttpApi = new HttpApi(getActivity(), Boolean.valueOf(true), this.mGenerateOnResponseCallback, Config.Baseurl + Config.CreateNewQAC, HttpPost.METHOD_NAME, this.mUtility.SendParams(getActivity(), "0", new Gson().toJson(mJobWiseModel), null));
                mHttpApi.execute(new String[0]);
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

            if (responseString != null && !responseString.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.has("Status")) {
                        if (jsonObject.getBoolean("Status")) {
                            JSONArray mJsonArray = new JSONArray(jsonObject.getString("Response"));
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
                        } else {
                            mUtility.showToast(getActivity(), jsonObject.getString("Response"), "0");
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

        }
    };


    void SetAdapter() {

        mAdapterCustName = new NewQacListAdapter(getActivity(), Config.mAlCustomer);
        mSpCustName.setAdapter(this.mAdapterCustName);
        mSpDupStations.setAdapter(mAdapterCustName);
        mSpDupCust.setAdapter(mAdapterCustName);

        mAdapterPortNo = new NewQacPartListAdapter(getActivity(), mAlPartJobCode);
        mSpPartNo.setAdapter(this.mAdapterPortNo);

//      mAdFurnace,mAdGrpCode,mAdShift

        CommonAdapter mAdFurnace = new CommonAdapter(getActivity(), mAlFurnace);
        mSpFurnace.setAdapter(mAdFurnace);

        CommonAdapter mAdGrpCode = new CommonAdapter(getActivity(), mAlGroupCode);
        mSpGrpCode.setAdapter(mAdGrpCode);

        CommonAdapter mAdShift = new CommonAdapter(getActivity(), mAlShift);
        mSpShift.setAdapter(mAdShift);

//      mAdStations = new CommonAdapter(getActivity(), mAlStation);
//      mSpStations.setAdapter(mAdStations);

        CommonAdapter mAdProcess = new CommonAdapter(getActivity(), mAlCommon);
        mSpProcessType.setAdapter(mAdProcess);
        mSpDupPType.setAdapter(mAdProcess);

        CommonAdapter mAdMachine = new CommonAdapter(getActivity(), mAlMachineNo);
        mSpCoreShooter.setAdapter(mAdMachine);

    }


    void getPartDetails(Integer ID) {
        mAlPartJobCode.clear();
        for (int j = 0; j < Config.mAlParts.size(); j++) {
            if (ID == Integer.parseInt(Config.mAlParts.get(j).getCustomerIDHF()) && Config.mAlParts.get(j).getSpecTypeHF().equals("SAAPL")) {
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

                                        if (mJsonObject.getJSONArray("Clients") != null) {
                                            mAlCustomer.clear();
                                            for (int i = 0; i < mJsonObject.getJSONArray("Clients").length(); i++) {
                                                JSONObject jsonObject = mJsonObject.getJSONArray("Clients").getJSONObject(i);
                                                CustomerModel customerModel = new CustomerModel();
                                                customerModel.setID(jsonObject.getInt("ID"));
                                                customerModel.setClientName(jsonObject.getString("ClientNameHF"));
                                                mAlCustomer.add(customerModel);
                                            }
                                        }
                                    }
                                    if (mJsonObject.has("Parts")) {
                                        mAlParts.clear();
                                        for (int i = 0; i < mJsonObject.getJSONArray("Parts").length(); i++) {
                                            JSONObject jsonObject = mJsonObject.getJSONArray("Parts").getJSONObject(i);
                                            PartModel mPartModel = new PartModel();
                                            mPartModel.setID(jsonObject.getInt("ID"));
                                            mPartModel.setCustomerIDHF(jsonObject.getString("CustomerIDHF"));
                                            mPartModel.setPartNameHF(jsonObject.getString("PartNameHF"));
                                            mPartModel.setPartNoHF(jsonObject.getString("PartNoHF"));
                                            mPartModel.setSpecTypeHF(jsonObject.getString("SpecTypeHF"));
                                            mPartModel.setPartJobCodeHF(jsonObject.getString("PartJobCodeHF"));
                                            mAlParts.add(mPartModel);
                                        }
                                    }
                                    SetAdapter();
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
        }
    }


    Boolean SpinnerValidation(Activity mActivity, Spinner mSpinner, String Type) {
        Boolean status = false;
        if (Type != null && Type.toLowerCase().equals("shift")) {
            if (mSpinner.getSelectedItem().toString() == null || mSpinner.getSelectedItem().toString().equalsIgnoreCase("please select shift")) {
                this.mUtility.showToast(mActivity, "Select Shift", "0");
            } else {
                status = true;
            }
        }
        if (Type != null && Type.toLowerCase().equals("group")) {
            if (mSpinner.getSelectedItem().toString() == null || mSpinner.getSelectedItem().toString().equalsIgnoreCase("please select group code")) {
                this.mUtility.showToast(mActivity, "Select Group Code", "0");
            } else {
                status = Boolean.valueOf(true);
            }
        }
        if (Type != null && Type.toLowerCase().equals("furnace")) {
            if (mSpinner.getSelectedItem().toString() == null || mSpinner.getSelectedItem().toString().equalsIgnoreCase("please choose furnace")) {
                this.mUtility.showToast(mActivity, "Select Furnace", "0");
            } else {
                status = Boolean.valueOf(true);
            }
        }
        if (Type != null && Type.toLowerCase().equals("coreshooter")) {
            if (mSpinner.getSelectedItem().toString() == null || mSpinner.getSelectedItem().toString().equalsIgnoreCase("please choose machine no")) {
                this.mUtility.showToast(mActivity, "Select Machine No", "0");
            } else {
                status = Boolean.valueOf(true);
            }
        }
        if (Type != null && Type.toLowerCase().equals("process")) {
            if (mSpinner.getSelectedItem().toString() == null || mSpinner.getSelectedItem().toString().equalsIgnoreCase("select core shop")) {
                this.mUtility.showToast(mActivity, "Select Core Shop ", "0");
            } else {
                status = Boolean.valueOf(true);
            }
        }
        if (Type != null && Type.toLowerCase().equals("pattern")) {
            if (mSpinner.getSelectedItem().toString() == null || mSpinner.getSelectedItem().toString().equalsIgnoreCase("select pattern shop")) {
                this.mUtility.showToast(mActivity, "Select Pattern Shop", "0");
            } else {
                status = true;
            }
        }
        if (Type == null || !Type.toLowerCase().equals("stations")) {
            return status;
        }
        if (mSpinner.getSelectedItem().toString() != null && !mSpinner.getSelectedItem().toString().equalsIgnoreCase("please select stations")) {
            return Boolean.valueOf(true);
        }
        this.mUtility.showToast(mActivity, "Select Stations", "0");
        return status;
    }

    int ProcessType(String type) {
        int position = 0;
        ArrayList<ProcessType> mAlProcessType = new ArrayList();
        ProcessType mProcessType = new ProcessType("Core Sand Mixing", 1);
        ProcessType mProcessType1 = new ProcessType("Core Shooter Parameter", 2);
        ProcessType mProcessType2 = new ProcessType("Coating Mixing", 3);
        ProcessType mProcessType3 = new ProcessType("Core Heating", 4);
        ProcessType mProcessType4 = new ProcessType("Core Dressing & Inspection", 5);
        ProcessType mProcessType5 = new ProcessType("Core Box Inspection", 6);
        ProcessType mProcessType6 = new ProcessType("Pattern Inspection", 7);
        mAlProcessType.add(mProcessType);
        mAlProcessType.add(mProcessType1);
        mAlProcessType.add(mProcessType2);
        mAlProcessType.add(mProcessType3);
        mAlProcessType.add(mProcessType4);
        mAlProcessType.add(mProcessType5);
        mAlProcessType.add(mProcessType6);
        Iterator it = mAlProcessType.iterator();
        while (it.hasNext()) {
            ProcessType processType = (ProcessType) it.next();
            if (processType.getProcessTypeText().toLowerCase().contains(type.toLowerCase())) {
                position = processType.getProcessTypeId();
            }
        }
        return position;
    }

}
