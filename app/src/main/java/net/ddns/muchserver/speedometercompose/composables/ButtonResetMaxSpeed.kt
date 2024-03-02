package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.repository.THEME_LIGHT
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel

@Composable
fun ButtonResetMaxSpeed(
    modifier: Modifier,
    speedometerViewModel: SpeedometerViewModel,
    colorList: List<Color>
) {
    val colorGauge = colorList[INDEX_COLOR_BUTTON_BACKGROUND]
    val colorText = colorList[INDEX_COLOR_BUTTON_TEXT]
    Button(
        onClick = {
            speedometerViewModel.resetSpeedMax()
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorGauge,
            contentColor = colorText
        ),
    ) {
        Text("Reset Max")
    }
}