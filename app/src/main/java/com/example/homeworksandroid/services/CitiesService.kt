package com.example.homeworksandroid.services

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "38b77b2ee349a9b3c7b01a4c19660ebb"

interface CitiesService {
    @GET("data/2.5/weather")
    fun searchCityAsync(
        @Query("q") query: String,
        @Query("appid") ApiKey: String = API_KEY
    ): Deferred<Response<CoordinatesResponse>>
}