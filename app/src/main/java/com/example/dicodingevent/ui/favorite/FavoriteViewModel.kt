package com.example.dicodingevent.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.local.entity.EventEntity
import com.example.dicodingevent.data.repositories.EventRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel() {
//    fun getEvents(active: Int) = eventRepository.getEvents(active)
    fun getFavoriteEvents() = eventRepository.getFavoritesEvents()

    fun setFavoriteEvent(event: EventEntity, favoriteState: Boolean) {
        viewModelScope.launch {
            eventRepository.setFavoriteEvent(event, favoriteState)
        }
    }

    fun deleteFavoriteEvent(eventId: Int) {
        viewModelScope.launch {
            eventRepository.deleteFavoriteEvent(eventId)
        }
    }
}