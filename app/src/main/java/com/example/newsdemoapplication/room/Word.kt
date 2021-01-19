package com.example.newsdemoapplication.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "WordTable")
class Word(
        @PrimaryKey(autoGenerate = true)
        var id:Int,
        var name:String,
        var word:String)