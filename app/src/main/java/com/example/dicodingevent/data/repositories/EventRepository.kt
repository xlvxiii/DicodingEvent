package com.example.dicodingevent.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.dicodingevent.data.local.entity.EventEntity
import com.example.dicodingevent.data.local.room.EventDao
import com.example.dicodingevent.data.response.EventResponse
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
    private val result = MediatorLiveData<Result<List<EventEntity>>>()

    fun getEvents(active: Int): LiveData<Result<List<EventEntity>>> = liveData {
//        result.value = Result.Loading
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

//        val client = apiService.getEvents(-1)
//        client.enqueue(object : Callback<EventResponse> {
//            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val events = response.body()?.listEvents
//                    val eventList = ArrayList<EventEntity>()
//                    appExecutors.diskIO.execute {
//                        events?.forEach { event ->
//                            val isFavorite = eventDao.isEventFavorite(event?.id!!)
//                            val eventEntity = EventEntity(
//                                event.id,
//                                event.name!!,
//                                event.summary!!,
//                                event.mediaCover!!,
//                                event.imageLogo!!,
//                                event.beginTime!!,
//                                isFavorite
//                            )
//                            eventList.add(eventEntity)
//                        }
//                        eventDao.deleteAll()
//                        eventDao.insertEvent(eventList)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
//                result.value = Result.Error(t.message.toString())
//            }
//        })

//        val localData = eventDao.getAllEvents()
//        result.addSource(localData) { newData: List<EventEntity> ->
//            result.value = Result.Success(newData)
//        }
//        return result
    }

    fun getFavoritesEvents(): LiveData<List<EventEntity>> {
        return eventDao.getFavoriteEvents()
    }

    suspend fun setFavoriteEvent(event: EventEntity, favoriteState: Boolean) {
        event.isFavorite = favoriteState
        eventDao.updateEvent(event)
    }

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