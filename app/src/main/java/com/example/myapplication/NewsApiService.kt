package com.example.myapplication

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("category") category: String = "general",
        @Query("lang") lang: String = "en",
        @Query("country") country: String,
        @Query("max") max: Int = 10,
        @Query("apikey") apiKey: String = "01171f7a5e48d1f7f777848d695252bd"
    ): NewsResponse

    @GET("search")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("lang") lang: String = "en",
        @Query("country") country: String = "us",
        @Query("max") max: Int = 10,
        @Query("apikey") apiKey: String = "01171f7a5e48d1f7f777848d695252bd"
    ): NewsResponse
}
