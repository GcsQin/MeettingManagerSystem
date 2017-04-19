package com.chumfuchiu.meetingmanagersystem.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 37266 on 2017/4/17.
 */

public class ActivityManager {
    public static ArrayList<Activity> list=new ArrayList<Activity>();
    public static void addActivityIntoActManagger(Activity activity){
        list.add(activity);
    }
    public static void delActivityIntoActManager(Activity activity){
        list.remove(activity);
    }
    public static void finishAllActivity(){
        for(Activity activity:list){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
