package com.hubfly.ctq.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hubfly.ctq.Model.NavigationModel;
import com.hubfly.ctq.R;
import com.hubfly.ctq.adapter.NavigationAdapter;
import com.hubfly.ctq.fragement.CloseCtQ;
import com.hubfly.ctq.fragement.NewCtQ;
import com.hubfly.ctq.fragement.OpenCtQ;
import com.hubfly.ctq.util.SessionManager;

/**
 * Created by Admin on 04-07-2017.
 */

public class HomePage extends FragmentActivity {

    //View Elements
    DrawerLayout drawerLayout;
    LinearLayout mLlDrawerList;
    ListView mLvNavigation;
    NavigationAdapter mAdapter;
    NavigationModel[] drawerItem;
    SessionManager mSessionManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

//        Thread.setDefaultUncaughtExceptionHandler(new TopExceptionHandler(this));

        Initialization();
        InitializationViews();
        SetAdapter();
        getData();
        SetClickEvents();

        if(savedInstanceState==null){
                displayView(2);
        }
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null && mBundle.containsKey("Details")) {
            if (mBundle.getBoolean("Details")) {
                displayView(2);
            } else {
                displayView(3);
            }
        }
    }

    void getData() {
        this.drawerItem[0] = new NavigationModel("New QAC", R.drawable.ic_menu_add);
        this.drawerItem[1] = new NavigationModel("Open QAC", R.drawable.ic_menu_open);
        this.drawerItem[2] = new NavigationModel("Closed QAC", R.drawable.ic_menu_close);
        this.drawerItem[3] = new NavigationModel("Sign out", R.drawable.ic_menu_signout);
    }

    void SetAdapter() {
        mAdapter = new NavigationAdapter(this, R.layout.adapter_navigation_item, drawerItem);
        mLvNavigation.setAdapter(mAdapter);
    }

    void Initialization() {
        this.mSessionManager = new SessionManager(this);
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("Loading...");
        drawerItem = new NavigationModel[4];
    }

    void InitializationViews() {
        mLlDrawerList = (LinearLayout) findViewById(R.id.testing);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLvNavigation = (ListView) findViewById(R.id.lv_navigation);

        View header = getLayoutInflater().inflate(R.layout.app_common_navigation_header, null);
        mLvNavigation.addHeaderView(header);

    }

    void SetClickEvents() {
        mLvNavigation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              /*  for (int i=0;i<=drawerItem.length;i++){
                    if(position==i){
                        mLvNavigation.getChildAt(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_tab_active));
                    }else{
                        mLvNavigation.getChildAt(i).setBackgroundColor(Color.WHITE);
                    }
                }*/
                displayView(position);
            }
        });
    }

    public void displayView(int position) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 1:
                fragment = new NewCtQ();
                break; case 2:
                fragment = new OpenCtQ();
                break;
            case 3:
                fragment = new CloseCtQ();
                break;
            case 4:
                Logout();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            drawerLayout.closeDrawer(mLlDrawerList);
        }
    }

    public void onBackPressed() {
        finish();
    }

    public void Logout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Sign out");
        alertDialogBuilder.setMessage("Are you sure want to Sign out?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HomePage.this.mSessionManager.ClearUser();
                HomePage.this.mSessionManager.ClearSharepointId();
                HomePage.this.progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (HomePage.this.progressDialog != null && HomePage.this.progressDialog.isShowing()) {
                            HomePage.this.progressDialog.dismiss();
                            HomePage.this.progressDialog = null;
                            Intent mIntent = new Intent(HomePage.this, Splash.class);
                            HomePage.this.startActivity(mIntent);
                            HomePage.this.finish();
                        }
                    }
                }, 3000);
                dialogInterface.cancel();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialogBuilder.create().show();
    }
}
