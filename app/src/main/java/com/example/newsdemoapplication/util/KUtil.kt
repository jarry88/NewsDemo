package com.example.newsdemoapplication.util

import android.app.Activity
import android.app.Application
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import java.lang.Math.random
import java.lang.Math.round
import kotlin.math.roundToInt

//全局共用函数工具
@JvmOverloads
fun ImageView.loadUrl(url:String?=null){
    Glide.with(context).load(url?: randomUrl()).into(this)
}
fun randomUrl()=round(random()*15).let { "file:///android_asset/avatar/home_icon${it}.jpg" }

val Int.rad: Int
    get() {
        return (Math.random() * this).roundToInt().toInt()
    }

fun log(string: String) {
    Log.e("TAG", "Log: $string")
}

fun getRandomData(size: Int): ArrayList<String>{
    val data= arrayListOf<String>()
    for (i in 0 until size) {
        data.add("$i--$i")
    }
    return data
}

/**
 * Show message
 *
 * @param activity Activity
 * @param msg message
 */
fun showMessage(activity: Activity?, msg: String) {
    LogHelper.e("showMessage ：$msg")
    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
}

fun showMessage(application: Application?, msg: String) {
    LogHelper.e("showMessage ：$msg")
    Toast.makeText(application, msg, Toast.LENGTH_SHORT).show()
}
fun show(application: Application?, msg: String) {
    LogHelper.e("showMessage ：$msg")
    Toast.makeText(application, msg, Toast.LENGTH_SHORT).show()
}