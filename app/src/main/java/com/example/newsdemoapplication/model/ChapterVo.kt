package com.example.newsdemoapplication.model

import java.io.Serializable

var Id=0
//一级标题属性 （chapter -》news-》content）
 data class ChapterVo(
         var chapterName:String,
         var locked:Boolean=false,
         val description:String="掉线",
         var listNews:List<NewsVo>?=null,
         val id:Int= Id++,
         var index:Int=0):Serializable{
     fun getLockStr()=if(locked)"已锁" else "未锁"
 }