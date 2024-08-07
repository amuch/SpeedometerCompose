package net.ddns.muchserver.speedometercompose.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
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
import net.ddns.muchserver.speedometercompose.database.CheckPoint
import net.ddns.muchserver.speedometercompose.database.Trip
import net.ddns.muchserver.speedometercompose.preferences.UPDATE_INTERVAL_DEFAULT
import net.ddns.muchserver.speedometercompose.service.ServiceForeground
import java.lang.ref.WeakReference
import java.util.Calendar
import java.util.Date

const val INTERVAL_LOCATION_REQUEST = 500L
const val INTERVAL_LOCATION_REQUEST_FASTEST = 500L
const val INTERVAL_MAP_UPDATE = 5000L
const val DISPLACEMENT_MILE_ONE_TENTH = 170f
const val CONVERSION_MPS_TO_MPH = 2.236936f
const val CONVERSION_MPS_TO_KPH = 3.6f
const val CONVERSION_METERS_TO_MILES = 0.0006213712f
const val CONVERSION_METERS_TO_FEET = 3.28084f
const val CONVERSION_METERS_TO_KM = 0.001f
const val CAPACITY_LAT_LNG_LIST = 30
const val DISPLACEMENT_MIN_LAT_LNG = 0.0005
const val LAT_LNG_DEFAULT = 0.0
class SpeedometerViewModel(
    activity: Activity,
    tripViewModel: TripViewModel,
) : ViewModel() {

    private val activity: WeakReference<Activity>
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val tripViewModel: TripViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    val speed: MutableLiveData<Float> = MutableLiveData(0.0f)
    val speedMax: MutableLiveData<Float> = MutableLiveData(0.0f)
    val latitude: MutableLiveData<Double> = MutableLiveData(LAT_LNG_DEFAULT)
    val longitude: MutableLiveData<Double> = MutableLiveData(LAT_LNG_DEFAULT)
    val bearing: MutableLiveData<Float> = MutableLiveData(0.0f)
    val altitude: MutableLiveData<Double> = MutableLiveData(0.0)
    val requestingUpdates: MutableLiveData<Boolean> = MutableLiveData(false)
    val latLng: MutableLiveData<LatLng> = MutableLiveData(LatLng(LAT_LNG_DEFAULT, LAT_LNG_DEFAULT))
    val lastUpdate: MutableLiveData<Long> = MutableLiveData(0L)
    val distance: MutableLiveData<Double> = MutableLiveData(0.0)

    private val locations = ArrayList<LatLng>()
    val listLocations: MutableLiveData<ArrayList<LatLng>> = MutableLiveData(locations)

    val trip: MutableLiveData<Trip> = tripViewModel.trip
    val tripInProcess: MutableLiveData<Boolean> = tripViewModel.tripInProcess
    val checkPoints: LiveData<List<CheckPoint>> = tripViewModel.checkPoints

    val updateInterval: MutableLiveData<Long> = MutableLiveData(UPDATE_INTERVAL_DEFAULT)

    init {
        this.activity = WeakReference(activity)
        this.tripViewModel = tripViewModel
        getLocationUpdates()
    }

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

                if(locationResult.locations.isNotEmpty()) {
                    val location = locationResult.lastLocation

                    latitude.value = location.latitude
                    longitude.value = location.longitude

                    val timeCurrent = System.currentTimeMillis()
                    println("updateInterval ${updateInterval.value}")
                    if(timeCurrent - lastUpdate.value!! > updateInterval.value!!) {
                        println("updateInterval exceeded")
                        lastUpdate.value = timeCurrent

                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        latLng.value = currentLatLng
                        addLocation(currentLatLng)
                        sumDistance()
                    }
                    speed.value = location.speed
                    if(location.hasAltitude()) {
                        altitude.value = location.altitude
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

    private fun addLocation(latLng: LatLng) {
        val currentTime = Calendar.getInstance().time
        println("$currentTime")
        locations.add(latLng)

        if(tripInProcess.value!!) {
            val idTrip = trip.value!!.id
            println("Id trip ${idTrip}")
            val date = Date()
            val checkPoint = CheckPoint(
                idTrip = idTrip,
                latitude = latLng.latitude,
                longitude = latLng.longitude,
                date = date
            )
            tripViewModel.insertCheckPoint(checkPoint)
        }
    }

    private fun sumDistance() {
        if(locations.size < 2) {
            return
        }

        var distanceTotal = 0.0
        for(i in 0 until locations.size - 1) {
            val results = FloatArray(1)
            Location.distanceBetween(
                locations[i].latitude,
                locations[i].longitude,
                locations[i + 1].latitude,
                locations[i + 1].longitude,
                results
            )
            distanceTotal = distanceTotal.plus(results[0])
        }
        distance.value = distanceTotal
    }
}