package com.example.newsdemoapplication.model.test

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsdemoapplication.vo.ContentVo

@Entity(tableName = NewsModel.Table_NAME)
class NewsModel {
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

    var listContent= listOf<ContentVo>()
}