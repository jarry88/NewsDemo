package com.example.newsdemoapplication.popup

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.view.ChapterDragView
import com.example.newsdemoapplication.view.DragView
import com.example.newsdemoapplication.view.TitleDragView
import com.example.newsdemoapplication.vo.ChapterVo
import com.lxj.xpopup.core.DrawerPopupView
import java.util.*

class ChapterPopupView(context: Context):DrawerPopupView(context) {
    override fun getImplLayoutId()= R.layout.left_list_drawer
    private var mOnClickListener: OnClickListener? = null
    private var mOnDismissListener: OnDismissListener? = null
    //    final ArrayList<String> data = new ArrayList<>();
    val llContainer: LinearLayout by  lazy { findViewById(R.id.ll_container) }
    override fun onCreate() {
        val listStr: MutableList<String> = ArrayList()
        val list= mutableListOf<ChapterVo>()
        for (i in 0..10) {
            listStr.add("chapterName$i")
            list.add(ChapterVo("chapterName$i"))
        }
        val titleDragView =findViewById<TitleDragView>(R.id.chapter_view)
        val dragView =findViewById<DragView>(R.id.drag_view)
        dragView.addAll(listStr)
        titleDragView.setData(list)
        findViewById<View>(R.id.textView).setOnClickListener { v: View? -> if (mOnClickListener != null) mOnClickListener?.onClick(v) }
        findViewById<View>(R.id.btn_close_left).setOnClickListener { v: View? -> dismiss() }
        findViewById<View>(R.id.btn_add).setOnClickListener { v: View? ->
            dismiss()
            if (mOnClickListener != null) mOnClickListener?.onClick(v)
        }
    }

    override fun onDismiss() {
        super.onDismiss()
        if (mOnDismissListener != null ) {
//            mOnDismissListener?.dismiss(list.get(dragView.getSelectedPosition()).id)
        }
    }


    fun setmOnDismissListener(onClickListener: OnDismissListener) {
        mOnDismissListener = onClickListener
    }

    fun setmOnClickListener(onClickListener: OnClickListener) {
        mOnClickListener = onClickListener
    }

    interface OnDismissListener {
        fun dismiss(position: Int)
    }
}