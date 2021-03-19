package com.example.newsdemoapplication.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import java.lang.Math.*
import kotlin.math.roundToInt


@JvmOverloads
fun ImageView.loadUrl(url:String?=null){
    Glide.with(context).load(url?: randomUrl()).into(this)
}
fun randomUrl()=round(random()*15).let { "file:///android_asset/avatar/home_icon${it}.jpg" }

val Int.rad: Int
    get() {
        return (Math.random() * this).roundToInt().toInt()
    }
