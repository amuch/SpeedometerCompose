package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel

@Composable
fun MainScreen(speedometerViewModel: SpeedometerViewModel) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SpeedometerDigital(speedometerViewModel)
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
        ) {
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