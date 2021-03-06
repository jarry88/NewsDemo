package com.example.newsdemoapplication.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.model.ChapterVo
import com.example.newsdemoapplication.util.callback.OnItemClickListener
import com.example.newsdemoapplication.util.callback.SimpleCallback

class ChapterDragAdapter constructor(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    //    private List<T> listData;
    var list= mutableListOf<ChapterVo>()
    var selectCallback:SimpleCallback?=null
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mConvertView: ListDragAdapter.ConvertView? = null
    var currSelectPosition =0
        set(value) {
            Log.e("TAG", "setCurrSelectPosition: " + value + list!![value])
            val old = field
            field = value
            notifyItemChanged(old)
            notifyItemChanged(field)
            selectCallback?.changeChapter(list[value].id)
        }

    private var showImage = false


    override fun getItemCount(): Int {
        return list.size ?:0
    }

    fun getData(): MutableList<ChapterVo>? {
        return list
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(
                if (showImage) R.layout.image_item_style else R.layout.title_item_style, parent, false)
        return ListDragAdapter.MyDragViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mHolder = holder as ListDragAdapter.MyDragViewHolder
        mHolder.setSelected(holder.getAdapterPosition() == currSelectPosition)
        if (mOnItemClickListener != null) {
            mHolder.clItem.setOnClickListener { v: View? ->
                mOnItemClickListener!!.onItemClick(mHolder.itemView, holder.getAdapterPosition())
            }
        }

        mHolder.tvTag.text = list.get(position).chapterName
        if (mConvertView != null) {
            mConvertView!!.convert(mHolder, list.get(position))
        }
    }

}