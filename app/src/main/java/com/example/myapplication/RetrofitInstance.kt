package com.example.myapplication

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val GNEWS_BASE_URL = "https://gnews.io/api/v4/"
    private const val COUNTRIES_BASE_URL = "https://restcountries.com/"

    val newsApi: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(GNEWS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }

    val countriesApi: CountriesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(COUNTRIES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountriesApiService::class.java)
    }
}
