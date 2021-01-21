package com.example.newsdemoapplication.di

import com.example.newsdemoapplication.model.room.Chapter
import com.example.newsdemoapplication.util.randomUrl
import org.koin.dsl.module

var chapterId=0L
var chapterIndex=0
val chapterModel = module {
    factory {
        Chapter(chapterId++, chapterIndex++,"",false,randomUrl())
    }
}