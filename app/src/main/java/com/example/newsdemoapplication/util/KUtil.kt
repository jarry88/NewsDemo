package com.example.newsdemoapplication.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import java.lang.Math.*


@JvmOverloads
fun ImageView.loadUrl(url:String?=null){
    Glide.with(context).load(url?: randomUrl()).into(this)
}
fun randomUrl()=round(random()*15).let { "file:///android_asset/avatar/home_icon${it}.jpg" }

fun findActivity(context:Context?):Activity?=context?.let {
    if (context is Activity) {
        context
    } else if (context is ContextWrapper) {
        findActivity(context.baseContext)
    } else {
        null
    }
}