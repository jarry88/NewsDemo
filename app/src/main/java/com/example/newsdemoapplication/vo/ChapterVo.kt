package com.example.newsdemoapplication.vo

var Id=0
 data class ChapterVo(val chapterName:String,var locked:Boolean=false,val description:String="掉线",var listNews:List<NewsVo>?=null,val id:Int=Id++){
     fun getLockStr()=if(locked)"已锁" else "未锁"
 }