package com.example.newsdemoapplication

fun main(){
    println(A().name)
    println(A().name)
    B::class.members.firstOrNull { it.name=="sex" }?.let {
        println(it)
    }
//    println(B.sex)
}
object B{
    private val sex:String="fale"
}
class A(val name:String="string")
