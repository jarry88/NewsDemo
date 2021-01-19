package com.example.newsdemoapplication.room

import android.content.Context

class WordRepository(context: Context) {
    val WordDao by lazy {
        WordDataBase.getInstance(context)
    }
}