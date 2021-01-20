package com.example.newsdemoapplication.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WordTable")
data class Word(
        @PrimaryKey val id:Long,
        var name:String,
        var word:String)