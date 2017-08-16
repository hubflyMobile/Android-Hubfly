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

import com.google.gson.Gson;
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
    ValueFilter mValueFilter;
    ArrayList<OpenCtqModel> mAlLocal;
    LinearLayout mLlNoData, mLlCtqList;
    Boolean Option;

    public CtoListAdapter(Activity mActivity, ArrayList<OpenCtqModel> mAlCto, LinearLayout mLlNoData, LinearLayout mLlCtqList, Boolean Option) {
        this.mActivity = mActivity;
        this.mAlCto = mAlCto;
        this.mLlCtqList = mLlCtqList;
        this.mLlNoData = mLlNoData;
        this.Option = Option;
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
        TextView mTxtCustName, mTxtPartName, mTxtJobCode, mTxtHeatedNo, mTxtCtq, mTxtQap;
        LinearLayout mLlCard;
        RippleView mRvCTO;

        public DataObjectHolder(View itemView) {
            super(itemView);
            mTxtCustName = (TextView) itemView.findViewById(R.id.txt_cust_name);
            mTxtJobCode = (TextView) itemView.findViewById(R.id.txt_job_code);
            mTxtPartName = (TextView) itemView.findViewById(R.id.txt_part_name);
            mTxtHeatedNo = (TextView) itemView.findViewById(R.id.txt_heat_no);
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
        holder.mTxtJobCode.setText(mCtoModel.getJobCode());
        holder.mTxtPartName.setText(mCtoModel.getPartname());
        holder.mTxtHeatedNo.setText(mCtoModel.getHeatNo());
        holder.mTxtCtq.setText(mCtoModel.getCTQStatus());
        holder.mTxtQap.setText(mCtoModel.getQAPStatus());

        holder.mRvCTO.setClickable(true);

        if (Option) {
            holder.mRvCTO.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    Gson gson = new Gson();
                    String jsonCtq = gson.toJson(mCtoModel.getmAlCtq());
                    String jsonQap = gson.toJson(mCtoModel.getmAlQap());
                    Intent mIntent = new Intent(mActivity, NewCTQ.class);
                    mIntent.putExtra("CTQ", jsonCtq);
                    mIntent.putExtra("QAP", jsonQap);
                    mIntent.putExtra("CUSTOMERNAME", mCtoModel.getCustname());
                    mIntent.putExtra("PARTNAME", mCtoModel.getPartname());
                    mIntent.putExtra("JOBCODE", mCtoModel.getJobCode());
                    mIntent.putExtra("CTQSTATUS", mCtoModel.getCTQStatus());
                    mIntent.putExtra("QAPSTATUS", mCtoModel.getQAPStatus());
                    mActivity.startActivity(mIntent);
                }
            });
        } else {
            holder.mRvCTO.setClickable(false);
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
                    if (mAlLocal.get(i).getJobCode().toLowerCase().trim().replace(" ", "").contains(constraint.toString().toLowerCase())) {
                        filterList.add(mAlLocal.get(i));
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