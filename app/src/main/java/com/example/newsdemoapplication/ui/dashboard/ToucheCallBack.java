package com.example.newsdemoapplication.ui.dashboard;

import android.app.Service;
import android.graphics.Color;
import android.os.Vibrator;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ToucheCallBack extends ItemTouchHelper.Callback {

    private ItemHelper itemHelper;

    public ToucheCallBack(ItemHelper itemHelper){

        this.itemHelper = itemHelper;
    }

    //声明不同转台下的移动方向
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        int dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
//        int swipedFlags = ItemTouchHelper.START|ItemTouchHelper.END;
        int swipedFlags = 0;
        return makeMovementFlags(dragFlags,swipedFlags);
    }

    // 拖动的条目从旧位置--到新位置时调用
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        itemHelper.itemMoved(viewHolder.getLayoutPosition(),target.getLayoutPosition());
        return false;
    }

    // 滑动到消失调用
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//        itemHelper.itemDismiss(viewHolder.getLayoutPosition());
    }

    /**
     * true --开启长按
     * false --关闭长按拖动 默认开启
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    //状态改变时调用 比如正在滑动，正在拖动
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        //不是空闲状态--背景加深
        if(actionState!= ItemTouchHelper.ACTION_STATE_IDLE){
            viewHolder.itemView.setBackgroundColor(Color.GRAY);
        }
        if(viewHolder!=null)
        itemHelper.itemSelected(viewHolder.getLayoutPosition());
    }

    //滑动完，拖动完调用
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(0);
        itemHelper.itemClear(viewHolder.getLayoutPosition());
    }

}