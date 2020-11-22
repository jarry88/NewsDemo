package com.example.newsdemoapplication.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.newsdemoapplication.Util;
import com.example.newsdemoapplication.adapter.ListDragAdapter;
import com.example.newsdemoapplication.ui.dashboard.ItemHelper;

public class ItemDragHelperCallBack extends ItemTouchHelper.Callback {

    private com.example.newsdemoapplication.ui.dashboard.ItemHelper ItemHelper;

    public ItemDragHelperCallBack(ItemHelper ItemHelper) {
        this.ItemHelper = ItemHelper;
    }

    /**
     * 返回可以滑动的方向
     *
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        int dragFlags;
        if (manager instanceof GridLayoutManager || manager instanceof StaggeredGridLayoutManager) {
            //网格布局管理器允许上下左右拖动
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        } else {
            //其他布局管理器允许上下拖动
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }
        return makeMovementFlags(dragFlags, 0);
    }

    /**
     * 拖拽到新位置时候的回调方法
     *
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

        //不同Type之间不允许移动
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        if (ItemHelper != null) {
            ItemHelper.itemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }else{
            Util.Loge("ss");
        }
        return true;
    }
    /**
     * 当用户左右滑动的时候执行的方法
     *
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Util.Loge("swipe");
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof ListDragAdapter.MyDragViewHolder) {
            // Let the view holder know that this item is being moved or dragged
            ListDragAdapter.MyDragViewHolder itemViewHolder = (ListDragAdapter.MyDragViewHolder) viewHolder;
            itemViewHolder.setSelected(false);
            //选中状态回调
//                itemViewHolder.onItemSelected();
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // We only want the active item to change

        if(actionState ==ItemTouchHelper.ANIMATION_TYPE_DRAG){
            Util.Loge("drag");
        }
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ListDragAdapter.MyDragViewHolder) {
                // Let the view holder know that this item is being moved or dragged
                ListDragAdapter.MyDragViewHolder itemViewHolder = (ListDragAdapter.MyDragViewHolder) viewHolder;
                itemViewHolder.setSelected(true);
                ItemHelper.itemSelected();

                //选中状态回调
//                itemViewHolder.onItemSelected();
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

}
