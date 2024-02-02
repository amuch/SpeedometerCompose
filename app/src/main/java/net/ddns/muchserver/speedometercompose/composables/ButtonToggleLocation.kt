package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel

@Composable
fun ButtonToggleLocation(modifier: Modifier, speedometerViewModel: SpeedometerViewModel) {
    val requestingUpdates: Boolean by speedometerViewModel.requestingUpdates.observeAsState(false)
    Button(
        onClick = {
            if(requestingUpdates) {
                speedometerViewModel.stopLocationUpdates()
            }
            else {
                if(MainActivity.permissionsGranted) {
                    speedometerViewModel.requestLocationUpdates()
                }
            }
        },
        modifier = modifier
    ) {
        Text(
            text = if(requestingUpdates) "Stop Updates" else "Start Updates"
        )
    }
}