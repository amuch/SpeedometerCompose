package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.repository.THEME_DARK
import net.ddns.muchserver.speedometercompose.repository.THEME_LIGHT
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel

@Composable
fun ButtonSetting(
    modifier: Modifier,
    preferencesViewModel: PreferencesViewModel,
    colorList: List<Color>
) {
    val colorGauge = colorList[INDEX_COLOR_BUTTON_BACKGROUND]
    val colorText = colorList[INDEX_COLOR_BUTTON_TEXT]

    Button(
        onClick = {
            if(preferencesViewModel.readFromDataStore.value!! == THEME_LIGHT) {
                preferencesViewModel.saveToDataStore(THEME_DARK)
            }
            else {
                preferencesViewModel.saveToDataStore(THEME_LIGHT)
            }
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            Color.Transparent
        ),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .background(color = colorGauge)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = preferencesViewModel.readFromDataStore.value!!,
                color = colorText
            )
        }
    }
}