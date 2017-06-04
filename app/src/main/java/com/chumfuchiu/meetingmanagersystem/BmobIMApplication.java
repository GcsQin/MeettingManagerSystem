package com.chumfuchiu.meetingmanagersystem;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.chumfuchiu.meetingmanagersystem.chat.ChatMessageHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;

/**
 * Created by ChumFuchiu on 2017/5/23.
 */

public class BmobIMApplication extends Application {
    private static BmobIMApplication INSTANCE;
    public static BmobIMApplication INSTANCE(){
        return INSTANCE;
    }
    private void setInstance(BmobIMApplication app) {
        setBmobIMApplication(app);
    }
    private static void setBmobIMApplication(BmobIMApplication a) {
        BmobIMApplication.INSTANCE = a;
    }
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        setInstance(this);
        if(getApplicationInfo().packageName.equals(getMyProcessName())){
            Log.e("BmobIMApplication",""+getApplicationInfo()+getPackageName());
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new ChatMessageHandler(this));
        }
    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Context getMYContext(){
        return context;
    };
}
