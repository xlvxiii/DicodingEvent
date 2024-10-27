package com.example.dicodingevent.ui.finished_event

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.repositories.EventRepository

class FinishedEventViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun getFinishedEvents(active: Int = 0, limit: Int? = null) = eventRepository.getEvents(active, limit)
}