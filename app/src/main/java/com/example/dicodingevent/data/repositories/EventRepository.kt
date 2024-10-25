package com.example.dicodingevent.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.dicodingevent.data.local.entity.EventEntity
import com.example.dicodingevent.data.local.room.EventDao
import com.example.dicodingevent.data.response.EventDetailItem
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.data.retrofit.ApiService
import com.example.dicodingevent.utilities.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {

    fun getEvents(active: Int): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.suspendedGetEvents(active)
            val events = response.listEvents
            val eventList = events?.map { event ->
                val isFavorite = eventDao.isEventFavorite(event?.id!!)
                EventEntity(
                    event.id,
                    event.name!!,
                    event.summary!!,
                    event.mediaCover!!,
                    event.imageLogo!!,
                    event.beginTime!!,
                    isFavorite
                )
            }

            eventDao.deleteAll()
            eventDao.insertEvent(eventList!!)
        } catch (e: Exception) {
            Log.d("EventRepository", "getAllEvents: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<EventEntity>>> = eventDao.getAllEvents().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getFavoritesEvents(): LiveData<List<EventEntity>> {
        return eventDao.getFavoriteEvents()
    }

    suspend fun setFavoriteEvent(event: EventEntity, favoriteState: Boolean) {
        event.isFavorite = favoriteState
        eventDao.updateEvent(event)
    }

    private val _events = MutableLiveData<EventDetailItem?>()
    fun fetchEventDetail(eventId: Int): LiveData<Result<EventDetailItem?>> = liveData {
        emit(Result.Loading)
        val events: LiveData<Result<EventDetailItem?>>
        try {
            val response = apiService.getEventDetail(eventId)
             events = _events.map { Result.Success(it) }
            _events.value = response.event
            emitSource(events)
        } catch (e: Exception) {
            Log.d("EventRepository", "getEventDetail: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }

    }

//    suspend fun saveFavoriteEvent(event: EventEntity) {
//        eventDao.insert(event)
//    }


    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}