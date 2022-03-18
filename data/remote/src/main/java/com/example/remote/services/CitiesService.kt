package com.example.remote.services

import com.example.remote.BuildConfig
import com.example.remote.dto.CityDto
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CitiesService {
    @GET("data/2.5/weather")
    fun searchCityAsync(
        @Query("q") query: String,
        @Query("appid") ApiKey: String = BuildConfig.API_KEY
    ): Deferred<Response<CityDto>>
}