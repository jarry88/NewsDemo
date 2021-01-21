package com.example.newsdemoapplication.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Word::class,Chapter::class,Subsection::class], version = 2, exportSchema = false)
abstract class AppDataBase:RoomDatabase(){
    abstract fun getWordDao():WordDao
    abstract fun getChapterDao():ChapterDao
    abstract fun getSubsectionDao():SubsectionDao
    companion object {
        @Volatile private var INSTANCE: AppDataBase? = null
//        fun getInstance(context: Context)=
//                INSTANCE?: synchronized(this){
//                    INSTANCE?:Room.databaseBuilder(context.applicationContext,
//                            AppDataBase::class.java, wordDataBaseName)
//                            .build().also { INSTANCE=it }
//                }
        fun getDatabase(context: Context,scope: CoroutineScope): AppDataBase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        "todo_database"
                ).addCallback(WordDataBaseCallBack(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }
    private class WordDataBaseCallBack(private val scope: CoroutineScope):RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database->
                scope.launch {
                    //数据库初始化操作
                }
            }
        }
    }
}