package com.example.dicodingevent.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.local.SettingPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getReminderSettings(): LiveData<Boolean> {
        return pref.getReminderSetting().asLiveData()
    }

    fun getReminderStatus(): LiveData<Set<String>> {
        return pref.getReminderStatus().asLiveData()
    }

    fun getWorkId(): LiveData<Set<String>> {
        return pref.getWorkId().asLiveData()
    }

    fun saveReminderSetting(isReminderActive: Boolean) {
        viewModelScope.launch {
            pref.saveReminderSetting(isReminderActive)
        }
    }

    fun saveReminderStatus(workState: String, workId: String) {
        viewModelScope.launch {
            pref.saveReminderStatus(workState, workId)
        }
    }
}