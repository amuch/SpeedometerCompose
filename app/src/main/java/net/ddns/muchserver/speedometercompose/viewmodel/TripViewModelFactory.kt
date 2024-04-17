package net.ddns.muchserver.speedometercompose.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TripViewModelFactory(val application: Application): ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return TripViewModel(application) as T
    }
}