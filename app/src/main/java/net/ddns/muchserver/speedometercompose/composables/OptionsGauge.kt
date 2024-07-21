package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.GaugeOptions
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.TripViewModel

@Composable
fun OptionsGauge (
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
        modifier = modifier
    ) {
        val modifierGauge = Modifier
            .fillMaxWidth()
            .fillMaxHeight(gaugeProportion)
            .padding(10.dp)
            .border(
                2.dp,
                colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
                shape = RoundedCornerShape(10.dp)
            )
        when(gaugeOption) {
            GaugeOptions.GAUGE_MAP -> {
                MapGauge(modifierGauge, activity, speedometerViewModel, settingsViewModel)
            }
            GaugeOptions.GAUGE_TRIP -> {
                TripGauge(modifierGauge, tripViewModel, speedometerViewModel, settingsViewModel)
            }
            GaugeOptions.GAUGE_CONTROLS -> {
                ControlsGauge(modifierGauge, activity, speedometerViewModel, settingsViewModel)
            }
            GaugeOptions.GAUGE_PREFERENCES -> {
                PreferencesGauge(modifierGauge, activity, preferencesViewModel, settingsViewModel)
            }
        }
    }
    OptionsMenu(modifier, onGaugeOptionChange, activity, settingsViewModel)
}


