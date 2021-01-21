package com.example.newsdemoapplication.popup

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.dsl.*
import com.example.newsdemoapplication.view.ChapterDragView
import com.example.newsdemoapplication.vo.ChapterVo

class LeftChapterDrawerView(context: Context,attributeSet: AttributeSet?=null):FrameLayout(context,attributeSet) {
    private val liveDate by  lazy {
        object :LiveData<MutableList<ChapterVo>>(){}
    }
    lateinit var dragView: ChapterDragView<ChapterVo>
    lateinit var llContainer: LinearLayout
    private val contentLayout by lazy {
        LinearLayout(300, match_parent) {
            bgShape(10, R.color.yellow)
            orientation=vertical

            LinearLayout(match_parent,200) {
                orientation=vertical
                ImageView { src =R.mipmap.ic_launcher_round  }
                TextView { text="昵称：大猪哥  帐号：1388080800" }
                TextView { text="普通成员  单机单登        》》" }
            }
//            ChapterDragView(context,liveDate =liveDate ).apply {
//                layout_width= match_parent
//                layout_height= 0
//                weight=1f
//                bgShape(6,R.color.white)
//            }.also {
//                dragView=it
//                addView(it) }
            llContainer=LinearLayout {
                layout_width= match_parent
                layout_height= 0
                weight=1f
                bgShape(6,R.color.white)
            }

            LinearLayout(match_parent,120) {
                bgShape(10,R.color.blue)
            }
        }
    }
    init {
        contentLayout
    }
}