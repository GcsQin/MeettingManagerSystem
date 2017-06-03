package com.chumfuchiu.meetingmanagersystem.chat;

import android.content.Context;

import com.chumfuchiu.meetingmanagersystem.BmobIMApplication;

public abstract class BaseModel {

    public int CODE_NULL=1000;
    public static int CODE_NOT_EQUAL=1001;

    public static final int DEFAULT_LIMIT=20;

    public Context getContext(){
        return BmobIMApplication.INSTANCE();
    }
}
