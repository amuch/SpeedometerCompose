package net.ddns.muchserver.speedometercompose.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.ddns.muchserver.speedometercompose.preferences.DataStoreRepository

class PreferencesViewModel(application: Application): AndroidViewModel(application) {
    private val repository = DataStoreRepository(application)
    val readFromDataStore = repository.readFromDataStore.asLiveData()

    fun saveToDataStore(key: String, any: Any) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveToDataStore(key, any)
    }
}