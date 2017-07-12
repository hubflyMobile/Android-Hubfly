package com.hubfly.ctq.fragement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hubfly.ctq.R;
import com.hubfly.ctq.util.RippleView;
import com.hubfly.ctq.util.Utility;

/**
 * Created by Admin on 04-07-2017.
 */

public class NewCtQ extends Fragment {

    RippleView mRvImgNavigation;
    TextView mTxtHeading;
    RippleView mRvCustName, mRvPortNo, mRvDepartment,  mRvCreate,mRvCancel;
    Spinner mSpCustName, mSpPortNo, mSpDept;
    EditText mEdtHeatNo, mEdtBoxes, mEdtJobCode;
    Utility mUtility;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_new_ctq, container, false);

        Initializaion();
        InitiizationViews(rootView);
        SetAdapter();
        SetClickEvents();
        return rootView;

    }

    void Initializaion(){
        mUtility = new Utility(getActivity());
    }

    void InitiizationViews(View rootView){
        mRvImgNavigation =(RippleView)rootView.findViewById(R.id.rv_back);
        mTxtHeading =(TextView)rootView.findViewById(R.id.txt_title);

        mEdtBoxes = (EditText) rootView.findViewById(R.id.edt_boxes);
        mEdtHeatNo = (EditText) rootView.findViewById(R.id.edt_head_no);
        mEdtJobCode = (EditText) rootView.findViewById(R.id.edt_job_code);

        mSpDept = (Spinner) rootView.findViewById(R.id.sp_dept);
        mSpPortNo = (Spinner)rootView. findViewById(R.id.sp_part_no);
        mSpCustName = (Spinner) rootView.findViewById(R.id.sp_cust_name);

        mRvCreate = (RippleView)rootView. findViewById(R.id.rv_submit);
        mRvCustName = (RippleView) rootView.findViewById(R.id.rv_cust_name);
        mRvDepartment = (RippleView) rootView.findViewById(R.id.rv_dept);
        mRvPortNo = (RippleView) rootView.findViewById(R.id.rv_part_name);
        mRvCancel = (RippleView) rootView.findViewById(R.id.rv_cancel);

        mTxtHeading.setText("New Quality Assurance Check");
    }

    void SetClickEvents(){
        mRvImgNavigation.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                DrawerLayout drawerLayout = (DrawerLayout) getActivity()
                        .findViewById(R.id.drawer_layout);
                LinearLayout drawerList = (LinearLayout) getActivity()
                        .findViewById(R.id.testing);
                drawerLayout.openDrawer(drawerList);
            }
        });

        mRvCreate.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (!mSpCustName.getSelectedItem().toString().equals("Please Select Customer")) {
                    if (!mSpPortNo.getSelectedItem().toString().equals("Please Select Part No")) {
                        if (mEdtHeatNo.getText().toString().trim().length() > 0) {
                            if (mEdtBoxes.getText().toString().trim().length() > 0) {
                                if (!mSpDept.getSelectedItem().toString().equals("Please Select Department")) {
                                    if (mEdtJobCode.getText().toString().trim().length() > 0) {
                                        SetAlertDialog();
                                    } else {
                                        mUtility.showToast(getActivity(), "Please Add Job Code", "0");
                                    }
                                } else {
                                    mUtility.showToast(getActivity(), "Please Select Department", "0");
                                }
                            } else {
                                mUtility.showToast(getActivity(), "Please Enter no of boxes", "0");
                            }
                        } else {
                            mUtility.showToast(getActivity(), "Please Enter Heat No ", "0");
                        }
                    }else {
                        mUtility.showToast(getActivity(), "Please Select Part No ", "0");
                    }
                }else {
                    mUtility.showToast(getActivity(), "Please Select Customer ", "0");
                }
            }
        });

        mRvCancel.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                mEdtBoxes.setText("");
                mEdtHeatNo.setText("");
                mEdtJobCode.setText("");
                mSpCustName.setSelection(0);
                mSpDept.setSelection(0);
                mSpPortNo.setSelection(0);
            }
        });

    }
    void SetAdapter() {
        ArrayAdapter<String> mAdapterCustName = new ArrayAdapter<String>(getActivity(), R.layout.app_spinner_item, getResources().getStringArray(R.array.cust));
        mAdapterCustName.setDropDownViewResource(R.layout.app_spinner_dropdown_item);
        mSpCustName.setAdapter(mAdapterCustName);

        ArrayAdapter<String> mAdapterPortNo = new ArrayAdapter<String>(getActivity(), R.layout.app_spinner_item, getResources().getStringArray(R.array.portno));
        mAdapterPortNo.setDropDownViewResource(R.layout.app_spinner_dropdown_item);
        mSpPortNo.setAdapter(mAdapterPortNo);

        ArrayAdapter<String> mAdapterDept = new ArrayAdapter<String>(getActivity(), R.layout.app_spinner_item, getResources().getStringArray(R.array.dept));
        mAdapterDept.setDropDownViewResource(R.layout.app_spinner_dropdown_item);
        mSpDept.setAdapter(mAdapterDept);

    }


    void SetAlertDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(
                getActivity()).create();

        // Setting Dialog Title
        alertDialog.setTitle("Message");

        // Setting Dialog Message
        alertDialog.setMessage("Created Successfully...");

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Fragment fragment1 = new OpenCtQ();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, fragment1);
                fragmentTransaction.commit();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
