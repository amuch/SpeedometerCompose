package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.viewmodel.PreferenceViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel

@Composable
fun LayoutPortrait(activity: MainActivity, speedometerViewModel: SpeedometerViewModel, preferenceViewModel: PreferenceViewModel) {
    val speed: Float by speedometerViewModel.speed.observeAsState(0.0f)
    val speedMax: Float by speedometerViewModel.speedMax.observeAsState(0.0f)

    val modifierText = Modifier
        .padding(10.dp)
        .fillMaxWidth()
    val fontSizeText = 20.sp

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    val backgroundColors = listOf(Color(0xFF000000), Color(0xFF4B5151))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(backgroundColors))
    ) {
        Text(
            "Speed: ${DECIMAL_FORMAT.format(speed)} MPH",
            modifier = modifierText.then(Modifier.fillMaxHeight(0.08f)),
            fontSize = fontSizeText
        )
        Text(
            "Max Speed: ${DECIMAL_FORMAT.format(speedMax)} MPH",
            modifier = modifierText.then(Modifier.fillMaxHeight(0.0736f)),
            fontSize = fontSizeText
        )
        ButtonSetting(
            preferencesViewModel = preferenceViewModel,
            activity = activity,
            modifier = Modifier.align(Alignment.CenterHorizontally))
        SpeedometerAnalog(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.56f),
            speedometerViewModel = speedometerViewModel,
            width = screenWidth,
            height = screenHeight
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()

        ) {
            ButtonResetMaxSpeed(
                modifier = Modifier
                    .padding(5.dp),
                speedometerViewModel = speedometerViewModel
            )
            ButtonToggleLocation(
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(5.dp),
                speedometerViewModel = speedometerViewModel
            )
            ButtonToggleService(
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(5.dp)
            )
        }
    }
}
