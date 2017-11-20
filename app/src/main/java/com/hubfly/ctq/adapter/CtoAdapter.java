package com.hubfly.ctq.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hubfly.ctq.Model.CtoModel;
import com.hubfly.ctq.Model.SendCTQModel;
import com.hubfly.ctq.R;
import com.hubfly.ctq.activity.NewCTQ;
import com.hubfly.ctq.util.Config;
import com.hubfly.ctq.util.HttpApi;
import com.hubfly.ctq.util.OnResponseCallback;
import com.hubfly.ctq.util.RippleView;
import com.hubfly.ctq.util.Utility;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Admin on 08-06-2017.
 */

public class CtoAdapter extends RecyclerView.Adapter<CtoAdapter.DataObjectHolder> {

    private Activity mActivity;
    private RecyclerView mRvCtq;
    private ArrayList<CtoModel> mAlCto;
    private NewCTQ mNewCTQ;
    private Utility mUtility;
    private RadioButton lastCheckedRB;
    private static int lastCheckedPos = 0;

    public CtoAdapter(Activity mActivity, ArrayList<CtoModel> mAlCto, RecyclerView view) {
        this.mActivity = mActivity;
        this.mAlCto = mAlCto;
        this.mRvCtq = view;
        mNewCTQ = (NewCTQ) mActivity;
        mUtility = new Utility(mActivity);
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView mTxtTaskName;
        RadioButton mRbChecked;
        LinearLayout mLlRoot;

        public DataObjectHolder(View itemView) {
            super(itemView);
            mTxtTaskName = (TextView) itemView.findViewById(R.id.txt_task_name);
            mLlRoot = (LinearLayout) itemView.findViewById(R.id.ll_root_ctq);
            mRbChecked = (RadioButton) itemView.findViewById(R.id.rb_check);
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ctq, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.mLlRoot.setTag("LL" + position);
        holder.mRbChecked.setTag("RB" + position);

        CtoModel mCtoModel = mAlCto.get(position);

        holder.mTxtTaskName.setText(mCtoModel.getTaskName());
        holder.mRbChecked.setChecked(mCtoModel.isChecked());
        holder.mLlRoot.setBackgroundColor(Color.WHITE);

        if (mCtoModel.getVerifiedHF()) {
            holder.mRbChecked.setChecked(true);
            holder.mLlRoot.setBackgroundResource(R.drawable.ic_radio_selected_gray);
        }

        if (mCtoModel.isChecked()) {
            lastCheckedRB = holder.mRbChecked;
            lastCheckedPos = position;
            holder.mLlRoot.setBackgroundResource(R.drawable.ic_list_active);
        }

        holder.mRbChecked.setClickable(false);

        holder.mLlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastCheckedRB != null) {
                    lastCheckedRB.setChecked(false);
                    CtoModel model = mAlCto.get(lastCheckedPos);
                    model.setChecked(false);
                    notifyItemChanged(lastCheckedPos);
                }
                String tag = v.getTag().toString().replace("LL", "");
                int clickedPos = Integer.parseInt(tag);

                RadioButton mRbChecked = (RadioButton) getView("RB" + clickedPos);
                mRbChecked.setChecked(true);

                lastCheckedRB = mRbChecked;
                lastCheckedPos = clickedPos;

                CtoModel model = mAlCto.get(clickedPos);
                model.setChecked(true);
                notifyItemChanged(clickedPos);
                if (!model.getVerifiedHF()) {
                    mNewCTQ.mEdtCtoValue.setText("");
                    mNewCTQ.mEdtRemarks.setText("");
                    hideshow(true);
                } else {
                    mNewCTQ.mEdtCtoValue.setText(String.valueOf(model.getCTQValueHF()));
                    if (model.getRemarks() != null && !model.getRemarks().equals("") && !model.getRemarks().equals("null")) {
                        mNewCTQ.mEdtRemarks.setText(model.getRemarks());
                    } else {
                        mNewCTQ.mEdtRemarks.setText("");
                    }
                    hideshow(false);
                }
            }
        });

        mNewCTQ.mRvSubmit.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (mUtility.validation(mActivity, mNewCTQ.mEdtCtoValue, "CTQ")) {
                    if (Utility.isInternetConnected(mActivity)) {
                        GenerateNewCtq(mActivity, mAlCto.get(lastCheckedPos).getIndex(), mNewCTQ.mEdtCtoValue.getText().toString(), mNewCTQ.mEdtRemarks.getText().toString(), lastCheckedPos, mAlCto.get(lastCheckedPos).getTaskName(), mAlCto.get(lastCheckedPos).getCTQMinValueHF(), mAlCto.get(lastCheckedPos).getCTQMaxValueHF(), mAlCto.get(lastCheckedPos).getQACJobIDHF());
                    }
                }
            }
        });
    }

    private View getView(String tag) {
        return mRvCtq.findViewWithTag(tag);
    }

    @Override
    public int getItemCount() {
        return mAlCto.size();
    }


    private void GenerateNewCtq(final Activity mActivity, Integer ID, final String CtqValue, final String Remarks, final int Position, final String taskname, final double minValue, final double maxValue, final int QacJobId) {
        try {
            SendCTQModel mSendCTQModel = new SendCTQModel();
            mSendCTQModel.setID(ID);
            mSendCTQModel.setCTQValueHF(CtqValue);
            mSendCTQModel.setRemarksHF(Remarks);
            mSendCTQModel.setQACJobIDHF(QacJobId);
            mSendCTQModel.setCTQMinValueHF(minValue);
            mSendCTQModel.setCTQMaxValueHF(maxValue);
            Gson mGson = new Gson();
            String mJobData = mGson.toJson(mSendCTQModel);

            JSONObject mJsonObject = mUtility.SendParams(mActivity, "0", mJobData, null);
            HttpApi api = new HttpApi(mActivity, true, new OnResponseCallback() {
                @Override
                public void responseCallBack(Activity activity, String responseString) {
                    if (responseString != null && responseString.equals("200")) {

                        mUtility.showToast(mActivity, "CTQ Updated Successfully", "0");

                        String value = mNewCTQ.mTxtCtqCount.getText().toString();
                        String ctq[] = value.split("/");

                        if (Integer.parseInt(ctq[1]) >= Integer.parseInt(ctq[0])) {
                            mNewCTQ.mTxtCtqCount.setText(Integer.parseInt(ctq[0]) + 1 + "/" + ctq[1]);
                        }

                        CtoModel ctoModel = new CtoModel();
                        ctoModel.setTaskName(taskname);
                        ctoModel.setCTQValueHF(CtqValue);
                        ctoModel.setVerifiedHF(true);
                        ctoModel.setRemarks(Remarks);
                        ctoModel.setCTQMinValueHF(minValue);
                        ctoModel.setCTQMaxValueHF(maxValue);
                        ctoModel.setChecked(true);
                        mNewCTQ.mAlCto.set(Position, ctoModel);
                        hideshow(false);
                        notifyDataSetChanged();
                    } else {
                        mUtility.showToast(mActivity, "Server Error", "0");
                    }

                }
            }, Config.Baseurl + Config.UpdateHeatActivityForCTQ, "POST", mJsonObject);
            api.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideshow(Boolean option) {
        mNewCTQ.mLlQapValue.setVisibility(View.GONE);
        mNewCTQ.mLlCtoValue.setVisibility(View.VISIBLE);
        if (!option) {
            mNewCTQ.mEdtCtoValue.setClickable(false);
            mNewCTQ.mEdtRemarks.setClickable(false);
            mNewCTQ.mEdtCtoValue.setEnabled(false);
            mNewCTQ.mEdtRemarks.setEnabled(false);
            mNewCTQ.mRvSubmit.setClickable(false);
        } else {
            mNewCTQ.mEdtCtoValue.setClickable(true);
            mNewCTQ.mEdtRemarks.setClickable(true);
            mNewCTQ.mEdtCtoValue.setEnabled(true);
            mNewCTQ.mEdtRemarks.setEnabled(true);
            mNewCTQ.mRvSubmit.setClickable(true);
        }
    }

}
