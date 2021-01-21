package com.example.newsdemoapplication.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsdemoapplication.model.repository.NewsService
import com.example.newsdemoapplication.model.room.Chapter
import com.example.newsdemoapplication.model.room.ChapterDao
import com.example.newsdemoapplication.model.room.Subsection
import com.example.newsdemoapplication.model.room.SubsectionDao
import com.example.newsdemoapplication.util.randomUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChapterDetailViewModel(private val subsectionDao: SubsectionDao, val newsApi:NewsService):ViewModel() {

    var id=0L
    var subsectionIndex=0
    val currSubsectionList by lazy {
        MutableLiveData<List<Subsection>>()
    }
    fun factorySubsection(chapterId:Long=0,name:String="$subsectionIndex aaa",subTitle:String="副标题")=
            Subsection(id++,chapterId,subsectionIndex++,name,subTitle, randomUrl())
    val currSelectSubsection by lazy {
        MutableLiveData<Subsection>()
    }
    fun getAllSubsection()=subsectionDao.getAllSubsection()

    fun deleteAllSubsection()=viewModelScope.launch {
        subsectionDao.deleteAll()
        subsectionIndex=0
    }

    fun insertSubsection(subsection: Subsection)=viewModelScope.launch {
        subsectionDao.insert(subsection)
    }
    fun insertAndGetSubsections(subsection: Subsection)=viewModelScope.launch {
        subsectionDao.insert(subsection)
        getSubsectionListByChapterId(subsection.chapter_id)
    }

//    fun getSubsectionListByChapterId(selectedChapterId: Long)= flow {
////        var info =subsectionDao.queryByChapterId(selectedChapterId)
////        val model= mapper2InfoModel.map(info)
////        emit(model)
////        var info =subsectionDao.queryByChapterId(selectedChapterId)
////        val model =i
//        emit(subsectionDao.queryByChapterId(selectedChapterId))
//    }.flowOn(Dispatchers.IO) // 通过 flowOn 切换到 IO 线程
    fun getSubsectionListByChapterId(selectedChapterId: Long)= viewModelScope.launch {
            currSubsectionList.postValue(subsectionDao.queryByChapter(selectedChapterId))
    }
    fun getSubsectionList(selectedChapterId: Long)= subsectionDao.getAllSubsection()
//            viewModelScope.launch {
//        currSubsectionList.postValue(subsectionDao.queryByChapter(selectedChapterId))
//    }

}