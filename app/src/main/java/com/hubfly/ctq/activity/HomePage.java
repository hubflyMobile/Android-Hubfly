package com.hubfly.ctq.activity;

import android.graphics.Color;
import android.os.Bundle;
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
    }

    void getData() {
        drawerItem[0] = new NavigationModel("New QAC", R.drawable.ic_menu_add);
        drawerItem[1] = new NavigationModel("Open QAC", R.drawable.ic_menu_open);
        drawerItem[2] = new NavigationModel("Closed QAC", R.drawable.ic_menu_close);
    }

    void SetAdapter() {
        mAdapter = new NavigationAdapter(this, R.layout.adapter_navigation_item, drawerItem);
        mLvNavigation.setAdapter(mAdapter);
    }

    void Initialization() {
        drawerItem = new NavigationModel[3];
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
                for (int i=0;i<=drawerItem.length;i++){
                    if(position==i){
                        mLvNavigation.getChildAt(i).setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_tab_active));
                    }else{
                        mLvNavigation.getChildAt(i).setBackgroundColor(Color.WHITE);
                    }
                }
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
        }
        if (fragment != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            drawerLayout.closeDrawer(mLlDrawerList);
        }
    }

    @Override
    public void onBackPressed() {
        displayView(2);
    }

}
