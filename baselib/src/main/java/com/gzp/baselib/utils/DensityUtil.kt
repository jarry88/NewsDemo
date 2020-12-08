package com.gzp.baselib.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.View
import android.view.ViewConfiguration

object DensityUtil {

    fun getScreenHeight(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun getScreenWidth(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun dip2px(context: Context?, dpVale: Float): Int {
        val scale =context!!.resources.displayMetrics.density
        return (dpVale * scale + 0.5f).toInt()
    }

    fun getStatusBarHeight(context: Context): Int {
        val resources = context.resources
        val resourcesId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourcesId)
    }

    fun dip2sp(context: Context?, sp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context!!.resources.displayMetrics).toInt()
    }

    fun px2dip(context: Context?, pxValue: Float): Int {
        val scale = context!!.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun px2sp(context: Context?, pxValue: Float): Int {
        val fontScale = context!!.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    fun sp2px(context: Context?, spValue: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context!!.resources.displayMetrics).toInt()
    }

    fun getViewWidthAndHeight(contentView: View): IntArray {
        val result = IntArray(2)
        if (contentView.measuredHeight == 0 || contentView.measuredWidth == 0) {
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            // 计算contentView的高宽
            val windowHeight = contentView.measuredHeight
            val windowWidth = contentView.measuredWidth
            result[0] = windowWidth
            result[1] = windowHeight
        }
        return result
    }

    /**
     * 判断是否有虚拟按键
     * @return
     */
    fun hasNavigationBar(context: Context?): Boolean {
        val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        val hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME)
        return if (!hasMenuKey && (hasHomeKey || hasBackKey)) {
            true
        } else {
            false
        }
    }

    //判断屏幕下方是否有虚拟按键
    fun checkDeviceHasNavigationBar(context: Context): Boolean {
        //Android 5.0以下没有虚拟按键
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false
        }
        var hasNavigationBar = false
        val rs = context.resources
        val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val m = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
            } else if ("0" == navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: Exception) {
        }
        return hasNavigationBar
    }

    //获取NavigationBar的高度：
    fun getNavigationBarHeight(context: Context): Int {
        var navigationBarHeight = 0
        val rs = context.resources
        val id = rs.getIdentifier("navigation_bar_height", "dimen", "android")
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id)
        }
        return navigationBarHeight
    }
}