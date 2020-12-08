package com.gzp.baselib.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gzp.baselib.R


class NavigationBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {
    var firstLeftIv: ImageView? = null
    var firstRightIv: ImageView? = null


    private var mTitleView: TextView? = null
    private var mRightTv: TextView? = null
    private var navigationBar: LinearLayout? = null
    private var llRightClickArea: LinearLayout? = null
    var backLL: LinearLayout? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        navigationBar = findViewById<View>(R.id.navigationBar) as LinearLayout
        backLL = findViewById<View>(R.id.ll_back) as LinearLayout

        firstLeftIv = findViewById<View>(R.id.iv_first_left) as ImageView
        firstRightIv = findViewById<View>(R.id.iv_first_right) as ImageView
        mTitleView = findViewById(R.id.title_tv)
        mRightTv = findViewById<View>(R.id.right_tv) as TextView
        llRightClickArea = findViewById<View>(R.id.ll_right) as LinearLayout
    }

    fun setNavigationBarBackgroudColor(id: Int) {
        navigationBar!!.setBackgroundColor(id)
    }



    /*
     * @see android.view.View#getBottomFadingEdgeStrength()
     */
    override fun getBottomFadingEdgeStrength(): Float {
        return 1.0f
    }



    fun getmTitleView(): TextView? {
        return mTitleView
    }

    fun getmRightTv(): TextView? {
        return mRightTv
    }

    fun setTitle(title: String) {
        mTitleView!!.text = title
    }

    fun setTitleColor(color: Int) {
        mTitleView!!.setTextColor(color)
    }

    fun setmRightTvText(text: String) {
        mRightTv!!.text = text
    }

    fun setmRightTvColor(color: Int) {
        mRightTv!!.setTextColor(color)
    }

    fun setBackLLVisiable(i: Int) {
        backLL!!.visibility = i
    }

    fun getRightIv(): ImageView? {
        return firstRightIv
    }

    fun setBackIvImageResource(i: Int){
        firstLeftIv?.setImageResource(i)
    }

    fun setBackClickListener(activity: Activity) {
        backLL!!.setOnClickListener { activity.finish() }
    }

    fun setBackOnClickListener(listener: View.OnClickListener) {
        backLL!!.setOnClickListener(listener)
    }


    fun setRightClickListener(listener: View.OnClickListener) {
        llRightClickArea!!.setOnClickListener(listener)
    }

    fun setRightClickAreaVisiable(v: Int) {
        llRightClickArea!!.visibility = v
    }

}
