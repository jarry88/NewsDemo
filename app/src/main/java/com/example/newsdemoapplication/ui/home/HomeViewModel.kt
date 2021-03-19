package com.example.newsdemoapplication.ui.home

import androidx.lifecycle.MutableLiveData
import com.example.newsdemoapplication.view.ChapterDragView
import com.example.newsdemoapplication.model.ChapterVo
import com.example.newsdemoapplication.util.common.BaseViewModel
import kotlin.reflect.KProperty

class HomeViewModel : BaseViewModel() {
    operator fun getValue(chapterDragView: ChapterDragView, property: KProperty<*>): HomeViewModel {
        return this
    }

    val listChapter by lazy {
        MutableLiveData<MutableList<ChapterVo>>()
    }
    val currChapter by lazy {
        MutableLiveData<ChapterVo>()
    }
}