package com.example.newsdemoapplication.model.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SubsectionDao{
    @Insert
    suspend fun insertChapter(vararg chapter: Subsection)

    //按照id排序
    @Query("SELECT * FROM SubsectionTable ORDER BY `index` DESC")
    fun getAllSubsection(): LiveData<List<Subsection>>

    @Query("SELECT * FROM SubsectionTable WHERE id =:id")
    fun queryById(id:Long): LiveData<Subsection>
    @Query("SELECT * FROM SubsectionTable WHERE chapter_id =:chapter_id")
    fun queryByChapterId(chapter_id:Long): Flow<List<Subsection>>
    @Query("SELECT * FROM SubsectionTable WHERE chapter_id =:chapter_id")
    suspend fun queryByChapter(chapter_id:Long): List<Subsection>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(subsection: Subsection)

    @Query("DELETE FROM SubsectionTable")
    suspend fun deleteAll()
}