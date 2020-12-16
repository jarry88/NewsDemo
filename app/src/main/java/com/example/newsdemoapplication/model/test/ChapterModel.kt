package com.example.newsdemoapplication.model.test

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsdemoapplication.vo.ContentVo
import com.example.newsdemoapplication.vo.NewsVo

@Entity(tableName = ChapterModel.Table_NAME)
class ChapterModel {
    companion object{
        const val Table_NAME="news_model"
    }
    @PrimaryKey(autoGenerate = true)
    var id :Long=0

    var index:Int=0
    var title:String=""

    var description =""

    var messageCount=0

    var unreadCount=0
}