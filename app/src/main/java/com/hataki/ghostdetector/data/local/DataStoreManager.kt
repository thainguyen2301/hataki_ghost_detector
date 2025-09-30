package com.hataki.ghostdetector.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("settings")

class DataStoreManager(private val context: Context) {

    companion object {
        const val IS_ONBOARDING = "IS_ONBOARDING"
        const val KEY_LANGUAGE = "selected_language"
        const val DEFAULT_LANGUAGE = "en"
    }

    suspend fun saveStringData(key: String, value: String) {
        val prfKey = stringPreferencesKey(key)
        context.dataStore.edit { prefs ->
            prefs[prfKey] = value
        }
    }

    fun getStringData(key: String): Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[stringPreferencesKey(key)]
    }

    fun getBoolData(key: String): Flow<Boolean?> = context.dataStore.data.map { prefs ->
        prefs[booleanPreferencesKey(key)]
    }

    suspend fun saveBoolData(value: Boolean, key: String) {
        val prfKey = booleanPreferencesKey(key)
        context.dataStore.edit { prefs ->
            prefs[prfKey] = value
        }
    }
}