package com.chumfuchiu.meetingmanagersystem.actlogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.chumfuchiu.meetingmanagersystem.MainActivity;
import com.chumfuchiu.meetingmanagersystem.R;
import com.chumfuchiu.meetingmanagersystem.bean.UserInfo;
import com.chumfuchiu.meetingmanagersystem.utils.ActivityManager;
import com.chumfuchiu.meetingmanagersystem.utils.ToastUitls;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;


/*
* 邮箱登录界面
* */
public class EmailLoginActivity extends AppCompatActivity {
    EditText etEmail,etEmailPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_email_login);
        ActivityManager.addActivityIntoActManagger(EmailLoginActivity.this);
        etEmail= (EditText) findViewById(R.id.login_et_email);
        etEmailPass=(EditText) findViewById(R.id.login_et_emailpassword);
    }
    public void loginByEmailNow(View view){
        String email=etEmail.getText().toString();
        String pass=etEmailPass.getText().toString();
        if(!email.isEmpty()&&!pass.isEmpty()){
            BmobUser.loginByAccount(email, pass, new LogInListener<UserInfo>() {
                @Override
                public void done(UserInfo userInfo, BmobException e) {
                    if(e==null){
                        startActivity(new Intent(EmailLoginActivity.this, MainActivity.class));
                    }else {
                        ToastUitls.showShortToast(getApplicationContext(),"该邮箱尚未注册");
                    }
                }
            });
        }else {
            ToastUitls.showLongToast(getApplicationContext(),"所填信息不能为空");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.delActivityIntoActManager(EmailLoginActivity.this);
    }
}
