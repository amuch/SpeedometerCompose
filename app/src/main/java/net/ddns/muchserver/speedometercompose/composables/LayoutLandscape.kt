package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.TripViewModel

@Composable
fun LayoutLandscape(
    activity: MainActivity,
    tripViewModel: TripViewModel,
    speedometerViewModel: SpeedometerViewModel,
    preferencesViewModel: PreferencesViewModel,
    settingsViewModel: SettingsViewModel
) {
    val colorScheme: List<Color> by settingsViewModel.colorScheme.observeAsState(settingsViewModel.schemeLight(0))
    val brushBackground = Brush.verticalGradient(
        colors = colorScheme.subList(INDEX_COLOR_PRIMARY, INDEX_COLOR_TERTIARY + 1)
    )

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = brushBackground)
    ) {
        SpeedometerGauge(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight()
                .padding(10.dp)
                .background(color = Color.Transparent),
            speedometerViewModel = speedometerViewModel,
            settingsViewModel = settingsViewModel
        )
        MapTab(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.Transparent),
            activity = activity,
            tripViewModel = tripViewModel,
            speedometerViewModel = speedometerViewModel,
            preferencesViewModel = preferencesViewModel,
            settingsViewModel = settingsViewModel
        )
    }
}