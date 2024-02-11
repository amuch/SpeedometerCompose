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

class DataStoreRepository(context: Context) {
    private object PreferenceKeys {
        val theme = preferencesKey<String>(KEY_THEME)
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCES
    )

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