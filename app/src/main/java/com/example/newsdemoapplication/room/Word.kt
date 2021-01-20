package com.example.newsdemoapplication.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WordTable")
data class Word(
        @PrimaryKey(autoGenerate = true)
        var id:Long,
        var name:String,
        var word:String)