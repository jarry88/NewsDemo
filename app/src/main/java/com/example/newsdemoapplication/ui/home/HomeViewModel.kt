package com.example.newsdemoapplication.ui.home

import androidx.lifecycle.MutableLiveData
import com.example.newsdemoapplication.view.ChapterDragView
import com.example.newsdemoapplication.model.ChapterVo
import com.example.newsdemoapplication.util.common.BaseViewModel
import kotlin.reflect.KProperty

/**
 * 保持首页的变量
 */
class HomeViewModel : BaseViewModel() {
    operator fun getValue(chapterDragView: ChapterDragView, property: KProperty<*>): HomeViewModel {
        return this
    }
    //章节列表
    val listChapter by lazy {
        MutableLiveData<MutableList<ChapterVo>>()
    }
    //当前章节
    val currChapter by lazy {
        MutableLiveData<ChapterVo>()
    }
}