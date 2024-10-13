package com.example.dicodingevent.ui.event_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.response.EventDetailItem
import com.example.dicodingevent.data.response.EventDetailResponse
import com.example.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _eventDetail = MutableLiveData<EventDetailItem?>()
    val eventDetail: MutableLiveData<EventDetailItem?> = _eventDetail

    private val _isLoadSuccess = MutableLiveData<Boolean>()
    val isLoadSuccess: LiveData<Boolean> = _isLoadSuccess

    companion object{
        const val TAG = "EventDetailViewModel"
    }
    
    fun fetchEventDetail(eventId: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventDetail(eventId)
        
        client.enqueue(object: Callback<EventDetailResponse> {
            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _eventDetail.value = response.body()?.event
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _isLoadSuccess.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}