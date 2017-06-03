package com.chumfuchiu.meetingmanagersystem.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMMessageType;
import cn.bmob.v3.BmobUser;

/**
 * Created by ChumFuchiu on 2017/6/2.
 */

public class ChatAdapter extends RecyclerView.Adapter {
    //文本消息类型
    private final int TYPE_RECEIVE_TXT=0;
    private final int TYPE_SEND_TXT=1;
    /**
     * 显示时间间隔:10分钟
     */
    private final long TIME_INTERVAL = 10 * 60 * 1000;
    //消息的集合
    private List<BmobIMMessage> msgs=new ArrayList<>();
    //id
    private String currentUid="";
    //
    BmobIMConversation c;
    Context context;
    public ChatAdapter(Context context, BmobIMConversation c) {
        try {
            currentUid = BmobUser.getCurrentUser().getObjectId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.context=context;
        this.c =c;
    }
    //获取消息的位置
    public int findPosition(BmobIMMessage message) {
        int index = this.getCount();
        int position = -1;
        while(index-- > 0) {
            if(message.equals(this.getItem(index))) {
                position = index;
                break;
            }
        }
        return position;
    }
    //获取消息数量
    public int getCount() {
        return this.msgs == null?0:this.msgs.size();
    }
    //获取消息
    public BmobIMMessage getItem(int position){
        return this.msgs == null?null:(position >= this.msgs.size()?null:this.msgs.get(position));
    }
    //移除消息
    public void remove(int position){
        msgs.remove(position);
        notifyDataSetChanged();
    }
    //获取第一条消息
    public BmobIMMessage getFirstMessage() {
        if (null != msgs && msgs.size() > 0) {
            return msgs.get(0);
        } else {
            return null;
        }
    }
    public void addMessages(List<BmobIMMessage> messages) {
        msgs.addAll(0, messages);
        notifyDataSetChanged();
    }

    public void addMessage(BmobIMMessage message) {
        msgs.addAll(Arrays.asList(message));
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_SEND_TXT){
            return new SendTextHolder(parent.getContext(),parent,c,onRecyclerViewListener);
        }else if (viewType==TYPE_RECEIVE_TXT){
            return new ReceiveTextHolder(parent.getContext(),parent,onRecyclerViewListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder)holder).bindData(msgs.get(position));
        if(holder instanceof ReceiveTextHolder){
            ((ReceiveTextHolder)holder).showTime(shouldShowTime(position));
        }else if(holder instanceof SendTextHolder){
            ((SendTextHolder)holder).showTime(shouldShowTime(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        BmobIMMessage message = msgs.get(position);
        if(message.getMsgType().equals(BmobIMMessageType.TEXT.getType())){
            return message.getFromId().equals(currentUid) ? TYPE_SEND_TXT: TYPE_RECEIVE_TXT;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }
    private OnRecyclerViewListener onRecyclerViewListener;
    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }
    private boolean shouldShowTime(int position) {
        if (position == 0) {
            return true;
        }
        long lastTime = msgs.get(position - 1).getCreateTime();
        long curTime = msgs.get(position).getCreateTime();
        return curTime - lastTime > TIME_INTERVAL;
    }
}
