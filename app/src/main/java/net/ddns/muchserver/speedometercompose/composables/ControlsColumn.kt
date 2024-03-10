package net.ddns.muchserver.speedometercompose.composables

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.preferences.KEY_INDEX_THEME
import net.ddns.muchserver.speedometercompose.preferences.KEY_STANDARD_UNITS
import net.ddns.muchserver.speedometercompose.preferences.KEY_THEME
import net.ddns.muchserver.speedometercompose.preferences.THEME_DARK
import net.ddns.muchserver.speedometercompose.preferences.THEME_LIGHT
import net.ddns.muchserver.speedometercompose.service.ServiceForeground
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import java.lang.StrictMath.floor


const val INDEX_LOCATION_UPDATES = 1
const val INDEX_UNITS = 2
const val INDEX_FOREGROUND_SERVICE = 3
const val INDEX_COLOR_SCHEME = 4
const val INDEX_WAKE_SCREEN = 5

@Composable
fun ControlsColumn(
    modifier: Modifier,
    activity: MainActivity,
    speedometerViewModel: SpeedometerViewModel,
    preferencesViewModel: PreferencesViewModel,
    settingsViewModel: SettingsViewModel
) {
    var screenAwake by remember { mutableStateOf(false) }
    settingsViewModel.screenAwake.observe(activity) {
        screenAwake = it
    }
    var theme by remember { mutableStateOf(THEME_LIGHT) }
    preferencesViewModel.readFromDataStore.observe(activity) { preferences ->
        theme = preferences.theme
    }

    var indexTheme by remember { mutableStateOf(0) }
    preferencesViewModel.readFromDataStore.observe(activity) { preferences ->
        indexTheme = preferences.indexTheme
    }

    var standardUnits by remember { mutableStateOf(true) }
    preferencesViewModel.readFromDataStore.observe(activity) { preferences ->
        standardUnits = preferences.standardUnits
    }

    Column(
        modifier = modifier
    ) {
        Button(
            modifier = Modifier
                .padding(20.dp)
                .align(alignment = Alignment.End),
            onClick = {
                settingsViewModel.closeSettings()
                settingsViewModel.setIndexOpenSetting(0)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = settingsViewModel.colorScheme.value!![INDEX_COLOR_BUTTON_BACKGROUND],
                contentColor = settingsViewModel.colorScheme.value!![INDEX_COLOR_BUTTON_TEXT]
            )
        ) {
            Text("X")
        }

        val requestingUpdates: Boolean by speedometerViewModel.requestingUpdates.observeAsState(false)
        SettingRow(
            modifier = Modifier
                .fillMaxWidth(),
            settingsViewModel = settingsViewModel,
            isSet = requestingUpdates,
            setCheckedOn = {
                if(MainActivity.permissionsGranted) {
                    speedometerViewModel.requestLocationUpdates()
                }
            },
            setCheckedOff = { speedometerViewModel.stopLocationUpdates() },
            messages = arrayOf(
                "Location ON",
                "Location OFF",
                "This setting requests location updates."
            ),
            index = INDEX_LOCATION_UPDATES
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
                "This setting determines the units in which the speed, distance, and altitude are reported. When toggled, speed will be reported in miles per hour, distance in miles, and altitude in feet. Otherwise, speed will be reported in kilometers per hour, distance in kilometers, and elevation in meters."
            ),
            index = INDEX_UNITS
        )

        val context = LocalContext.current
        val serviceRunning = ServiceForeground.serviceRunning
        SettingRow(
            modifier = Modifier
                .fillMaxWidth(),
            settingsViewModel = settingsViewModel,
            isSet = serviceRunning,
            setCheckedOn = {
                Intent(context, ServiceForeground::class.java).also {
                    it.action = ServiceForeground.Actions.START.toString()
                    context.startService(it)
                }
            },
            setCheckedOff = {
                Intent(context, ServiceForeground::class.java).also {
                    it.action = ServiceForeground.Actions.STOP.toString()
                    context.startService(it)
                }
            },
            messages = arrayOf(
                "Service ON",
                "Service OFF",
                "This setting runs a background service. Location updates can continue in the background if this setting is enabled."
            ),
            index = INDEX_FOREGROUND_SERVICE
        )

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
            isSet = screenAwake,
            setCheckedOn = { settingsViewModel.screenOn() },
            setCheckedOff = { settingsViewModel.screenOff() },
            messages = arrayOf(
                "Screen ON",
                "Screen OFF",
                "This setting assures that the screen stays on while the app is running. This will increase battery usage."
            ),
            index = INDEX_WAKE_SCREEN
        )

        ButtonResetMaxSpeed(
            modifier = Modifier
                .padding(10.dp),
            speedometerViewModel = speedometerViewModel,
            settingsViewModel = settingsViewModel
        )
    }
}