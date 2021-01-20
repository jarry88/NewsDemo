package com.example.newsdemoapplication.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordDataBase:RoomDatabase(){
    abstract fun getWordDao():WordDao
    companion object {
        @Volatile private var INSTANCE: WordDataBase? = null
        fun getInstance(context: Context)=
                INSTANCE?: synchronized(this){
                    INSTANCE?:Room.databaseBuilder(context.applicationContext,
                            WordDataBase::class.java, wordDataBaseName)
                            .build().also { INSTANCE=it }
                }
        fun getDatabase(context: Context,scope: CoroutineScope): WordDataBase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WordDataBase::class.java,
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