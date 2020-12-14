package com.example.newsdemoapplication.popup;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsdemoapplication.R;
import com.example.newsdemoapplication.view.DragView;
import com.lxj.easyadapter.EasyAdapter;
import com.lxj.easyadapter.ViewHolder;
import com.lxj.xpopup.core.DrawerPopupView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 自定义带列表的Drawer弹窗
 * Create by dance, at 2019/1/9
 */
public class LeftDrawerPopupView extends DrawerPopupView {
    DragView dragView;
    private List<String> list;
    public LeftDrawerPopupView(@NonNull Context context) {
        super(context);
    }
    private OnClickListener mOnClickListener;
    @Override
    protected int getImplLayoutId() {
        return R.layout.left_list_drawer;
    }
    final ArrayList<String> data = new ArrayList<>();
    @Override
    protected void onCreate() {
        dragView =findViewById(R.id.drag_view);
        dragView.setLineLayoutManager();
        dragView.addAll(list);
        findViewById(R.id.textView).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickListener!=null)
                    mOnClickListener.onClick(v);
            }
        });
        findViewById(R.id.btn_close_left).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.btn_add).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(mOnClickListener!=null)
                    mOnClickListener.onClick(v);
            }
        });

    }


    public void setData(@NotNull List<String> list) {
        this.list=list;
    }
    public void setmOnClickListener(OnClickListener onClickListener){
        mOnClickListener=onClickListener;
    }
}
