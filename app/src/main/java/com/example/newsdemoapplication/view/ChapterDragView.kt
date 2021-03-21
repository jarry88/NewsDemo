package com.example.newsdemoapplication.view

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.os.Vibrator
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.util.callback.ItemDragHelperCallBack
import com.example.newsdemoapplication.util.callback.OnItemClickListener
import com.example.newsdemoapplication.util.callback.ItemHelper
import com.example.newsdemoapplication.model.ChapterVo
import com.lxj.xpopup.core.BasePopupView
import java.util.*
import kotlin.math.abs

/**
 * 首页左侧拉出抽屉
 * 章节列表信息可拖动视图
 * @author gzp
 */
@SuppressLint("ViewConstructor")
class ChapterDragView  @JvmOverloads constructor(context: Context, val liveDate :LiveData<MutableList<ChapterVo>>, attributes: AttributeSet? = null, def: Int = 0) : RecyclerView(context, attributes, def)  {
    var count =0;
    var moveCount =0
    var startX =0f
    var startY =0f
    var currX= 0f
    var currY=0f
    var maxDistance =30
    var parent:BasePopupView?=null
    var mCallBack : TitleRecycleView.DragAndPressCallBack?=null
        set(value) {
            field= value
            addTouch()
        }
    private var longPress =false
    var beginTime =System.currentTimeMillis()
    var bottomShowTime =System.currentTimeMillis()
    var clickCallBack:ClickCallBack?=null
    val mAdapter by lazy {  ChapterDragAdapter(context, liveDate.value).apply {
        setOnItemClickListener(
                object : OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        updatePosition(position)
                        parent?.dismiss()
                    }
                    override fun onItemLongClick(view: View, position: Int) {}
                })
    }
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
                        if (abs(startX - currX) >= maxDistance ||
                                abs(startY - currY) > maxDistance) {
                            startX = currX
                            startY = currY

                            if (moveCount++ > 0) {
                                longPress=false
                                mCallBack?.onAfterPressMove()
                                reset()
                            }
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    reset()
                }
                else ->{}
            }
            return@setOnTouchListener false
        }
    }
    //重置开始按压的时间
    fun reset() {
        beginTime =System.currentTimeMillis().apply { Log.e("TAG", "reset: rese") }
    }

    /**
     * 间隔太短不响应
     */
    private fun shortShow() =System.currentTimeMillis()-bottomShowTime<200
    init {
        initView()
    }
    private fun initView(){
        adapter =mAdapter
        val itemTouchHelper = ItemTouchHelper(ItemDragHelperCallBack(object : ItemHelper {
            override fun itemMoved(oldPosition: Int, newPosition: Int) {
//                Util.Loge("move")
                //交换变换位置的集合数据
                mAdapter.getData()?.let {
                    Collections.swap(it, oldPosition, newPosition)
                }
                mAdapter.notifyItemMoved(oldPosition, newPosition)
            }

            override fun itemDismiss(position: Int) {}
            override fun itemClear(position: Int) {
                updatePosition(position)
                moveCount = 0
                mAdapter.notifyDataSetChanged()
                if (longPress) {
                    longPress = false
                    mCallBack?.onLongPress()
                }
            }

            override fun itemSelected(position: Int) { //选中title 后的响应操作
                Log.e("TAG", "itemSelected: $position")
                if (position < 0) return
                count = 1
                if (position != mAdapter.currSelectPosition) {
                    findViewHolderForAdapterPosition(mAdapter.currSelectPosition)?.let { (it as ListDragAdapter.MyDragViewHolder).setSelected(false) }
                }
                val vib = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
                vib.vibrate(100)
                //                 记录弹窗信息
                longPress = true
            }
        }))
        itemTouchHelper.attachToRecyclerView(this)
        layoutManager=GridLayoutManager(context, 4 * 3).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =12
            }
        }

    }

    private fun updatePosition(position: Int) {
        if(mAdapter.currSelectPosition!=position){
            mAdapter.currSelectPosition = position
            clickCallBack?.let {callBack ->
                mAdapter.getData()?.get(position)?.let {
                    callBack.onChapterClicked(it)
                }
            }
        }
    }

    fun getSelectedPosition()=mAdapter.currSelectPosition
    interface ClickCallBack{
        fun onChapterClicked(chapterVo: ChapterVo)
    }
}
class ChapterDragAdapter constructor(val context: Context,var list:MutableList<ChapterVo>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    //    private List<T> listData;
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mConvertView: ListDragAdapter.ConvertView? = null
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
                if (showImage) R.layout.image_item_style else R.layout.title_item_style, parent, false)
        return ListDragAdapter.MyDragViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mHolder = holder as ListDragAdapter.MyDragViewHolder
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