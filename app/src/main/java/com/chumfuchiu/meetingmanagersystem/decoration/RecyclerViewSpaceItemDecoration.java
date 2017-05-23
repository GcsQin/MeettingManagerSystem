package com.chumfuchiu.meetingmanagersystem.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;

/**
 * Created by ChumFuchiu on 2017/5/19.
 */

public class RecyclerViewSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private HashMap<String,Integer> mSpaceHashMap;
    public static final String TOP_DECORATION="top";
    public static final String BOTTOM_DECORATION="bottom";
    public static final String LEFT_DECORATION="left";
    public static final String RIGHT_DECORATION="right";
    public RecyclerViewSpaceItemDecoration(HashMap<String, Integer> mSpaceHashMap) {
        this.mSpaceHashMap = mSpaceHashMap;
    }



    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(mSpaceHashMap.get(TOP_DECORATION)!=null){
            outRect.top=mSpaceHashMap.get(TOP_DECORATION);
        }
        if(mSpaceHashMap.get(BOTTOM_DECORATION)!=null){
            outRect.bottom=mSpaceHashMap.get(BOTTOM_DECORATION);
        }
        if(mSpaceHashMap.get(RIGHT_DECORATION)!=null){
            outRect.right=mSpaceHashMap.get(RIGHT_DECORATION);
        }
        if(mSpaceHashMap.get(LEFT_DECORATION)!=null){
            outRect.left=mSpaceHashMap.get(LEFT_DECORATION);
        }
    }
}
