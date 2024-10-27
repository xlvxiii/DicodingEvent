package com.example.dicodingevent.ui.active_event

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.repositories.EventRepository

class EventViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun getEvents(active: Int, limit: Int? = null) = eventRepository.getEvents(active, limit)
}