package com.example.newsdemoapplication.util.callback;

public interface ItemHelper {
    void itemMoved(int oldPosition,int newPosition);
    void itemDismiss(int position);

    void itemSelected(int position);

    void itemClear(int position);
}
