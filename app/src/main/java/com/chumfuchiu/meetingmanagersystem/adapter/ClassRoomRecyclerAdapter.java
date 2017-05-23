package com.chumfuchiu.meetingmanagersystem.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chumfuchiu.meetingmanagersystem.R;
import com.chumfuchiu.meetingmanagersystem.bean.RoomInfo;
import com.chumfuchiu.meetingmanagersystem.utils.ToastUitls;

import java.util.ArrayList;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by ChumFuchiu on 2017/5/17.
 */

public class ClassRoomRecyclerAdapter extends RecyclerView.Adapter<ClassRoomRecyclerAdapter.ClassRoomViewHolder>
        implements View.OnClickListener,View.OnLongClickListener{
    private ArrayList<RoomInfo> mRoomInfoArrayList;
    private Context mContext;

    public ClassRoomRecyclerAdapter(ArrayList<RoomInfo> mRoomInfoArrayList, Context mContext) {
        this.mRoomInfoArrayList = mRoomInfoArrayList;
        this.mContext = mContext;
        Log.e("ClassRoomRecycler","size="+mRoomInfoArrayList.size());
    }

    @Override
    public ClassRoomRecyclerAdapter.ClassRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView=View.inflate(mContext,R.layout.item_recyclerview_roominfo,null);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        return new ClassRoomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ClassRoomRecyclerAdapter.ClassRoomViewHolder holder, int position) {
        RoomInfo roomInfo=mRoomInfoArrayList.get(position);
        Log.e("onBindViewHolder","info=="+roomInfo.getBuildingOfRoom()+roomInfo.getNumberOfRoom()+roomInfo.getResourceOfRoom()
        +roomInfo.getStateOfRoom()+roomInfo.getPersonsOfRoom());
        holder.tvBuilding.setText(roomInfo.getBuildingOfRoom());
        holder.tvState.setText(roomInfo.getStateOfRoom());
        holder.tvPersons.setText(""+roomInfo.getPersonsOfRoom());
        holder.tvResource.setText(roomInfo.getResourceOfRoom());
        holder.tvRoomId.setText(roomInfo.getNumberOfRoom());
        if(roomInfo.getStateOfRoom().equals("使用中")){
            holder.rootView.setBackgroundColor(Color.parseColor("#139D57"));
        }else if(roomInfo.getStateOfRoom().equals("空闲中")){
            holder.rootView.setBackgroundColor(Color.parseColor("#C7C4C9"));
        }else {
            holder.rootView.setBackgroundColor(Color.parseColor("#ff0000"));
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mRoomInfoArrayList.size();
    }
    //单点击事件。
    public interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,int postion);
    }
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener listener){
        this.onRecyclerViewItemClickListener=listener;
    }
    @Override
    public void onClick(View view) {
        if(onRecyclerViewItemClickListener!=null){
            onRecyclerViewItemClickListener.onItemClick(view,(Integer)view.getTag());
        }
    }
    //长点击事件
    public interface  OnRecyclerViewItemLongClickListener{
        void onItemLongClick(View view,int postion);
    }
    private OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener;
    public void setOnRecyclerViewItemLongClickListener(OnRecyclerViewItemLongClickListener longClickListener){
        this.onRecyclerViewItemLongClickListener=longClickListener;
    }
    @Override
    public boolean onLongClick(View view) {
        if(onRecyclerViewItemClickListener!=null){
            onRecyclerViewItemLongClickListener.onItemLongClick(view,(Integer)view.getTag());
        }
        return false;
    }
    public void addItem(final int position){

    }
    public void deleteItem(){

    }
    //
    class ClassRoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvBuilding,tvRoomId,tvState,tvResource,tvPersons;
        LinearLayout rootView;
        public ClassRoomViewHolder(View itemView) {
            super(itemView);
            rootView= (LinearLayout) itemView.findViewById(R.id.ll_item_recycler_view_root);
            tvBuilding= (TextView) itemView.findViewById(R.id.item_roomBuliding);
            tvRoomId= (TextView) itemView.findViewById(R.id.item_roomID);
            tvState= (TextView) itemView.findViewById(R.id.item_roomState);
            tvResource= (TextView) itemView.findViewById(R.id.item_roomResource);
            tvPersons= (TextView) itemView.findViewById(R.id.item_roomPersons);
        }
    }
}
