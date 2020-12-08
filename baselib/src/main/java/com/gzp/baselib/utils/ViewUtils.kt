package com.gzp.baselib.utils

import android.R
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.ColorInt
import com.gzp.baselib.BaseApplication
import com.gzp.baselib.utils.Logger.Companion.d

object ViewUtils {
    fun dp2px(dpValue: Float): Int {
        val scale = BaseApplication.instance.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dp(pxValue: Float): Int {
        val scale = BaseApplication.instance.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun sp2px(spValue: Float): Int {
        val fontScale = BaseApplication.instance.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun px2sp(pxValue: Float): Int {
        val fontScale = BaseApplication.instance.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    fun forceGetViewSize(view: View, listener: onGetSizeListener?) {
        view.post { listener?.onGetSize(view) }
    }

    fun setAlpha(v: View, alpha: Float) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            val animation = AlphaAnimation(1f, alpha)
            animation.fillAfter = true
            v.startAnimation(animation)
        } else {
            v.alpha = alpha
        }
    }

    fun setVisible(view: View) {
        if (view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
        }
    }

    fun setGone(view: View) {
        if (view.visibility != View.GONE) {
            view.visibility = View.GONE
        }
    }

    fun setInvisible(view: View) {
        if (view.visibility != View.INVISIBLE) {
            view.visibility = View.INVISIBLE
        }
    }

    /**
     * Set the visibility of view. The visibility must be one of [View.VISIBLE],
     * [View.INVISIBLE] or [View.GONE].
     *
     * @param v the view
     * @param visibility the visibility
     */
    fun setVisibility(v: View, visibility: Int) {
        if (v.visibility != visibility) {
            v.visibility = visibility
        }
    }

    /**
     * 获取 activity 的根 view
     *
     * @param activity activity
     * @return         根 view
     */
    fun getRootView(activity: Activity): View {
        return (activity.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup).getChildAt(0)
    }

    /**
     * 扩展点击区域的范围
     *
     * @param view       需要扩展的元素，此元素必需要有父级元素
     * @param expendSize 需要扩展的尺寸（以sp为单位的）
     */
    fun expendTouchArea(view: View?, expendSize: Int) {
        if (view != null) {
            val parentView = view.parent as View
            parentView.post {
                val rect = Rect()
                view.getHitRect(rect) // 如果太早执行本函数，会获取rect失败，因为此时UI界面尚未开始绘制，无法获得正确的坐标
                rect.left -= expendSize
                rect.top -= expendSize
                rect.right += expendSize
                rect.bottom += expendSize
                parentView.touchDelegate = TouchDelegate(rect, view)
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun setBackground(view: View, drawable: Drawable?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = drawable
        } else {
            view.setBackgroundDrawable(drawable)
        }
    }

    /**
     * 对 View 设置 paddingLeft
     *
     * @param view  需要被设置的 View
     * @param value 设置的值
     */
    fun setPaddingLeft(view: View, value: Int) {
        if (value != view.paddingLeft) {
            view.setPadding(value, view.paddingTop, view.paddingRight, view.paddingBottom)
        }
    }

    /**
     * 对 View 设置 paddingTop
     *
     * @param view  需要被设置的 View
     * @param value 设置的值
     */
    fun setPaddingTop(view: View, value: Int) {
        if (value != view.paddingTop) {
            view.setPadding(view.paddingLeft, value, view.paddingRight, view.paddingBottom)
        }
    }

    /**
     * 对 View 设置 paddingRight
     *
     * @param view  需要被设置的 View
     * @param value 设置的值
     */
    fun setPaddingRight(view: View, value: Int) {
        if (value != view.paddingRight) {
            view.setPadding(view.paddingLeft, view.paddingTop, value, view.paddingBottom)
        }
    }

    /**
     * 对 View 设置 paddingBottom
     *
     * @param view  需要被设置的 View
     * @param value 设置的值
     */
    fun setPaddingBottom(view: View, value: Int) {
        if (value != view.paddingBottom) {
            view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, value)
        }
    }

    fun setImageViewTintColor(imageView: ImageView, @ColorInt tintColor: Int): ColorFilter {
        val colorFilter = LightingColorFilter(Color.argb(255, 0, 0, 0), tintColor)
        imageView.colorFilter = colorFilter
        return colorFilter
    }

    /*---------------------------------- 屏幕信息 --------------------------------------*/
    val screenWidth: Int
        get() {
            val wm = BaseApplication.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(metrics)
            return metrics.widthPixels
        }
    val screenHeight: Int
        get() {
            val wm = BaseApplication.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(metrics)
            return metrics.heightPixels
        }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity 当前的 Activity
     * @return 截图
     */
    fun captureScreenWithoutStatusBar(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight = frame.top
        val width = screenWidth
        val height = screenHeight
        val bp: Bitmap
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight)
        view.destroyDrawingCache()
        return bp
    }

    /*---------------------------------- 软键盘 --------------------------------------*/
    fun showSoftInput(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                ?: return
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
            view.isFocusable = true
            view.isFocusableInTouchMode = true
            view.requestFocus()
        }
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    fun showSoftInput(view: View) {
        val imm = BaseApplication.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                ?: return
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.requestFocus()
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    fun hideSoftInput(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                ?: return
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideSoftInput(view: View) {
        val imm = BaseApplication.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                ?: return
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun toggleSoftInput() {
        val imm = BaseApplication.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                ?: return
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun isSoftInputVisible(activity: Activity): Boolean {
        return isSoftInputVisible(activity, 200)
    }

    fun isSoftInputVisible(activity: Activity, minHeightOfSoftInput: Int): Boolean {
        return getContentViewInvisibleHeight(activity) >= minHeightOfSoftInput
    }

    private fun getContentViewInvisibleHeight(activity: Activity): Int {
        val contentView = activity.findViewById<FrameLayout>(R.id.content)
        val contentViewChild = contentView.getChildAt(0)
        val outRect = Rect()
        contentViewChild.getWindowVisibleDisplayFrame(outRect)
        d("KeyboardUtils", "getContentViewInvisibleHeight: "
                + (contentViewChild.bottom - outRect.bottom))
        return contentViewChild.bottom - outRect.bottom
    }

    interface onGetSizeListener {
        fun onGetSize(view: View?)
    }
}