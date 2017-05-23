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
import cn.bmob.v3.listener.SaveListener;

public class UserNameActivity extends AppCompatActivity {
    EditText etUserName,etUserPass;
    String user,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_username_login);
        ActivityManager.addActivityIntoActManagger(UserNameActivity.this);
        etUserName= (EditText) findViewById(R.id.login_et_username);
        etUserPass= (EditText) findViewById(R.id.login_et_password);
    }
    public void loginNow(View view){
        user=etUserName.getText().toString();
        pass=etUserPass.getText().toString();
        if(!user.isEmpty()&&!pass.isEmpty()){
            BmobUser bmobUser=new BmobUser();
            bmobUser.setUsername(user);
            bmobUser.setPassword(pass);
            bmobUser.login(new SaveListener<UserInfo>() {
                @Override
                public void done(UserInfo userInfo, BmobException e) {
                    if(e==null){
                        ToastUitls.showLongToast(getApplicationContext(),"登录成功");
                        startActivity(new Intent(UserNameActivity.this, MainActivity.class));
                        finish();
                    }else {
                        ToastUitls.showLongToast(getApplicationContext(),"用户名尚未注册");
                    }
                }
            });
        }else {
            ToastUitls.showLongToast(getApplicationContext(),"输入信息不能为空");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.delActivityIntoActManager(UserNameActivity.this);
    }
}
