package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.viewmodel.INTERVAL_MAP_UPDATE
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel

@Composable
fun Map(
    modifier: Modifier,
    activity: MainActivity,
    speedometerViewModel: SpeedometerViewModel,
    preferencesViewModel: PreferencesViewModel,
    settingsViewModel: SettingsViewModel,
    colorList: List<Color>
) {
    var latLngCurrent by remember { mutableStateOf(LatLng(speedometerViewModel.latitude.value!!, speedometerViewModel.longitude.value!!)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLngCurrent, ZOOM_DEFAULT)
    }
    var lastUpdate by remember { mutableStateOf(speedometerViewModel.lastUpdate.value!!) }
    speedometerViewModel.latLng.observe(activity) {
        latLngCurrent = it
        if(speedometerViewModel.lastUpdate.value!! - INTERVAL_MAP_UPDATE > lastUpdate) {
            val zoom = cameraPositionState.position.zoom
            cameraPositionState.position = CameraPosition.fromLatLngZoom(latLngCurrent, zoom)
            lastUpdate = speedometerViewModel.lastUpdate.value!!
        }
    }
    val lLang = LatLng(latLngCurrent.latitude + 0.001f, latLngCurrent.longitude + 0.001f)
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
//        properties = MapProperties(mapType = MapType.NORMAL)
    ) {
        Marker(
            position = latLngCurrent,
            title = "You are here",
            snippet = "Marker",
            alpha = 0.8f,
            icon = BitmapDescriptorFactory.defaultMarker(115.0f)
        )
//        Marker(
//            position = lLang,
//            title = "You aren't here",
//            snippet = "Marker",
//            alpha = 0.8f,
//            icon = BitmapDescriptorFactory.defaultMarker(30.0f)
//        )
    }
}