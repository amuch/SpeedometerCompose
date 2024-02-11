package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel

@Composable
fun ButtonResetMaxSpeed(modifier: Modifier, speedometerViewModel: SpeedometerViewModel) {
    Button(
        onClick = {
            speedometerViewModel.resetSpeedMax()
        },
        modifier = modifier
    ) {
        Text("Reset Max")
    }
}