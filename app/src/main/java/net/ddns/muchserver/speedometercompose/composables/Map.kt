package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.viewmodel.LAT_LNG_DEFAULT
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel

const val DISPLACEMENT_CAMERA_POSITION_MAX = 2
@Composable
fun Map(
    modifier: Modifier,
    activity: MainActivity,
    speedometerViewModel: SpeedometerViewModel,
    preferencesViewModel: PreferencesViewModel,
    settingsViewModel: SettingsViewModel
) {
    var latLngCurrent by remember { mutableStateOf(LatLng(speedometerViewModel.latitude.value!!, speedometerViewModel.longitude.value!!)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLngCurrent, ZOOM_DEFAULT)
    }
    var lastUpdate by remember { mutableStateOf(speedometerViewModel.lastUpdate.value!!) }
    speedometerViewModel.latLng.observe(activity) {
        latLngCurrent = it
        if(cameraPositionState.position.target.latitude - latLngCurrent.latitude > DISPLACEMENT_CAMERA_POSITION_MAX ||
           cameraPositionState.position.target.longitude - latLngCurrent.longitude > DISPLACEMENT_CAMERA_POSITION_MAX
        ) {
            val zoom = cameraPositionState.position.zoom
            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLngCurrent, zoom)
            lastUpdate = speedometerViewModel.lastUpdate.value!!
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
//        properties = MapProperties(mapType = MapType.NORMAL)
    ) {
        Marker(
            position = latLngCurrent,
            title = "${formatLatitude(latLngCurrent.latitude)}, ${formatLongitude(latLngCurrent.longitude)}",
            snippet = "Current Location",
            alpha = 0.8f,
            icon = BitmapDescriptorFactory.defaultMarker(settingsViewModel.hueCurrent.value!!)
        )
        for(i in 0 until speedometerViewModel.listLocations.value!!.size) {
            val latitude = speedometerViewModel.listLocations.value!![i].latitude
            val longitude = speedometerViewModel.listLocations.value!![i].longitude
            Marker(
                position = speedometerViewModel.listLocations.value!![i],
                title = "${formatLatitude(latitude)}, ${formatLongitude(longitude)}",
                snippet = "Marker",
                alpha = 0.8f,
                icon = BitmapDescriptorFactory.defaultMarker(settingsViewModel.hueTrip.value!!)
            )
        }
    }
}