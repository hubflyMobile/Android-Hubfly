package com.hubfly.ctq.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hubfly.ctq.Model.CtoModel;
import com.hubfly.ctq.Model.OpenCtqModel;
import com.hubfly.ctq.R;
import com.hubfly.ctq.adapter.CtoListAdapter;
import com.hubfly.ctq.util.RippleView;
import com.hubfly.ctq.util.Utility;

import java.util.ArrayList;


/**
 * Created by Admin on 04-07-2017.
 */

public class CloseCtQ extends Fragment {

    RippleView mRvImgNavigation;
    TextView mTxtHeading;
    ArrayList<CtoModel> mAlClose;
    ArrayList<OpenCtqModel> mAlOpenCtq;
    CtoListAdapter mAdapter;
    LinearLayout mLlCloseCtq;
    Utility mUtility;
    RecyclerView mRvCloseData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_close_ctq, container, false);

        Initializaion();
        InitiizationViews(rootView);
        SetClickEvents();
        SetAdapter();
        getData();

        return rootView;

    }

    void Initializaion() {
        mAlClose = new ArrayList<>();
        mAlOpenCtq = new ArrayList<>();
        mUtility = new Utility(getActivity());
    }

    void InitiizationViews(View rootView) {
        mRvImgNavigation = (RippleView) rootView.findViewById(R.id.rv_back);
        mTxtHeading = (TextView) rootView.findViewById(R.id.txt_title);

        mLlCloseCtq = (LinearLayout)rootView.findViewById(R.id.ll_close_ctq);

        mRvCloseData = mUtility.CustomRecycleView(getActivity(),mLlCloseCtq);

        mTxtHeading.setText("Close Quality Assurance Check");
    }

    void SetClickEvents() {
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
    }

    void getData() {
        SetData("0");
    }

    void SetAdapter() {
        mAdapter = new CtoListAdapter(getActivity(), mAlOpenCtq,"0",null,null);
        mRvCloseData.setAdapter(mAdapter);
    }

    void SetData(String option){
        if(option.equals("0")){
            mAlOpenCtq.add(new OpenCtqModel("L&T", "LT02", "MAH S01", "Chandrasekar", "6th july ,3.40pm", "12/12", "12/12"));
        }if(option.equals("1")){
            mAlOpenCtq.add(new OpenCtqModel("VESTAS", "LT03", "MAH S02", "Sivaraj", "5th july ,5.40am", "12/12", "12/12"));
        }
        if (option.equals("2")) {
            mAlOpenCtq.add(new OpenCtqModel("Wipro", "LT04", "MAH S03", "Hari", "7th july ,12.40pm", "12/12", "12/12"));
        }
        mAdapter.notifyDataSetChanged();
    }

}
