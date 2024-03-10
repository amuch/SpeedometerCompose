package net.ddns.muchserver.speedometercompose.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import java.lang.IllegalArgumentException

const val ERROR_UNKNOWN_VIEW_MODEL = "Unknown View Model Class"
class SpeedometerViewModelFactory(private val activity: Activity): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SpeedometerViewModel::class.java)) {
            return SpeedometerViewModel(activity) as T
        }

        throw IllegalArgumentException(ERROR_UNKNOWN_VIEW_MODEL)
    }
}