package net.ddns.muchserver.speedometercompose.composables

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.R
import net.ddns.muchserver.speedometercompose.repository.THEME_LIGHT
import net.ddns.muchserver.speedometercompose.service.ServiceForeground
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel

@Composable
fun ButtonToggleService(
    modifier: Modifier,
    colorList: List<Color>
) {
    val context = LocalContext.current
    val serviceRunning = ServiceForeground.serviceRunning
    val idString = if(serviceRunning) R.string.service_stop else R.string.service_start
    val text = context.resources.getString(idString)

    val colorGauge = colorList[INDEX_COLOR_BUTTON_BACKGROUND]
    val colorText = colorList[INDEX_COLOR_BUTTON_TEXT]
    Button(
        onClick = {
            Intent(context, ServiceForeground::class.java).also {
                var action = ServiceForeground.Actions.START.toString()
                if(serviceRunning) {
                    action = ServiceForeground.Actions.STOP.toString()
                }

                it.action = action
                context.startService(it)
            }
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorGauge,
            contentColor = colorText
        ),
    ) {
        Text(
            text = text
        )
    }
}