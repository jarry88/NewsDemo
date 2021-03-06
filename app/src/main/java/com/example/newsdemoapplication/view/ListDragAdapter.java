package com.example.newsdemoapplication.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsdemoapplication.R;
import com.example.newsdemoapplication.util.callback.OnItemClickListener;
import com.example.newsdemoapplication.util.KUtilKt;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 拖动列表的基础适配器
 */
public class ListDragAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> list;
    private OnItemClickListener mOnItemClickListener;
    private int currSelectPosition;
    public ListDragAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.list = list;
    }
    private List<String> listUrl;
    private Boolean showImage =false;
    public void setImageView(boolean show){
        showImage=show;
        if(show){
            listUrl =new ArrayList<String >();
            for (int i=0;i<list.size();i++){
                listUrl.add(KUtilKt.randomUrl());
            }
        }
    }
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                showImage?R.layout.image_item_style //展示图片内容的样式
                        :R.layout.title_item_style, parent, false);//只展示标题文本的央视
        return new MyDragViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyDragViewHolder mHolder = (MyDragViewHolder) holder;
        mHolder.setSelected(holder.getAdapterPosition()==currSelectPosition);
        if(mOnItemClickListener!=null){
            mHolder.clItem.setOnClickListener(v -> {
                mOnItemClickListener.onItemClick(mHolder.itemView, holder.getAdapterPosition());
            });
        }
        if(showImage){
            if(mHolder.imageItem!=null){
                mHolder.tvTag.setText(list.get(position));
                KUtilKt.loadUrl(mHolder.imageItem,listUrl.get(position%7));
            }
        }else {
            mHolder.tvTag.setText(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
//        if(!showImage) return list.size() ;else if(listUrl==null){ return 0;} else return listUrl.size();
    }

    public List<String> getData() {
        return list;
    }
    public List<String> getUrlData() {
        return listUrl;
    }
    public static class MyDragViewHolder extends RecyclerView.ViewHolder {

         TextView tvTag;
         LinearLayout llItem;
         ConstraintLayout clItem;
         ImageView imageItem;

        public MyDragViewHolder(View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.tv_tag);
            clItem = itemView.findViewById(R.id.cl_container);
            llItem =itemView.findViewById(R.id.ll_title);
            imageItem =itemView.findViewById(R.id.image);
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
    public int getCurrSelectPosition(){
        return currSelectPosition;
    }
    public void setmDatas(List<String> datas) {
        if (list != null) {
            list.clear();
            this.notifyDataSetChanged();
        }else {
            list.clear();
        }
        if(datas!=null){
            list.addAll(datas);
            if(showImage) setImageView(true);
        }
        this.notifyDataSetChanged();
    }
    interface ConvertView{
        void  convert(MyDragViewHolder holder,Object item);
    }

}
