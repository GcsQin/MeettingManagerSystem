package com.chumfuchiu.meetingmanagersystem;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.chumfuchiu.meetingmanagersystem.adapter.ConversationAdapter;
import com.chumfuchiu.meetingmanagersystem.adapter.IMutlipleItem;
import com.chumfuchiu.meetingmanagersystem.chat.OnRecyclerViewListener;
import com.chumfuchiu.meetingmanagersystem.utils.Conversation;
import com.chumfuchiu.meetingmanagersystem.utils.PrivateConversation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;

public class ConversationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    ConversationAdapter adapter;
    LinearLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_conversation);
        initViews();
        //单一布局
        IMutlipleItem<Conversation> mutlipleItem = new IMutlipleItem<Conversation>() {

            @Override
            public int getItemViewType(int postion, Conversation c) {
                return 0;
            }

            @Override
            public int getItemLayoutId(int viewtype) {
                return R.layout.item_conversation;
            }

            @Override
            public int getItemCount(List<Conversation> list) {
                return list.size();
            }
        };
        adapter = new ConversationAdapter(ConversationActivity.this,mutlipleItem,null);
        recyclerView.setAdapter(adapter);
        linearLayoutManager=new LinearLayoutManager(ConversationActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout.setEnabled(true);

    }
    private void initViews(){
        root= (LinearLayout) findViewById(R.id.ll_conversation_root);
        recyclerView=(RecyclerView) findViewById(R.id.rc_view_conversation);
        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.sw_refresh_conversation);

    }
    private void setListener(){
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                swipeRefreshLayout.setRefreshing(true);
                query();
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                PrivateConversation privateConversation= (PrivateConversation) adapter.getItem(position);
                privateConversation.onClick(ConversationActivity.this);
            }
            @Override
            public boolean onItemLongClick(int position) {
                adapter.getItem(position).onLongClick(ConversationActivity.this);
                adapter.remove(position);
                return true;
            }
        });
        adapter.setOnRecyclerViewItemClickListener(new ConversationAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                adapter.getItem(position).onClick(getApplicationContext());
            }
        });
    }
    //查询本地会话
    public void query(){
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
    //获取会话数据列表
    private List<Conversation> getConversations(){
        //添加会话
        List<Conversation> conversationList = new ArrayList<>();
        conversationList.clear();
        List<BmobIMConversation> list = BmobIM.getInstance().loadAllConversation();
        if(list!=null && list.size()>0){
            for (BmobIMConversation item:list){
                switch (item.getConversationType()){
                    case 1://私聊
                        conversationList.add(new PrivateConversation(item));
                        break;
                    default:
                        break;
                }
            }
        }
        //重新排序
        Collections.sort(conversationList);
        return conversationList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeRefreshLayout.setRefreshing(true);
        query();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    //注册离线消息接收事件
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event){
        //重新刷新列表
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
    }
    //注册消息接收事件
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        //重新获取本地消息并刷新列表
        adapter.bindDatas(getConversations());
        adapter.notifyDataSetChanged();
    }
}
