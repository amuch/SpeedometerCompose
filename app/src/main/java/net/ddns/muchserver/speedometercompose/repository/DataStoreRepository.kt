package net.ddns.muchserver.speedometercompose.repository

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

class DataStoreRepository(context: Context) {
    private object PreferenceKeys {
        val theme = preferencesKey<String>(KEY_THEME)
        val indexTheme = preferencesKey<Int>(KEY_INDEX_THEME)
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCES
    )

    suspend fun saveToDataStore(key: String, any: Any) {
        dataStore.edit { preference ->
            when(key) {
                KEY_THEME -> preference[PreferenceKeys.theme] = any as String
                KEY_INDEX_THEME -> preference[PreferenceKeys.indexTheme] =  any as Int
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
            UserPreferences(theme, indexTheme)
    }

    suspend fun saveThemeToDataStore(theme: String) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.theme] = theme
        }
    }

    val readThemeFromDataStore: Flow<String> = dataStore.data
        .catch { exception ->
            if(exception is IOException) {
                println(exception.message.toString())
                emit(emptyPreferences())
            }
            else {
                throw exception
            }
        }
        .map { preference ->
            val theme = preference[PreferenceKeys.theme] ?: THEME_LIGHT
            theme
        }
}