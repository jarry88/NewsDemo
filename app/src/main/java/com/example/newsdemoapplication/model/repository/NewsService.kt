package com.example.newsdemoapplication.model.repository

import com.example.newsdemoapplication.model.room.Chapter
import retrofit2.Call
import retrofit2.http.GET

interface NewsService {
//
//    @GET("pokemon.json")
//    fun get(): Call<List<Pokemon>>
    @GET("chapter.json")
    fun get(): Call<List<Chapter>>
}