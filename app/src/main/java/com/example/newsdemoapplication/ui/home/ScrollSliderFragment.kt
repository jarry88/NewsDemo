package com.example.newsdemoapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.newsdemoapplication.Constants
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.model.NewsVo
import com.example.newsdemoapplication.util.log
import com.example.newsdemoapplication.view.DragView
import java.util.*

/**
 * 滚动的内容页
 */
class ScrollSliderFragment:Fragment() {
    val tvTitle: TextView by lazy {
        requireView().findViewById<TextView>(R.id.tv_title)
    }
    var newsVo:NewsVo?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.news_content_item_style, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(Constants.SLIDE_OBJECT) }?.apply {
            tvTitle.text = getInt(Constants.SLIDE_OBJECT).toString()
        }
        newsVo?.listContent?.let {
            log("size $it")
            val l: MutableList<String> = ArrayList()
            for ((title) in it) { l.add(title) }
            view.findViewById<DragView>(R.id.drag_view)?.let {
                it.addAll(l,true)
            }
        }
        newsVo?.title?.let {
            view.findViewById<TextView>(R.id.tv_content_title)?.text=it
                log("title $it")
        }
    }
}