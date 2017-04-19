package com.chumfuchiu.meetingmanagersystem.actlogin;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chumfuchiu.meetingmanagersystem.R;
import com.chumfuchiu.meetingmanagersystem.bean.UserInfo;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public class RegisterActivity extends AppCompatActivity {
    private CompositeSubscription mCompositeSubscription;
    EditText etUser,etPass,etPhone,etYanz;
    Button yanZ;
    private int time=60;
    Timer timer=new Timer();
    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initViews();
    }
    public void initViews(){
        etUser= (EditText) findViewById(R.id.register_user);
        etPass= (EditText) findViewById(R.id.register_pass);
        etPhone= (EditText) findViewById(R.id.register_phone);
        etYanz= (EditText) findViewById(R.id.register_yanz);
        yanZ=(Button)findViewById(R.id.register_btn_yanz);
    }
    public void registerUser(View view){
        String user=etUser.getText().toString();
        String pass=etPass.getText().toString();
        String phone=etPhone.getText().toString();
        String yanZ=etYanz.getText().toString();
        if(user.isEmpty()|pass.isEmpty()|phone.isEmpty()| yanZ.isEmpty()){
            Toast.makeText(RegisterActivity.this,"所填信息不能为空!",Toast.LENGTH_LONG).show();
        }else {
            register(user,pass,phone,yanZ);
        }
    }
    public void getSmsVerify(View view){
        String phone=etPhone.getText().toString();
//        yanZ.setEnabled(false);
//        yanZ.setBackgroundResource(R.drawable.shape_login_btn_unable);
//        Toast.makeText(RegisterActivity.this,"验证码发送成功",Toast.LENGTH_SHORT).show();
//        timer.schedule(timerTask,0,1000);
        if(phone!=null){
            BmobSMS.requestSMSCode(phone, "会议室预约管理系统", new QueryListener<Integer>() {
                @Override
                public void done(Integer integer, BmobException e) {
                    if(e==null){
                        yanZ.setEnabled(false);
                        yanZ.setBackgroundResource(R.drawable.shape_login_btn_unable);
                        Toast.makeText(RegisterActivity.this,"验证码发送成功",Toast.LENGTH_LONG).show();
                        timer.schedule(timerTask,0,1000);
                    }
                }
            });
        }
    }
    private void register(String user, String pass, String phone, String yanZ) {
        final UserInfo userInfo=new UserInfo();
        userInfo.setUsername(user);
        userInfo.setPassword(pass);
        userInfo.setMobilePhoneNumber(phone);
        addSubscription(userInfo.signOrLogin(yanZ, new SaveListener<UserInfo>() {
            @Override
            public void done(UserInfo userInfo, BmobException e) {
                if(e==null){
                    Toast.makeText(RegisterActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();

                }
            }
        }));
    }

    public void xxxItems(View view){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

    TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    time--;
                    yanZ.setText(time+"S");
                    if(time<0){
                        timer.cancel();
                        time=60;
                        yanZ.setEnabled(true);
                        yanZ.setBackgroundResource(R.drawable.shape_login_btn);
                        yanZ.setText("获取验证码");
                    }
                }
            });
        }
    };
}
