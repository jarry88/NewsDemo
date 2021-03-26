package com.example.newsdemoapplication.ui.home

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.newsdemoapplication.view.ChapterDragView
import com.example.newsdemoapplication.model.ChapterVo
import com.example.newsdemoapplication.model.ContentVo
import com.example.newsdemoapplication.model.NewsVo
import com.example.newsdemoapplication.util.common.BaseViewModel
import com.example.newsdemoapplication.util.rad
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
    val toast by lazy {
        MutableLiveData<String>()
    }

    // 前期提供假数据展示UI的方法
    fun randomData(){
        val list= mutableListOf<ChapterVo>()
//        Toast.makeText(context, "初始化生成了一些7 条假数据", Toast.LENGTH_SHORT).show()
        for (i in 4..9) {
            list.add(ChapterVo("章节$i", locked = random(), index = i).apply {
                val l = mutableListOf<NewsVo>()
                for (j in 0..20.rad) {
                    l.add(NewsVo("随机$j").apply {
                        val ll = mutableListOf<ContentVo>()
                        for (m in 0..20.rad) {
                            ll.add(ContentVo("随机$m", "sss"))
                        }
                        listContent = ll.toList()
                    })
                }
                listNews = l.toList()
            })
        }
        listChapter.postValue(list)
        currChapter.postValue(list.first())
    }
    fun saveChapter(chapterVo: ChapterVo) {
        listChapter.apply {
            var index=-1
            value?.forEachIndexed { i, it->
                if(it.id==chapterVo.id) index =i }
            val newlist =if(index<0)value else {value?.removeAt(index)
                value
            }
            newlist?.also {
                val chapterVoIndex =if(chapterVo.index<0) 0 else if(chapterVo.index>=newlist.size) newlist.size else chapterVo.index
                if(chapterVo.index!=index){ chapterVo.index=chapterVoIndex }
                it.add(chapterVo.index, chapterVo)
            }?.let {
                toast.postValue("${if (index>=0) "更新" else "新增"} 了-《${chapterVo.chapterName}》")
                postValue(it)
            }
        }
    }
    private fun random()=
            Math.random()>0.5
    fun findNewsVo(p:Int)=currChapter.value?.listNews?.let {
        if (p>it.size){
            it.last()
        }else{
            it.get(p)
        }
    }

}