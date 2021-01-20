package com.example.newsdemoapplication.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ArticleTable")
data class Article(
        @PrimaryKey val id:Long,
        val subsection_id:Long,
        var index: Int,
        //url path
        var url:String,
        var desc:String="这是一张图片")