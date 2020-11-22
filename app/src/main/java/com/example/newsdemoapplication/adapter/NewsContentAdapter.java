package com.example.newsdemoapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsdemoapplication.R;
import com.example.newsdemoapplication.callback.OnItemClickListener;

import java.util.List;

public class NewsContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> list;
    public NewsContentAdapter (Context context,List<String> list){
        this.list =list;
        this.context =context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.news_content_item, parent, false);
        return new NewsContentAdapter.NewsContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewsContentViewHolder viewHolder =(NewsContentViewHolder) holder;
        viewHolder.tvTitle.setText(list.get(position));
        viewHolder.tvContent.setText("内容"+list.get(position)+list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class NewsContentViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvContent;

        public NewsContentViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
        }

        public void setSelected(Boolean selected) {
        }
    }
}
