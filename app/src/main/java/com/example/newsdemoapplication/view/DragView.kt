package com.example.newsdemoapplication.view

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.os.Vibrator
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsdemoapplication.Util
import com.example.newsdemoapplication.adapter.ListDragAdapter
import com.example.newsdemoapplication.adapter.ListDragAdapter.MyDragViewHolder
import com.example.newsdemoapplication.callback.ItemDragHelperCallBack
import com.example.newsdemoapplication.callback.OnItemClickListener
import com.example.newsdemoapplication.ui.dashboard.ItemHelper
import okhttp3.internal.notify
import java.util.*


class DragView @JvmOverloads constructor(context: Context, attributes: AttributeSet? = null, def: Int = 0) :RecyclerView(context, attributes, def) {
    var count =0;
    var moveCount =0
    var ColumnNum =4
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
    private val list by lazy { mutableListOf<String>() }
    private val mLayoutManager by lazy {
        GridLayoutManager(context, ColumnNum * 3).apply {
            spanSizeLookup = object : SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val end: Int = list.size % ColumnNum
                    return if (list.size - position <= end) ColumnNum * 3 / end else 3
                }
            }
        }
    }
    private val mImageLayoutManager by lazy {
        GridLayoutManager(context, 2)
    }
    private val mAdapter by lazy {  ListDragAdapter(context, list).apply {
        setOnItemClickListener(
                object : OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
//                        currSelectPosition = position
//                        contentScrollToPosition(position)
                    }

                    override fun onItemLongClick(view: View, position: Int) {}
                })
    }
    }
    @JvmOverloads
    fun addAll(l: List<String>?,showImage:Boolean=false){
        l?:return
        mAdapter.setImageView(showImage)
        if(showImage)
            setImageLayoutManager()
        mAdapter.setmDatas(l)
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun addTouch() {
        setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.e("TAG", "ACTION_DOWN: ")
                    startX = event.rawX
                    startY = event.rawY
                    reset()
                }
                MotionEvent.ACTION_MOVE -> {
                    if (count > 0) {
                        currX = event.rawX
                        currY = event.rawY
                        if (Math.abs(startX - currX) >= maxDistance ||
                                Math.abs(startY - currY) > maxDistance) {
                            startX = currX
                            startY = currY

                            if (moveCount++ > 0) {
                                mCallBack?.onAfterPressMove()
                                reset()
                            }
                            Util.Loge("移动了")
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    Util.Loge("ACTION_UP")
                    reset()
                }
                else ->{}
            }
            return@setOnTouchListener false
        }
    }
    //拖动按压事件回调接口
    interface DragAndPressCallBack{
        fun onLongPress()
        fun onAfterPressMove()
    }
    //重置开始按压的时间
    private fun reset() {
        beginTime =System.currentTimeMillis().apply { Log.e("TAG", "reset: rese") }
    }

    /**
     * 间隔太短不响应
     */
    private fun shortShow() =System.currentTimeMillis()-bottomShowTime<200
    init {
        initView()
//        mCallBack = object :  DragAndPressCallBack {
//            //设置移动监听回调
//            override fun onLongPress() {
////                bottomSheetDialog.show()
////                mTitleRecycleView.bottomShowTime = System.currentTimeMillis()
//            }
//
//            override fun onAfterPressMove() {
////                longPress = false
////                bottomSheetDialog.dismiss()
//            }
//        }

    }
    fun initView(){
        adapter =mAdapter

        val itemTouchHelper = ItemTouchHelper(ItemDragHelperCallBack(object : ItemHelper {
            override fun itemMoved(oldPosition: Int, newPosition: Int) {
                Util.Loge("move")

                //交换变换位置的集合数据
                Collections.swap(mAdapter.getData(), oldPosition, newPosition)
                mAdapter.urlData?.let {
                    Collections.swap(it, oldPosition, newPosition)
                }
                mAdapter.notifyItemMoved(oldPosition, newPosition)
//                currSelectedPosition = newPosition
                //                titleAdapter.setCurrSelectPosition(currSelectedPosition);
            }

            override fun itemDismiss(position: Int) {}
            override fun itemClear(position: Int) {
//                mContenAdapter.notifyDataSetChanged();
//                if(position!=currSelectedPosition){
                mAdapter.currSelectPosition = position
                mAdapter.notifyDataSetChanged()
//                if (longPress) {
//                    bottomSheetDialog.show()
//                }
                //                }
            }

            override fun itemSelected(position: Int) { //选中title 后的响应操作
                Log.e("TAG", "itemSelected: $position")
                if (position < 0) return
                count = 1
                if (position != mAdapter.currSelectPosition) {
                    findViewHolderForAdapterPosition(mAdapter.currSelectPosition)?.
                    let { (it as MyDragViewHolder).setSelected(false)}
//                    mAdapter.currSelectPosition = position
                }
                val vib = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
                vib.vibrate(100)
                //                 记录弹窗信息
//                longPress = true
            }
        }))
        itemTouchHelper.attachToRecyclerView(this)
        layoutManager=mLayoutManager

    }

    fun setImageLayoutManager() {
        layoutManager =mImageLayoutManager
    }
    fun setLineLayoutManager() {
        layoutManager =LinearLayoutManager(context)
    }
}

