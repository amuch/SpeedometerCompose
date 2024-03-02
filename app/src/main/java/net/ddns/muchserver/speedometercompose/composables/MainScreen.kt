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

    var theme by remember { mutableStateOf(THEME_LIGHT) }
    preferencesViewModel.readFromDataStore.observe(activity) { themeSet ->
        theme = themeSet
    }

    val darkTheme = listOf(
        Color(0xFF000000),
        Color(0xFF000000),
        Color(0xFFB8B6B5),
        Color(0xFF8BC819),
        Color(0xFFFCFCFC)
    )
    val lightTheme = listOf(
        Color(0xFFFFFFFF),
        Color(0xFFD9D9D9),
        Color(0xFFD9D9D9),
        Color(0xFFF1731C),
        Color(0xFF585858)
    )

    val colorList = if(THEME_DARK == theme) darkTheme else lightTheme

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