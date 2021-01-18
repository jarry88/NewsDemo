package com.example.newsdemoapplication

import com.google.gson.reflect.TypeToken

fun main(){
    println("type ->>${type<MM>()}")
    println("type ->>${type<A<MM>>()}")
    println("type ->>${object :TypeToken<A<MM>>(){}.type}")
}
class MM
class A<T>
inline fun <reified  T:Any> type()=T::class