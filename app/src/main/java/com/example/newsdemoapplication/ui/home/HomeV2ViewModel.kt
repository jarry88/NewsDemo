package com.example.newsdemoapplication.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsdemoapplication.model.repository.NewsService
import com.example.newsdemoapplication.model.room.Chapter
import com.example.newsdemoapplication.model.room.ChapterDao
import kotlinx.coroutines.launch

class HomeV2ViewModel(private val chapterDao: ChapterDao,val newsApi:NewsService):ViewModel() {
    val currSelectChapter by lazy {
        MutableLiveData<Chapter>()
    }
    fun getAll()=chapterDao.getAllChapters()

    fun deleteAll()=viewModelScope.launch {
        chapterDao.deleteAll()
    }

    fun insert(chapter:Chapter)=viewModelScope.launch {
        chapterDao.insert(chapter)
    }

}