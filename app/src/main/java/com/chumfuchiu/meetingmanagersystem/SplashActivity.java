package com.chumfuchiu.meetingmanagersystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import cn.bmob.v3.Bmob;
/*
* 应用欢迎页面
* */
public class SplashActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private AnimationSet animationSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_splash);
        //初始化bmobSDK
        Bmob.initialize(this,"de3f9cdf2a992d3e894dbb11e99bbedf");
        //判断当前手机SDK版本,如果大于1，就将本页面设置为状态栏设置为沉浸式状态栏模式。
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //初始化界面
        initView();
    }
    private void initView(){
        //获取跟根布局组件对象
        relativeLayout= (RelativeLayout) findViewById(R.id.splash_rl_content);
        //设置动画集
        animationSet =new AnimationSet(false);
        //旋转动画
        RotateAnimation rotateAnimation=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(3000);
        rotateAnimation.setFillAfter(true);
        animationSet.addAnimation(rotateAnimation);
        //透明度动画
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1.0f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(3000);
        animationSet.addAnimation(alphaAnimation);
        //缩放动画
        ScaleAnimation scaleAnimation=new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(3000);
        animationSet.addAnimation(scaleAnimation);
        //为动画集设置动画监听器
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                enterLoginAct();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //为跟布局设置动画集
        relativeLayout.setAnimation(animationSet);
    }
    //跳转登录界面
    private void enterLoginAct(){
//        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }
}
