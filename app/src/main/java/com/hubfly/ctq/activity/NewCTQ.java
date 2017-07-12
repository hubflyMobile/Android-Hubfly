package com.hubfly.ctq.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hubfly.ctq.Model.CtoModel;
import com.hubfly.ctq.R;
import com.hubfly.ctq.adapter.CtoAdapter;
import com.hubfly.ctq.adapter.QapAdapter;
import com.hubfly.ctq.adapter.QapImageAdapter;
import com.hubfly.ctq.util.MediaUtility;
import com.hubfly.ctq.util.RippleView;
import com.hubfly.ctq.util.Utility;

import java.util.ArrayList;

/**
 * Created by Admin on 03-07-2017.
 */

public class NewCTQ extends Activity {

    public RecyclerView mRvQapList, mRvCtoList;
    RippleView mRvCto, mRvQap, mRvImgNavigation, mRvChooseImage, mRvSubmit;
    Utility mUtility;
    public LinearLayout mLlQap, mLlCto, mLlCtqList, mllQapList, mLlCtoValue, mLlQapValue, mLlCtqClick, mLlQapClick;
    TextView mTxtCustName, mTxtPartNo, mTxtJobCode, mTxtCtqName, mTxtQapName;
    String Option = "", Name = "", Portname = "", CreatedBy = "", JobCode = "", CreatedDate = "", Ctq = "", Qap = "";
    ImageView mImgBack;
    int SELECT_PICTURE = 1, REQUEST_CAMERA = 3;
    ArrayList<String> mAlImageUri = new ArrayList<String>();
    Dialog mLoginDialog;
    LayoutInflater mLayoutInflater;
    View mPopupView;
    MediaUtility mMediaUtil;
    CtoAdapter mAdapter;
    QapAdapter mQapAdapter;
    ArrayList<CtoModel> mAlCto;
    ArrayList<CtoModel> mAlQap;
    TextView mTxtCtqCount, mTxtQapCount;
    GridView mGridImage;
    QapImageAdapter mQapImageAdapter;
    int itemposition = 0;
    EditText mEdtCtoValue, mEdtRemarks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ctq);

        Initialization();
        getBundleData();
        InitializeViews();
        SetAdapter();
        GetData();
        SetClickEvents();
    }

    void Initialization() {
        mUtility = new Utility(NewCTQ.this);
        mAlCto = new ArrayList<>();
        mAlQap = new ArrayList<>();
        mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMediaUtil = new MediaUtility();
    }

    void getBundleData() {
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null)
            if (mBundle.containsKey("Option") && !mBundle.getString("Option").equals("")) {
                Option = mBundle.getString("Option");
            }
        if (mBundle.containsKey("Title") && !mBundle.getString("Title").equals("")) {
            Name = mBundle.getString("Title");
        }
        if (mBundle.containsKey("Portname") && !mBundle.getString("Portname").equals("")) {
            Portname = mBundle.getString("Portname");
        }
        if (mBundle.containsKey("Jobcode") && !mBundle.getString("Jobcode").equals("")) {
            JobCode = mBundle.getString("Jobcode");
        }
        if (mBundle.containsKey("CreateBy") && !mBundle.getString("CreateBy").equals("")) {
            CreatedBy = mBundle.getString("CreateBy");
        }
        if (mBundle.containsKey("CreatedDate") && !mBundle.getString("CreatedDate").equals("")) {
            CreatedDate = mBundle.getString("CreatedDate");
        }
        if (mBundle.containsKey("Ctq") && !mBundle.getString("Ctq").equals("")) {
            Ctq = mBundle.getString("Ctq");
        }
        if (mBundle.containsKey("Qap") && !mBundle.getString("Qap").equals("")) {
            Qap = mBundle.getString("Qap");
        }
    }


    void InitializeViews() {
        mRvCto = (RippleView) findViewById(R.id.rv_ctq_list);
        mRvQap = (RippleView) findViewById(R.id.rv_qap_list);
        mRvImgNavigation = (RippleView) findViewById(R.id.rv_back);
        mRvChooseImage = (RippleView) findViewById(R.id.rv_choose);
        mRvSubmit = (RippleView) findViewById(R.id.rv_ctq_submit);

        mLlCto = (LinearLayout) findViewById(R.id.ll_ctq);
        mLlCtqList = (LinearLayout) findViewById(R.id.ll_open_ctq_list);
        mLlQap = (LinearLayout) findViewById(R.id.ll_qap);
        mllQapList = (LinearLayout) findViewById(R.id.ll_open_qap_list);
        mLlCtoValue = (LinearLayout) findViewById(R.id.ll_cto_value);
        mLlQapValue = (LinearLayout) findViewById(R.id.ll_qap_value);
        mLlCtqClick = (LinearLayout) findViewById(R.id.ll_ctq_click);
        mLlQapClick = (LinearLayout) findViewById(R.id.ll_qap_click);

        mRvQapList = mUtility.CustomRecycleView(NewCTQ.this, mLlCtqList);
        mRvCtoList = mUtility.CustomRecycleView(NewCTQ.this, mllQapList);

        mTxtCtqCount = (TextView) findViewById(R.id.txt_ctq_count);
        mTxtQapCount = (TextView) findViewById(R.id.txt_qap_count);
        mTxtCustName = (TextView) findViewById(R.id.txt_customer_name);
        mTxtPartNo = (TextView) findViewById(R.id.txt_part_no);
        mTxtJobCode = (TextView) findViewById(R.id.txt_job_code);

        mTxtQapName = (TextView) findViewById(R.id.txt_qap_name);
        mTxtCtqName = (TextView) findViewById(R.id.txt_ctq_name);

        mImgBack = (ImageView) findViewById(R.id.img_back);
        mGridImage = (GridView) findViewById(R.id.gv_images);

        mEdtCtoValue = (EditText) findViewById(R.id.edt_cto_no);
        mEdtRemarks = (EditText) findViewById(R.id.edt_remarks);

        mTxtCustName.setText(Name);
        mTxtPartNo.setText(Portname);
        mTxtJobCode.setText(JobCode);
        mTxtCtqCount.setText(Ctq);
        mTxtQapCount.setText(Qap);

        mLlCto.setVisibility(View.VISIBLE);
        mLlQap.setVisibility(View.GONE);
    }

    void SetAdapter() {
        mAdapter = new CtoAdapter(NewCTQ.this, mAlCto);
        mRvQapList.setAdapter(mAdapter);

        mQapAdapter = new QapAdapter(NewCTQ.this, mAlQap);
        mRvCtoList.setAdapter(mQapAdapter);

        mQapImageAdapter = new QapImageAdapter(NewCTQ.this, mAlImageUri);
        mGridImage.setAdapter(mQapImageAdapter);
    }

    void GetData() {
        mAlCto.add(new CtoModel("POUR MOLDS BEFORE 2 HRS AFTER CLOSING", false, 0, Option));
        mAlCto.add(new CtoModel("MOLD HARDNESS = 90 MINIMUM", false, 1, Option));
        mAlCto.add(new CtoModel("NO LOOSE SAND ON & AROUND CHILL", false, 2, Option));
        mAdapter.notifyDataSetChanged();

        mAlQap.add(new CtoModel("POUR MOLDS BEFORE 2 HRS AFTER CLOSING", false, 0, Option));
        mAlQap.add(new CtoModel("MOLD HARDNESS = 90 MINIMUM", false, 1, Option));
        mAlQap.add(new CtoModel("NO LOOSE SAND ON & AROUND CHILL", false, 2, Option));
        mQapAdapter.notifyDataSetChanged();
    }

    void SetClickEvents() {

        mRvCto.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                itemposition = 0;
                changeLayout(mLlCtqClick, mLlQapClick);
                changeTextColor(mTxtCtqName, mTxtQapName);
                mLlCto.setVisibility(View.VISIBLE);
                mLlQap.setVisibility(View.GONE);
            }
        });

        mRvQap.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                itemposition = 2;
                changeLayout(mLlQapClick, mLlCtqClick);
                changeTextColor(mTxtQapName, mTxtCtqName);
                mLlCto.setVisibility(View.GONE);
                mLlQap.setVisibility(View.VISIBLE);
            }
        });

        mRvImgNavigation.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                finish();
            }
        });

        mRvChooseImage.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                /*selectMedia(NewCTQ.this);*/
                mMediaUtil.startCameraActivity(NewCTQ.this);
