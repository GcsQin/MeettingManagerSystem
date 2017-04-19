package com.chumfuchiu.meetingmanagersystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import cn.bmob.v3.Bmob;

public class SplashActivity extends AppCompatActivity {
    private RelativeLayout relativeLayout;
    private AnimationSet animationSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_splash);
        getSupportActionBar().hide();
        Bmob.initialize(this,"de3f9cdf2a992d3e894dbb11e99bbedf");
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initView();
    }
    private void initView(){
        relativeLayout= (RelativeLayout) findViewById(R.id.splash_rl_content);
        animationSet =new AnimationSet(false);
        RotateAnimation rotateAnimation=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(3000);
        rotateAnimation.setFillAfter(true);
        animationSet.addAnimation(rotateAnimation);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1.0f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(3000);
        animationSet.addAnimation(alphaAnimation);
        ScaleAnimation scaleAnimation=new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(3000);
        animationSet.addAnimation(scaleAnimation);
//        relativeLayout.setAnimation(animationSet);
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
        relativeLayout.setAnimation(animationSet);
    }
    private void enterLoginAct(){
        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }
}
