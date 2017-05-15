package com.chumfuchiu.meetingmanagersystem.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chumfuchiu.meetingmanagersystem.MainActivity;
import com.chumfuchiu.meetingmanagersystem.R;
import com.chumfuchiu.meetingmanagersystem.bean.UserInfo;
import com.chumfuchiu.meetingmanagersystem.utils.ToastUitls;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * A simple {@link Fragment} subclass.
 */
public class PassLoginFragment extends BasePagerFragment {
    private CompositeSubscription mCompositeSubscription;
    public Activity activity;
    private EditText etPhoneNum,etPass;
    private Button btnLogin;
    String phoneNum,pass;
    public PassLoginFragment(Activity baseActivity) {
        super(baseActivity);
        this.activity=baseActivity;
        initView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("PassLoginFragment","attachTo======");
    }

    @Override
    public View initView() {
        rootView=View.inflate(activity,R.layout.layout_frag_passlogin,null);
        etPhoneNum= (EditText) rootView.findViewById(R.id.et_passloginPhone);
        etPass=(EditText)rootView.findViewById(R.id.et_passloginPass);
        btnLogin=(Button)rootView.findViewById(R.id.btn_passloginButton);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNum=etPhoneNum.getText().toString();
                pass=etPass.getText().toString();
                if(!phoneNum.isEmpty()&&!pass.isEmpty()){
                    addSubscription(BmobUser.loginByAccount(phoneNum, pass, new LogInListener<UserInfo>() {
                        @Override
                        public void done(UserInfo userInfo, BmobException e) {
                            if(userInfo!=null){
                                ToastUitls.showShortToast(activity,"登陆成功");
                                if(activity==null){
                                    Log.e("PassLoginFragmentTag","activity is null");
                                }else {
                                    Log.e("PassLoginFragmentTag","activity is not null");
                                    startActivity(new Intent(getActivity(),MainActivity.class));
//                                    startActivity(new Intent(activity,MainActivity.class));
                                }
                            }else {
                                ToastUitls.showShortToast(activity,"登陆失败,"+"错误码"+
                                        e.getErrorCode()+"错误日志:"+e.getMessage());
                                Log.e("PassLoginFragment",e.getErrorCode()+"===="+e.getMessage());
                            }
                        }
                    }));
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
