package net.ddns.muchserver.speedometercompose.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.ddns.muchserver.speedometercompose.repository.DataStoreRepository

class PreferenceViewModel(application: Application): AndroidViewModel(application) {

    private val repository = DataStoreRepository(application)
    val readFromDataStore = repository.readThemeFromDataStore.asLiveData()

    fun saveToDataStore(theme: String) = viewModelScope.launch( Dispatchers.IO ) {
        repository.saveThemeToDataStore(theme)
    }
}