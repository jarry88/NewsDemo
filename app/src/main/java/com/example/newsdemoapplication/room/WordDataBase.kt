package com.example.newsdemoapplication.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordDataBase:RoomDatabase(){
    abstract fun getWordDao():WordDao
    companion object {
        @Volatile private var INSTANCE: WordDataBase? = null
        fun getInstance(context: Context)=
                INSTANCE?: synchronized(this){
                    INSTANCE?:Room.databaseBuilder(context.applicationContext,
                            WordDataBase::class.java, "word_database")
                            .build().also { INSTANCE=it }
                }
    }
}