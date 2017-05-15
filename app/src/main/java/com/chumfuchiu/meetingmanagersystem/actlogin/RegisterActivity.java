package com.chumfuchiu.meetingmanagersystem.actlogin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chumfuchiu.meetingmanagersystem.MainActivity;
import com.chumfuchiu.meetingmanagersystem.R;
import com.chumfuchiu.meetingmanagersystem.bean.UserInfo;
import com.chumfuchiu.meetingmanagersystem.utils.ActivityManager;
import com.chumfuchiu.meetingmanagersystem.utils.ToastUitls;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/*
* 应用注册页面
* */
public class RegisterActivity extends AppCompatActivity {
    private CompositeSubscription mCompositeSubscription;
    EditText etUser,etPass,etPhone,etYanz;
    Button yanZ;
    private int time=60;
    Timer timer=new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        ActivityManager.addActivityIntoActManagger(RegisterActivity.this);
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
        //验证码按钮（在这里获取该控件是为了限制用户在一定时间不能再次获取验证码）
        yanZ=(Button)findViewById(R.id.register_btn_yanz);
    }
    public void registerUser(View view){//注册按钮点击事件
        String user=etUser.getText().toString();
        String pass=etPass.getText().toString();
        String phone=etPhone.getText().toString();
        String yanZ=etYanz.getText().toString();
        if(user.isEmpty()|pass.isEmpty()|phone.isEmpty()){
            ToastUitls.showLongToast(getApplicationContext(),"所填用户信息不能为空");
        }else if(yanZ.isEmpty()){
            ToastUitls.showShortToast(getApplicationContext(),"验证码不能为空");
        }else {
            register(user,pass,phone,yanZ);
        }
    }
    public void getSmsVerify(View view){
        String phone=etPhone.getText().toString();
        if(phone!=null){
            BmobSMS.requestSMSCode(phone, "会议室预约管理系统", new QueryListener<Integer>() {
                @Override
                public void done(Integer integer, BmobException e) {
                    if(e==null){
                        yanZ.setEnabled(false);
                        yanZ.setBackgroundResource(R.drawable.shape_login_btn_unable);
                        ToastUitls.showShortToast(getApplicationContext(),"验证码发送成功");
                        timer.schedule(timerTask,0,1000);
                    }
                }
            });
        }else{
            ToastUitls.showShortToast(getApplicationContext(),"手机号不能为空");
        }
    }
    private void register(String user, String pass, String phone, String yanZ) {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        final UserInfo userInfo=new UserInfo();
        userInfo.setUsername(user);
        userInfo.setPassword(pass);
        userInfo.setMobilePhoneNumber(phone);
        userInfo.setPermission(new Integer(1001));
        addSubscription(userInfo.signOrLogin(yanZ, new SaveListener<UserInfo>() {
            @Override
            public void done(UserInfo userInfo, BmobException e) {
                if(e==null){
                    ToastUitls.showShortToast(getApplicationContext(),"注册成功");
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                }else {
                    ToastUitls.showLongToast(getApplicationContext(),"注册失败,"+"错误码："+e.getErrorCode()
                    +"错误信息:"+e.toString());
                }
            }
        }));
    }
    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    public void xxxItems(View view){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.delActivityIntoActManager(RegisterActivity.this);
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
