package com.example.dicodingevent.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    fun getReminderSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[REMINDER_KEY] ?: false
        }
    }

    fun getReminderStatus(): Flow<Set<String>> {
        return dataStore.data.map { preferences ->
            preferences[STATUS_KEY] ?: setOf()
        }
    }

    fun getWorkId(): Flow<Set<String>> {
        return dataStore.data.map { preferences ->
            preferences[WORK_ID] ?: setOf()
        }
    }

    suspend fun saveReminderSetting(isReminderActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMINDER_KEY] = isReminderActive
        }
    }
    suspend fun saveReminderStatus(workState: String, workId: String) {
        dataStore.edit { preferences ->
            preferences[STATUS_KEY] = setOf(workState)
            preferences[WORK_ID] = setOf(workId)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }

        private val WORK_ID = stringSetPreferencesKey("reminder_work_id")
        private val STATUS_KEY = stringSetPreferencesKey("reminder_status")
        private val REMINDER_KEY = booleanPreferencesKey("reminder_setting")
        private val THEME_KEY = booleanPreferencesKey("theme_setting")
    }
}