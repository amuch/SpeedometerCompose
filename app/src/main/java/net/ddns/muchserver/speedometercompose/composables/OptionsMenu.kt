package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import net.ddns.muchserver.speedometercompose.viewmodel.TripViewModel

@Composable
fun OptionsMenu(
    modifier: Modifier,
    gaugeOption: GaugeOptions,
    onGaugeOptionChange: (GaugeOptions) -> Unit,
    activity: MainActivity,
    tripViewModel: TripViewModel,
    speedometerViewModel: SpeedometerViewModel,
    preferencesViewModel: PreferencesViewModel,
    settingsViewModel: SettingsViewModel
) {
    var selectorVisible by remember { mutableStateOf(false) }
    var gaugeProportion by remember { mutableStateOf(1.0f) }
    settingsViewModel.optionGaugeSelectorVisible.observe(activity) {
        selectorVisible = it
        gaugeProportion = if(it) 0.80f else 1.0f
    }
    val colorScheme: List<Color> by settingsViewModel.colorScheme.observeAsState(settingsViewModel.schemeLight(0))
    Box(
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(1.0f - gaugeProportion)
        ),
        Alignment.BottomCenter
    ) {

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    color = Color.Transparent
                ),
            enter = slideInVertically(
                animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_IN),
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(
                animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_OUT),
                targetOffsetY = { it }
            ),
            visible = !selectorVisible
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .background(
                        color = Color.Transparent
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Bottom
            ) {
                Button(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        settingsViewModel.setOptionGaugeSelectorVisible(true)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
                        contentColor = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                    ),
//                    border = BorderStroke(0.dp, colorScheme[INDEX_COLOR_BUTTON_BACKGROUND]),
                    elevation = null,
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open",
                        tint = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                    )
                }
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    color = Color.Transparent
                ),
            enter = slideInVertically(
                animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_IN),
                initialOffsetY= { it }
            ),
            exit = slideOutVertically(
                animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_OUT),
                targetOffsetY = { it }
            ),
            visible = selectorVisible
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.Transparent
                    ),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        settingsViewModel.setOptionGaugeSelectorVisible(false)
                        onGaugeOptionChange(GaugeOptions.GAUGE_MAP)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
                        contentColor = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                    )
                ) {
                    Icon(
                        imageVector = Icons.Sharp.Close,
                        contentDescription = "Close",
                        tint = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                    )
                }
                Button(
                    onClick = {
                        onGaugeOptionChange(GaugeOptions.GAUGE_MAP)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
                        contentColor = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Map",
                        tint = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                    )
                }
                Button(
                    onClick = {
                        onGaugeOptionChange(GaugeOptions.GAUGE_TRIP)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
                        contentColor = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Trip",
                        tint = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                    )
                }
                Button(
                    onClick = {
                        onGaugeOptionChange(GaugeOptions.GAUGE_CONTROLS)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
                        contentColor = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Controls",
                        tint = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                    )
                }
                Button(
                    onClick = {
                        onGaugeOptionChange(GaugeOptions.GAUGE_PREFERENCES)
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
                        contentColor = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Preferences",
                        tint = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                    )
                }
            }
        }
    }
}