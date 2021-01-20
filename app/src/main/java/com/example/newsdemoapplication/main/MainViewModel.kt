package com.example.newsdemoapplication.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsdemoapplication.room.Word
import com.example.newsdemoapplication.room.WordDataBase
import com.example.newsdemoapplication.room.WordRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application){
    private val wordRepository by lazy {
        WordRepository(WordDataBase.getInstance(application).getWordDao())
    }
    val allWord by lazy {
        wordRepository.WordDao.getAllWords()
    }
    init {
        wordRepository.WordDao
    }
    fun insert(word:Word)=viewModelScope.launch{
        wordRepository.WordDao.insertWord(word)
    }
    fun queryById(id:Long)= wordRepository.WordDao.queryById(id)

    fun delete()=viewModelScope.launch {
        wordRepository.WordDao.deleteAll()
    }
}