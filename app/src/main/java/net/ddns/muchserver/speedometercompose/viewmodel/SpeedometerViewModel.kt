package net.ddns.muchserver.speedometercompose.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    var speed: MutableLiveData<Float> = MutableLiveData(0.0f)
    var speedMax: MutableLiveData<Float> = MutableLiveData(0.0f)
    var latitude: MutableLiveData<Double> = MutableLiveData(0.0)
    var longitude: MutableLiveData<Double> = MutableLiveData(0.0)
    var requestingUpdates: MutableLiveData<Boolean> = MutableLiveData(false)

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() {
        if(MainActivity.permissionsGranted) {
            coroutineScope.launch {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                requestingUpdates.value = true
            }
        }
    }

    fun stopLocationUpdates() {
        coroutineScope.launch {
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    requestingUpdates.value = false
                }
                else {
                    println("Failed to stop updates.")
                }
            }
        }
    }

    private fun getLocationUpdates() {
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
                    latitude.value = location.latitude
                    longitude.value = location.longitude
                    speed.value = location.speed * CONVERSION_MPS_TO_MPH
                    if(speed.value!! > speedMax.value!!) {
                        speedMax.value = speed.value
                    }
                }
            }
        }
    }
}