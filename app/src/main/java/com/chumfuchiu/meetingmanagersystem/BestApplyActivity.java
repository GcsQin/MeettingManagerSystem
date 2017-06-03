package com.chumfuchiu.meetingmanagersystem;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chumfuchiu.meetingmanagersystem.bean.RoomInfo;
import com.chumfuchiu.meetingmanagersystem.utils.ToastUitls;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class BestApplyActivity extends AppCompatActivity {
    Spinner spinner;
    EditText etMin,etMax;
    Button btn;
    ImageView imageView;
    ListView listView;
    String[] spItems=new String[]{"逸夫楼","机电楼","教学楼"};
    String build;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_apply);
        initViews();
        initSpinner();
    }
    private void initViews(){
        spinner= (Spinner) findViewById(R.id.sp_bestquery);
        etMin= (EditText) findViewById(R.id.et_bestquery_min);
        etMax= (EditText) findViewById(R.id.et_bestquery_max);
        btn= (Button) findViewById(R.id.btn_bestapply_query);
        imageView= (ImageView) findViewById(R.id.iv_bestquery_nomatch);
        listView= (ListView) findViewById(R.id.lv_best_query);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bestQuery();
            }
        });
    }
    private void initSpinner(){
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spItems);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                build=spItems[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                build="逸夫楼";
            }
        });
    }
    private void bestQuery(){
        String qBuild=build;
        final String qMin=etMin.getText().toString();
        final String qMax=etMax.getText().toString();
        if(qBuild.isEmpty()){
            qBuild="逸夫楼";
        }
        if(qMax.isEmpty()|qMin.isEmpty()){
            ToastUitls.showLongToast(getApplicationContext(),"请输入的教室最大最小容量");
            return;
        }
        if(Integer.parseInt(qMin)<0|Integer.parseInt(qMax)<0){
            ToastUitls.showLongToast(getApplicationContext(),"输入的教室容量不合法");
            return;
        }
        if(Integer.parseInt(qMin)>=Integer.parseInt(qMax)){
            ToastUitls.showLongToast(getApplicationContext(),"最小容量不能大于等于最大容量");
            return;
        }
        Log.e("查询条件",qBuild+Integer.parseInt(qMin)+Integer.parseInt(qMax));
        BmobQuery<RoomInfo> query1=new BmobQuery<RoomInfo>();
        query1.addWhereEqualTo("buildingOfRoom",qBuild);
        BmobQuery<RoomInfo> query2=new BmobQuery<RoomInfo>();
        query2.addWhereLessThan("personsOfRoom",Integer.parseInt(qMax));
        BmobQuery<RoomInfo> query3=new BmobQuery<RoomInfo>();
        query3.addWhereGreaterThanOrEqualTo("personsOfRoom",Integer.parseInt(qMin));
        List<BmobQuery<RoomInfo>> andQuery=new ArrayList<BmobQuery<RoomInfo>>();
        andQuery.add(query1);
        andQuery.add(query2);
        andQuery.add(query3);
        BmobQuery<RoomInfo> query=new BmobQuery<RoomInfo>();
        query.and(andQuery);
        query.setLimit(2017);
        query.findObjects(new FindListener<RoomInfo>() {
            @Override
            public void done(List<RoomInfo> list, BmobException e) {
                if(e==null){
                    ToastUitls.showLongToast(getApplicationContext(),"查询到"+list.size()+"条数据");
                    Log.e("List<BmobQuery>","查询到"+list.size()+"条数据");
                    if(list.size()==0){
                        imageView.setVisibility(View.VISIBLE);
                    }else {
                        showListView(list);
                    }
                }else {
                    ToastUitls.showLongToast(getApplicationContext(),"查询失败"+e.getMessage());
                }
            }
        });
    }
    private void showListView(final List<RoomInfo> xlist){
        listView.setAdapter(new BestQueryAdapter(xlist));
        listView.setVisibility(View.VISIBLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(BestApplyActivity.this,RoomDetialsActivity.class);
                intent.putExtra("roomInfo",xlist.get(i));
                startActivity(intent);
            }
        });
    }
    class BestQueryAdapter extends BaseAdapter{
        List<RoomInfo> mList;

        public BestQueryAdapter(List<RoomInfo> mList) {
            this.mList = mList;
        }
        @Override
        public int getCount() {
            return mList.size();
        }
        @Override
        public RoomInfo getItem(int i) {
            return mList.get(i);
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
                view=View.inflate(getApplicationContext(),R.layout.item_listview_bestquery,null);
                holder.tvBuild= (TextView) view.findViewById(R.id.tv_bestquery_buildroom);
                holder.tvSize= (TextView) view.findViewById(R.id.tv_bestquery_size);
                holder.tvSource= (TextView) view.findViewById(R.id.tv_bestquery_source);
                holder.tvState= (TextView) view.findViewById(R.id.tv_bestquery_state);
                view.setTag(holder);
            }else {
                holder= (ViewHolder) view.getTag();
            }
            holder.tvBuild.setText(getItem(i).getBuildingOfRoom()+getItem(i).getNumberOfRoom());
            holder.tvState.setText(getItem(i).getStateOfRoom());
            holder.tvSource.setText(getItem(i).getResourceOfRoom());
            holder.tvSize.setText("教室容量："+getItem(i).getPersonsOfRoom());
            if(getItem(i).getStateOfRoom().equals("使用中")){
                holder.tvState.setTextColor(Color.parseColor("#139D57"));
            }else if(getItem(i).getStateOfRoom().equals("空闲中")){
                holder.tvState.setTextColor(Color.parseColor("#C7C4C9"));
            }else if (getItem(i).getStateOfRoom().equals("申请中")){
                holder.tvState.setTextColor(Color.parseColor("#FEC7DE"));
            }else {
                holder.tvState.setTextColor(Color.parseColor("#ff0000"));
            }
            return view;
        }
        class ViewHolder{
            TextView tvBuild,tvSize,tvSource,tvState;
        }
    }


}
