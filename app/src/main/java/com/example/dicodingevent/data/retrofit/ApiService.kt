package com.example.dicodingevent.data.retrofit

import com.example.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvents(@Query("active") active: Int) : Call<EventResponse>

    @GET("events")
    fun getFiveActiveEvents(@Query("active") active: Int, @Query("limit") limit: Int) : Call<EventResponse>
}