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
import java.util.*


class TitleAdapter     //    public ListDragAdapter(Context context, List<String> list) {
//        this.mContext = context;
//        this.list = list;
//    }
(private val mContext: Context, private val list: List<String>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //    private List<T> listData;
    private var mOnItemClickListener: OnItemClickListener? = null
    private val mConvertView: ConvertView? = null
    private var currSelectPosition :Int= 0
    private var listUrl: MutableList<String>? = null
    private var showImage = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(
                if (showImage) R.layout.image_item else R.layout.tag_item, parent, false)
        return MyDragViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mHolder = holder as MyDragViewHolder
        mHolder.setSelected(holder.getAdapterPosition() == currSelectPosition)
        if (mOnItemClickListener != null) {
            mHolder.clItem.setOnClickListener { v: View? -> mOnItemClickListener!!.onItemClick(mHolder.itemView, holder.getAdapterPosition()) }
        }
        if (showImage) {
            if (mHolder.imageItem != null) {
                mHolder.tvTag.text = list!![position]
                mHolder.imageItem.loadUrl(listUrl!![position])
            }
        } else {
            mHolder.tvTag.text = list!![position]
        }
        mConvertView?.convert(mHolder, list!![position])
    }

    override fun getItemCount(): Int {
        return list!!.size
        //        if(!showImage) return list.size() ;else if(listUrl==null){ return 0;} else return listUrl.size();
    }

    val data: List<String>?
        get() = list
    val urlData: List<String>?
        get() = listUrl

    //    public List<T> getListData() {
    //        return listData;
    //    }
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

    fun setCurrSelectPosition(position: Int) {
        Log.e("TAG", "setCurrSelectPosition: " + position + list!![position])
        val old = currSelectPosition
        currSelectPosition = position
        notifyItemChanged(old)
        notifyItemChanged(currSelectPosition)
    }

    fun getCurrSelectPosition() =currSelectPosition

    fun setmDatas(datas: List<String>?) {
//        if (list != null) {
//            list.clear()
//            notifyDataSetChanged()
//        }
//        list!!.addAll(datas!!)
//        notifyDataSetChanged()
    }

    internal interface ConvertView {
        fun convert(holder: MyDragViewHolder?, item: Any?)
    }
}