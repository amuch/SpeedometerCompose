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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.GaugeOptions
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel

const val MILLISECONDS_ANIMATE_IN = 800
const val MILLISECONDS_ANIMATE_OUT = 500
@Composable
fun OptionsMenu(
    modifier: Modifier,
    onGaugeOptionChange: (GaugeOptions) -> Unit,
    activity: MainActivity,
    settingsViewModel: SettingsViewModel
) {
    var selectorVisible by remember { mutableStateOf(false) }
    var gaugeProportion by remember { mutableStateOf(1.0f) }
    settingsViewModel.optionGaugeSelectorVisible.observe(activity) {
        selectorVisible = it
        gaugeProportion = if(it) 0.80f else 1.0f
    }

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
                val modifier = Modifier.padding(10.dp)
                OptionsMenuButton(
                    modifier = modifier,
                    onClick = { settingsViewModel.setOptionGaugeSelectorVisible(true) },
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Open",
                    settingsViewModel = settingsViewModel
                )
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
                val modifier = Modifier
                OptionsMenuButton(
                    modifier = modifier,
                    onClick = {
                        settingsViewModel.setOptionGaugeSelectorVisible(false)
                        onGaugeOptionChange(GaugeOptions.GAUGE_MAP)
                    },
                    imageVector = Icons.Sharp.Close,
                    contentDescription = "Close",
                    settingsViewModel = settingsViewModel
                )

                val options = GaugeOptions.values()
                options.map {
                    val config = getConfig(it)
                    OptionsMenuButton(
                        modifier = config.modifier,
                        onClick = { onGaugeOptionChange(it) },
                        imageVector = config.imageVector,
                        contentDescription = config.contentDescription,
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}

fun getConfig(gaugeOption: GaugeOptions): OptionsMenuButtonConfig {
    val modifier = Modifier
    return when(gaugeOption) {
        GaugeOptions.GAUGE_MAP -> OptionsMenuButtonConfig(modifier, Icons.Default.LocationOn, "Map")
        GaugeOptions.GAUGE_TRIP -> OptionsMenuButtonConfig(modifier, Icons.Default.List, "Trip")
        GaugeOptions.GAUGE_CONTROLS -> OptionsMenuButtonConfig(modifier, Icons.Default.Build, "Controls")
        GaugeOptions.GAUGE_PREFERENCES -> OptionsMenuButtonConfig(modifier, Icons.Default.Settings, "Preferences")
    }
}

data class OptionsMenuButtonConfig(
    val modifier: Modifier,
    val imageVector: ImageVector,
    val contentDescription: String
)