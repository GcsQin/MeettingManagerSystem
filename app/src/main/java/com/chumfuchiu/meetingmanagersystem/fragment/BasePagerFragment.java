package com.chumfuchiu.meetingmanagersystem.fragment;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.chumfuchiu.meetingmanagersystem.R;

/**
 * Created by 37266 on 2017/4/18.
 */

public class BasePagerFragment extends BaseFragment {
    public Activity baseActivity;
    public View rootView;
    public BasePagerFragment(Activity baseActivity) {
        this.baseActivity = baseActivity;
    }
    @Override
    public View initView() {
        rootView=View.inflate(baseActivity, R.layout.layout_frag_yanzhenglogin,null);
        return rootView;
    }
}
