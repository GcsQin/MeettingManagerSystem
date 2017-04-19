package com.chumfuchiu.meetingmanagersystem.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 37266 on 2017/4/18.
 */

public class ScrollCancelViewPager extends ViewPager {
    public ScrollCancelViewPager(Context context) {
        super(context);
    }

    public ScrollCancelViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
