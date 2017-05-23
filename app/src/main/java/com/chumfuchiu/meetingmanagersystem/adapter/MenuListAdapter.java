package com.chumfuchiu.meetingmanagersystem.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chumfuchiu.meetingmanagersystem.R;


/**
 * Created by ChumFuchiu on 2017/5/20.
 */

public class MenuListAdapter extends BaseAdapter {
    private String[] stringArrayList;
    private Context mContext;

    public MenuListAdapter(String[] stringArrayList, Context mContext) {
        this.stringArrayList = stringArrayList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return stringArrayList.length;
    }

    @Override
    public String getItem(int i) {
        return stringArrayList[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null){
            viewHolder=new ViewHolder();
            view=View.inflate(mContext, R.layout.item_listview_menu,null);
            viewHolder.textView= (TextView) view.findViewById(R.id.tv_listview_menu);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(""+getItem(i));
        return view;
    }
    class ViewHolder{
        TextView textView;
    }
}
