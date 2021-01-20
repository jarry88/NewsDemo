package com.example.newsdemoapplication.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.lishuaihua.toast.ToastUtils
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

val Any?.toast:String
    get(){
        return this.toString().also {
            Log.i("toast", "toast--> : $it" )
            ToastUtils.show(it) }
    }
fun Fragment.backTo(id:Int)= with(this){
    NavHostFragment.findNavController(this).navigate(id)
}
fun Fragment.navigate(id:Int,addBundle:(bundle:Bundle)->Unit={})= with(this){
    NavHostFragment.findNavController(this).navigate(id,Bundle().also(addBundle))
}