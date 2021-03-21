package com.example.newsdemoapplication.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsdemoapplication.R;
import com.example.newsdemoapplication.model.ContentVo;
import com.example.newsdemoapplication.model.NewsVo;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NewsContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<NewsVo> list;
    public NewsContentAdapter (Context context,List<NewsVo> list){
        this.list =list;
        this.context =context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.news_content_item_style, parent, false);
        return new NewsContentAdapter.NewsContentViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewsContentViewHolder viewHolder =(NewsContentViewHolder) holder;
        viewHolder.tvTitle.setText(list.get(position).getTitle());
        viewHolder.tvContent.setText("标题"+list.get(position).getTitle());
        if(list.get(position).getListContent()!=null){
            List<String> l =new ArrayList<>();
            for (ContentVo c: list.get(position).getListContent()){
                l.add(c.getTitle());
            }
            viewHolder.dragView.addAll(l,true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(@Nullable List<NewsVo> listNews) {
            if (list != null && !list.isEmpty() ) {
                list.clear();
                this.notifyDataSetChanged();
            }else {
                list=new ArrayList<NewsVo>();
            }
            if(listNews!=null){
                list.addAll(listNews);
            }
            this.notifyDataSetChanged();
    }

    public static class NewsContentViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvContent;
        private DragView dragView;

        public NewsContentViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            dragView = itemView.findViewById(R.id.drag_view);
        }

        public void setSelected(Boolean selected) {
        }
    }
}
