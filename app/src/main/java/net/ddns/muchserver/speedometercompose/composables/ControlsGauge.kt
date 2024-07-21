package net.ddns.muchserver.speedometercompose.composables

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.service.ServiceForeground
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel

const val INDEX_LOCATION_UPDATES = 1
const val INDEX_FOREGROUND_SERVICE = 2
const val INDEX_WAKE_SCREEN = 3
@Composable
fun ControlsGauge(
    modifier: Modifier,
    activity: MainActivity,
    speedometerViewModel: SpeedometerViewModel,
    settingsViewModel: SettingsViewModel
) {
    val scrollState = rememberScrollState()

    var screenAwake by remember { mutableStateOf(false) }
    settingsViewModel.screenAwake.observe(activity) {
        screenAwake = it
    }

    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxSize()
                .background(
                    color = Color.Transparent
                )
                .verticalScroll(scrollState)
        )
    ) {
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
                "This setting requests location updates. This is necessary for the app's core functionality."
            ),
            index = INDEX_LOCATION_UPDATES
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
            isSet = screenAwake,
            setCheckedOn = { settingsViewModel.screenOn() },
            setCheckedOff = { settingsViewModel.screenOff() },
            messages = arrayOf(
                "Screen ON",
                "Screen OFF",
                "This setting assures that the screen stays on while the app is running. This will reduce battery life."
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