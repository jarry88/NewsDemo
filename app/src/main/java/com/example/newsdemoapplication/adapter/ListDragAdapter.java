package com.example.newsdemoapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
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
    private int currSelectPosition;
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
        mHolder.setSelected(holder.getAdapterPosition()==currSelectPosition);
        if(mOnItemClickListener!=null){
            mHolder.clItem.setOnClickListener(v -> {
                mOnItemClickListener.onItemClick(mHolder.itemView,
                        holder.getAdapterPosition());
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
    public void setCurrSelectPosition(int position){
        Log.e("TAG", "setCurrSelectPosition: "+position+list.get(position));
        int old =currSelectPosition;
        currSelectPosition =position;
        notifyItemChanged(old);
        notifyItemChanged(currSelectPosition);
    }
}
