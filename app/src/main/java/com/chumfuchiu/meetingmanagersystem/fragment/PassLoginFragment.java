package com.chumfuchiu.meetingmanagersystem.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chumfuchiu.meetingmanagersystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PassLoginFragment extends BasePagerFragment {
    public Activity activity;
    public PassLoginFragment(Activity baseActivity) {
        super(baseActivity);
        this.activity=baseActivity;
        initView();
    }

    @Override
    public View initView() {
        rootView=View.inflate(activity,R.layout.layout_frag_passlogin,null);
        return rootView;
    }
}
