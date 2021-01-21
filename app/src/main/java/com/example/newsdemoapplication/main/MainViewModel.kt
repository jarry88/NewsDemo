package com.example.newsdemoapplication.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsdemoapplication.model.room.Word
import com.example.newsdemoapplication.model.room.WordDao
import kotlinx.coroutines.launch

class MainViewModel(val wordDao: WordDao) : ViewModel(){

    val allWord by lazy {
        wordDao.getAllWords()
    }
    fun insert(word:Word)=viewModelScope.launch{
        wordDao.insertWord(word)
    }
    fun queryById(id:Long)= wordDao.queryById(id)

    fun delete()=viewModelScope.launch {
        wordDao.deleteAll()
    }
}