package com.example.newsdemoapplication.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.example.newsdemoapplication.util.Util


class DragRecycleView @JvmOverloads constructor(context: Context, attributes: AttributeSet? = null, def: Int = 0) :RecyclerView(context, attributes, def) {

    private val Long.finish: Boolean
        get() {
            if(System.currentTimeMillis() -this>2*longPressDuration)
                return false
            val diff =System.currentTimeMillis() -this
            return  diff>longPressDuration
        }
    var count =0;
    var moveCount =0
    var startX =0f
    var startY =0f
    var currX= 0f
    var currY=0f
    var maxDistance =30
    var mCallBack : DragAndPressCallBack?=null
        set(value) {
            field= value
            addTouch()
        }
    var beginTime =System.currentTimeMillis()
    var bottomShowTime =System.currentTimeMillis()
    var longPressDuration =1000
    @SuppressLint("ClickableViewAccessibility")
    private fun addTouch() {
        Log.e("TAG", "addTouch: " )
        setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.rawX
                    startY = event.rawY
                    reset()
                }
                MotionEvent.ACTION_MOVE -> {
                    if(count>0){
                        currX = event.rawX
                        currY = event.rawY
                        if (Math.abs(startX - currX) >= maxDistance ||
                                Math.abs(startY - currY) > maxDistance) {
                            startX =currX
                            startY =currY


                            count--
                            if(moveCount++>0&&count<=0){
                                mCallBack?.onAfterPressMove()
                                reset()
                                Util.Loge("移动了")
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    Util.Loge("ACTION_UP")
                    reset()
//                    if(count>0){
//                        count=0
//                        mCallBack?.onLongPress()
//                    }
//                    reset()
                }
                else ->{}
            }
            return@setOnTouchListener false
        }
    }
    interface DragAndPressCallBack{
        fun onLongPress()
        fun onAfterPressMove()
    }

    private fun reset() {
        beginTime =System.currentTimeMillis().apply { Log.e("TAG", "reset: rese", ) }
    }

    /**
     * 间隔太短不响应
     */
    private fun shortShow() =System.currentTimeMillis()-bottomShowTime<200

}

