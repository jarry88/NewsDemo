package com.example.newsdemoapplication.di

import com.example.newsdemoapplication.main.MainViewModel
import com.example.newsdemoapplication.ui.home.ChapterDetailView
import com.example.newsdemoapplication.ui.home.ChapterDetailViewModel
import com.example.newsdemoapplication.ui.home.HomeV2ViewModel
import com.example.newsdemoapplication.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { HomeViewModel() }
    viewModel { MainViewModel(get()) }
    viewModel { HomeV2ViewModel(get(),get()) }
    viewModel { ChapterDetailViewModel(get(),get()) }
}
