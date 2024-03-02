package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel

const val MILLISECONDS_ANIMATE_IN = 800
const val MILLISECONDS_ANIMATE_OUT = 500

@Composable
fun SettingsTab(
    modifier: Modifier,
    activity: MainActivity,
    speedometerViewModel: SpeedometerViewModel,
    preferencesViewModel: PreferencesViewModel,
    colorList: List<Color>,
    settingsViewModel: SettingsViewModel
) {

    var settingsVisible by remember { mutableStateOf(false) }
    settingsViewModel.settingsVisible.observe(activity) {
        settingsVisible = it
    }

    val brushBackground = Brush.verticalGradient(
        colors = colorList.subList(INDEX_COLOR_PRIMARY, INDEX_COLOR_TERTIARY + 1)
    )

    Box(
        modifier = modifier
    ) {
        Button(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp),
            onClick = {
                settingsViewModel.openSettings()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorList[INDEX_COLOR_BUTTON_BACKGROUND],
                contentColor = colorList[INDEX_COLOR_BUTTON_TEXT]
            )
        ) {
                Text("Menu")
        }

        Text(
            text = "Place Map Here",
            modifier = Modifier.align(Alignment.Center),
            color = colorList[INDEX_COLOR_BUTTON_TEXT]
        )
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(10.dp)
                .fillMaxSize()
                .background(
                    brush = brushBackground
                ),
            enter = fadeIn(animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_IN)),
            exit = fadeOut(animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_OUT)),
            visible = settingsVisible
        ) {
            val scrollState = rememberScrollState()
            ControlsColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.Transparent
                    )
                    .verticalScroll(scrollState),
                activity = activity,
                speedometerViewModel = speedometerViewModel,
                preferencesViewModel = preferencesViewModel,
                colorList = colorList,
                settingsViewModel = settingsViewModel
            )
        }
    }
}