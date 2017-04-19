package com.chumfuchiu.meetingmanagersystem;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chumfuchiu.meetingmanagersystem.actlogin.EmailLoginActivity;
import com.chumfuchiu.meetingmanagersystem.actlogin.PhoneLoginActivity;
import com.chumfuchiu.meetingmanagersystem.actlogin.RegisterActivity;
import com.chumfuchiu.meetingmanagersystem.utils.ActivityManager;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.layout_login);
        ActivityManager.addActivityIntoActManagger(LoginActivity.this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
    public void loginByPhone(View view){
        startActivity(new Intent(LoginActivity.this,PhoneLoginActivity.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
    public void loginByEmail(View view){
        startActivity(new Intent(LoginActivity.this, EmailLoginActivity.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
    public void userRegister(View view){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.delActivityIntoActManager(LoginActivity.this);
    }
}
