package com.example.newsdemoapplication.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SubsectionTable")
data class Subsection(
        @PrimaryKey val id:Long,
        val chapter_id:Long,
        var index: Int,
        //url path
        var name:String,
        var subTitle:String,
        var url:String,
        var desc:String="这是一张图片")