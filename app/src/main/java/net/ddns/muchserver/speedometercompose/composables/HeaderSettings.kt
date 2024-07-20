package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.viewmodel.MenuVisible
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel


const val MENU_BUTTON_TEXT_TRIPS = "Trips"
const val MENU_BUTTON_TEXT_SETTINGS = "Settings"
@Composable
fun HeaderSettings(
    modifier: Modifier,
    activity: MainActivity,
//    tripViewModel: TripViewModel,
//    speedometerViewModel: SpeedometerViewModel,
//    preferencesViewModel: PreferencesViewModel,
    settingsViewModel: SettingsViewModel
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    )
    {
        var settingsVisible by remember { mutableStateOf(MenuVisible.MENU_MAP) }
        var textMenuButton = MENU_BUTTON_TEXT_TRIPS
        settingsViewModel.menuVisible.observe(activity) {
            settingsVisible = it
            textMenuButton = if(it == MenuVisible.MENU_CONTROLS) MENU_BUTTON_TEXT_TRIPS
                             else MENU_BUTTON_TEXT_SETTINGS
        }
        Button(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    top = 10.dp,
                    end = 0.dp,
                    bottom = 0.dp
                ),
            onClick = {
                val menuAction = if(settingsVisible == MenuVisible.MENU_CONTROLS) MenuVisible.MENU_TRIP
                                 else MenuVisible.MENU_CONTROLS
                settingsViewModel.setMenuVisible(menuAction)
                settingsViewModel.setIndexOpenSetting(0)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = settingsViewModel.colorScheme.value!![INDEX_COLOR_BUTTON_BACKGROUND],
                contentColor = settingsViewModel.colorScheme.value!![INDEX_COLOR_BUTTON_TEXT]
            )
        ) {
            Text(textMenuButton)
        }
        Button(
            modifier = Modifier
                .padding(
                    start = 0.dp,
                    top = 10.dp,
                    end = 20.dp,
                    bottom = 0.dp
                ),
            onClick = {
                settingsViewModel.setMenuVisible(MenuVisible.MENU_MAP)
                settingsViewModel.setIndexOpenSetting(0)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = settingsViewModel.colorScheme.value!![INDEX_COLOR_BUTTON_BACKGROUND],
                contentColor = settingsViewModel.colorScheme.value!![INDEX_COLOR_BUTTON_TEXT]
            )
        ) {
            Text("X")
        }
    }
}