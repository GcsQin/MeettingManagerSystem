package com.chumfuchiu.meetingmanagersystem;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.chumfuchiu.meetingmanagersystem.chat.ChatAdapter;
import com.chumfuchiu.meetingmanagersystem.chat.OnRecyclerViewListener;
import com.chumfuchiu.meetingmanagersystem.utils.ToastUitls;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.BmobRecordManager;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.exception.BmobException;

public class ChatActivity extends AppCompatActivity  implements ObseverListener,MessageListHandler{
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    EditText etMsg;
    Button btnSendMsg;
    ChatAdapter adapter;
    LinearLayout ll_chat_root;
    BmobIMConversation c;
    BmobRecordManager recordManager;
    protected LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bundle bundle=getBundle();
        c= BmobIMConversation.obtain(BmobIMClient.getInstance(),(BmobIMConversation)bundle.getSerializable("c"));
        initViews();
        initSwipLayout();
    }
    private void initViews(){
        recyclerView=(RecyclerView) findViewById(R.id.chat_rc_view);
        refreshLayout=(SwipeRefreshLayout) findViewById(R.id.chat_sw_refresh);
        etMsg=(EditText) findViewById(R.id.chat_et_msg);
        btnSendMsg=(Button) findViewById(R.id.chat_btn_send);
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        ll_chat_root= (LinearLayout) findViewById(R.id.ll_chat_root);
    }
    private void initSwipLayout(){
        refreshLayout.setEnabled(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ChatAdapter(ChatActivity.this,c);
        recyclerView.setAdapter(adapter);
        ll_chat_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ll_chat_root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                refreshLayout.setRefreshing(true);
                //自动刷新
                queryMessages(null);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobIMMessage msg = adapter.getFirstMessage();
                queryMessages(msg);
            }
        });
        //设置RecyclerView的点击事件
        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Logger.i(""+position);
            }

            @Override
            public boolean onItemLongClick(int position) {
                //这里省了个懒，直接长按就删除了该消息
                c.deleteMessage(adapter.getItem(position));
                adapter.remove(position);
                return true;
            }
        });

    }
    /**
     * 发送文本消息
     */
    private void sendMessage(){
        String text=etMsg.getText().toString();
        if(TextUtils.isEmpty(text.trim())){
            ToastUitls.showLongToast(getApplicationContext(),"请输入内容");
            return;
        }
        BmobIMTextMessage msg =new BmobIMTextMessage();
        msg.setContent(text);
        //可设置额外信息
        Map<String,Object> map =new HashMap<>();
        map.put("level", "1");//随意增加信息
        msg.setExtraMap(map);
        c.sendMessage(msg, listener);
    }
    /**
     * 消息发送监听器
     */
    public MessageSendListener listener =new MessageSendListener() {

        @Override
        public void onProgress(int value) {
            super.onProgress(value);
            //文件类型的消息才有进度值
            Logger.i("onProgress："+value);
        }

        @Override
        public void onStart(BmobIMMessage msg) {
            super.onStart(msg);
            adapter.addMessage(msg);
            etMsg.setText("");
            scrollToBottom();
        }

        @Override
        public void done(BmobIMMessage msg, BmobException e) {
            adapter.notifyDataSetChanged();
            etMsg.setText("");
            scrollToBottom();
            if (e != null) {
                ToastUitls.showLongToast(getApplicationContext(),""+e.getMessage());
            }
        }
    };
    private void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, 0);
    }
    /**首次加载，可设置msg为null，下拉刷新的时候，默认取消息表的第一个msg作为刷新的起始时间点，默认按照消息时间的降序排列
     */
    public void queryMessages(BmobIMMessage msg){
        c.queryMessages(msg, 10, new MessagesQueryListener() {
            @Override
            public void done(List<BmobIMMessage> list, BmobException e) {
                refreshLayout.setRefreshing(false);
                if (e == null) {
                    if (null != list && list.size() > 0) {
                        adapter.addMessages(list);
                        layoutManager.scrollToPositionWithOffset(list.size() - 1, 0);
                    }
                } else {
                    ToastUitls.showLongToast(getApplicationContext(),e.getMessage()+"("+e.getErrorCode()+")");
                }
            }
        });
    }
    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        for (int i=0;i<list.size();i++){
            addMessage2Chat(list.get(i));
        }
    }
    /**接收到聊天消息
     * @param event
     */
    @Subscribe
    public void onEventMainThread(MessageEvent event){
        addMessage2Chat(event);
    }
    @Subscribe
    public void onEventMainThread(OfflineMessageEvent event){
        Map<String,List<MessageEvent>> map =event.getEventMap();
        if(map!=null&&map.size()>0){
            //只获取当前聊天对象的离线消息
            List<MessageEvent> list = map.get(c.getConversationId());
            if(list!=null && list.size()>0){
                for (int i=0;i<list.size();i++){
                    addMessage2Chat(list.get(i));
                }
            }
        }
    }
    /**添加消息到聊天界面中
     * @param event
     */
    private void addMessage2Chat(MessageEvent event){
        BmobIMMessage msg =event.getMessage();
        if(c!=null && event!=null && c.getConversationId().equals(event.getConversation().getConversationId()) //如果是当前会话的消息
                && !msg.isTransient()){//并且不为暂态消息
            if(adapter.findPosition(msg)<0){//如果未添加到界面中
                adapter.addMessage(msg);
                //更新该会话下面的已读状态
                c.updateReceiveStatus(msg);
            }
            scrollToBottom();
        }else{
            Logger.i("不是与当前聊天对象的消息");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //锁屏期间的收到的未读消息需要添加到聊天界面中
        addUnReadMessage();
        //添加页面消息监听器
        BmobIM.getInstance().addMessageListHandler(this);
        // 有可能锁屏期间，在聊天界面出现通知栏，这时候需要清除通知
        BmobNotificationManager.getInstance(this).cancelNotification();
    }
    /**
     * 添加未读的通知栏消息到聊天界面
     */
    private void addUnReadMessage(){
        List<MessageEvent> cache = BmobNotificationManager.getInstance(this).getNotificationCacheList();
        if(cache.size()>0){
            int size =cache.size();
            for(int i=0;i<size;i++){
                MessageEvent event = cache.get(i);
                addMessage2Chat(event);
            }
        }
        scrollToBottom();
    }
    @Override
    protected void onPause() {
        //移除页面消息监听器
        BmobIM.getInstance().removeMessageListHandler(this);
        super.onPause();
    }
    /**
     * 显示软键盘
     */
    public void showSoftInputView() {
        if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                        .showSoftInput(etMsg, 0);
        }
    }
    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    @Override
    protected void onDestroy() {
        //清理资源
        if(recordManager!=null){
            recordManager.clear();
        }
        //更新此会话的所有消息为已读状态
        if(c!=null){
            c.updateLocalCache();
        }
        hideSoftInputView();
        super.onDestroy();
    }

    //------------------
    public Bundle getBundle() {
        if (getIntent() != null && getIntent().hasExtra(getPackageName()))
            return getIntent().getBundleExtra(getPackageName());
        else
            return null;
    }
}
