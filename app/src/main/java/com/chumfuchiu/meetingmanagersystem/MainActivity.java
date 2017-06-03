package com.chumfuchiu.meetingmanagersystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chumfuchiu.meetingmanagersystem.adapter.ClassRoomRecyclerAdapter;
import com.chumfuchiu.meetingmanagersystem.adapter.MenuListAdapter;
import com.chumfuchiu.meetingmanagersystem.bean.RoomInfo;
import com.chumfuchiu.meetingmanagersystem.bean.UserInfo;
import com.chumfuchiu.meetingmanagersystem.decoration.RecyclerViewSpaceItemDecoration;
import com.chumfuchiu.meetingmanagersystem.utils.ActivityManager;
import com.chumfuchiu.meetingmanagersystem.utils.ToastUitls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;

/*
* 程序主页面。
* */
public class MainActivity extends AppCompatActivity {
    //ToolBar,侧滑布局，主布局。
    Toolbar mToolbar;
    LinearLayout linearMenu;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle mToggle;
    //
    RecyclerView recyclerView;
    ClassRoomRecyclerAdapter roomRecyclerAdapter;
    //侧滑菜单
    TextView tvUserName,tvUserPhone,tvUserEmail,tvUserPermission;
    ListView listView;
    private ArrayList<RoomInfo> roomInfoArrayList;
    Integer permission;
    MenuListAdapter menuListAdapter;
    String[] user=new String[]{"邮箱中心","我预约的","最佳预约","退出登录"};
    String[] admin=new String[]{"邮箱中心","我预约的","最佳预约","预约管理","一键空闲","退出登录"};
    //更新对话框
    EditText etBuild,etRoomNum,etSource,etState,etSize;
    Boolean isUpdateSuccess=null;
    String roomUpSource,roomUpState,upSize;
    //申请信息
    ArrayList<RoomInfo> applyRoomList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        ActivityManager.addActivityIntoActManagger(MainActivity.this);
        roomInfoArrayList=new ArrayList<RoomInfo>();
        initViews();
        //判断当前用户类型
        justCurrentUser();
        initToolBarAndDrawableLayout();
        getDataFromServer();
    }
    private void justCurrentUser(){
        UserInfo userInfo= BmobUser.getCurrentUser(UserInfo.class);
        if(userInfo!=null){
            String userName=userInfo.getUsername();
            if(userName!=null){
                tvUserName.setText(userName);
            }
            if(userInfo.getMobilePhoneNumberVerified()!=null&&userInfo.getMobilePhoneNumberVerified()){
                tvUserPhone.setText("手机号:"+userInfo.getMobilePhoneNumber());
            }else {
                tvUserPhone.setText("手机号:未认证");
            }
            if(userInfo.getEmailVerified()!=null&&userInfo.getEmailVerified()){
                tvUserEmail.setText("邮箱:"+userInfo.getEmail());
            }else {
                tvUserEmail.setText("邮箱：未认证");
            }
            permission=userInfo.getPermission();
            if(permission==1001){
                tvUserPermission.setText("普通用户");
            }else if (permission==1011){
                tvUserPermission.setText("管理人员");
                queryApplyInfo();
            }
            Log.e("MainActivity",userInfo.getUsername()+"==="+userInfo.getMobilePhoneNumber()+"==="+userInfo.getPermission());
        }else {
            Log.e("MainActivity","userInfoIsNull");
        }
        //链接即使通讯IM
        BmobIM.connect(userInfo.getObjectId(), new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    ToastUitls.showShortToast(getApplicationContext(),"用户连接通讯服务器成功");
                    Log.e("BmobIM.connect","用户连接通讯服务器成功");
                }else {
                    ToastUitls.showLongToast(getApplicationContext(),"用户连接通讯服务器失败"+e.getMessage()+e.getErrorCode());
                    Log.e("BmobIM.connect","用户连接通讯服务器失败"+e.getMessage()+e.getErrorCode());
                }
            }
        });
        initMenuListView();
    }
    private void initViews(){
        mToolbar= (Toolbar) findViewById(R.id.main_toolbar);
        linearMenu= (LinearLayout) findViewById(R.id.llMenu);
        drawerLayout= (DrawerLayout) findViewById(R.id.dl_Menu);
        //
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView_main_classroom);
        //menu
        tvUserName= (TextView) findViewById(R.id.menu_tv_username);
        tvUserPhone= (TextView) findViewById(R.id.menu_tv_phone);
        tvUserEmail= (TextView) findViewById(R.id.menu_tv_email);
        tvUserPermission=(TextView) findViewById(R.id.menu_tv_user_permission);
        listView= (ListView) findViewById(R.id.lv_user_menu);
    }
    private void initToolBarAndDrawableLayout(){
        setSupportActionBar(mToolbar);
//      决定toolbar左侧的图标是否可以点击
        getSupportActionBar().setHomeButtonEnabled(true);
//      给左上角图标设置一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//      显示标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToggle = new ActionBarDrawerToggle(this,drawerLayout,mToolbar, R.string.toggle_open, R.string.toggle_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
    }
    private void getDataFromServer(){
        BmobQuery<RoomInfo> roomInfoBmobQuery=new BmobQuery<RoomInfo>();

        roomInfoBmobQuery.setLimit(2017);
        roomInfoBmobQuery.findObjects(new FindListener<RoomInfo>() {
            @Override
            public void done(List<RoomInfo> list, BmobException e) {
                if(e==null){
                    for(RoomInfo roomInfo:list){
                        roomInfoArrayList.add(roomInfo);
                    }
                    ToastUitls.showLongToast(getApplicationContext(),"查询到"+list.size()+"条数据");
                    Log.e("MainActivity","查询到"+list.size()+"条数据");
                    initRecyclerView();
                }else {
                    ToastUitls.showLongToast(getApplicationContext(),"查询失败,"+e.getMessage());
                }
            }
        });
    }
    private void initRecyclerView(){
        Log.e("initRecyclerView","调用了");
        recyclerView.setHasFixedSize(true);
        HashMap<String,Integer> spaceHashMap=new HashMap<String,Integer>();
        spaceHashMap.put(RecyclerViewSpaceItemDecoration.TOP_DECORATION,0);
        spaceHashMap.put(RecyclerViewSpaceItemDecoration.BOTTOM_DECORATION,8);
        spaceHashMap.put(RecyclerViewSpaceItemDecoration.LEFT_DECORATION,8);
        spaceHashMap.put(RecyclerViewSpaceItemDecoration.RIGHT_DECORATION,8);
        recyclerView.addItemDecoration(new RecyclerViewSpaceItemDecoration(spaceHashMap));
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        roomRecyclerAdapter=new ClassRoomRecyclerAdapter(roomInfoArrayList,getApplicationContext());
        roomRecyclerAdapter.setOnRecyclerViewItemClickListener(new ClassRoomRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Intent intent=new Intent(MainActivity.this,RoomDetialsActivity.class);
                intent.putExtra("roomInfo",roomInfoArrayList.get(postion));
                startActivity(intent);
                ToastUitls.showLongToast(getApplicationContext(),"单点击了第"+postion+"个view");
            }
        });
        roomRecyclerAdapter.setOnRecyclerViewItemLongClickListener(new ClassRoomRecyclerAdapter.OnRecyclerViewItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int postion) {
                if(permission==1011){
                    showAdminDialog(view,postion);
                }
            }
        });
