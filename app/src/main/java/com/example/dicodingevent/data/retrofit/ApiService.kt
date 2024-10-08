package com.example.dicodingevent.data.retrofit

import com.example.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvents(@Query("active") active: String) : Call<EventResponse>
}