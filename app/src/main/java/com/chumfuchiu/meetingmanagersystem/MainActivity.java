package com.chumfuchiu.meetingmanagersystem;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.chumfuchiu.meetingmanagersystem.bean.UserInfo;

import cn.bmob.v3.BmobUser;

/*
* 程序主页面。
* */
public class MainActivity extends AppCompatActivity {
    Toolbar mToolbar;
    LinearLayout linearMenu;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        //判断当前用户类型
        UserInfo userInfo= BmobUser.getCurrentUser(UserInfo.class);
        if(userInfo!=null){
            Log.e("MainActivity",userInfo.getUsername()+"==="+userInfo.getMobilePhoneNumber()+"==="+userInfo.getPermission());
        }else {
            Log.e("MainActivity","userInfoIsNull");
        }
        initViews();
        initToolBarAndDrawableLayout();
    }
    private void initViews(){
        mToolbar= (Toolbar) findViewById(R.id.main_toolbar);
        linearMenu= (LinearLayout) findViewById(R.id.llMenu);
        drawerLayout= (DrawerLayout) findViewById(R.id.dl_Menu);
    }
    private void initToolBarAndDrawableLayout(){
        setSupportActionBar(mToolbar);
//      决定toolbar左侧的图标是否可以点击
        getSupportActionBar().setHomeButtonEnabled(true);
//      给左上角图标设置一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//      显示标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToggle = new ActionBarDrawerToggle(this,drawerLayout,mToolbar, R.string.toggle_open, R.string.toggle_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
    }
}
