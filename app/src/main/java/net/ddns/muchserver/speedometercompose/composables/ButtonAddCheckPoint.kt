package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import net.ddns.muchserver.speedometercompose.database.CheckPoint
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.TripViewModel
import java.util.Date

@Composable
fun ButtonAddCheckPoint(
    modifier: Modifier,
    tripViewModel: TripViewModel,
    speedometerViewModel: SpeedometerViewModel,
    settingsViewModel: SettingsViewModel
) {
    Button(
        onClick = {
            val idTrip = 0L
            val date = Date()
            val latitude = speedometerViewModel.latitude.value!!
            val longitude = speedometerViewModel.longitude.value!!
            val checkPoint = CheckPoint(
                idTrip = idTrip,
                date = date,
                latitude = latitude,
                longitude = longitude
            )
            tripViewModel.insertCheckPoint(checkPoint)
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = settingsViewModel.colorScheme.value!![INDEX_COLOR_BUTTON_BACKGROUND],
            contentColor = settingsViewModel.colorScheme.value!![INDEX_COLOR_BUTTON_TEXT]
        ),
    ) {
        Text("Add CheckPoint")
    }
}