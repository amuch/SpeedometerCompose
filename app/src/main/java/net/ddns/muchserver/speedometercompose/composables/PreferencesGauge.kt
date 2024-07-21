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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.preferences.KEY_INDEX_THEME
import net.ddns.muchserver.speedometercompose.preferences.KEY_STANDARD_UNITS
import net.ddns.muchserver.speedometercompose.preferences.KEY_THEME
import net.ddns.muchserver.speedometercompose.preferences.KEY_UPDATE_INTERVAL
import net.ddns.muchserver.speedometercompose.preferences.THEME_DARK
import net.ddns.muchserver.speedometercompose.preferences.THEME_LIGHT
import net.ddns.muchserver.speedometercompose.preferences.UPDATE_INTERVAL_DEFAULT
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel

const val INDEX_COLOR_SCHEME = 4
const val INDEX_INTERVAL = 5
const val INDEX_UNITS = 6
@Composable
fun PreferencesGauge(
    modifier: Modifier,
    activity: MainActivity,
    preferencesViewModel: PreferencesViewModel,
    settingsViewModel: SettingsViewModel
) {

    var theme by remember { mutableStateOf(THEME_LIGHT) }
    var indexTheme by remember { mutableIntStateOf(0) }
    var standardUnits by remember { mutableStateOf(true) }
    var updateInterval by remember { mutableStateOf(UPDATE_INTERVAL_DEFAULT) }
    preferencesViewModel.readFromDataStore.observe(activity) { preferences ->
        theme = preferences.theme
        indexTheme = preferences.indexTheme
        standardUnits = preferences.standardUnits
        updateInterval = preferences.updateInterval
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxSize()
                .background(
                    color = Color.Transparent
                )
                .verticalScroll(scrollState)
        ),
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

        SettingRow(
            modifier = Modifier
                .fillMaxWidth(),
            settingsViewModel = settingsViewModel,
            isSet = true,
            setCheckedOn = {
//                preferencesViewModel.saveToDataStore(KEY_THEME, THEME_DARK)
            },
            setCheckedOff = {
//                preferencesViewModel.saveToDataStore(KEY_THEME, THEME_LIGHT)
            },
            messages = arrayOf(
                "Minutes",
                "Seconds",
                "This setting is responsible for setting the frequency with which the app will place a marker on the map."
            ),
            index = INDEX_INTERVAL,
            composable = {
                var sliderPosition by remember { mutableStateOf(updateIntervalToIndex(updateInterval)) }
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
                        val updateIntervalSelected = indexToUpdateInterval(it)
                        if(updateIntervalSelected != updateInterval) {
                            updateInterval = updateIntervalSelected
                            preferencesViewModel.saveToDataStore(KEY_UPDATE_INTERVAL, updateIntervalSelected)
                        }
                    },
                    steps = 3,
                    valueRange = 0f..4f
                )
            }
        )

        SettingRow(
            modifier = Modifier
                .fillMaxWidth(),
            settingsViewModel = settingsViewModel,
            isSet = standardUnits,
            setCheckedOn = { preferencesViewModel.saveToDataStore(KEY_STANDARD_UNITS, true) },
            setCheckedOff = { preferencesViewModel.saveToDataStore(KEY_STANDARD_UNITS, false) },
            messages = arrayOf(
                "Standard Units",
                "SI Units",
                "This setting determines the units in which the speed, distance, and altitude are reported. When toggled, speed will be reported in miles per hour, distance in miles, and altitude in feet. Otherwise, speed will be reported in kilometers per hour, distance in kilometers, and altitude in meters."
            ),
            index = INDEX_UNITS
        )
    }
}

fun indexToUpdateInterval(index: Float): Long {
    return when(index) {
        0f -> 5000L
        1f -> 30000L
        2f -> UPDATE_INTERVAL_DEFAULT
        3f -> 90000L
        else -> 300000L
    }
}

fun updateIntervalToIndex(interval: Long): Float {
    return when (interval) {
        5000L -> 0.0f
        30000L -> 1.0f
        UPDATE_INTERVAL_DEFAULT -> 2.0f
        90000L -> 3.0f
        else -> 4.0f
    }
}