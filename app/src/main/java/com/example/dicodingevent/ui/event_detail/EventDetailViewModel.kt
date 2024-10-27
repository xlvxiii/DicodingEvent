package com.example.dicodingevent.ui.event_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.repositories.EventRepository
import com.example.dicodingevent.data.response.EventDetailItem
import kotlinx.coroutines.launch

class EventDetailViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun fetchEventDetail(eventId: Int) = eventRepository.fetchEventDetail(eventId)
    fun saveFavoriteEvent(eventDetail: EventDetailItem?) {
        viewModelScope.launch {
            eventRepository.saveFavoriteEvent(eventDetail)
        }
    }

    fun isFavoriteEvent(eventId: Int): LiveData<Boolean> {
        return eventRepository.isExist(eventId)
    }

    fun deleteFavoriteEvent(eventId: Int?) {
        viewModelScope.launch {
            eventRepository.deleteFavoriteEvent(eventId)
        }
    }
}