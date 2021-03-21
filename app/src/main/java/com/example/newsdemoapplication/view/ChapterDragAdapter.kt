package com.example.newsdemoapplication.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.util.callback.OnItemClickListener
import com.example.newsdemoapplication.model.ChapterVo
import com.example.newsdemoapplication.view.ListDragAdapter.*

class ChapterDragAdapter constructor(val context: Context,var list:MutableList<ChapterVo>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    //    private List<T> listData;
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mConvertView: ConvertView? = null
    var currSelectPosition =0
        set(value) {
            Log.e("TAG", "setCurrSelectPosition: " + value + list!![value])
            val old = field
            field = value
            notifyItemChanged(old)
            notifyItemChanged(field)
        }

    private var showImage = false


    override fun getItemCount(): Int {
        return list?.size?:0
    }

    fun getData(): MutableList<ChapterVo>? {
        return list
    }

    fun setData(l: MutableList<ChapterVo>?) =l?.let {
        list=l
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(
                if (showImage) R.layout.image_item else R.layout.title_style, parent, false)
        return MyDragViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mHolder = holder as MyDragViewHolder
        mHolder.setSelected(holder.getAdapterPosition() == currSelectPosition)
        if (mOnItemClickListener != null) {
            mHolder.clItem.setOnClickListener { v: View? ->
                mOnItemClickListener!!.onItemClick(mHolder.itemView, holder.getAdapterPosition())
            }
        }

        mHolder.tvTag.text = list?.get(position)?.chapterName
        if (mConvertView != null) {
            mConvertView!!.convert(mHolder, list?.get(position))
        }
    }

}