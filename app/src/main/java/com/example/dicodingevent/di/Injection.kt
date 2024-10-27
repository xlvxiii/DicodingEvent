package com.example.dicodingevent.di

import android.content.Context
import com.example.dicodingevent.data.local.room.EventDatabase
import com.example.dicodingevent.data.repositories.EventRepository
import com.example.dicodingevent.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        return EventRepository.getInstance(apiService, dao)
    }
}