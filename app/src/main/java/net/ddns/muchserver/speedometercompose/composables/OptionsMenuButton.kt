package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel

@Composable
fun OptionsMenuButton(
    modifier: Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String,
    settingsViewModel: SettingsViewModel
) {
    val colorScheme: List<Color> by settingsViewModel.colorScheme.observeAsState(settingsViewModel.schemeLight(0))

    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
            contentColor = colorScheme[INDEX_COLOR_BUTTON_TEXT]
        ),
        elevation = null,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription= contentDescription,
            tint = colorScheme[INDEX_COLOR_BUTTON_TEXT]
        )
    }
}