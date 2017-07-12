package com.hubfly.ctq.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hubfly.ctq.Model.OpenCtqModel;
import com.hubfly.ctq.R;

import java.util.ArrayList;

/**
 * Created by Admin on 08-06-2017.
 */

public class OpenCtqAdapter extends RecyclerView.Adapter<OpenCtqAdapter.DataObjectHolder> {

    Activity mActivity;
    ArrayList<OpenCtqModel> mAlCto;


    public OpenCtqAdapter(Activity mActivity, ArrayList<OpenCtqModel> mAlCto) {
        this.mActivity = mActivity;
        this.mAlCto = mAlCto;
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView mTxtTaskName, mTxtPartNo, mTxtJobCode, mTxtCreateBy, mTxtCreated;

        public DataObjectHolder(View itemView) {
            super(itemView);
            mTxtTaskName = (TextView) itemView.findViewById(R.id.txt_task_name);
            mTxtPartNo = (TextView) itemView.findViewById(R.id.txt_port_no);
            mTxtJobCode = (TextView) itemView.findViewById(R.id.txt_job_code);
            mTxtCreateBy = (TextView) itemView.findViewById(R.id.txt_create_by);
            mTxtCreated = (TextView) itemView.findViewById(R.id.txt_create_date);
        }
    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_open_ctq, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final OpenCtqModel mOpenCtqModel = mAlCto.get(position);
        holder.mTxtTaskName.setText(mOpenCtqModel.getCustname());
        holder.mTxtPartNo.setText(mOpenCtqModel.getPartname());
        holder.mTxtJobCode.setText(mOpenCtqModel.getJobCode());
        holder.mTxtCreateBy.setText(mOpenCtqModel.getCreateBy());
        holder.mTxtCreated.setText(mOpenCtqModel.getDeptName());

    }
    @Override
    public int getItemCount() {
        return mAlCto.size();
    }
}
