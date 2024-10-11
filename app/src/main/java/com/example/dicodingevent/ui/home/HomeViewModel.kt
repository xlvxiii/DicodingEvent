package com.example.dicodingevent.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.response.EventResponse
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _listEvent = MutableLiveData<List<ListEventsItem?>?>()
    val listEvent: LiveData<List<ListEventsItem?>?> = _listEvent

    private val _listFinishedEvent = MutableLiveData<List<ListEventsItem?>?>()
    val listFinishedEvent: LiveData<List<ListEventsItem?>?> = _listFinishedEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoading2 = MutableLiveData<Boolean>()
    val isLoading2: LiveData<Boolean> = _isLoading2

    companion object {
        private const val TAG = "HomeViewModel"
    }

    init {
        fetchEvents()
        fetchFinishedEvents()
    }

    private fun fetchEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFiveActiveEvents(1, 5)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listEvent.value = response.body()?.listEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun fetchFinishedEvents() {
        _isLoading2.value = true
        val client = ApiConfig.getApiService().getEvents(0)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading2.value = false
                if (response.isSuccessful) {
                    _listFinishedEvent.value = response.body()?.listEvents
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading2.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

}