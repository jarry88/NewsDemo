package com.example.newsdemoapplication.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsdemoapplication.di.databaseModule
import com.example.newsdemoapplication.model.room.Word
import com.example.newsdemoapplication.model.room.AppDataBase
import com.example.newsdemoapplication.model.repository.WordRepository
import com.example.newsdemoapplication.model.room.WordDao
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application){
    val re :WordDao by databaseModule

    private val wordRepository by lazy {
        WordRepository(databaseModule.)
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