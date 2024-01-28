package net.ddns.muchserver.speedometercompose.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import net.ddns.muchserver.speedometercompose.MainActivity
import java.lang.ref.WeakReference

const val INTERVAL_LOCATION_REQUEST = 500L
const val INTERVAL_LOCATION_REQUEST_FASTEST = 500L
const val DISPLACEMENT_MILE_ONE_TENTH = 170f
const val CONVERSION_MPS_TO_MPH = 2.236936f

class SpeedometerViewModel(activity: Activity) : ViewModel() {

    private val activity: WeakReference<Activity>
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    init {
        this.activity = WeakReference(activity)

        getLocationUpdates()
    }

    companion object {
        var speed by mutableStateOf(0.0f)
        var speedMax by mutableStateOf(0.0f)
        var latitude by mutableStateOf(0.0000)
        var longitude by mutableStateOf(0.0000)
        var requestingUpdates by mutableStateOf(false)
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() {
        println("request updates")
        if(MainActivity.permissionsGranted) {
            coroutineScope.launch {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                requestingUpdates = true
            }
        }
    }

    fun stopLocationUpdates() {
        coroutineScope.launch {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            requestingUpdates = false
        }
    }

    private fun getLocationUpdates() {
        println("get updates")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity.get()!!)
        locationRequest = LocationRequest()
        locationRequest.interval = INTERVAL_LOCATION_REQUEST
        locationRequest.fastestInterval = INTERVAL_LOCATION_REQUEST_FASTEST
//        locationRequest.smallestDisplacement = DISPLACEMENT_MILE_ONE_TENTH
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                println("Location update")
                if(locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation
                    println("Speed: ${location.speed}")
                    latitude = location.latitude
                    longitude = location.longitude
                    speed = location.speed * CONVERSION_MPS_TO_MPH
                    if(speed > speedMax) {
                        speedMax = speed
                    }
                }
            }
        }
    }
}