package com.example.newsdemoapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.callback.OnItemClickListener
import com.example.newsdemoapplication.util.loadUrl
import com.example.newsdemoapplication.util.randomUrl
import com.example.newsdemoapplication.vo.ChapterVo
import java.util.*

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

    fun setBindViewHolder(convertView: ConvertView?) {
        mConvertView = convertView
    }

    override fun getItemCount(): Int {
        return list?.size?:0
//        if(!showImage) return list.size() ;else if(listUrl==null){ return 0;} else return listUrl.size();
    }

    fun getData(): MutableList<ChapterVo>? {
        return list
    }
    fun setData(data:MutableList<ChapterVo>?) {
        list=data
        notifyDataSetChanged()
    }
    class MyDragViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTag: TextView = itemView.findViewById(R.id.tv_tag)
         val llItem: LinearLayout = itemView.findViewById(R.id.ll_title)
         val clItem: ConstraintLayout = itemView.findViewById(R.id.cl_container)
         val imageItem: ImageView? = itemView.findViewById(R.id.image)
        fun setSelected(selected: Boolean) {
            clItem.isSelected = selected
            llItem.setBackgroundColor(Color.parseColor(if (selected) "#CDDC39" else "#2196F3"))
        }

    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = onItemClickListener
    }



    interface ConvertView {
        fun convert(holder: MyDragViewHolder?, item: Any?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(
                if (showImage) R.layout.image_item else R.layout.tag_item, parent, false)
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