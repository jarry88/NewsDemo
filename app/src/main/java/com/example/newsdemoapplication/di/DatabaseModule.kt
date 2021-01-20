package com.example.newsdemoapplication.di

import androidx.room.Room
import com.example.newsdemoapplication.R
import com.example.newsdemoapplication.model.room.AppDataBase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDataBase::class.java,
            androidApplication().baseContext.getString(R.string.app_name)
        ).build()
    }

    single {
        get<AppDataBase>().getChapterDao()
    }
    single {
        get<AppDataBase>().getWordDao()
    }
}
