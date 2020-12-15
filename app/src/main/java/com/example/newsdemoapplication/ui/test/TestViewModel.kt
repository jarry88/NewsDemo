package com.example.newsdemoapplication.ui.test

import androidx.lifecycle.MutableLiveData
import com.example.newsdemoapplication.view.ChapterDragView
import com.example.newsdemoapplication.vo.ChapterVo
import com.gzp.baselib.base.BaseViewModel
import kotlin.reflect.KProperty

class TestViewModel : BaseViewModel() {
    operator fun getValue(chapterDragView: ChapterDragView, property: KProperty<*>): TestViewModel {
        return this
    }

    val listChapter by lazy {
        MutableLiveData<MutableList<ChapterVo>>()
    }
//    val currChapter by lazy {
//        MutableLiveData<ChapterVo>()
//    }
}