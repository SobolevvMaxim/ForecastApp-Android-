package com.example.remote.services

import com.example.remote.dto.CityDto
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CitiesService {
    @GET("weather")
    fun searchCityAsync(
        @Query("q") query: String,
    ): Deferred<Response<CityDto>>
}