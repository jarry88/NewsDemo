package com.example.newsdemoapplication

import kotlin.reflect.KMutableProperty
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class Book(private var name :String )

fun main() {
    val a =Book("english")
    println(a::class.members)
    Book::class.memberProperties.find { it.name=="name" }?.let {
        it.isAccessible=true
        println(it.get(a))
        (it as? KMutableProperty1<Book,String>)?.let { p->
            p.set(a,"chinese")
        }
        println(it.get(a))
    }
    a::class.memberProperties.find { it.name=="name" }?.let {
        it.isAccessible=true
        println(it.getter)
        (it as? KMutableProperty1<Book,String>)?.let { p->
            p.set(a,"chinese")
        }
        println(it.getter)
    }
}
fun String.p()= println(this)