//                Permission();
            }
        });

        mRvSubmit.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (mUtility.validation(NewCTQ.this, mEdtCtoValue, "CTQ")) {
                    if (mUtility.validation(NewCTQ.this, mEdtRemarks, "Remarks")) {
                        final ProgressDialog pDialog = new ProgressDialog(NewCTQ.this);
                        pDialog.setMessage("Loading");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(false);
                        pDialog.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mEdtRemarks.setText("");
                                mEdtCtoValue.setText("");
                                mLlCtoValue.setVisibility(View.GONE);
                                itemposition=0;
                                mAlCto.clear();
                                mAlQap.clear();

                                SetAdapter();
                                GetData();
                                pDialog.cancel();
                            }
                        }, 1000);
                    }
                }
            }
        });

    }

    public void changeLayout(LinearLayout view1, LinearLayout view2) {
        view1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_tab_active));
        view2.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void changeTextColor(TextView view1, TextView view2) {
        view1.setTextColor(getResources().getColor(R.color.list_active_tab));
        view2.setTextColor(getResources().getColor(R.color.list_normal_tab));
    }

    public void selectMedia(final Activity mActivity) {
        if (mLoginDialog == null) {
            mLoginDialog = mUtility.getDialog(mActivity);
        }
        mLoginDialog.setCancelable(true);
        mPopupView = mLayoutInflater.inflate(R.layout.app_popup_profile_picker, null, false);
        mPopupView.setBackgroundResource(R.drawable.app_dialog_bg);
        mLoginDialog.setContentView(mPopupView);
        mLoginDialog.show();
        mUtility.setDialogFullScreen(mActivity, mLoginDialog, -1);

        RippleView mRvCamera = (RippleView) mLoginDialog.findViewById(R.id.rv_camera);
        RippleView mRvGallery = (RippleView) mLoginDialog.findViewById(R.id.rv_gallery);
        RippleView mRvCancel = (RippleView) mLoginDialog.findViewById(R.id.rv_cancel);

        mRvCamera.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                mMediaUtil.startCameraActivity(mActivity);
                CloseDialog();
            }
        });

        mRvGallery.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                mMediaUtil.GalleryActivity(mActivity);
                CloseDialog();
            }
        });

        mRvCancel.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                CloseDialog();
            }
        });
    }

    public void CloseDialog() {
        if (mLoginDialog != null) {
            if (mLoginDialog.isShowing()) {
                mLoginDialog.dismiss();
            }
        }
        mLoginDialog = null;
        mPopupView = null;
    }



    public void ImageIntent(Intent data) {
        if (Build.VERSION.SDK_INT < 16) {
            if (data.getData() != null) {
                Uri mUri = data.getData();
                String MediaPath = mMediaUtil.UriToPath(NewCTQ.this, mUri);
                if (!MediaPath.equals("")) {
                    AddImage(MediaPath, "0", data);
                }
            }
        } else {
            if (data.getClipData() != null) {
                ClipData images = data.getClipData();
                for (int i = 0; i < images.getItemCount(); i++) {
                    Uri mUri = images.getItemAt(i).getUri();
                    String MediaPath = mMediaUtil.UriToPath(NewCTQ.this, mUri);
                    if (!MediaPath.equals("")) {
                        AddImage(MediaPath, "0", data);
                    }
                }

            } else if (data.getData() != null) {
                final Uri mUri = data.getData();
                String MediaPath = mMediaUtil.UriToPath(NewCTQ.this, mUri);
                if (!MediaPath.equals("")) {
                    AddImage(MediaPath, "0", data);
                }
            }
        }
    }

    public void AddImage(final String MediaPath, String option, Intent data) {
        final View mView = mLayoutInflater.inflate(R.layout.app_common_media, null, false);
        ImageView mImgView = (ImageView) mView.findViewById(R.id.img_thumb);
        ImageView mImgDelete = (ImageView) mView.findViewById(R.id.img_delete);
        ProgressBar mProgressBar = (ProgressBar) mView.findViewById(R.id.pb_loading);

        if (mMediaUtil.isFile(NewCTQ.this, MediaPath)) {
            if (data != null) {
                Uri selectedImage = data.getData();
                String filepath = mMediaUtil.getImageRealPath(getApplicationContext(), selectedImage);
            }

            mView.setTag(MediaPath);
            mImgDelete.setTag(MediaPath);

            mAlImageUri.add(MediaPath);

            mImgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(MediaPath, options);

            Glide.with(this).load("file://" + MediaPath.toString()).into(mImgView);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                if (data != null) {
                    ImageIntent(data);
                }
            } else if (requestCode == REQUEST_CAMERA) {
                ImageIntent(data);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
