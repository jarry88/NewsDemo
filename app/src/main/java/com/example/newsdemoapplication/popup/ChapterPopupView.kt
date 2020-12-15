package com.example.newsdemoapplication.popup

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.example.newsdemoapplication.R
import com.lxj.xpopup.core.DrawerPopupView
import java.util.*

class ChapterPopupView(context: Context):DrawerPopupView(context) {
    override fun getImplLayoutId()= R.layout.left_list_drawer
    private var mOnClickListener: OnClickListener? = null
    private var mOnDismissListener: OnDismissListener? = null
    //    final ArrayList<String> data = new ArrayList<>();
    val llContainer: LinearLayout by  lazy { findViewById(R.id.ll_container) }
    override fun onCreate() {
        findViewById<View>(R.id.textView).setOnClickListener { v: View? -> if (mOnClickListener != null) mOnClickListener?.onClick(v) }
        findViewById<View>(R.id.btn_close_left).setOnClickListener { v: View? -> dismiss() }
        findViewById<View>(R.id.btn_add).setOnClickListener { v: View? ->
            dismiss()
            if (mOnClickListener != null) mOnClickListener?.onClick(v)
        }
    }


    interface OnDismissListener {
        fun dismiss(position: Int)
    }
}