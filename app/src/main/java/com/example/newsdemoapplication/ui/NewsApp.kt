package com.example.newsdemoapplication.ui

import android.app.Activity
import android.util.Log
import androidx.room.Room
import com.example.newsdemoapplication.model.test.NewsDatabase
import com.example.newsdemoapplication.room.WordDataBase
import com.gzp.baselib.BaseApplication
import kotlinx.coroutines.*

class NewsApp:BaseApplication(),CoroutineScope by MainScope(){
    val wordDb:WordDataBase by lazy {
        launch {
            withContext(){
                Room.databaseBuilder(
                        applicationContext,
                        WordDataBase::class.java, "wdb"
                ).build()
            }

        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        super.onActivityDestroyed(activity)
        cancel()
    }
}