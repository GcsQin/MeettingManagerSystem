package com.chumfuchiu.meetingmanagersystem;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.chumfuchiu.meetingmanagersystem.bean.RoomInfo;
import com.chumfuchiu.meetingmanagersystem.bean.UserInfo;
import com.chumfuchiu.meetingmanagersystem.utils.ToastUitls;

import java.util.Calendar;
import java.util.Date;

import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.views.DatePicker;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
/*
* 用户填写借阅教室的信息
*
* */
public class BorrowActivity extends AppCompatActivity {
    RoomInfo roomInfo;
    TextView tvBuilding,tvRoomNum,tvUser,tvStartTime,tvEndTime,tvLine;
    EditText etReason;
    Button startPick,endPick,sumbit;
    UserInfo userInfo;
    Date date=new Date();
    Calendar calendar=Calendar.getInstance();
    int mYear=calendar.get(Calendar.YEAR);
    int mMonth=calendar.get(Calendar.MONTH)+1;
    String sb_startTime,sb_endTime;
    String hour,minute;
    Boolean roomUpdateSuccess,userUpdateSuccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_borrow);
        initView();
        userInfo= BmobUser.getCurrentUser(UserInfo.class);
        if(userInfo!=null){
            tvUser.setText("申请人:"+userInfo.getUsername());
        }
        roomInfo= (RoomInfo) getIntent().getSerializableExtra("roomInfos");
        if(roomInfo!=null){
            tvBuilding.setText("申请教室:"+roomInfo.getBuildingOfRoom());
            tvRoomNum.setText(""+roomInfo.getNumberOfRoom());
        }
    }
    private void initView() {
        tvBuilding= (TextView) findViewById(R.id.tv_yuyue_building);
        tvRoomNum= (TextView) findViewById(R.id.tv_yuyue_roomNum);
        tvUser=(TextView) findViewById(R.id.tv_yuyue_user);
        tvStartTime=(TextView) findViewById(R.id.tv_yuyue_startTime);
        tvEndTime= (TextView) findViewById(R.id.tv_yuyue_endTime);
        tvLine= (TextView) findViewById(R.id.tv_yuyue_line);
        etReason= (EditText) findViewById(R.id.et_yuyue_reason);
        startPick= (Button) findViewById(R.id.btn_starPick);
        endPick= (Button) findViewById(R.id.btn_endPick);
        sumbit= (Button) findViewById(R.id.btn_sumbit);
        startPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(tvStartTime);
            }
        });
        endPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(tvEndTime);
            }
        });
        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user=userInfo.getUsername();
                String reason=etReason.getText().toString();
                String startTime=tvStartTime.getText().toString();
                String endTime=tvEndTime.getText().toString();
                sumbitRequst(user,reason,startTime,endTime);
            }
        });
    }
    private void showDialog(final TextView tv){
        final AlertDialog dialog=new AlertDialog.Builder(BorrowActivity.this).create();
        dialog.show();
        View dialogView=View.inflate(BorrowActivity.this,R.layout.custom_dialog_datapicker,null);
        final DatePicker datePicker= (DatePicker) dialogView.findViewById(R.id.datePicker);
        final TimePicker timePicker= (TimePicker) dialogView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        final ImageButton btnOk= (ImageButton) dialogView.findViewById(R.id.imgbtn_ok);
        datePicker.setDate(mYear,mMonth);
        datePicker.setMode(DPMode.SINGLE);
        datePicker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                sb_startTime=date;
                Log.e("BorrowActivity","=="+date);
            }
        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hour=""+i;
                if(i1<=9){
                    minute="0"+i1;
                }else {
                    minute=""+i1;
                }
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date=sb_startTime+"-"+hour+":"+minute;
                tv.setText(date);
                tvLine.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(dialogView,params);
        dialog.getWindow().setGravity(Gravity.CENTER);
    }
    private void sumbitRequst(String user,String reason,String startTime,String endTime ){
        if(user.isEmpty()|reason.isEmpty()|startTime.isEmpty()|endTime.isEmpty()){
            ToastUitls.showShortToast(getApplicationContext(),"申请信息必须完整填写");
            return;
        }
        final String obejectID=roomInfo.getObjectId();
        roomInfo.setStateOfRoom("申请中");
        roomInfo.setUsingPerson(user);
        roomInfo.setUsingReason(reason);
        roomInfo.setUsingPersonid(userInfo.getObjectId());
        roomInfo.setUsingPersonAvatar(userInfo.getAvatar());
        roomInfo.setUsingTime(startTime+"-"+endTime);
        roomInfo.update(obejectID, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                Log.e("roomUpdateSuccess","=1======");
                if(e==null){
                    updateUserInfos(obejectID);
                } else {
                    ToastUitls.showLongToast(getApplicationContext(),"提交失败");
                }
            }
        });
    }
    private void updateUserInfos(String id){
        userInfo.setRoomID(id);
        userInfo.update(userInfo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    ToastUitls.showShortToast(getApplicationContext(),"提交成功");
                    startActivity(new Intent(BorrowActivity.this,MainActivity.class));
                }else {
                    ToastUitls.showLongToast(getApplicationContext(),"提交失败");
                }
            }
        });
    }
}
