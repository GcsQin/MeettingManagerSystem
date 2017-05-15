package com.chumfuchiu.meetingmanagersystem.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ChumFuchiu on 2017/5/8.
 */

public class ToastUitls {
//    public static Context mContext;
    public static Toast mToast;
    public static void showLongToast(Context context,String content){
        if(mToast==null){
            mToast=Toast.makeText(context,content,Toast.LENGTH_LONG);
        }else {
            mToast.setText(content);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }
    public static void showShortToast(Context context,String content){
        if(mToast==null){
            mToast=Toast.makeText(context,content,Toast.LENGTH_SHORT);
        }else {
            mToast.setText(content);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
