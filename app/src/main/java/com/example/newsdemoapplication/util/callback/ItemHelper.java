package com.example.newsdemoapplication.util.callback;

//列表元素选中 交换 撤销 清除回调
public interface ItemHelper {
    void itemMoved(int oldPosition,int newPosition);
    void itemDismiss(int position);

    void itemSelected(int position);

    void itemClear(int position);
}
