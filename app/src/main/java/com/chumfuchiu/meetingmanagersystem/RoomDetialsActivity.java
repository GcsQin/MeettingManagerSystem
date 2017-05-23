package com.chumfuchiu.meetingmanagersystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chumfuchiu.meetingmanagersystem.bean.RoomInfo;
import com.chumfuchiu.meetingmanagersystem.utils.ActivityManager;

public class RoomDetialsActivity extends AppCompatActivity {
    TextView tvBuinding,tvRoomNum,tvRoomState,tvRoomSource,tvRoomSize,
            tvUsingPerson,tvUsingTime,tvUsingReason;
    Button button;
    RoomInfo roomInfo;
    String state;
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
            Log.e("Button","========其他");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state.equals("空闲中")){
                    
                }else if (state.equals("使用中")){

                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.delActivityIntoActManager(RoomDetialsActivity.this);
    }
}
