package com.chumfuchiu.meetingmanagersystem;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chumfuchiu.meetingmanagersystem.bean.RoomInfo;
import com.chumfuchiu.meetingmanagersystem.utils.ToastUitls;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ApplyHandleActivity extends AppCompatActivity {
    ImageButton cancel;
    Button agree,disagree;
    TextView reason,time;
    ArrayList<RoomInfo> infos;
    ListView mListView;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_manager);
        linearLayout= (LinearLayout) findViewById(R.id.root_ll_info);
        mListView= (ListView) findViewById(R.id.lv_apply_infos);
        Intent intent=getIntent();
//        infos= (ArrayList<RoomInfo>) intent.getSerializableExtra("applyInfos");
        queryApplyInfo();
    }
    private void initListView(final ArrayList<RoomInfo> infos){
        mListView.setAdapter(new ApplyListAdapter());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showHandleDialog(infos.get(i));
            }
        });
    }
    private void showHandleDialog(final RoomInfo roomInfo){
        AlertDialog.Builder builder=new AlertDialog.Builder(ApplyHandleActivity.this);
        final AlertDialog dialog=builder.create();
        View dialogView=View.inflate(getApplicationContext(),R.layout.custom_dialog_apply_detials,null);
        time= (TextView) dialogView.findViewById(R.id.tv_dialog_apply_time);
        cancel= (ImageButton) dialogView.findViewById(R.id.btn_cancel);
        agree= (Button) dialogView.findViewById(R.id.btn_agree_apply);
        disagree=(Button) dialogView.findViewById(R.id.btn_disagree_apply);
        reason= (TextView) dialogView.findViewById(R.id.tv_dialog_apply_reason);
        //
        time.setText(roomInfo.getUsingTime());
        reason.setText(roomInfo.getUsingReason());
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agreeApply(roomInfo);
                dialog.dismiss();
            }
        });
        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disagreeApply(roomInfo);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setView(dialogView);
        dialog.show();
    }
    private void agreeApply(RoomInfo roomInfo){
        roomInfo.setStateOfRoom("占中中");
        roomInfo.update(roomInfo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    ToastUitls.showLongToast(getApplicationContext(),"操作成功");
                }else {
                    ToastUitls.showLongToast(getApplicationContext(),"操作失败");
                }
            }
        });
    }
    private void disagreeApply(RoomInfo roomInfo){
        roomInfo.setUsingPersonAvatar("");
        roomInfo.setUsingPerson("");
        roomInfo.setUsingPersonid("");
        roomInfo.setUsingTime("");
        roomInfo.setUsingReason("");
        roomInfo.setStateOfRoom("空闲中");
        roomInfo.update(roomInfo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    ToastUitls.showLongToast(getApplicationContext(),"操作成功");
                }else {
                    ToastUitls.showLongToast(getApplicationContext(),"操作失败");
                }
            }
        });
    }
    class ApplyListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public RoomInfo getItem(int i) {
            return infos.get(i);
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view==null){
                holder=new ViewHolder();
                view=View.inflate(getApplicationContext(),R.layout.item_listview_apply,null);
                holder.tvBulid= (TextView) view.findViewById(R.id.tv_item_apply_build);
                holder.tvUser= (TextView) view.findViewById(R.id.tv_item_apply_user);
                holder.tvTime= (TextView) view.findViewById(R.id.tv_item_apply_time);
                holder.tvReason= (TextView) view.findViewById(R.id.tv_item_apply_reason);
                view.setTag(holder);
            }else {
                holder= (ViewHolder) view.getTag();
            }
            holder.tvBulid.setText(getItem(i).getBuildingOfRoom());
            holder.tvUser.setText("申请人："+getItem(i).getUsingPerson());
            holder.tvReason.setText(getItem(i).getUsingReason());
            holder.tvTime.setText(getItem(i).getUpdatedAt());
            return view;
        }
        class ViewHolder{
            TextView tvBulid,tvUser,tvReason,tvTime;
        }
    }
    private void queryApplyInfo(){
        BmobQuery<RoomInfo> query=new BmobQuery<RoomInfo>();
        infos=new ArrayList<RoomInfo>();
        query.addWhereEqualTo("stateOfRoom","申请中");
        query.setLimit(2017);
        query.findObjects(new FindListener<RoomInfo>() {
            @Override
            public void done(List<RoomInfo> list, BmobException e) {
                if(e==null){
                    for(RoomInfo info:list){
                        infos.add(info);
                    }
                    if(infos.size()==0){
                        ToastUitls.showLongToast(getApplicationContext(),"暂时没有请求信息哦！");
                        linearLayout.setBackgroundResource(R.drawable.apply);
                    }else {
                        linearLayout.setBackgroundResource(R.drawable.apply_handle);
                        initListView(infos);
                    }
                }
            }
        });
    }
}
