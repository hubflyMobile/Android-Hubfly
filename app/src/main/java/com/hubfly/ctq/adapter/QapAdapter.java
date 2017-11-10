package com.hubfly.ctq.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hubfly.ctq.Model.CtoModel;
import com.hubfly.ctq.Model.ImageModel;
import com.hubfly.ctq.R;
import com.hubfly.ctq.activity.NewCTQ;
import com.hubfly.ctq.util.Config;
import com.hubfly.ctq.util.HttpApi;
import com.hubfly.ctq.util.OnResponseCallback;
import com.hubfly.ctq.util.RippleView;
import com.hubfly.ctq.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    Utility mUtility;
    private RecyclerView mRvCtq;
    boolean isClicked = false;


    public QapAdapter(Activity mActivity, ArrayList<CtoModel> mAlCto, RecyclerView view) {
        this.mActivity = mActivity;
        this.mAlCto = mAlCto;
        this.mRvCtq = view;
        mNewCTQ = (NewCTQ) mActivity;
        mUtility = new Utility(mActivity);
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView mTxtTaskName;
        LinearLayout mLlRoot;
        RadioButton mRbChecked;

        public DataObjectHolder(View itemView) {
            super(itemView);
            mTxtTaskName = (TextView) itemView.findViewById(R.id.txt_task_name);
            mLlRoot = (LinearLayout) itemView.findViewById(R.id.ll_root_qap);
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
        holder.mLlRoot.setTag("LL" + position);
        holder.mRbChecked.setTag("RB" + position);
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
                if (!isClicked) {
                    isClicked = true;
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
                        mNewCTQ.mEdtRemarksQap.setText("");
                        mNewCTQ.AddImage(mAlCto.get(clickedPos).getImagePath(), "2", mAlCto.get(clickedPos).getImagePath(), true);
                        hideshow(true);
                    } else {
                        if (mAlCto.get(clickedPos).getRemarks() != null && !mAlCto.get(clickedPos).getRemarks().equals("")&& !mAlCto.get(clickedPos).getRemarks().equals("null")) {
                            mNewCTQ.mEdtRemarksQap.setText(mAlCto.get(clickedPos).getRemarks());
                        } else {
                            mNewCTQ.mEdtRemarksQap.setText("");
                        }
                        hideshow(false);
                    }

                    if (mCtoModel.getmAlImage().size() > 0) {
                        mNewCTQ.mAlImageUri.clear();
                        for (ImageModel imageModel : mCtoModel.getmAlImage()) {
                            if (imageModel.getBaseImage().startsWith("https:")) {
                                mNewCTQ.AddImage(imageModel.getBaseImage(), "1", imageModel.getBaseImage(), true);
                            } else {
                                mNewCTQ.AddImage(imageModel.getBaseImage(), "0", imageModel.getBaseImage(), true);
                            }
                        }
                    }
                    ChangeClickedStatus();
                }
            }
        });


        mNewCTQ.mRvQapSubmit.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (!isClicked) {
                    isClicked = true;
                    if (mNewCTQ.mAlBase.size() > 0) {
                        if (Utility.isInternetConnected(mActivity)) {
                            GenerateNewCtq(mActivity, mAlCto.get(lastCheckedPos).getTaskName(), mAlCto.get(lastCheckedPos).getIndex(), mNewCTQ.mEdtRemarksQap.getText().toString(), mNewCTQ.mAlImageUri, lastCheckedPos,mAlCto.get(lastCheckedPos).getCTQMinValueHF(),mAlCto.get(lastCheckedPos).getCTQMaxValueHF(),mAlCto.get(lastCheckedPos).getQACJobIDHF());
                        }
                    } else {
                        mUtility.showToast(mActivity, "Please select Images", "0");
                    }
                    ChangeClickedStatus();
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

    public void ChangeClickedStatus() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isClicked = false;
            }
        }, 300);
    }

    void GenerateNewCtq(final Activity mActivity, final String mStrTaskName, final Integer ID, final String remarks, final ArrayList<ImageModel> mAlImage, final Integer Position,final double minValue,final double maxValue,final int QacJobId) {
        try {
            JSONObject mJsonInput = new JSONObject();
            try {
                mJsonInput.putOpt("ID", ID);
                mJsonInput.putOpt("RemarksHF", remarks);
                mJsonInput.putOpt("CTQMaxValueHF",maxValue);
                mJsonInput.putOpt("CTQMinValueHF",minValue);
                mJsonInput.putOpt("QACJobIDHF",QacJobId);
                JSONArray mJsonArray = new JSONArray();
                JSONObject mImgJson;
                for (int i = 0; i < mNewCTQ.mAlBase.size(); i++) {
                    mImgJson = new JSONObject();
                    mImgJson.putOpt("Base64String", mNewCTQ.mAlBase.get(i).getBaseImage());
                    mImgJson.putOpt("FileName", mNewCTQ.mAlBase.get(i).getFileName());
                    mImgJson.putOpt("FileNew", true);
                    mImgJson.putOpt("FileExists", false);
                    mImgJson.putOpt("FileDelete", false);
                    mImgJson.putOpt("FileTouched", true);
                    mJsonArray.put(mImgJson);
                }
                mJsonInput.put("AttachmentFiles", mJsonArray);
                JSONObject mJsonObject = mUtility.SendParams(mActivity, "0", null, ID);
                mJsonObject.put("JsonInput", mJsonInput.toString());

                HttpApi api = new HttpApi(mActivity, true, new OnResponseCallback() {
                    @Override
                    public void responseCallBack(Activity activity, String responseString) {
                        if (responseString != null && responseString.equals("200")) {
                            mUtility.showToast(mActivity, "QAP Updated Successfully", "0");
                            String value = mNewCTQ.mTxtQapCount.getText().toString();
                            String ctq[] = value.split("/");
                            mNewCTQ.mTxtQapCount.setText(Integer.parseInt(ctq[0]) + 1 + "/" + ctq[1]);

                            CtoModel mCtoModel = new CtoModel();
                            mCtoModel.setTaskName(mStrTaskName);
                            mCtoModel.setVerifiedHF(true);
                            mCtoModel.setIndex(ID);
                            mCtoModel.setRemarks(remarks);
                            mCtoModel.setCTQMaxValueHF(maxValue);
                            mCtoModel.setCTQMinValueHF(minValue);
                            if (mNewCTQ.mAlImageUri.size() > 0) {
                                ArrayList<ImageModel> mAlImage = new ArrayList<>();
                                for (ImageModel imageModel : mNewCTQ.mAlImageUri) {
                                    ImageModel mImageModel1 = new ImageModel();
                                    mImageModel1.setBaseImage(imageModel.getBaseImage());
                                    mImageModel1.setFileName(imageModel.getFileName());
                                    mImageModel1.setServer(true);
                                    mAlImage.add(mImageModel1);
                                }
                                mCtoModel.setmAlImage(mAlImage);
                            }
                            mCtoModel.setChecked(true);
                            mNewCTQ.mAlQap.set(Position, mCtoModel);
                            hideshow(false);
                            notifyDataSetChanged();
                        } else {
                            mUtility.showToast(mActivity, "Server Error", "0");
                        }
                    }
                }, Config.Baseurl + Config.UpdateHeatActivityForQAP, "POST", mJsonObject);
                api.execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideshow(Boolean option) {
        mNewCTQ.mLlCtoValue.setVisibility(View.GONE);
        mNewCTQ.mLlQapValue.setVisibility(View.VISIBLE);
        if (!option) {
            mNewCTQ.mEdtRemarksQap.setClickable(false);
            mNewCTQ.mEdtRemarksQap.setEnabled(false);
            mNewCTQ.mRvChooseImage.setClickable(false);
            mNewCTQ.mRvQapSubmit.setClickable(false);
        } else {
            mNewCTQ.mEdtRemarksQap.setClickable(true);
            mNewCTQ.mEdtRemarksQap.setEnabled(true);
            mNewCTQ.mRvChooseImage.setClickable(true);
            mNewCTQ.mRvQapSubmit.setClickable(true);
        }
    }

}
