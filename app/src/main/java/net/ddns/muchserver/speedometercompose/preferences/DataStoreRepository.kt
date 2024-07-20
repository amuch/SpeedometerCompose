package net.ddns.muchserver.speedometercompose.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

const val THEME_LIGHT = "Light"
const val THEME_DARK = "Dark"
const val PREFERENCES = "preferences"
const val KEY_THEME = "theme"
const val KEY_INDEX_THEME = "indexTheme"
const val KEY_STANDARD_UNITS = "standardUnits"
const val KEY_UPDATE_INTERVAL = "updateInterval"
const val UPDATE_INTERVAL_DEFAULT = 45000L

class DataStoreRepository(context: Context) {
    private object PreferenceKeys {
        val theme = preferencesKey<String>(KEY_THEME)
        val indexTheme = preferencesKey<Int>(KEY_INDEX_THEME)
        val standardUnits = preferencesKey<Boolean>(KEY_STANDARD_UNITS)
        val updateInterval = preferencesKey<Long>(KEY_UPDATE_INTERVAL)
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCES
    )

    suspend fun saveToDataStore(key: String, any: Any) {
        dataStore.edit { preference ->
            when(key) {
                KEY_THEME -> preference[PreferenceKeys.theme] = any as String
                KEY_INDEX_THEME -> preference[PreferenceKeys.indexTheme] = any as Int
                KEY_STANDARD_UNITS -> preference[PreferenceKeys.standardUnits] = any as Boolean
                KEY_UPDATE_INTERVAL -> preference[PreferenceKeys.updateInterval] = any as Long
            }
        }
    }

    val readFromDataStore: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if(exception is IOException) {
                println(exception.message.toString())
                emit(emptyPreferences())
            }
            else {
                throw exception
            }
        }
        .map { preferences ->
            val theme = preferences[PreferenceKeys.theme] ?: THEME_LIGHT
            val indexTheme = preferences[PreferenceKeys.indexTheme] ?: 0
            val standardUnits = preferences[PreferenceKeys.standardUnits] ?: true
            val updateInterval = preferences[PreferenceKeys.updateInterval] ?: UPDATE_INTERVAL_DEFAULT
            UserPreferences(theme, indexTheme, standardUnits, updateInterval)
    }
}