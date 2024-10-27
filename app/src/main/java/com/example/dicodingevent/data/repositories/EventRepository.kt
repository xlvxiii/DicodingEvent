package com.example.dicodingevent.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.dicodingevent.data.local.entity.EventEntity
import com.example.dicodingevent.data.local.room.EventDao
import com.example.dicodingevent.data.response.EventDetailItem
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.data.retrofit.ApiService

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao
) {

    private val _listEvents = MutableLiveData<List<ListEventsItem?>?>()
    private val listEvents: LiveData<Result<List<ListEventsItem?>?>> = _listEvents.map { Result.Success(it) }

    private val _listFinishedEvents = MutableLiveData<List<ListEventsItem?>?>()
    private val listFinishedEvents: LiveData<Result<List<ListEventsItem?>?>> = _listFinishedEvents.map { Result.Success(it) }

    private val _eventDetail = MutableLiveData<EventDetailItem?>()
    private val eventDetail: LiveData<Result<EventDetailItem?>> = _eventDetail.map { Result.Success(it) }

    private val _searchResult = MutableLiveData<List<ListEventsItem?>?>()
    private val searchResult: LiveData<Result<List<ListEventsItem?>?>> = _searchResult.map { Result.Success(it) }

    fun getEvents(active: Int, limit: Int?): LiveData<Result<List<ListEventsItem?>?>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.suspendedGetEvents(active, limit)
            if (active == 1) {
                _listEvents.value = response.listEvents
            } else {
                _listFinishedEvents.value = response.listEvents
            }
        } catch (e: Exception) {
            Log.d("EventRepository", "getEvents: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        if (active == 1) emitSource(listEvents) else emitSource(listFinishedEvents)
    }

    fun getSearchResult(keyword: String): LiveData<Result<List<ListEventsItem?>?>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.suspendedSearchEvent(-1, keyword)
            _searchResult.value = response.listEvents
        } catch (e: Exception) {
            Log.d("EventRepository", "getSearchResult: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        emitSource(searchResult)
    }

    fun getFavoritesEvents(): LiveData<List<EventEntity>> {
        return eventDao.getFavoriteEvents()
    }

    fun fetchEventDetail(eventId: Int): LiveData<Result<EventDetailItem?>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEventDetail(eventId)
            _eventDetail.value = response.event
        } catch (e: Exception) {
            Log.d("EventRepository", "getEventDetail: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        emitSource(eventDetail)
    }

    suspend fun saveFavoriteEvent(eventDetail: EventDetailItem?) {
        try {
            val eventData = EventEntity(
                eventDetail?.id!!,
                eventDetail.name!!,
                eventDetail.summary!!,
                eventDetail.mediaCover!!,
                eventDetail.imageLogo!!,
                eventDetail.beginTime!!,
            )

            eventDao.insertEvent(eventData)
        } catch (e: Exception) {
            Log.d("EventRepository", "saveFavoriteEvent: ${e.message.toString()} ")
        }
    }

    suspend fun deleteFavoriteEvent(eventId: Int?) {
            eventDao.deleteEventById(eventId!!)
    }

    fun isExist(eventId: Int): LiveData<Boolean> {
        return eventDao.isExist(eventId)
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao)
            }.also { instance = it }
    }
}