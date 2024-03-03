package net.ddns.muchserver.speedometercompose.composables

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.repository.THEME_DARK
import net.ddns.muchserver.speedometercompose.repository.THEME_LIGHT
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel


const val INDEX_COLOR_PRIMARY = 0
const val INDEX_COLOR_SECONDARY = 1
const val INDEX_COLOR_TERTIARY = 2
const val INDEX_COLOR_BUTTON_BACKGROUND = 3
const val INDEX_COLOR_BUTTON_TEXT = 4

@Composable
fun MainScreen(
    activity: MainActivity,
    speedometerViewModel: SpeedometerViewModel,
    preferencesViewModel: PreferencesViewModel,
    settingsViewModel: SettingsViewModel,
) {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    var colorList by remember { mutableStateOf(settingsViewModel.lightTheme) }
    settingsViewModel.colorScheme.observe(activity) { colors ->
        colorList = colors
    }

    if(Configuration.ORIENTATION_PORTRAIT == orientation) {
        LayoutPortrait(
            activity,
            speedometerViewModel,
            preferencesViewModel,
            colorList,
            settingsViewModel
        )
    }
    else {
        LayoutLandscape(
            activity,
            speedometerViewModel,
            preferencesViewModel,
            colorList,
            settingsViewModel
        )
    }
}