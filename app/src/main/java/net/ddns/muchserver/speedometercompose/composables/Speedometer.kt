package net.ddns.muchserver.speedometercompose.composables

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.viewmodel.MenuVisible
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel

@Composable
fun Speedometer(
    modifier: Modifier,
    gaugeOption: GaugeOptions,
    onGaugeOptionChange: (GaugeOptions) -> Unit,
    speedometerViewModel: SpeedometerViewModel,
    settingsViewModel: SettingsViewModel
) {
    val colorScheme: List<Color> by settingsViewModel.colorScheme.observeAsState(settingsViewModel.schemeLight(0))
    Box(
        modifier = modifier
    ) {
        SpeedometerGauge(
            modifier = Modifier.fillMaxSize(),
            speedometerViewModel = speedometerViewModel,
            settingsViewModel = settingsViewModel
        )
//        Column(
//            modifier = Modifier
//                .fillMaxHeight()
//                .fillMaxWidth(0.25f)
//                .align(Alignment.TopEnd)
//                .padding(10.dp)
//                .background(color = Color.Transparent)
//        ) {
//            var dropDownVisible by remember { mutableStateOf(false) }
//            Button(
//                onClick = {
//                    dropDownVisible = !dropDownVisible
//                },
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
//                    contentColor = colorScheme[INDEX_COLOR_BUTTON_TEXT]
//                )
//            ) {
//                Text(text = if(dropDownVisible) "Close" else "Menu")
//            }
//            AnimatedVisibility(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(
//                        brush = Brush.verticalGradient(
//                            colors = colorScheme.subList(
//                                INDEX_COLOR_PRIMARY,
//                                INDEX_COLOR_TERTIARY + 1
//                            )
//                        )
//                    ),
//                enter = fadeIn(animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_IN)),
//                exit = fadeOut(animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_OUT)),
//                visible = dropDownVisible
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(color = Color.Transparent),
//                    verticalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Button(
//                        onClick = {
//                            println("Map")
//                            onGaugeOptionChange(GaugeOptions.GAUGE_MAP)
//                        }
//                    ) {
//                        Text("Map")
//                    }
//                    Button(
//                        onClick = {
//                            println("Trip")
//                            onGaugeOptionChange(GaugeOptions.GAUGE_TRIP)
//                        }
//                    ) {
//                        Text("Trip")
//                    }
//                    Button(
//                        onClick = {
//                            println("Controls")
//                            onGaugeOptionChange(GaugeOptions.GAUGE_CONTROLS)
//                        }
//                    ) {
//                        Text("Controls")
//                    }
//                    Button(
//                        onClick = {
//                            println("Prefs")
//                            onGaugeOptionChange(GaugeOptions.GAUGE_PREFERENCES)
//                        }
//                    ) {
//                        Text("Prefs")
//                    }
//                }
//            }
//        }
    }
}