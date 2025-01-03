package com.example.dicodingevent.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.repositories.EventRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getFavoriteEvents() = eventRepository.getFavoritesEvents()

    fun deleteFavoriteEvent(eventId: Int) {
        viewModelScope.launch {
            eventRepository.deleteFavoriteEvent(eventId)
        }
    }
}