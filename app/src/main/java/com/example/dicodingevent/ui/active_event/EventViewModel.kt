package com.example.dicodingevent.ui.active_event

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.repositories.EventRepository

class EventViewModel(private val eventRepository: EventRepository) : ViewModel() {

//    private val _listEvent = MutableLiveData<List<ListEventsItem?>?>()
//    val listEvent: LiveData<List<ListEventsItem?>?> = _listEvent
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _isLoadSuccess = MutableLiveData<Boolean>()
//    val isLoadSuccess: LiveData<Boolean> = _isLoadSuccess
//
//    companion object {
//        private const val TAG = "EventViewModel"
//    }
//
//    init {
//        fetchEvents()
//    }
//
//    private fun fetchEvents() {
//        _isLoading.value = true
//        val client = ApiConfig.getApiService().getEvents(1)
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

    fun getEvents(active: Int, limit: Int? = null) = eventRepository.getEvents(active, limit)
}