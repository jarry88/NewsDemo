package com.example.newsdemoapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsdemoapplication.R;
import com.example.newsdemoapplication.callback.OnItemClickListener;

import java.util.List;

public class ListDragAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> list;
    private OnItemClickListener mOnItemClickListener;
    public ListDragAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.tag_item, parent, false);
        return new MyDragViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyDragViewHolder mHolder = (MyDragViewHolder) holder;
        mHolder.tvTag.setText(list.get(position));
//        mHolder.clItem.setOnLongClickListener(v -> {
//            //获取系统震动服务
//            Vibrator vib = (Vibrator) mContext.getSystemService(Service.VIBRATOR_SERVICE);
//            vib.vibrate(150);
//            return false;
//        });
        if(mOnItemClickListener!=null){
            mHolder.clItem.setOnClickListener(v -> {
                mOnItemClickListener.onItemClick(mHolder.itemView,position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<String> getData() {
        return list;
    }

    public void setSelectPosition(int topPosition) {
    }

    public static class MyDragViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTag;
        private LinearLayout llItem;
        private ConstraintLayout clItem;

        public MyDragViewHolder(View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.tv_tag);
            clItem = itemView.findViewById(R.id.cl_container);
            llItem =itemView.findViewById(R.id.ll_title);
        }

        public void setSelected(Boolean selected) {
            clItem.setSelected(selected);
            llItem.setBackgroundColor( Color.parseColor(selected?"#CDDC39":"#2196F3"));
        }
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener =onItemClickListener;
    }
}
