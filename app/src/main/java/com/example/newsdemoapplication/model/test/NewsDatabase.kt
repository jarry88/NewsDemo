package com.example.newsdemoapplication.model.test

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [(ChapterModel::class)],version = 1)
abstract class NewsDatabase :RoomDatabase(){
    companion object{
        const val DATABASE_NAME="news_database"
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getInstance(context: Context): NewsDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                        context.applicationContext,
                        NewsDatabase::class.java,
                        "nba_db"
                ).addCallback(object : RoomDatabase.Callback() {
                }).build().also {
                    INSTANCE = it
                }
            }
        }
    }
    abstract fun getNewsDao():ChapterDao
}
//数据库的创建是一件非常消耗资源的工作，所以我们将数据库设计为单例，避免创建多个数据库对象。另外对数据库的操作都不能放在 UI 线程中完成，否则会出现异常：