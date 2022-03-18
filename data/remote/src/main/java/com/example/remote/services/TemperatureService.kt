package com.example.remote.services

import com.example.remote.dto.TemperaturesDto
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TemperatureService {
    @GET("data/2.5/onecall")
    fun searchTempAsync(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") ApiKey: String
    ): Deferred<Response<TemperaturesDto>>
}