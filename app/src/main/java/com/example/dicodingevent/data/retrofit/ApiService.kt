package com.example.dicodingevent.data.retrofit

import com.example.dicodingevent.data.response.EventDetailResponse
import com.example.dicodingevent.data.response.EventResponse
import retrofit2.http.*

interface ApiService {

    @GET("events")
    suspend fun suspendedGetEvents(@Query("active") active: Int, @Query("limit") limit: Int? = null) : EventResponse

    @GET("events/{id}")
    suspend fun getEventDetail(@Path("id") id: Int) : EventDetailResponse

    @GET("events")
    suspend fun suspendedSearchEvent(@Query("active") active: Int, @Query("q") q: String) : EventResponse
}