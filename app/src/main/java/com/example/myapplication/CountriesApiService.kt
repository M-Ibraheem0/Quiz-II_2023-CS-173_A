package com.example.myapplication

import retrofit2.http.GET
import retrofit2.http.Query

interface CountriesApiService {
    @GET("v3.1/all")
    suspend fun getAllCountries(
        @Query("fields") fields: String = "name,cca2"
    ): List<CountryResponse>
}
