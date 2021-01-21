package com.example.newsdemoapplication.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsdemoapplication.model.repository.NewsService
import com.example.newsdemoapplication.model.room.Chapter
import com.example.newsdemoapplication.model.room.ChapterDao
import com.example.newsdemoapplication.util.randomUrl
import kotlinx.coroutines.launch

class HomeV2ViewModel(private val chapterDao: ChapterDao,val newsApi:NewsService):ViewModel() {

    var chapterId=0L
    var chapterIndex=0
    fun factoryChapter(index:Int=chapterIndex++,name:String="$chapterIndex sss",isLocked:Boolean=false)=Chapter(chapterId++,index,name,isLocked, randomUrl())
    val currSelectChapter by lazy {
        MutableLiveData<Chapter>()
    }
    fun getAll()=chapterDao.getAllChapters()

    fun deleteAll()=viewModelScope.launch {
        chapterDao.deleteAll()
        chapterIndex=0
    }

    fun insert(chapter:Chapter)=viewModelScope.launch {
        chapterDao.insert(chapter)
    }

}