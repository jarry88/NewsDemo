package com.example.newsdemoapplication.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ChapterTable")
data class Chapter(
        @PrimaryKey val id:Long,
        var index: Int,
        //url path
        var name:String,
        var isLocked:Boolean,
        var url:String,
        var desc:String="这是一张图片")