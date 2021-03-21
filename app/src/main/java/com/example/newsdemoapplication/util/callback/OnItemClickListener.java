package com.example.newsdemoapplication.util.callback;

import android.view.View;
//列表元素的点击响应接口回调
public interface OnItemClickListener {
    void onItemClick(View view ,int position);
    void onItemLongClick(View view ,int position);
}
