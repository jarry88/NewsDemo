package com.example.newsdemoapplication.model.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordDao{
    @Insert
    suspend fun insertWord(vararg words: Word)
    @Query("DELETE FROM WordTable")
    fun deleteWords()
    //按照id排序
    @Query("SELECT * FROM WordTable ORDER BY ID DESC")
    fun getAllWords(): LiveData<List<Word>>

    @Query("SELECT * FROM WordTable WHERE id =:id")
    fun queryById(id:Long): LiveData<Word?>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)
    @Query("DELETE FROM WordTable")
    suspend fun deleteAll()
}