package com.chumfuchiu.meetingmanagersystem.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chumfuchiu.meetingmanagersystem.R;

/**
 * Created by 37266 on 2017/4/18.
 */

public class YanzhengLoginFragment extends BasePagerFragment {
    public Activity activity;
//    public View rootView;
    public YanzhengLoginFragment(Activity baseActivity) {
        super(baseActivity);
        this.activity=baseActivity;
        initView();
    }

    @Override
    public View initView() {
//        if(activity==null){
//            Log.e("anzhengLoginFragment","anzhengLoginFragment----null");
//        }else {
//            Log.e("anzhengLoginFragment","anzhengLoginFragment----not null");
//        }
        rootView=View.inflate(activity,R.layout.layout_frag_yanzhenglogin,null);
        if(rootView==null){
            Log.e("111111111","111111111111");
        }
        return rootView;
    }
}
