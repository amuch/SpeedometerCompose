package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel

@Composable
fun ControlsColumn(
    modifier: Modifier,
    activity: MainActivity,
    speedometerViewModel: SpeedometerViewModel,
    preferencesViewModel: PreferencesViewModel,
    colorList: List<Color>,
    settingsViewModel: SettingsViewModel
) {
    var screenAwake by remember { mutableStateOf(false) }
    settingsViewModel.screenAwake.observe(activity) {
        screenAwake = it
    }
    Column(
        modifier = modifier
    ) {
        Button(
            modifier = Modifier
                .padding(10.dp)
                .align(alignment = Alignment.Start),
                    onClick = {
                        settingsViewModel.closeSettings()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorList[INDEX_COLOR_BUTTON_BACKGROUND],
                contentColor = colorList[INDEX_COLOR_BUTTON_TEXT]
            )
        ) {
            Text("Close")
        }
        ButtonSetting(
            modifier = Modifier
                .padding(10.dp),
            preferencesViewModel = preferencesViewModel,
            colorList = colorList
        )
        Button(
            modifier = Modifier
                .padding(10.dp)
                .align(alignment = Alignment.Start),
            onClick = {
                if(screenAwake) {
                    settingsViewModel.screenOff()
                }
                else {
                    settingsViewModel.screenOn()
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorList[INDEX_COLOR_BUTTON_BACKGROUND],
                contentColor = colorList[INDEX_COLOR_BUTTON_TEXT]
            )
        ) {
            Text(if(screenAwake) "Screen ON" else "Screen OFF")
        }
        ButtonResetMaxSpeed(
            modifier = Modifier
                .padding(5.dp),
            speedometerViewModel = speedometerViewModel,
            colorList = colorList
        )
        ButtonToggleLocation(
            modifier = Modifier
                .padding(5.dp),
            speedometerViewModel = speedometerViewModel,
            colorList = colorList
        )
        ButtonToggleService(
            modifier = Modifier
                .padding(5.dp),
            colorList = colorList
        )
    }
}