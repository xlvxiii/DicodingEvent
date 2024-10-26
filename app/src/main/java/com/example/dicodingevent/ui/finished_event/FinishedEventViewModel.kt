package com.example.dicodingevent.ui.finished_event

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.repositories.EventRepository

class FinishedEventViewModel(private val eventRepository: EventRepository) : ViewModel() {

//    init {
//        fetchFinishedEvents()
//    }

//    private fun fetchFinishedEvents() {
//        _isLoading.value = true
//        val client = ApiConfig.getApiService().getEvents(0)
//        client.enqueue(object : Callback<EventResponse> {
//            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    _listEvent.value = response.body()?.listEvents
//                } else {
//                    Log.e(TAG, "onFailure: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
//                _isLoading.value = false
//                _isLoadSuccess.value = false
//                Log.e(TAG, "onFailure: ${t.message}")
//            }
//        })
//    }

    fun getFinishedEvents(active: Int = 0, limit: Int? = null) = eventRepository.getEvents(active, limit)
}