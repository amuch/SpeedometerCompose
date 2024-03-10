package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel

@Composable
fun ButtonResetMaxSpeed(
    modifier: Modifier,
    speedometerViewModel: SpeedometerViewModel,
    settingsViewModel: SettingsViewModel
) {
    Button(
        onClick = {
            speedometerViewModel.resetSpeedMax()
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = settingsViewModel.colorScheme.value!![INDEX_COLOR_BUTTON_BACKGROUND],
            contentColor = settingsViewModel.colorScheme.value!![INDEX_COLOR_BUTTON_TEXT]
        ),
    ) {
        Text("Reset Max")
    }
}