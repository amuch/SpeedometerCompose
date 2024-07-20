package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.preferences.KEY_INDEX_THEME
import net.ddns.muchserver.speedometercompose.preferences.KEY_THEME
import net.ddns.muchserver.speedometercompose.preferences.THEME_DARK
import net.ddns.muchserver.speedometercompose.preferences.THEME_LIGHT
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.TripViewModel

@Composable
fun PreferencesGauge(
    activity: MainActivity,
    tripViewModel: TripViewModel,
    speedometerViewModel: SpeedometerViewModel,
    preferencesViewModel: PreferencesViewModel,
    settingsViewModel: SettingsViewModel
) {
    val scrollState = rememberScrollState()
    var theme by remember { mutableStateOf(THEME_LIGHT) }
    preferencesViewModel.readFromDataStore.observe(activity) { preferences ->
        theme = preferences.theme
    }

    var indexTheme by remember { mutableStateOf(0) }
    val checkPoints by tripViewModel.checkPoints.observeAsState(listOf())
    val checkPointsCurrent by tripViewModel.checkPointsCurrent.observeAsState(listOf())
    val trips by tripViewModel.trips.observeAsState(listOf())
    val colorScheme: List<Color> by settingsViewModel.colorScheme.observeAsState(settingsViewModel.schemeLight(0))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Transparent
            )
            .verticalScroll(scrollState),
    ) {
        SettingRow(
            modifier = Modifier
                .fillMaxWidth(),
            settingsViewModel = settingsViewModel,
            isSet = theme == THEME_DARK,
            setCheckedOn = { preferencesViewModel.saveToDataStore(KEY_THEME, THEME_DARK) },
            setCheckedOff = { preferencesViewModel.saveToDataStore(KEY_THEME, THEME_LIGHT) },
            messages = arrayOf(
                "Dark Mode",
                "Light Mode",
                "This setting is responsible for setting the color scheme of the application."
            ),
            index = INDEX_COLOR_SCHEME,
            composable = {
                var sliderPosition by remember { mutableStateOf(indexTheme.toFloat()) }
                Slider(
                    modifier = Modifier
                        .padding(20.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = settingsViewModel.colorScheme.value!![INDEX_COLOR_BUTTON_BACKGROUND],
                        activeTrackColor = settingsViewModel.colorScheme.value!![INDEX_COLOR_BUTTON_TEXT],
                        inactiveTickColor = settingsViewModel.colorScheme.value!![INDEX_COLOR_BUTTON_TEXT]
                    ),
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        if(it.toInt() != indexTheme) {
                            indexTheme = it.toInt()
                            settingsViewModel.setColorScheme(theme, it.toInt())
                            preferencesViewModel.saveToDataStore(KEY_INDEX_THEME, it.toInt())
                        }
                    },
                    steps = 3,
                    valueRange = 0f..4f
                )
            }
        )
    }
}