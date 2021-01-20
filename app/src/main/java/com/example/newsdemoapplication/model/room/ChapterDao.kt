package com.example.newsdemoapplication.model.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChapterDao{
    @Insert
    suspend fun insertChapter(vararg chapter: Chapter)

    //按照id排序
    @Query("SELECT * FROM ChapterTable ORDER BY `index` DESC")
    fun getAllChapters(): LiveData<List<Chapter>>

    @Query("SELECT * FROM ChapterTable WHERE id =:id")
    fun queryById(id:Long): LiveData<Chapter>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chapter: Chapter)

    @Query("DELETE FROM ChapterTable")
    suspend fun deleteAll()
}