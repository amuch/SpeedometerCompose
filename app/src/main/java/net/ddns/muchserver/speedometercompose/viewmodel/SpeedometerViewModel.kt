package net.ddns.muchserver.speedometercompose.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.service.ServiceForeground
import java.lang.ref.WeakReference

const val INTERVAL_LOCATION_REQUEST = 500L
const val INTERVAL_LOCATION_REQUEST_FASTEST = 500L
const val INTERVAL_MAP_UPDATE = 10000L
const val DISPLACEMENT_MILE_ONE_TENTH = 170f
const val CONVERSION_MPS_TO_MPH = 2.236936f
const val CONVERSION_METERS_TO_FEET = 3.28084f

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

    val speed: MutableLiveData<Float> = MutableLiveData(0.0f)
    val speedMax: MutableLiveData<Float> = MutableLiveData(0.0f)
    val latitude: MutableLiveData<Double> = MutableLiveData(0.0)
    val longitude: MutableLiveData<Double> = MutableLiveData(0.0)
    val bearing: MutableLiveData<Float> = MutableLiveData(0.0f)
    val altitude: MutableLiveData<Double> = MutableLiveData(0.0)
    val requestingUpdates: MutableLiveData<Boolean> = MutableLiveData(false)
    val latLng: MutableLiveData<LatLng> = MutableLiveData(LatLng(0.0, 0.0))
    val lastUpdate: MutableLiveData<Long> = MutableLiveData(0L)

    fun resetSpeedMax() {
        speedMax.value = 0.0f
    }

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
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

//                println("Location update")
                if(locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation
                    println("Speed: $location")
                    latitude.value = location.latitude
                    longitude.value = location.longitude
                    lastUpdate.value = System.currentTimeMillis()
                    latLng.value = LatLng(location.latitude, location.longitude)
                    speed.value = location.speed * CONVERSION_MPS_TO_MPH
                    if(location.hasAltitude()) {
                        altitude.value = location.altitude * CONVERSION_METERS_TO_FEET
                    }
                    if(location.hasBearing()) {
                        bearing.value = location.bearing
                    }
                    if(speed.value!! > speedMax.value!!) {
                        speedMax.value = speed.value

//                        val serviceRunning = ServiceForeground.serviceRunning
//                        if(serviceRunning) {
//                            val context = activity.get()
//                            Intent(context, ServiceForeground::class.java).also {
//                                it.action = ServiceForeground.Actions.UPDATE.toString()
//                                it.putExtra("speedMax", speedMax.value)
//                                context!!.startService(it)
//                            }
//                        }
                    }
                }
            }
        }
    }
}