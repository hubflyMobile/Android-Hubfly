package com.hubfly.ctq.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
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

import com.hubfly.ctq.Model.OpenCtqModel;
import com.hubfly.ctq.R;
import com.hubfly.ctq.adapter.CtoListAdapter;
import com.hubfly.ctq.util.RippleView;
import com.hubfly.ctq.util.Utility;

import java.util.ArrayList;

/**
 * Created by Admin on 04-07-2017.
 */

public class OpenCtQ extends Fragment {

    RippleView mRvImgNavigation;
    TextView mTxtHeading;
    ArrayList<OpenCtqModel> mAlOpenCtq;
    CtoListAdapter mAdapter;
    RecyclerView mRvOpenCTQ;
    Utility mUtility;
    LinearLayout mLlOpenCtq,mLlNoData,mLlRootList;
    EditText mEdtSearch;
    ImageView mImgClear;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_open_ctq, container, false);

        Initializaion();
        InitiizationViews(rootView);
        SetClickEvents();
        setAdapter();
        GetData();
        return rootView;
    }

    void Initializaion() {
        mAlOpenCtq = new ArrayList<>();
        mUtility = new Utility(getActivity());
    }

    void InitiizationViews(View rootView) {
        mRvImgNavigation = (RippleView) rootView.findViewById(R.id.rv_back);
        mTxtHeading = (TextView) rootView.findViewById(R.id.txt_title);
        mLlOpenCtq = (LinearLayout) rootView.findViewById(R.id.ll_open_ctq);
        mLlNoData = (LinearLayout) rootView.findViewById(R.id.ll_no_data);
        mLlRootList = (LinearLayout) rootView.findViewById(R.id.ll_root_list);
        mEdtSearch = (EditText) rootView.findViewById(R.id.edt_search);
        mImgClear = (ImageView) rootView.findViewById(R.id.img_clear);
        mRvOpenCTQ = mUtility.CustomRecycleView(getActivity(), mLlOpenCtq);
        mTxtHeading.setText("Open Quality Assurance Check");

        mUtility.HideShowKeyboard(getActivity(), mEdtSearch, "0");
        mLlRootList.getBackground().setAlpha(45);
    }

    void GetData() {
        mAlOpenCtq.add(new OpenCtqModel("L&T", "LT02", "MAH S01", "Chandrasekar", "6th july ,3.40pm", "4/12", "9/12"));
        mAlOpenCtq.add(new OpenCtqModel("VESTAS", "LT03", "MAH S02", "Sivaraj", "5th july ,5.40am", "5/12", "9/12"));
        mAlOpenCtq.add(new OpenCtqModel("Wipro", "LT04", "MAH S03", "Hari", "7th july ,12.40pm", "4/12", "9/12"));
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

        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = mEdtSearch.getText().toString().trim();
                if (text != null && !text.equals("")) {
                    mAdapter.getFilter().filter(text);
                    mUtility.HideShowKeyboard(getActivity(), mEdtSearch, "0");
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
        mAdapter = new CtoListAdapter(getActivity(), mAlOpenCtq, "1",mLlNoData,mLlOpenCtq);
        mRvOpenCTQ.setAdapter(mAdapter);
    }




}
