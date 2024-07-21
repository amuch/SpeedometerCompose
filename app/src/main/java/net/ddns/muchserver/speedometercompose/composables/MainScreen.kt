package net.ddns.muchserver.speedometercompose.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.TripViewModel
import net.ddns.muchserver.speedometercompose.GaugeOptions


const val INDEX_COLOR_PRIMARY = 0
const val INDEX_COLOR_SECONDARY = 1
const val INDEX_COLOR_TERTIARY = 2
const val INDEX_COLOR_BUTTON_BACKGROUND = 3
const val INDEX_COLOR_BUTTON_TEXT = 4

@Composable
fun MainScreen(
    activity: MainActivity,
    tripViewModel: TripViewModel,
    speedometerViewModel: SpeedometerViewModel,
    preferencesViewModel: PreferencesViewModel,
    settingsViewModel: SettingsViewModel,
) {
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    val colorScheme: List<Color> by settingsViewModel.colorScheme.observeAsState(settingsViewModel.schemeLight(0))
    val brushBackground = Brush.verticalGradient(
        colors = colorScheme.subList(INDEX_COLOR_PRIMARY, INDEX_COLOR_TERTIARY + 1)
    )

    var gaugeOption by remember { mutableStateOf(GaugeOptions.GAUGE_MAP) }
    settingsViewModel.optionGaugeVisible.observe(activity) {
        gaugeOption = it
    }
    val onGaugeOptionChange = { option: GaugeOptions ->
        settingsViewModel.setOptionGaugeVisible(option)
    }

    val modifierSpeedometerGauge =
        if(Configuration.ORIENTATION_PORTRAIT == orientation)
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        else Modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight()
    val modifierMapTab =
        if(Configuration.ORIENTATION_PORTRAIT == orientation)
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        else Modifier
            .fillMaxWidth(0.5f)
            .fillMaxHeight()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = brushBackground)
    ) {
        SpeedometerGauge(
            modifier = modifierSpeedometerGauge.then(
                Modifier
                    .align(
                        if(Configuration.ORIENTATION_PORTRAIT == orientation) Alignment.TopCenter
                        else Alignment.CenterStart
                    )
                    .padding(10.dp)
                    .background(color = Color.Transparent)
            ),
            speedometerViewModel = speedometerViewModel,
            settingsViewModel = settingsViewModel
        )
        OptionsGauge (
            modifier = modifierMapTab.then(
                Modifier
                    .align(
                        if(Configuration.ORIENTATION_PORTRAIT == orientation) Alignment.BottomCenter
                        else Alignment.CenterEnd
                    )
                    .background(color = Color.Transparent)
            ),
            gaugeOption = gaugeOption,
            onGaugeOptionChange = onGaugeOptionChange,
            activity = activity,
            tripViewModel = tripViewModel,
            speedometerViewModel = speedometerViewModel,
            preferencesViewModel = preferencesViewModel,
            settingsViewModel = settingsViewModel
        )
    }
}