package com.example.newsdemoapplication.vo

data class NewsVo(var title:String,val listContent:List<ContentVo>?,val messageList:List<String>?=null) {

}
data class ContentVo (var title: String,var content:String?,var imageUrl:String="")