//        roomRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(roomRecyclerAdapter);
    }
    private void initMenuListView(){
        if(permission==1001){
            menuListAdapter=new MenuListAdapter(user,getApplicationContext());
            listView.setOnItemClickListener(new UserItemClickListener());
        }else if (permission==1011){
            menuListAdapter=new MenuListAdapter(admin,getApplicationContext());
            listView.setOnItemClickListener(new AdminItemClickListener());
        }
        listView.setAdapter(menuListAdapter);
    }
    private void showAdminDialog(View view,final int position){
        TextView tvDelete,tvUpdate,tvCannotUse;
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        final AlertDialog dialog=builder.create();
        View dialogPermission=View.inflate(getApplicationContext(),R.layout.custom_dialog_admin,null);
        tvDelete= (TextView) dialogPermission.findViewById(R.id.tv_admin_delete);
        tvUpdate= (TextView) dialogPermission.findViewById(R.id.tv_admin_update);
        tvCannotUse=(TextView) dialogPermission.findViewById(R.id.tv_admin_jin);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String objectId=roomInfoArrayList.get(position).getObjectId();
                RoomInfo roomInfo=new RoomInfo();
                roomInfo.setObjectId(objectId);
                roomInfo.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            roomInfoArrayList.remove(position);
                            roomRecyclerAdapter.notifyDataSetChanged();
                            roomRecyclerAdapter.notifyItemRemoved(position);
                            roomRecyclerAdapter.notifyItemRangeChanged(0,roomInfoArrayList.size());
                            ToastUitls.showShortToast(getApplicationContext(),"删除成功");
                            dialog.dismiss();
                        }else {
                            ToastUitls.showShortToast(getApplicationContext(),"删除失败"+e.getMessage());
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String objectId=roomInfoArrayList.get(position).getObjectId();
                RoomInfo roomInfo=roomInfoArrayList.get(position);
                showUpdateDialog(roomInfo,position);
                dialog.dismiss();
            }
        });
        tvCannotUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String obejectId=roomInfoArrayList.get(position).getObjectId();
                RoomInfo roomInfo=roomInfoArrayList.get(position);
                roomInfo.setStateOfRoom("禁用中");
                roomInfo.update(obejectId,new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            ToastUitls.showShortToast(getApplicationContext(),"操作成功");
                            roomRecyclerAdapter.notifyItemChanged(position);
                            roomRecyclerAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }else {
                            ToastUitls.showLongToast(getApplicationContext(),"操作失败"+e.getMessage());
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.setView(dialogPermission);
        dialog.show();
    }
    private void showUpdateDialog(final RoomInfo roomInfo, final int position){
        final EditText etUpRoomSource,etUpState,etUpSize;
        Button btnUpdate;
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        View view=View.inflate(getApplicationContext(),R.layout.custom_dialog_update,null);
        builder.setView(view);
        etUpRoomSource= (EditText) view.findViewById(R.id.et_update_source);
        etUpState= (EditText) view.findViewById(R.id.et_update_state);
        etUpSize= (EditText) view.findViewById(R.id.et_update_size);
        btnUpdate= (Button) view.findViewById(R.id.btn_update_roomInfo);
        final Dialog dialog=builder.create();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomUpSource=etUpRoomSource.getText().toString();
                roomUpState=etUpState.getText().toString();
                upSize=etUpSize.getText().toString();
               updateRoomInfo(roomUpSource,roomUpState,upSize,roomInfo,position);
                if(isUpdateSuccess){
                    ToastUitls.showLongToast(getApplicationContext(),"更新成功");
                    dialog.dismiss();
                }else {
                    ToastUitls.showLongToast(getApplicationContext(),"更新失败");
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
    private Boolean updateRoomInfo(String source, String state, String size, RoomInfo roomInfo, final int position){
            if(!source.isEmpty()){
                roomInfo.setResourceOfRoom(source);
            }
            if(!state.isEmpty()){
                roomInfo.setStateOfRoom(state);
            }
            if(!size.isEmpty()){
                roomInfo.setPersonsOfRoom(Integer.parseInt(size));
            }
            String objectId=roomInfo.getObjectId();
             roomInfo.update(objectId, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                         if(e==null){
                             roomRecyclerAdapter.notifyItemRemoved(position);
                             roomRecyclerAdapter.notifyDataSetChanged();
                             isUpdateSuccess=true;
                             Log.e("updateRoomInfo","==="+isUpdateSuccess);
                         }else {
                             isUpdateSuccess=false;
                             Log.e("updateRoomInfo","==="+isUpdateSuccess);
                         }
                }
            });
        return isUpdateSuccess;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        BmobIM.getInstance().addMessageListHandler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.delActivityIntoActManager(MainActivity.this);
    }
    class UserItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i){
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    logOut();
                    break;

            }
        }
    }
    class AdminItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i){
                case 0:
                    break;
                case 1:
                    break;
                case 2:

                    break;
                case 3:
                    handerAppylyInfos();
                    break;
                case 4:
                    oneKeyRelease();
                    drawerLayout.closeDrawers();
                    break;
                case 5:
                    logOut();
                    break;
            }
        }
    }
    private void handerAppylyInfos(){
        Intent intent=new Intent(MainActivity.this,ApplyHandleActivity.class);
        intent.putExtra("applyInfos",applyRoomList);
        startActivity(intent);
    }
    private void oneKeyRelease(){
        List<BmobObject> roomInfos=new ArrayList<BmobObject>();
        for(int i=0;i<roomInfoArrayList.size();i++){
            RoomInfo roomInfo=roomInfoArrayList.get(i);
            roomInfo.setStateOfRoom("空闲中");
            roomInfo.setUsingTime("");
            roomInfo.setUsingPerson("");
            roomInfo.setUsingReason("");
            roomInfo.setUsingPersonid("");
            roomInfo.setUsingPersonAvatar("");
            roomInfos.add(roomInfo);
        }
        new BmobBatch().updateBatch(roomInfos).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if(e==null){
                    Log.e("BmobBatch()","======操作成功");
                    Log.e("BmobBatch()","=========size"+list.size());
                    for(int j=0;j<list.size();j++){
                        BatchResult result=list.get(j);
                        BmobException ex=result.getError();
                        if(ex!=null) {
                            ToastUitls.showLongToast(getApplicationContext(),"一键空闲操作失败(部分数据不成功)");
                            Log.e("BmobBatch","ex========部分数据不成功"+j);
                        }else {
                            Log.e("BmobBatch","ex========部分数据成功"+j);
                            roomRecyclerAdapter.notifyItemChanged(j);
                            if(j==list.size()-1){
                                roomRecyclerAdapter.notifyDataSetChanged();
                                ToastUitls.showShortToast(getApplicationContext(),"操作成功");
                            }
                        }
                    }
                }else {
                    ToastUitls.showLongToast(getApplicationContext(),"操作失败"+e.getMessage());
                    Log.e("BmobBatch()","操作失败"+e.getMessage());
                }
            }
        });
    }
    private void logOut(){
        //即使通讯连接断开
        BmobIM.getInstance().disConnect();
        //用户注销登录
        BmobUser.logOut();
        BmobUser currentUser=BmobUser.getCurrentUser();
        if(currentUser==null){
            ToastUitls.showShortToast(getApplicationContext(),"退出成功");
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
    }
    private void queryApplyInfo(){
        BmobQuery<RoomInfo> query=new BmobQuery<RoomInfo>();
        applyRoomList=new ArrayList<RoomInfo>();
        query.addWhereEqualTo("stateOfRoom","申请中");
        query.setLimit(2017);
        query.findObjects(new FindListener<RoomInfo>() {
            @Override
            public void done(List<RoomInfo> list, BmobException e) {
                if(e==null){
                    for(RoomInfo info:list){
                        applyRoomList.add(info);
                    }
                }
            }
        });
    }
}
