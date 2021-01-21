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
import com.example.newsdemoapplication.model.room.Subsection
import com.example.newsdemoapplication.util.loadUrl
import com.example.newsdemoapplication.util.log

class ListSubsectionDragAdapter(private val mContext:Context,var subsectionList:List<Subsection>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mOnItemClickListener: OnItemClickListener? = null
    private val mConvertView: ConvertView? = null
    var currSelectPosition :Int=0
        set(value) {
            Log.e("TAG", "setCurrSelectPosition: " + value + subsectionList[value])
            val old = field
            if(old!=value){
                field = value
                notifyItemChanged(old)
            }
            notifyItemChanged(field)
        }


    private var showImage = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(
                if (showImage) R.layout.image_item else R.layout.tag_item, parent, false)
        return MyDragViewHolder(view)
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mHolder = holder as MyDragViewHolder
        mHolder.setSelected(holder.getAdapterPosition() == currSelectPosition)
        mOnItemClickListener?.let {
            mHolder.clItem.setOnClickListener {v->
                it.onItemClick(mHolder.itemView, holder.getAdapterPosition())
            }
        }
        if (showImage) {
            if (mHolder.imageItem != null) {
                mHolder.tvTag.text = subsectionList[position].name
                mHolder.imageItem!!.loadUrl(subsectionList[position].url)
            }
        } else {
            mHolder.tvTag.text =subsectionList[position].name
        }
        mConvertView?.convert(mHolder, subsectionList[position])
    }

    override fun getItemCount()= subsectionList.size


    fun getData()=subsectionList

    class MyDragViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTag: TextView
        var llItem: LinearLayout
        var clItem: ConstraintLayout
        var imageItem: ImageView?
        fun setSelected(selected: Boolean) {
            clItem.isSelected = selected
            llItem.setBackgroundColor(Color.parseColor(if (selected) "#CDDC39" else "#2196F3"))
        }

        init {
            tvTag = itemView.findViewById(R.id.tv_tag)
            clItem = itemView.findViewById(R.id.cl_container)
            llItem = itemView.findViewById(R.id.ll_title)
            imageItem = itemView.findViewById(R.id.image)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = onItemClickListener
    }


    fun setmDatas(data: List<Subsection>) {
        subsectionList =data
        this.notifyDataSetChanged()
    }

    internal interface ConvertView {
        fun convert(holder: MyDragViewHolder?, item: Any?)
    }


}