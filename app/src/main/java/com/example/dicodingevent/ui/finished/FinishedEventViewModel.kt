package com.example.dicodingevent.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FinishedEventViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is finished event Fragment"
    }
    val text: LiveData<String> = _text
}