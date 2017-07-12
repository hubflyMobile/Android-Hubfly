package com.hubfly.ctq.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hubfly.ctq.Model.CtoModel;
import com.hubfly.ctq.R;
import com.hubfly.ctq.activity.NewCTQ;
import com.hubfly.ctq.util.RippleView;

import java.util.ArrayList;

/**
 * Created by Admin on 08-06-2017.
 */

public class QapAdapter extends RecyclerView.Adapter<QapAdapter.DataObjectHolder> {

    Activity mActivity;
    ArrayList<CtoModel> mAlCto;
    NewCTQ mNewCTQ;
    RadioButton lastCheckedRB;
    private static int lastCheckedPos = 0;

    public QapAdapter(Activity mActivity, ArrayList<CtoModel> mAlCto) {
        this.mActivity = mActivity;
        this.mAlCto = mAlCto;
        mNewCTQ = (NewCTQ) mActivity;
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView mTxtTaskName;
        LinearLayout mLlRoot;
        RippleView mRvTaskName;
        RadioButton mRbChecked;

        public DataObjectHolder(View itemView) {
            super(itemView);
            mTxtTaskName = (TextView) itemView.findViewById(R.id.txt_task_name);
            mLlRoot = (LinearLayout) itemView.findViewById(R.id.ll_root_qap);
            mRvTaskName = (RippleView) itemView.findViewById(R.id.rv_task_name);
            mRbChecked = (RadioButton) itemView.findViewById(R.id.rb_check);
        }
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_qap, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final CtoModel mCtoModel = mAlCto.get(position);
        holder.mTxtTaskName.setText(mCtoModel.getTaskName());

        holder.mRbChecked.setChecked(mCtoModel.isChecked());
        holder.mRbChecked.setTag(new Integer(position));

        if (position == 0 && mAlCto.get(position).isChecked() && holder.mRbChecked.isChecked()) {
            lastCheckedRB = holder.mRbChecked;
            lastCheckedPos = 0;
        }

        holder.mRbChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mNewCTQ.mLlQapValue.setVisibility(View.VISIBLE);
                    mNewCTQ.mLlCtoValue.setVisibility(View.GONE);
                } else {
                    mNewCTQ.mLlCtoValue.setVisibility(View.VISIBLE);
                    mNewCTQ.mLlQapValue.setVisibility(View.GONE);
                }
            }
        });

       /* holder.mRbChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RadioButton cb = (RadioButton) v;
                int clickedPos = ((Integer) cb.getTag()).intValue();
                if (cb.isChecked()) {
                    if (lastCheckedRB != null) {
                        lastCheckedRB.setChecked(false);
                        mAlCto.get(lastCheckedPos).setChecked(false);
                        mNewCTQ.mLlCtoValue.setVisibility(View.VISIBLE);
                        mNewCTQ.mLlQapValue.setVisibility(View.GONE);
                    }
                    lastCheckedRB = cb;
                    lastCheckedPos = clickedPos;
                    mNewCTQ.mLlQapValue.setVisibility(View.VISIBLE);
                    mNewCTQ.mLlCtoValue.setVisibility(View.GONE);
                } else {
                    lastCheckedRB = null;
                }
                mAlCto.get(clickedPos).setChecked(cb.isChecked());

                *//*if (indexvalue == position) {
                    holder.mLlRoot.setBackgroundColor(Color.parseColor("#567845"));
                } else {
                    holder.mLlRoot.setBackgroundColor(Color.parseColor("#000000"));
                }*//*
            }
        });*/


        holder.mLlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout mLinearLayout = (LinearLayout) v;
                int clickedPos = ((Integer) mLinearLayout.getTag()).intValue();
                holder.mRbChecked.setChecked(true);
                if (holder.mRbChecked.isChecked()) {
                    if (lastCheckedRB != null) {
                        lastCheckedRB.setChecked(false);
                        mAlCto.get(lastCheckedPos).setChecked(false);
                        mNewCTQ.mLlQapValue.setVisibility(View.VISIBLE);
                        mNewCTQ.mLlCtoValue.setVisibility(View.GONE);
                    }
                    lastCheckedRB = holder.mRbChecked;
                    lastCheckedPos = clickedPos;
                    mNewCTQ.mLlCtoValue.setVisibility(View.VISIBLE);
                    mNewCTQ.mLlQapValue.setVisibility(View.GONE);
                } else {
                    lastCheckedRB = null;
                }
                mAlCto.get(clickedPos).setChecked(holder.mRbChecked.isChecked());
            }
        });



    }

    @Override
    public int getItemCount() {
        return mAlCto.size();
    }

}
