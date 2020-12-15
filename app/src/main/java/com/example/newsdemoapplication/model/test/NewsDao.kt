package com.example.newsdemoapplication.model.test

import androidx.room.*

@Dao
interface NewsDao {
    @Query("select * from "+NewsModel.Table_NAME)
    fun getAll():List<NewsModel>

    @Query("select * from "+NewsModel.Table_NAME+"where id LIKE :id LIMIT 1")
    fun queryById(id:Long):NewsModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item:NewsModel):Long

    @Update
    fun update(item: NewsModel):Int

    @Delete
    fun delete(item: NewsModel):Int
}