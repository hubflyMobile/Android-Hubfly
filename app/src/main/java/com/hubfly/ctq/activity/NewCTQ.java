package com.hubfly.ctq.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Admin on 03-07-2017.
 */

public class NewCTQ extends Activity {

    public RecyclerView mRvQapList, mRvCtoList;
    public RippleView mRvCto, mRvQap, mRvImgNavigation, mRvChooseImage, mRvSubmit, mRvQapSubmit;
    Utility mUtility;
    public LinearLayout mLlCtqValue, mLlQap, mLlCto, mLlCtqList, mllQapList, mLlCtoValue, mLlQapValue, mLlCtqClick, mLlQapClick, mLlOpenQapCtqHeader;
    TextView mTxtCustName, mTxtPartNo, mTxtJobCode, mTxtCtqName, mTxtQapName, mTxtUserName;
    ImageView mImgBack, mImgProfile;
    int REQUEST_CAMERA = 3;
    public ArrayList<ImageModel> mAlImageUri;
    public ArrayList<ImageModel> mAlBase;
    Dialog mLoginDialog;
    LayoutInflater mLayoutInflater;
    View mPopupView;
    MediaUtility mMediaUtil;
    CtoAdapter mAdapter;
    QapAdapter mQapAdapter;
    public ArrayList<CtoModel> mAlCto,mAlQap;
    public TextView mTxtCtqCount, mTxtQapCount;
    GridView mGridImage;
    QapImageAdapter mQapImageAdapter;
    public EditText mEdtCtoValue, mEdtRemarks, mEdtRemarksQap;
    static String CaptureImageName = "";
    Uri mMediaCaptureUri;
    Handler mHandler = new Handler();
    private static final int REQUEST_CODE = 100;
    Runnable mTask;
    GlideUtil mGlideUtil;
    public ArrayList<ActivityModel> mAlActivityCTQ;
    public ArrayList<ActivityModel> mAlActivityQAP;


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

        mAlActivityCTQ.clear();
        mAlActivityQAP.clear();

        Gson mGson = new Gson();
        String mStrCtq = getIntent().getStringExtra("CTQ");
        String mStrQap = getIntent().getStringExtra("QAP");
        Type type = new TypeToken<ArrayList<ActivityModel>>() {}.getType();
        mAlActivityCTQ = mGson.fromJson(mStrCtq, type);
        mAlActivityQAP = mGson.fromJson(mStrQap, type);

        getCtqQapData();

        mTxtCtqCount.setText(getIntent().getStringExtra("CTQSTATUS"));
        mTxtQapCount.setText(getIntent().getStringExtra("QAPSTATUS"));

