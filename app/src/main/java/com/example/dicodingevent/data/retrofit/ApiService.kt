package com.example.dicodingevent.data.retrofit

import com.example.dicodingevent.data.response.EventDetailResponse
import com.example.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvents(@Query("active") active: Int) : Call<EventResponse>

    @GET("events")
    suspend fun suspendedGetEvents(@Query("active") active: Int) : EventResponse

    @GET("events")
    fun getFiveActiveEvents(@Query("active") active: Int, @Query("limit") limit: Int) : Call<EventResponse>

    @GET("events/{id}")
    suspend fun getEventDetail(@Path("id") id: Int) : EventDetailResponse

    @GET("events")
    fun searchEvent(@Query("active") active: Int, @Query("q") q: String) : Call<EventResponse>
}