package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.TripViewModel

@Composable
fun ButtonToggleTrip(
    modifier: Modifier,
    tripViewModel: TripViewModel,
    speedometerViewModel: SpeedometerViewModel,
    settingsViewModel: SettingsViewModel
) {
    val tripInProcess: Boolean by tripViewModel.tripInProcess.observeAsState(false)
    val text = if(tripInProcess) "Stop" else "Start"
    Button(
        onClick = {
            if(!tripInProcess) {
                tripViewModel.startTrip()
            }
            else {
                tripViewModel.stopTrip()
            }
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = settingsViewModel.colorScheme.value!![INDEX_COLOR_BUTTON_BACKGROUND],
            contentColor = settingsViewModel.colorScheme.value!![INDEX_COLOR_BUTTON_TEXT]
        ),
    ) {
        Text(text)
    }
}