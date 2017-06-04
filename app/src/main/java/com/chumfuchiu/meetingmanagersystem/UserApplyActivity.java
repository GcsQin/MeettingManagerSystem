package com.chumfuchiu.meetingmanagersystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chumfuchiu.meetingmanagersystem.bean.MessageEvent;
import com.chumfuchiu.meetingmanagersystem.bean.RoomInfo;
import com.chumfuchiu.meetingmanagersystem.bean.UserInfo;
import com.chumfuchiu.meetingmanagersystem.utils.ToastUitls;

import org.greenrobot.eventbus.EventBus;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserApplyActivity extends AppCompatActivity {
    RelativeLayout rlRoot;
    TextView tvBuild,tvRoomNum,tvState,tvSource,tvSize,tvApplyUser,tvUsingTime,tvUsingReason;
    Button button;
    boolean roomIDFIsEmpty;
    String roomID;
    UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_apply);
        initView();
        userInfo= BmobUser.getCurrentUser(UserInfo.class);
        roomID=userInfo.getRoomID();
        if(roomID==null){
            roomID="";
            rlRoot.setBackgroundResource(R.drawable.not_apply);
            button.setText("好");
            roomIDFIsEmpty=true;
        }else {
            roomIDFIsEmpty=false;
            queryRoomDataAndViewVisable(roomID);
        }
    }
    private void  queryRoomDataAndViewVisable(final String roomID){
        BmobQuery<RoomInfo> query=new BmobQuery<>();
        query.getObject(roomID, new QueryListener<RoomInfo>() {
            @Override
            public void done(RoomInfo roomInfo, BmobException e) {
                if(e==null){
                    setViewData(roomInfo.getBuildingOfRoom(),roomInfo.getNumberOfRoom()
                    ,roomInfo.getStateOfRoom(),roomInfo.getResourceOfRoom(),roomInfo.getPersonsOfRoom()
                    ,roomInfo.getUsingPerson(),roomInfo.getUsingTime(),roomInfo.getUsingReason());
                    viewVisable();
                }else {
                    ToastUitls.showLongToast(getApplicationContext(),"查询失败"+e.getMessage()+"("+e.getErrorCode()+")");
                }
            }
        });
    }
    private void setViewData(String build,String num,String state,String source,int size,String user,String usingTime,String reason){
        Log.e("=====",""+build+num+state+source+size+user+usingTime+reason);
        tvBuild.setText(build);
        tvRoomNum.setText(num);
        tvState.setText("状态："+state);
        tvSource.setText("教室资源："+source);
        tvSize.setText("教室容量"+size);
        tvApplyUser.setText("申请人："+user);
        tvUsingTime.setText("申请使用时间:"+usingTime);
        tvUsingReason.setText("申请理由："+reason);
    }
    private void viewVisable(){
        tvBuild.setVisibility(View.VISIBLE);
        tvRoomNum.setVisibility(View.VISIBLE);
        tvState.setVisibility(View.VISIBLE);
        tvSource.setVisibility(View.VISIBLE);
        tvSize.setVisibility(View.VISIBLE);
        tvApplyUser.setVisibility(View.VISIBLE);
        tvUsingTime.setVisibility(View.VISIBLE);
        tvUsingReason.setVisibility(View.VISIBLE);

    }
    private void initView(){
        tvBuild= (TextView) findViewById(R.id.tv_userapply_building);
        tvRoomNum= (TextView) findViewById(R.id.tv_userapply_num);
        tvState= (TextView) findViewById(R.id.tv_userapply_state);
        tvSource= (TextView) findViewById(R.id.tv_userapply_source);
        tvSize= (TextView) findViewById(R.id.tv_userapply_persons);
        tvApplyUser= (TextView) findViewById(R.id.tv_userapply_usingperson);
        tvUsingTime= (TextView) findViewById(R.id.tv_userapply_usingtime);
        tvUsingReason= (TextView) findViewById(R.id.tv_userapply_usingReason);
        button= (Button) findViewById(R.id.btn_userapply);
        rlRoot= (RelativeLayout) findViewById(R.id.rl_userapply_root);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(roomIDFIsEmpty){
                    startActivity(new Intent(UserApplyActivity.this,MainActivity.class));
                }else {
                    showCancelDialog(roomID);
                }
            }
        });
    }
    private void showCancelDialog(final String roomID){
        Button ok,no;
        AlertDialog.Builder builder=new AlertDialog.Builder(UserApplyActivity.this);
        View view=View.inflate(getApplicationContext(),R.layout.custom_dialog_cancel_apply,null);
        builder.setView(view);
        ok= (Button) view.findViewById(R.id.dialog_ok);
        no= (Button) view.findViewById(R.id.dialog_no);
        final Dialog dialog=builder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelApplyRoom(roomID);
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void cancelApplyRoom(String roomID){
        RoomInfo roomInfo=new RoomInfo();
        roomInfo.setStateOfRoom("空闲中");
        roomInfo.setUsingTime("");
        roomInfo.setUsingPerson("");
        roomInfo.setUsingPersonAvatar("");
        roomInfo.setUsingPersonid("");
        roomInfo.setUsingReason("");
        roomInfo.update(roomID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    updateUserInfo(userInfo.getObjectId());
                }else {
                    ToastUitls.showLongToast(getApplicationContext(),"操作失败,"+e.getMessage());
                }
            }
        });
    }
    private void updateUserInfo(String userID){
        userInfo.setRoomID("");
        userInfo.update(userID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    ToastUitls.showLongToast(getApplicationContext(),"操作成功");
                    EventBus.getDefault().post(new MessageEvent());
                    startActivity(new Intent(UserApplyActivity.this,MainActivity.class));
                }else {
                    ToastUitls.showLongToast(getApplicationContext(),"操作失败,"+e.getMessage());
                }
            }
        });
    }
}
