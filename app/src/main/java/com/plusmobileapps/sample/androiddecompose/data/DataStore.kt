package com.plusmobileapps.sample.androiddecompose.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

interface PreferenceDataStore {
    fun getIntPreferenceFlow(key: String, defaultValue: Int): Flow<Int>
    suspend fun setIntPreference(key: String, value: Int)
}

class PreferenceDataStoreImpl(private val context: Context) : PreferenceDataStore {
    override fun getIntPreferenceFlow(key: String, defaultValue: Int): Flow<Int> {
        val preferencesKey = intPreferencesKey(key)
        return context.dataStore.data.map { preferences ->
            preferences[preferencesKey] ?: defaultValue
        }
    }

    override suspend fun setIntPreference(key: String, value: Int) {
        val preferencesKey = intPreferencesKey(key)
        context.dataStore.edit { settings ->
            settings[preferencesKey] = value
        }
    }
}