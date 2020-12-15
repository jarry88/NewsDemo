package com.example.newsdemoapplication.util

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
