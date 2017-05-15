package com.chumfuchiu.meetingmanagersystem.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.chumfuchiu.meetingmanagersystem.R;
import com.chumfuchiu.meetingmanagersystem.bean.UserInfo;
import com.chumfuchiu.meetingmanagersystem.utils.ToastUitls;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by 37266 on 2017/4/18.
 */

public class YanzhengLoginFragment extends BasePagerFragment {
    private CompositeSubscription mCompositeSubscription;
    public Activity activity;
    private EditText etNum,etCode;
    private Button login;
    String phone,code;
    public YanzhengLoginFragment(Activity baseActivity) {
        super(baseActivity);
        this.activity=baseActivity;
        initView();
    }

    @Override
    public View initView() {
        rootView=View.inflate(activity,R.layout.layout_frag_yanzhenglogin,null);
        etNum=(EditText)rootView.findViewById(R.id.et_yanzLoginNum);
        etCode=(EditText)rootView.findViewById(R.id.et_yanzLoginCode);
        login=(Button)rootView.findViewById(R.id.btn_yanZLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone=etNum.getText().toString();
                code=etCode.getText().toString();
                if(!phone.isEmpty()&&!code.isEmpty()){
                addSubscription(BmobUser.loginBySMSCode(phone, code, new LogInListener<UserInfo>() {
                    @Override
                    public void done(UserInfo userInfo, BmobException e) {
                        if(userInfo!=null){
                            ToastUitls.showShortToast(activity,"登录成功");
                            startActivity(new Intent());
                        }else {
                            ToastUitls.showLongToast(activity,"登录失败"+e.getErrorCode()+e.getMessage());
                        }
                    }
                }));
                }else {
                    ToastUitls.showShortToast(activity,"输入信息不能为空");
                }
            }
        });
        return rootView;
    }
    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }
}
