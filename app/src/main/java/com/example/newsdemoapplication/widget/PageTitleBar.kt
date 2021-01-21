package com.example.newsdemoapplication.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.dsl.*

class PageTitleBar(context: Context,attributes: AttributeSet?=null):FrameLayout(context,attributes) {
    lateinit var btnBack:ImageView
    lateinit var btnRight:LinearLayout
    lateinit var tvTitle: TextView
    lateinit var llMainTitle: LinearLayout
    lateinit var llSubContainer: LinearLayout
    val contentView by lazy {
        LinearLayout (match_parent, wrap_content){
            orientation= vertical
            LinearLayout(match_parent,44){
                gravity= gravity_center_vertical
                padding=3
                btnBack=ImageView(24,24){
                    src= R.drawable.ic_arrow_back_black_24dp
                }
                llMainTitle =LinearLayout {
                    weight=1f
                    tvTitle =TextView(){
                        weight=1f
                        text="标题"
                        textStyle= bold
                    }
                }
                btnRight=LinearLayout(24,24){
                    ImageView {
                        src=R.drawable.y
                    }
                }
            }
            llSubContainer=LinearLayout (match_parent, wrap_content){bg_id=R.color.grey_bg }
        }
    }
    init {
        contentView
    }
}