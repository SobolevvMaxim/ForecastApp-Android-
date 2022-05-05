package com.example.forecast.feature_forecast.presentation.prefstore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val STORE_NAME = "chosen_city_store"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORE_NAME)

class PrefStore @Inject constructor(
    @ApplicationContext context: Context
) : IPrefStore {

    private val _dataStore = context.dataStore

    override fun getChosen(): Flow<String> =
        _dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { it[PreferencesKeys.CHOSEN_CITY_KEY] ?: "0" }


    override suspend fun changeChosen(newChosenID: String) {
        _dataStore.edit {
            it[PreferencesKeys.CHOSEN_CITY_KEY] = newChosenID
        }
    }

    private object PreferencesKeys {
        val CHOSEN_CITY_KEY = stringPreferencesKey("chosen_prefs")
    }
}