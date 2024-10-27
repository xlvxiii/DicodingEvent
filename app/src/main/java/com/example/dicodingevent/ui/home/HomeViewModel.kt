package com.example.dicodingevent.ui.home

import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.repositories.EventRepository

class HomeViewModel(private val eventRepository: EventRepository) : ViewModel() {

    fun getActiveEvents(active: Int, limit: Int? = null) = eventRepository.getEvents(active, limit)
    fun getFinishedEvents(active: Int, limit: Int? = null) = eventRepository.getEvents(active, limit)
    fun getSearchResult(keyword: String) = eventRepository.getSearchResult(keyword)
}