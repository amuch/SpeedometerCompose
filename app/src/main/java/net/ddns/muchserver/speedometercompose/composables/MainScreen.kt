package net.ddns.muchserver.speedometercompose.composables

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.TripViewModel


const val INDEX_COLOR_PRIMARY = 0
const val INDEX_COLOR_SECONDARY = 1
const val INDEX_COLOR_TERTIARY = 2
const val INDEX_COLOR_BUTTON_BACKGROUND = 3
const val INDEX_COLOR_BUTTON_TEXT = 4

@Composable
fun MainScreen(
    activity: MainActivity,
    tripViewModel: TripViewModel,
    speedometerViewModel: SpeedometerViewModel,
    preferencesViewModel: PreferencesViewModel,
    settingsViewModel: SettingsViewModel,
) {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    if(Configuration.ORIENTATION_PORTRAIT == orientation) {
        LayoutPortrait(
            activity,
            tripViewModel,
            speedometerViewModel,
            preferencesViewModel,
            settingsViewModel
        )
    }
    else {
        LayoutLandscape(
            activity,
            tripViewModel,
            speedometerViewModel,
            preferencesViewModel,
            settingsViewModel
        )
    }
}