        mTxtCustName.setText(getIntent().getStringExtra("CUSTOMERNAME"));
        mTxtPartNo.setText(getIntent().getStringExtra("PARTNAME"));
        mTxtJobCode.setText(getIntent().getStringExtra("JOBCODE"));
    }


    void InitializeViews() {
        mRvCto = (RippleView) findViewById(R.id.rv_ctq_list);
        mRvQap = (RippleView) findViewById(R.id.rv_qap_list);
        mRvImgNavigation = (RippleView) findViewById(R.id.rv_back);
        mRvChooseImage = (RippleView) findViewById(R.id.rv_choose);
        mRvSubmit = (RippleView) findViewById(R.id.rv_ctq_submit);
        mRvQapSubmit = (RippleView) findViewById(R.id.rv_qap_submit);

        mLlOpenQapCtqHeader = (LinearLayout) findViewById(R.id.ll_root_qap_ctq);
        mLlCto = (LinearLayout) findViewById(R.id.ll_ctq);
        mLlCtqList = (LinearLayout) findViewById(R.id.ll_open_ctq_list);
        mLlQap = (LinearLayout) findViewById(R.id.ll_qap);
        mllQapList = (LinearLayout) findViewById(R.id.ll_open_qap_list);
        mLlCtoValue = (LinearLayout) findViewById(R.id.ll_cto_value);
        mLlQapValue = (LinearLayout) findViewById(R.id.ll_qap_value);
        mLlCtqClick = (LinearLayout) findViewById(R.id.ll_ctq_click);
        mLlQapClick = (LinearLayout) findViewById(R.id.ll_qap_click);
        mLlCtqValue = (LinearLayout) findViewById(R.id.ll_ctq_value);

        mRvCtoList = mUtility.CustomRecycleView(NewCTQ.this, mLlCtqList);
        mRvQapList = mUtility.CustomRecycleView(NewCTQ.this, mllQapList);

        mTxtCtqCount = (TextView) findViewById(R.id.txt_ctq_count);
        mTxtQapCount = (TextView) findViewById(R.id.txt_qap_count);
        mTxtCustName = (TextView) findViewById(R.id.txt_customer_name);
        mTxtPartNo = (TextView) findViewById(R.id.txt_part_no);
        mTxtJobCode = (TextView) findViewById(R.id.txt_job_code);

        mTxtQapName = (TextView) findViewById(R.id.txt_qap_name);
        mTxtCtqName = (TextView) findViewById(R.id.txt_ctq_name);


        mImgBack = (ImageView) findViewById(R.id.img_back);
        mImgProfile = (ImageView) findViewById(R.id.img_profile);
        mGridImage = (GridView) findViewById(R.id.gv_images);

        mEdtCtoValue = (EditText) findViewById(R.id.edt_cto_no);
        mEdtRemarks = (EditText) findViewById(R.id.edt_remarks);
        mEdtRemarksQap = (EditText) findViewById(R.id.edt_qap_remarks);
        mTxtUserName = (TextView) findViewById(R.id.txt_name);
        mTxtUserName.setText(Config.UserName);

        mLlCto.setVisibility(View.VISIBLE);
        mLlQap.setVisibility(View.GONE);


        mLlQapValue.setVisibility(View.GONE);
        mLlCtoValue.setVisibility(View.GONE);

        if (Config.PictureUrl != null && !Config.PictureUrl.equals("")) {
            mGlideUtil.LoadImages(mImgProfile, 1, Config.PictureUrl, true, 1.5f, Config.PictureUrl);
        }

    }


    void getCtqQapData() {
        if (mAlCto != null && mAlCto.size() > 0) {
            mAlCto.clear();
        }
        for (int i = 0; i < mAlActivityCTQ.size(); i++) {
            ActivityModel model = mAlActivityCTQ.get(i);

            CtoModel mCtoModel = new CtoModel();
            mCtoModel.setTaskName(model.getActivityNameHF());
            mCtoModel.setIndex(model.getID());
            mCtoModel.setRemarks(model.getRemarksHF());
            mCtoModel.setCTQValueHF(model.getCTQValueHF());
            mCtoModel.setCTQMinValueHF(model.getCTQMinValueHF());
            mCtoModel.setCTQMaxValueHF(model.getCTQMaxValueHF());
            mCtoModel.setQACJobIDHF(model.getQACJobIDHF());
            Boolean isVerified = model.getVerifiedHF() == null ? false : model.getVerifiedHF();
            mCtoModel.setChecked(false);
            mCtoModel.setVerifiedHF(isVerified);

            mAlCto.add(mCtoModel);
        }
        mAdapter.notifyDataSetChanged();

        if (mAlQap != null && mAlQap.size() > 0) {
            mAlQap.clear();
        }

        if (mAlActivityQAP != null && mAlActivityQAP.size() > 0) {
            for (int i = 0; i < mAlActivityQAP.size(); i++) {
                ActivityModel model = mAlActivityQAP.get(i);

                CtoModel mCtoModel = new CtoModel();
                mCtoModel.setTaskName(model.getActivityNameHF());
                mCtoModel.setIndex(model.getID());
                mCtoModel.setRemarks(model.getRemarksHF());
                mCtoModel.setmAlImage(model.getmAlImage());
                mCtoModel.setCTQMinValueHF(model.getCTQMinValueHF());
                mCtoModel.setCTQMaxValueHF(model.getCTQMaxValueHF());
                mCtoModel.setQACJobIDHF(model.getQACJobIDHF());
                Boolean isVerified = model.getVerifiedHF() == null ? false : model.getVerifiedHF();
                mCtoModel.setChecked(false);
                mCtoModel.setVerifiedHF(isVerified);

                mAlQap.add(mCtoModel);
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

    public void AddImage(final String MediaPath, String option, String filename,Boolean server) {
        if (option.equals("0")) {
            mAlImageUri.add(AddImageModelData(MediaPath, filename,server));
            mQapImageAdapter = new QapImageAdapter(NewCTQ.this, mAlImageUri, "0");
            mGridImage.setAdapter(mQapImageAdapter);
            mQapImageAdapter.notifyDataSetChanged();
        } else if (option.equals("1")) {
            mAlImageUri.add(AddImageModelData(MediaPath, filename,server));
            mQapImageAdapter = new QapImageAdapter(NewCTQ.this, mAlImageUri, "1");
            mGridImage.setAdapter(mQapImageAdapter);
            mQapImageAdapter.notifyDataSetChanged();
        } else {
            mAlImageUri.clear();
            mQapImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                File mFile = new File(Utility.UPLOAD_IMAGE_DIRECTORY + "/" + CaptureImageName);
                if (mFile.exists()) {
                    AddImage("file://" + mFile.getPath(), "0", CaptureImageName,false);
                    try {
                        Uri imageUri = Uri.fromFile(mFile);
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        mAlBase.add(AddImageModelData(encodeImage(selectedImage), CaptureImageName,false));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean isPermissionGranted(Activity mActivity, int option, Runnable run) {
        String permissions = "";
        String information = "";
        mTask = run;
        if (option == 2) {
            permissions = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            information = "" + "Folder Access";
        } else if (option == 3) {
            permissions = Manifest.permission.READ_EXTERNAL_STORAGE;
            information = "" + "Folder Access";
        }
        if (!permissions.equals("")) {
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    if (ActivityCompat.checkSelfPermission(mActivity, permissions) == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(mActivity, permissions, information);
                        return false;
                    } else if (ActivityCompat.checkSelfPermission(mActivity, permissions) == PackageManager.PERMISSION_GRANTED) {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
            }
        }
        return true;
    }

    private void requestPermissions(Activity mActivity, final String permissions, String information) {
        /**
         * Commented due to directly asking permission
         */
        if (Build.VERSION.SDK_INT >= 23) {
            if (shouldShowRequestPermissionRationale(permissions)) {
                try {
                    if (mLoginDialog == null) {
                        mLoginDialog = mUtility.getDialog(mActivity);
                    }
                    mPopupView = mLayoutInflater.inflate(R.layout.app_popup_password_changed, null, false);
                    mPopupView.setBackgroundResource(R.drawable.app_dialog_bg);
                    mLoginDialog.setContentView(mPopupView);
                    mLoginDialog.setCancelable(true);
                    mLoginDialog.show();
                    mUtility.setDialogFullScreen(mActivity, mLoginDialog, -1);

                    TextView mTxtHeading = (TextView) mPopupView.findViewById(R.id.txt_title);
                    TextView mTxtSubHeading = (TextView) mPopupView.findViewById(R.id.txt_message);
                    RippleView mRvCancel = (RippleView) mPopupView.findViewById(R.id.rv_ok);
                    mTxtHeading.setText("PERMISSION REQUIRED");
                    mTxtSubHeading.setText(information);

                    mRvCancel.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                        @Override
                        public void onComplete(RippleView rippleView) {
                            if (mLoginDialog != null && mLoginDialog.isShowing()) {
                                mLoginDialog.dismiss();
                            }
                            mLoginDialog = null;
                            if (Build.VERSION.SDK_INT >= 23) {
                                requestPermissions(new String[]{permissions}, REQUEST_CODE);
                            }
                        }
                    });
                } catch (Exception e) {
                    try {
                        if (Build.VERSION.SDK_INT >= 23) {
                            requestPermissions(new String[]{permissions}, REQUEST_CODE);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            } else {
                requestPermissions(new String[]{permissions}, REQUEST_CODE);
            }
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                if (mTask != null) {
                    mTask.run();
                }
            } else {
                Utility.logging("Required Permission");
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mIntent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(mIntent);
        finish();
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        String imageStr = Base64.encodeToString(byte_arr, Base64.DEFAULT);
        return imageStr;
    }

    ImageModel AddImageModelData(String filepath, String fileName,Boolean server) {
        ImageModel mImageModel = new ImageModel();
        mImageModel.setFileName(fileName);
        mImageModel.setBaseImage(filepath);
        mImageModel.setServer(server);
        return mImageModel;
    }

}
