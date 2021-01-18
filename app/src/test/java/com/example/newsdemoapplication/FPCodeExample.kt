package com.example.newsdemoapplication

import android.content.Context
import com.lxj.xpopup.XPopup

object FPCodeExample {
    fun main(){

    }
}

fun main() {
    val s=sum(6)

    println(sum(5))
    println(sum(15))
    println(s(75))
    println(s(15))

    val test=1.builder1("a")
    test{2}
    2.builder1("b").invoke { 3 }
//    2.builder1("b"){ 3 }

}

fun sum(x:Int)={y:Int->x+y}

fun <T>builder(context: Context)={build:(T).(Context,r:T)->Unit ->  }

fun <T:Any> T.builder1(context: String)={build:(String)->T -> build(context).also { println(context) }}

fun printo(origin:String)={ println(origin)}