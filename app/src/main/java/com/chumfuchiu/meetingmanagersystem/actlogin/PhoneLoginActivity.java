package com.chumfuchiu.meetingmanagersystem.actlogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chumfuchiu.meetingmanagersystem.LoginActivity;
import com.chumfuchiu.meetingmanagersystem.R;
import com.chumfuchiu.meetingmanagersystem.customview.ScrollCancelViewPager;
import com.chumfuchiu.meetingmanagersystem.fragment.BasePagerFragment;
import com.chumfuchiu.meetingmanagersystem.fragment.PassLoginFragment;
import com.chumfuchiu.meetingmanagersystem.fragment.YanzhengLoginFragment;
import com.chumfuchiu.meetingmanagersystem.utils.ActivityManager;

import java.util.ArrayList;
/*
* 手机登录页面
* */
public class PhoneLoginActivity extends AppCompatActivity {
    PassLoginFragment passLoginFragment;
    YanzhengLoginFragment yanzhengLoginFragment;
    ScrollCancelViewPager viewPager;
    RadioGroup radioGroup;
    ArrayList<BasePagerFragment> basePagerFragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_phonelogin);
        ActivityManager.addActivityIntoActManagger(PhoneLoginActivity.this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initDatas();
        initView();
    }
    protected void initDatas(){
        //初始化我们的fragment容器，将我们的fragment加入到容器中。
        basePagerFragments=new ArrayList<BasePagerFragment>();
        passLoginFragment=new PassLoginFragment(PhoneLoginActivity.this);
        basePagerFragments.add(passLoginFragment);
        yanzhengLoginFragment=new YanzhengLoginFragment(PhoneLoginActivity.this);
        basePagerFragments.add(yanzhengLoginFragment);
    }
    protected void initView(){
        //获取radioGroup对象并且为其设置radioButton的单选响应事件，与viewPager进行绑定。
        radioGroup= (RadioGroup) findViewById(R.id.rg_phonelogin);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i){
                    case R.id.rb_one:
                        viewPager.setCurrentItem(0,false);
                        break;
                    case R.id.rb_two:
                        viewPager.setCurrentItem(1,false);
                        break;
                }
            }
        });
        viewPager= (ScrollCancelViewPager) findViewById(R.id.login_viewpager);
        viewPager.setCurrentItem(0);
        viewPager.setAdapter(new LoginPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                RadioButton rb1= (RadioButton) findViewById(R.id.rb_one);
                RadioButton rb2= (RadioButton) findViewById(R.id.rb_two);
                switch (position){
                    case 0:
                        rb1.setTextColor(getResources().getColor(R.color.selector_white));
                        rb2.setTextColor(getResources().getColor(R.color.selector_gray));
                        radioGroup.check(R.id.rb_one);
                        break;
                    case 1:
                        rb1.setTextColor(getResources().getColor(R.color.selector_gray));
                        rb2.setTextColor(getResources().getColor(R.color.selector_white));
                        radioGroup.check(R.id.rb_two);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    public void backToLogin(View view){
        startActivity(new Intent(PhoneLoginActivity.this,LoginActivity.class));
    }
    class LoginPagerAdapter extends FragmentPagerAdapter{
        public LoginPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return basePagerFragments.get(position);
        }
        @Override
        public int getCount() {
            return basePagerFragments.size();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.delActivityIntoActManager(PhoneLoginActivity.this);
    }
}
