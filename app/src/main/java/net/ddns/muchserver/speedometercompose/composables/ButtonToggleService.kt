package net.ddns.muchserver.speedometercompose.composables

import android.content.Intent
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import net.ddns.muchserver.speedometercompose.R
import net.ddns.muchserver.speedometercompose.service.ServiceForeground

@Composable
fun ButtonToggleService(modifier: Modifier) {
    val context = LocalContext.current
    val serviceRunning = ServiceForeground.serviceRunning
    val idString = if(serviceRunning) R.string.service_stop else R.string.service_start
    val text = context.resources.getString(idString)
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
        modifier = modifier
    ) {
        Text(
            text = text
        )
    }
}