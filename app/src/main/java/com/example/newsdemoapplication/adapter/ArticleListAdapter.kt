package com.example.newsdemoapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.callback.OnItemClickListener
import com.example.newsdemoapplication.ui.test.TestFragment
import com.example.newsdemoapplication.util.Subsection_ID
import com.example.newsdemoapplication.util.backTo
import com.example.newsdemoapplication.util.navigate
import com.example.newsdemoapplication.view.DragView
import com.example.newsdemoapplication.vo.NewsVo
import com.gzp.baselib.constant.Constants
import com.lishuaihua.toast.ToastUtils.show
import java.util.*


class ArticleListAdapter(private val context: Context, private var list: MutableList<NewsVo>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.news_content_item, parent, false)
        return NewsContentViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as NewsContentViewHolder
        viewHolder.tvTitle.text = list!![position].title
        viewHolder.tvContent.text = "标题" + list!![position].title
        if (list!![position].listContent != null) {
            val l: MutableList<String> = ArrayList()
            for ((title) in list!![position].listContent!!) {
                l.add(title)
            }
            viewHolder.dragView.addAll(l, true)
            viewHolder.dragView.mOnItemClickListener = object : OnItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    show("")
                    TestFragment.instance.navigate(R.id.action_navigation_test_to_photoViewPagerFragment){ bundle ->
                        bundle.putLong(Subsection_ID,1)
                    }
                }

                override fun onItemLongClick(view: View, position: Int) {}
            }
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    fun setData(listNews: List<NewsVo>?) {
        if (list != null && !list!!.isEmpty()) {
            list!!.clear()
            notifyDataSetChanged()
        } else {
            list = ArrayList()
        }
        if (listNews != null) {
            list!!.addAll(listNews)
        }
        notifyDataSetChanged()
    }

    class NewsContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView by lazy {
            itemView.findViewById(R.id.tv_title)
        }
        val tvContent: TextView by lazy {
            itemView.findViewById(R.id.tv_content)
        }
        val dragView: DragView by lazy {
            itemView.findViewById(R.id.drag_view)
        }
        fun setSelected(selected: Boolean?) {}

        init {
            tvTitle
            tvContent
            dragView
        }
    }
}