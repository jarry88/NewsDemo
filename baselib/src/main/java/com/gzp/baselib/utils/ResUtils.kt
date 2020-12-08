package com.gzp.baselib.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import androidx.annotation.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.gzp.baselib.BaseApplication
import java.io.InputStream

object ResUtils {
    fun getAttrFloatValue(context: Context, attrRes: Int): Float {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrRes, typedValue, true)
        return typedValue.float
    }

    fun getAttrColor(context: Context, attrRes: Int): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrRes, typedValue, true)
        return typedValue.data
    }

    fun getAttrColorStateList(context: Context, attrRes: Int): ColorStateList? {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrRes, typedValue, true)
        return ContextCompat.getColorStateList(context, typedValue.resourceId)
    }

    fun getAttrDrawable(context: Context, attrRes: Int): Drawable? {
        val attrs = intArrayOf(attrRes)
        val ta = context.obtainStyledAttributes(attrs)
        val drawable = getAttrDrawable(context, ta, 0)
        ta.recycle()
        return drawable
    }

    fun getAttrDrawable(context: Context?, typedArray: TypedArray, index: Int): Drawable? {
        val value = typedArray.peekValue(index)
        if (value != null) {
            if (value.type != TypedValue.TYPE_ATTRIBUTE && value.resourceId != 0) {
                return AppCompatResources.getDrawable(context!!, value.resourceId)
            }
        }
        return null
    }

    fun getAttrDimen(context: Context, attrRes: Int): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrRes, typedValue, true)
        return TypedValue.complexToDimensionPixelSize(typedValue.data, context.resources.displayMetrics)
    }

    /*----------------------------------wrapper methods--------------------------------------*/
    fun getIntArray(@ArrayRes id: Int): IntArray {
        return BaseApplication.instance.resources.getIntArray(id)
    }

    fun getTextArray(@ArrayRes id: Int): Array<CharSequence> {
        return BaseApplication.instance.resources.getTextArray(id)
    }

    fun getStringArray(@ArrayRes id: Int): Array<String> {
        return BaseApplication.instance.resources.getStringArray(id)
    }

    @ColorInt
    fun getColor(@ColorRes id: Int): Int {
        return BaseApplication.instance.resources.getColor(id)
    }

    fun getString(@StringRes id: Int): String {
        return BaseApplication.instance.resources.getString(id)
    }

    fun getString(@StringRes id: Int, vararg formatArgs: Any?): String {
        return BaseApplication.instance.resources.getString(id, *formatArgs)
    }

    fun getText(@StringRes id: Int): CharSequence {
        return BaseApplication.instance.resources.getText(id)
    }

    fun getQuantityText(@PluralsRes id: Int, quantity: Int): CharSequence {
        return BaseApplication.instance.resources.getQuantityText(id, quantity)
    }

    fun getQuantityString(@PluralsRes id: Int, quantity: Int): String {
        return BaseApplication.instance.resources.getQuantityString(id, quantity)
    }

    fun getQuantityString(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any?): String {
        return BaseApplication.instance.resources.getQuantityString(id, quantity, *formatArgs)
    }

    fun getDrawable(@DrawableRes id: Int): Drawable {
        return BaseApplication.instance.resources.getDrawable(id,null)
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun getFont(@FontRes id: Int): Typeface {
        return BaseApplication.instance.resources.getFont(id)
    }

    fun getDimension(@DimenRes id: Int): Float {
        return BaseApplication.instance.resources.getDimension(id)
    }

    fun getBoolean(@BoolRes id: Int): Boolean {
        return BaseApplication.instance.resources.getBoolean(id)
    }

    fun getInteger(@IntegerRes id: Int): Int {
        return BaseApplication.instance.resources.getInteger(id)
    }

    fun openRawResource(@RawRes id: Int): InputStream {
        return BaseApplication.instance.resources.openRawResource(id)
    }

    fun openRawResourceFd(@RawRes id: Int): AssetFileDescriptor {
        return BaseApplication.instance.resources.openRawResourceFd(id)
    }

    val assets: AssetManager
        get() = BaseApplication.instance.resources.assets
}