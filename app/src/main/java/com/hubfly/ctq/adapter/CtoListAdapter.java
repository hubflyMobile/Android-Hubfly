package com.hubfly.ctq.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hubfly.ctq.Model.OpenCtqModel;
import com.hubfly.ctq.R;
import com.hubfly.ctq.activity.NewCTQ;
import com.hubfly.ctq.util.RippleView;

import java.util.ArrayList;


/**
 * Created by Admin on 08-06-2017.
 */

public class CtoListAdapter extends RecyclerView.Adapter<CtoListAdapter.DataObjectHolder> implements AdapterView.OnItemSelectedListener, Filterable {

    Activity mActivity;
    ArrayList<OpenCtqModel> mAlCto;
    String option;
    ValueFilter mValueFilter;
    ArrayList<OpenCtqModel> mAlLocal;
    LinearLayout mLlNoData, mLlCtqList;

    public CtoListAdapter(Activity mActivity, ArrayList<OpenCtqModel> mAlCto, String option, LinearLayout mLlNoData, LinearLayout mLlCtqList) {
        this.mActivity = mActivity;
        this.mAlCto = mAlCto;
        this.option = option;
        this.mLlCtqList = mLlCtqList;
        this.mLlNoData = mLlNoData;
        this.option = option;
        mAlLocal = mAlCto;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public Filter getFilter() {
        if (mValueFilter == null) {
            mValueFilter = new ValueFilter();
        }
        return mValueFilter;
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView mTxtCustName, mTxtPartNo, mTxtJobCode, mTxtCreateBy, mTxtCreated, mTxtCtq, mTxtQap;
        LinearLayout mLlCard;
        RippleView mRvCTO;

        public DataObjectHolder(View itemView) {
            super(itemView);
            mTxtCustName = (TextView) itemView.findViewById(R.id.txt_cust_name);
            mTxtPartNo = (TextView) itemView.findViewById(R.id.txt_part_name);
            mTxtJobCode = (TextView) itemView.findViewById(R.id.txt_job_code);
            mTxtCreateBy = (TextView) itemView.findViewById(R.id.txt_created_by);
            mTxtCreated = (TextView) itemView.findViewById(R.id.txt_create_date);
            mTxtCtq = (TextView) itemView.findViewById(R.id.txt_ctq);
            mTxtQap = (TextView) itemView.findViewById(R.id.txt_qap);
            mLlCard = (LinearLayout) itemView.findViewById(R.id.ll_card);
            mRvCTO = (RippleView) itemView.findViewById(R.id.rv_open_cto);
        }
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cto_list, parent, false);


        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {


        final OpenCtqModel mCtoModel = mAlCto.get(position);
        holder.mTxtCustName.setText(mCtoModel.getCustname());
        holder.mTxtPartNo.setText(mCtoModel.getPartname());
        holder.mTxtJobCode.setText(mCtoModel.getJobCode());
        holder.mTxtCreateBy.setText(mCtoModel.getCreateBy());
        holder.mTxtCreated.setText(mCtoModel.getDeptName());
        holder.mTxtCtq.setText(mCtoModel.getCTQ());
        holder.mTxtQap.setText(mCtoModel.getQAP());


        if (position % 2 == 0) {
            holder.mLlCard.setBackgroundResource(R.color.list_bg);
        } else {
            holder.mLlCard.setBackgroundResource(R.color.white);
        }

        if (option.equals("1")) {
            holder.mRvCTO.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    Intent mIntent = new Intent(mActivity, NewCTQ.class);
                    mIntent.putExtra("Option", String.valueOf(position));
                    mIntent.putExtra("Title", mCtoModel.getCustname());
                    mIntent.putExtra("Portname", mCtoModel.getPartname());
                    mIntent.putExtra("Jobcode", mCtoModel.getJobCode());
                    mIntent.putExtra("CreateBy", mCtoModel.getCreateBy());
                    mIntent.putExtra("CreatedDate", mCtoModel.getDeptName());
                    mIntent.putExtra("Ctq", mCtoModel.getCTQ());
                    mIntent.putExtra("Qap", mCtoModel.getQAP());
                    mActivity.startActivity(mIntent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mAlCto.size();
    }

    public class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<OpenCtqModel> filterList = new ArrayList<OpenCtqModel>();
                for (int i = 0; i < mAlLocal.size(); i++) {
                    if (mAlLocal.get(i).getJobCode().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        filterList.add(mAlLocal.get(i));
                    } else if (mAlLocal.get(i).getPartname().toUpperCase().contains(constraint.toString().toUpperCase())) {
//                        filterList.add(mAlLocal.get(i));
                    } else if (mAlLocal.get(i).getCustname().toUpperCase().contains(constraint.toString().toUpperCase())) {
//                        filterList.add(mAlLocal.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mAlLocal.size();
                results.values = mAlLocal;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mAlCto = (ArrayList<OpenCtqModel>) results.values;

            if (mAlCto != null && mAlCto.size() > 0) {
                mLlCtqList.setVisibility(View.VISIBLE);
                mLlNoData.setVisibility(View.GONE);
            } else {
                mLlCtqList.setVisibility(View.GONE);
                mLlNoData.setVisibility(View.VISIBLE);
            }
            notifyDataSetChanged();
        }
    }

}