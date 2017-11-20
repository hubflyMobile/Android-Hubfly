package com.hubfly.ctq.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hubfly.ctq.Model.ActivityModel;
import com.hubfly.ctq.Model.CtoModel;
import com.hubfly.ctq.Model.ImageModel;
import com.hubfly.ctq.R;
import com.hubfly.ctq.adapter.CtoAdapter;
import com.hubfly.ctq.adapter.QapAdapter;
import com.hubfly.ctq.adapter.QapImageAdapter;
import com.hubfly.ctq.util.Config;
import com.hubfly.ctq.util.GlideUtil;
import com.hubfly.ctq.util.MediaUtility;
import com.hubfly.ctq.util.PermissionUtil;
import com.hubfly.ctq.util.RippleView;
import com.hubfly.ctq.util.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Admin on 03-07-2017.
 */

public class NewCTQ extends Activity {

    static String CaptureImageName = "";
    private static final int REQUEST_CODE = 100;
    int REQUEST_CAMERA = 3;
    CtoAdapter mAdapter;
    public ArrayList<ActivityModel> mAlActivityCTQ;
    public ArrayList<ActivityModel> mAlActivityQAP;
    public ArrayList<ImageModel> mAlBase;
    public ArrayList<CtoModel> mAlCto;
    public ArrayList<ImageModel> mAlImageUri;
    public ArrayList<CtoModel> mAlQap;
    public EditText mEdtCtoValue;
    public EditText mEdtRemarks;
    public EditText mEdtRemarksQap;
    GlideUtil mGlideUtil;
    GridView mGridImage;
    Handler mHandler = new Handler();
    ImageView mImgBack;
    ImageView mImgProfile;
    LayoutInflater mLayoutInflater;
    public LinearLayout mLlCto;
    public LinearLayout mLlCtoValue;
    public LinearLayout mLlCtqClick;
    public LinearLayout mLlCtqList;
    public LinearLayout mLlCtqValue;
    LinearLayout mLlFurnace;
    public LinearLayout mLlOpenQapCtqHeader;
    public LinearLayout mLlQap;
    public LinearLayout mLlQapClick;
    public LinearLayout mLlQapValue;
    Dialog mLoginDialog;
    Uri mMediaCaptureUri;
    MediaUtility mMediaUtil;
    View mPopupView;
    QapAdapter mQapAdapter;
    QapImageAdapter mQapImageAdapter;
    public RippleView mRvChooseImage;
    public RippleView mRvCto;
    public RecyclerView mRvCtoList;
    public RippleView mRvImgNavigation;
    public RippleView mRvQap;
    public RecyclerView mRvQapList;
    public RippleView mRvQapSubmit;
    public RippleView mRvSubmit;
    Runnable mTask;
    public TextView mTxtCtqCount;
    TextView mTxtCtqName;
    TextView mTxtFurnace;
    TextView mTxtGroupCode;
    TextView mTxtJobCode;
    TextView mTxtPartNo;
    public TextView mTxtQapCount;
    TextView mTxtQapName;
    TextView mTxtUserName;
    Utility mUtility;
    public LinearLayout mllQapList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ctq);

        Initialization();
        InitializeViews();
        SetAdapter();
        getBundleData();
        SetClickEvents();
    }

    void Initialization() {
        mUtility = new Utility(NewCTQ.this);
        mGlideUtil = new GlideUtil(NewCTQ.this);
        mAlCto = new ArrayList<>();
        mAlQap = new ArrayList<>();
        mAlBase = new ArrayList<>();
        mAlActivityCTQ = new ArrayList<>();
        mAlActivityQAP = new ArrayList<>();
        mAlImageUri = new ArrayList<>();
        mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMediaUtil = new MediaUtility();
    }

    void getBundleData() {


        this.mAlActivityCTQ.clear();
        this.mAlActivityQAP.clear();
        Gson mGson = new Gson();
        String mStrCtq = getIntent().getStringExtra("CTQ");
        String mStrQap = getIntent().getStringExtra("QAP");
        Type type = new TypeToken<ArrayList<ActivityModel>>() {
        }.getType();
        this.mAlActivityCTQ = (ArrayList) mGson.fromJson(mStrCtq, type);
        this.mAlActivityQAP = (ArrayList) mGson.fromJson(mStrQap, type);
        getCtqQapData();
        this.mTxtCtqCount.setText(getIntent().getStringExtra("CTQSTATUS"));
        this.mTxtQapCount.setText(getIntent().getStringExtra("QAPSTATUS"));
        this.mTxtGroupCode.setText(getIntent().getStringExtra("GROUPCODE"));
        this.mTxtPartNo.setText(getIntent().getStringExtra("PARTNAME"));
        this.mTxtJobCode.setText(getIntent().getStringExtra("JOBCODE"));
        if (getIntent().getStringExtra("FURNACE") == null || getIntent().getStringExtra("FURNACE").equals("") || !getIntent().getStringExtra("FURNACE").toLowerCase().equals(Config.MELTING)) {
            this.mLlFurnace.setVisibility(View.INVISIBLE);
            return;
        }
        this.mLlFurnace.setVisibility(View.VISIBLE);
        this.mTxtFurnace.setText(getIntent().getStringExtra("FURNACE"));


    }


    void InitializeViews() {
        this.mRvCto = (RippleView) findViewById(R.id.rv_ctq_list);
        this.mRvQap = (RippleView) findViewById(R.id.rv_qap_list);
        this.mRvImgNavigation = (RippleView) findViewById(R.id.rv_back);
        this.mRvChooseImage = (RippleView) findViewById(R.id.rv_choose);
        this.mRvSubmit = (RippleView) findViewById(R.id.rv_ctq_submit);
        this.mRvQapSubmit = (RippleView) findViewById(R.id.rv_qap_submit);
        this.mLlOpenQapCtqHeader = (LinearLayout) findViewById(R.id.ll_root_qap_ctq);
        this.mLlCto = (LinearLayout) findViewById(R.id.ll_ctq);
        this.mLlCtqList = (LinearLayout) findViewById(R.id.ll_open_ctq_list);
        this.mLlQap = (LinearLayout) findViewById(R.id.ll_qap);
        this.mllQapList = (LinearLayout) findViewById(R.id.ll_open_qap_list);
        this.mLlCtoValue = (LinearLayout) findViewById(R.id.ll_cto_value);
        this.mLlQapValue = (LinearLayout) findViewById(R.id.ll_qap_value);
        this.mLlCtqClick = (LinearLayout) findViewById(R.id.ll_ctq_click);
        this.mLlQapClick = (LinearLayout) findViewById(R.id.ll_qap_click);
        this.mLlCtqValue = (LinearLayout) findViewById(R.id.ll_ctq_value);
        this.mLlFurnace = (LinearLayout) findViewById(R.id.ll_furnace);
        this.mRvCtoList = this.mUtility.CustomRecycleView(this, this.mLlCtqList);
        this.mRvQapList = this.mUtility.CustomRecycleView(this, this.mllQapList);
        this.mTxtCtqCount = (TextView) findViewById(R.id.txt_ctq_count);
        this.mTxtQapCount = (TextView) findViewById(R.id.txt_qap_count);
        this.mTxtGroupCode = (TextView) findViewById(R.id.txt_customer_name);
        this.mTxtPartNo = (TextView) findViewById(R.id.txt_part_no);
        this.mTxtJobCode = (TextView) findViewById(R.id.txt_job_code);
        this.mTxtFurnace = (TextView) findViewById(R.id.txt_furnace);
        this.mTxtQapName = (TextView) findViewById(R.id.txt_qap_name);
        this.mTxtCtqName = (TextView) findViewById(R.id.txt_ctq_name);
        this.mTxtUserName = (TextView) findViewById(R.id.txt_name);
        this.mImgBack = (ImageView) findViewById(R.id.img_back);
        this.mImgProfile = (ImageView) findViewById(R.id.img_profile);
        this.mGridImage = (GridView) findViewById(R.id.gv_images);
        this.mEdtCtoValue = (EditText) findViewById(R.id.edt_cto_no);
        this.mEdtRemarks = (EditText) findViewById(R.id.edt_remarks);
        this.mEdtRemarksQap = (EditText) findViewById(R.id.edt_qap_remarks);
        this.mLlCto.setVisibility(View.VISIBLE);
        this.mLlQap.setVisibility(View.GONE);
        this.mLlQapValue.setVisibility(View.GONE);
        this.mLlCtoValue.setVisibility(View.GONE);
        this.mTxtUserName.setText(Config.UserName);
        if (Config.Email != null && !Config.Email.equals("")) {
            String Image = Config.UserProfilePhoto + Config.Email;
            this.mGlideUtil.LoadImages(this.mImgProfile, Integer.valueOf(1), Image, true, Float.valueOf(1.75f), Image);
        }

        if (Config.Department.toLowerCase().equals(Config.PATTERN_SHOP)) {
            this.mTxtGroupCode.setVisibility(View.GONE);
        } else {
            this.mTxtGroupCode.setVisibility(View.VISIBLE);
        }
    }


    void getCtqQapData() {
        if (mAlCto != null && mAlCto.size() > 0) {
            mAlCto.clear();
        }
        for (int i = 0; i < mAlActivityCTQ.size(); i++) {
            ActivityModel model = (ActivityModel) this.mAlActivityCTQ.get(i);
            CtoModel mCtoModel = new CtoModel();
            mCtoModel.setTaskName(model.getActivityNameHF());
            mCtoModel.setIndex(Integer.valueOf(model.getID()));
            mCtoModel.setRemarks(model.getRemarksHF());
            mCtoModel.setCTQValueHF(model.getCTQValueHF());
            mCtoModel.setQACJobIDHF(model.getQACJobIDHF());
            mCtoModel.setCTQMaxValueHF(model.getCTQMaxValueHF());
            mCtoModel.setCTQMinValueHF(model.getCTQMinValueHF());
            Boolean isVerified = Boolean.valueOf(model.getVerifiedHF() == null ? false : model.getVerifiedHF().booleanValue());
            mCtoModel.setChecked(Boolean.valueOf(false));
            mCtoModel.setVerifiedHF(isVerified);
            this.mAlCto.add(mCtoModel);
        }
        mAdapter.notifyDataSetChanged();

        if (mAlQap != null && mAlQap.size() > 0) {
            mAlQap.clear();
        }

        if (mAlActivityQAP != null && mAlActivityQAP.size() > 0) {
            for (int i = 0; i < mAlActivityQAP.size(); i++) {
                ActivityModel model = (ActivityModel) this.mAlActivityQAP.get(i);
                CtoModel mCtoModel = new CtoModel();
                mCtoModel.setTaskName(model.getActivityNameHF());
                mCtoModel.setIndex(Integer.valueOf(model.getID()));
                mCtoModel.setRemarks(model.getRemarksHF());
                mCtoModel.setmAlImage(model.getmAlImage());
                mCtoModel.setQACJobIDHF(model.getQACJobIDHF());
                Boolean isVerified = Boolean.valueOf(model.getVerifiedHF() == null ? false : model.getVerifiedHF().booleanValue());
                mCtoModel.setChecked(Boolean.valueOf(false));
                mCtoModel.setVerifiedHF(isVerified);
                this.mAlQap.add(mCtoModel);
            }
            mQapAdapter.notifyDataSetChanged();
        }
    }


    void SetAdapter() {
        mAdapter = new CtoAdapter(NewCTQ.this, mAlCto, mRvCtoList);
        mRvCtoList.setAdapter(mAdapter);

        mQapAdapter = new QapAdapter(NewCTQ.this, mAlQap, mRvQapList);
        mRvQapList.setAdapter(mQapAdapter);

        mQapImageAdapter = new QapImageAdapter(NewCTQ.this, mAlImageUri, "0");
        mGridImage.setAdapter(mQapImageAdapter);
    }


    void SetClickEvents() {

        mRvCto.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                changeLayout(mLlCtqClick, mLlQapClick);
                changeTextColor(mTxtCtqName, mTxtQapName);
                mLlCto.setVisibility(View.VISIBLE);
                mLlQap.setVisibility(View.GONE);
                mLlQapValue.setVisibility(View.GONE);
                mLlCtoValue.setVisibility(View.GONE);
                mLlCtqValue.setBackgroundColor(getResources().getColor(R.color.white));
                mAlBase.clear();
            }
        });

        mRvQap.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                changeLayout(mLlQapClick, mLlCtqClick);
                changeTextColor(mTxtQapName, mTxtCtqName);
                mLlCto.setVisibility(View.GONE);
                mLlQap.setVisibility(View.VISIBLE);
                mLlQapValue.setVisibility(View.GONE);
                mLlCtoValue.setVisibility(View.GONE);
                mLlCtqValue.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });

        mRvImgNavigation.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent mIntent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(mIntent);
                finish();
            }
        });

        mRvChooseImage.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (isPermissionGranted(NewCTQ.this, 2, mMoveActivity) && isPermissionGranted(NewCTQ.this, 3, mMoveActivity)) {
                        Utility.PrepareFolder();
                        mHandler.postDelayed(mRunnable, 500);
                    }
                } else {
                    Utility.PrepareFolder();
                    CaptureImageName = "CTQ" + System.currentTimeMillis() + ".png";
                    mMediaCaptureUri = Uri.fromFile(new File(Utility.UPLOAD_IMAGE_DIRECTORY, CaptureImageName));
                    mMediaUtil.startCameraActivity(NewCTQ.this, mMediaCaptureUri);
                }

            }
        });

    }

    Runnable mMoveActivity = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(mRunnable, 500);
        }
    };

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(mRunnable);
            CaptureImageName = "CTQ" + System.currentTimeMillis() + ".png";
            mMediaCaptureUri = Uri.fromFile(new File(Utility.UPLOAD_IMAGE_DIRECTORY, CaptureImageName));
            mMediaUtil.startCameraActivity(NewCTQ.this, mMediaCaptureUri);
        }
    };

    public void changeLayout(LinearLayout view1, LinearLayout view2) {
        view1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_tab_active));
        view2.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void changeTextColor(TextView view1, TextView view2) {
        view1.setTextColor(getResources().getColor(R.color.list_active_tab));
        view2.setTextColor(getResources().getColor(R.color.list_normal_tab));
    }

    public void AddImage(String MediaPath, String option, String filename, Boolean server) {
        if (option.equals("0")) {
            this.mAlImageUri.add(AddImageModelData(MediaPath, filename, server));
            this.mQapImageAdapter = new QapImageAdapter(this, this.mAlImageUri, "0");
            this.mGridImage.setAdapter(this.mQapImageAdapter);
            this.mQapImageAdapter.notifyDataSetChanged();
        } else if (option.equals("1")) {
            this.mAlImageUri.add(AddImageModelData(MediaPath, filename, server));
            this.mQapImageAdapter = new QapImageAdapter(this, this.mAlImageUri, "1");
            this.mGridImage.setAdapter(this.mQapImageAdapter);
            this.mQapImageAdapter.notifyDataSetChanged();
        } else {
            this.mAlImageUri.clear();
            this.mQapImageAdapter.notifyDataSetChanged();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && requestCode == this.REQUEST_CAMERA) {
            File mFile = new File(Utility.UPLOAD_IMAGE_DIRECTORY + "/" + CaptureImageName);
            if (mFile.exists()) {
                AddImage("file://" + mFile.getPath(), "0", CaptureImageName, Boolean.valueOf(false));
                try {
                    this.mAlBase.add(AddImageModelData(encodeImage(BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(mFile)))), CaptureImageName, Boolean.valueOf(false)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public boolean isPermissionGranted(Activity mActivity, int option, Runnable run) {
        String permissions = "";
        String information = "";
        this.mTask = run;
        if (option == 2) {
            permissions = "android.permission.WRITE_EXTERNAL_STORAGE";
            information = "Folder Access";
        } else if (option == 3) {
            permissions = "android.permission.READ_EXTERNAL_STORAGE";
            information = "Folder Access";
        }
        if (permissions.equals("") || Build.VERSION.SDK_INT < 23) {
            return true;
        }
        try {
            if (ContextCompat.checkSelfPermission(mActivity, permissions) != -1) {
                return ContextCompat.checkSelfPermission(mActivity, permissions) == 0 ? true : true;
            } else {
                requestPermissions(mActivity, permissions, information);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    private void requestPermissions(Activity mActivity, final String permissions, String information) {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (shouldShowRequestPermissionRationale(permissions)) {
            try {
                if (this.mLoginDialog == null) {
                    this.mLoginDialog = this.mUtility.getDialog(mActivity);
                }
                this.mPopupView = this.mLayoutInflater.inflate(R.layout.app_popup_password_changed, null, false);
                this.mPopupView.setBackgroundResource(R.drawable.app_dialog_bg);
                this.mLoginDialog.setContentView(this.mPopupView);
                this.mLoginDialog.setCancelable(true);
                this.mLoginDialog.show();
                this.mUtility.setDialogFullScreen(mActivity, this.mLoginDialog, -1);
                TextView mTxtSubHeading = (TextView) this.mPopupView.findViewById(R.id.txt_message);
                RippleView mRvCancel = (RippleView) this.mPopupView.findViewById(R.id.rv_ok);
                ((TextView) this.mPopupView.findViewById(R.id.txt_title)).setText("PERMISSION REQUIRED");
                mTxtSubHeading.setText(information);
                mRvCancel.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    public void onComplete(RippleView rippleView) {
                        if (NewCTQ.this.mLoginDialog != null && NewCTQ.this.mLoginDialog.isShowing()) {
                            NewCTQ.this.mLoginDialog.dismiss();
                        }
                        NewCTQ.this.mLoginDialog = null;
                        if (Build.VERSION.SDK_INT >= 23) {
                            NewCTQ.this.requestPermissions(new String[]{permissions}, 100);
                        }
                    }
                });
                return;
            } catch (Exception e) {
                try {
                    if (Build.VERSION.SDK_INT >= 23) {
                        requestPermissions(new String[]{permissions}, 100);
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
                return;
            }
        }
        requestPermissions(new String[]{permissions}, 100);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 100) {
            return;
        }
        if (!PermissionUtil.verifyPermissions(grantResults)) {
            Utility.logging("Required Permission");
        } else if (this.mTask != null) {
            this.mTask.run();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent(getApplicationContext(), HomePage.class);
        mIntent.putExtra("Details", true);
        startActivity(mIntent);
        finish();
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return Base64.encodeToString(stream.toByteArray(), 0);
    }

    ImageModel AddImageModelData(String filepath, String fileName, Boolean server) {
        ImageModel mImageModel = new ImageModel();
        mImageModel.setFileName(fileName);
        mImageModel.setBaseImage(filepath);
        mImageModel.setServer(server);
        return mImageModel;
    }

}
