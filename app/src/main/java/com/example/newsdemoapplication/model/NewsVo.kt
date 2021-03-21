package com.example.newsdemoapplication.model
//
data class NewsVo(
        var title:String,
        var listContent:List<ContentVo>?=null,
        val messageList:List<String>?=null) {

}
data class ContentVo (
        var title: String,
        var content:String?,
        var imageUrl:String="")