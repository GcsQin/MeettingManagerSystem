package com.chumfuchiu.meetingmanagersystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chumfuchiu.meetingmanagersystem.bean.RoomInfo;
import com.chumfuchiu.meetingmanagersystem.bean.UserInfo;
import com.chumfuchiu.meetingmanagersystem.utils.ActivityManager;
import com.chumfuchiu.meetingmanagersystem.utils.ToastUitls;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobUser;

/*
* 当用户点击一个教室后进入该页面，该页面显示了详细的教室信息
*
*
* */
public class RoomDetialsActivity extends BaseActivity {
    TextView tvBuinding,tvRoomNum,tvRoomState,tvRoomSource,tvRoomSize,
            tvUsingPerson,tvUsingTime,tvUsingReason;
    Button button;
    RoomInfo roomInfo;
    String state;
    UserInfo userInfo;
    BmobIMUserInfo imInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivityIntoActManagger(RoomDetialsActivity.this);
        setContentView(R.layout.layout_room_detials);
        initViews();
        Intent intent=getIntent();
        roomInfo= (RoomInfo) intent.getSerializableExtra("roomInfo");
        if(roomInfo!=null){
            initViewData();
        }
        if(roomInfo.getUsingPersonid()!=null){
            if(roomInfo.getUsingPersonid().equals(BmobUser.getCurrentUser(UserInfo.class).getObjectId())){
                button.setVisibility(View.INVISIBLE);
            }
        }
        //構造聊天方用戶信息：傳入用戶id、用戶名和用戶頭像三個參數
        imInfo=new BmobIMUserInfo(roomInfo.getUsingPersonid(),roomInfo.getUsingPerson(),roomInfo.getUsingPersonAvatar());
    }
    private void initViews(){
        tvBuinding= (TextView) findViewById(R.id.tv_roomdetials_building);
        tvRoomNum= (TextView) findViewById(R.id.tv_roomdetials_num);
        tvRoomState= (TextView) findViewById(R.id.tv_roomdetials_state);
        tvRoomSource= (TextView) findViewById(R.id.tv_roomdetials_source);
        tvRoomSize= (TextView) findViewById(R.id.tv_roomdetials_persons);
        tvUsingPerson= (TextView) findViewById(R.id.tv_roomdetials_usingperson);
        tvUsingTime= (TextView) findViewById(R.id.tv_roomdetials_usingtime);
        tvUsingReason= (TextView) findViewById(R.id.tv_roomdetials_usingReason);
        button= (Button) findViewById(R.id.btn_roomdetials);
    }
    private void initViewData(){
        tvBuinding.setText(roomInfo.getBuildingOfRoom());
        tvRoomNum.setText(roomInfo.getNumberOfRoom());
        state=roomInfo.getStateOfRoom().trim();
        Log.e("RoomDetialsActivity","state"+state);
        tvRoomState.setText(state);
        tvRoomSource.setText(roomInfo.getResourceOfRoom());
        tvRoomSize.setText("建议人数："+roomInfo.getPersonsOfRoom());
        tvUsingPerson.setText(roomInfo.getUsingPerson());
        tvUsingTime.setText(roomInfo.getUsingTime());
        tvUsingReason.setText(roomInfo.getUsingReason());
        if(state.equals("空闲中")){
            button.setText("申请使用");
            button.setBackgroundResource(R.drawable.shape_roomdetials_btn_green);
            Log.e("Button","========空闲");
        }else if(state.equals("使用中")){
            button.setText("与他协商");
            Log.e("Button","========使用");
        }else {
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state.equals("空闲中")){
                    //判断当前用户是否预约过教室，如果预约过教室，就不让他预约。
                    userInfo=BmobUser.getCurrentUser(UserInfo.class);
                    if(!userInfo.getRoomID().isEmpty()){
                        ToastUitls.showLongToast(getApplicationContext(),"您正在预约一个教室或您有预约的教室正在使用中,不能重复预约。");
                        return;
                    }
                    Intent intent=new Intent(RoomDetialsActivity.this,BorrowActivity.class);
                    intent.putExtra("roomInfos",roomInfo);
                    startActivity(intent);
                }else if(state.equals("禁用中")){
                    ToastUitls.showLongToast(getApplicationContext(),"教室处于禁用状态！");
                }else {
                    startPrivateConversation();
                }
            }
        });
    }
    private void startPrivateConversation(){
        BmobIMConversation c=BmobIM.getInstance().startPrivateConversation(imInfo,false,null);
        Bundle bundle=new Bundle();
        bundle.putSerializable("c",c);
        startActivity(ChatActivity.class,bundle,false);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.delActivityIntoActManager(RoomDetialsActivity.this);
    }
}
