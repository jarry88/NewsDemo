package com.example.newsdemoapplication.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WordDao{
    @Insert
    fun insertWord(vararg words: Word)
    @Query("DELETE FROM WordTable")
    fun deleteWords()

    @Query("SELECT * FROM WordTable ORDER BY ID DESC")
    fun getAllWords(): List<Word?>?
}