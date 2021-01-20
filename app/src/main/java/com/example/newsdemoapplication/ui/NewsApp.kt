package com.example.newsdemoapplication.ui

import com.example.newsdemoapplication.di.appComponent
import com.gzp.baselib.BaseApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NewsApp:BaseApplication(){
    override fun onCreate() {
        super.onCreate()
        configureDI()
    }

    private fun configureDI() = startKoin {
        androidContext(this@NewsApp)
        modules(appComponent)
    }
}