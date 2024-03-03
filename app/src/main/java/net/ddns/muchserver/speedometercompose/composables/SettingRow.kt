package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel

const val INDEX_SETTING_ACTIVE = 0
const val INDEX_SETTING_INACTIVE = 1
const val INDEX_SETTING_EXPLANATION = 2

@Composable
fun SettingRow(
    modifier: Modifier,
    settingsViewModel: SettingsViewModel,
    isSet: Boolean,
    setCheckedOn: () -> Unit,
    setCheckedOff: () -> Unit,
    messages: Array<String>,
    index: Int
) {
    val colorScheme: List<Color> by settingsViewModel.colorScheme.observeAsState(settingsViewModel.lightTheme)
    val indexOpen: Int by settingsViewModel.indexOpenSetting.observeAsState(0)

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                modifier = Modifier
                    .padding(10.dp),
                checked = isSet,
                onCheckedChange = {
                    if(isSet) setCheckedOff() else setCheckedOn()
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
                    checkedTrackColor = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
                    checkedTrackAlpha = 0.8f,
                    uncheckedThumbColor = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
                    uncheckedTrackColor = colorScheme[INDEX_COLOR_PRIMARY],
                    uncheckedTrackAlpha = 0.8f
                )
            )
            Text(
                modifier = Modifier
                    .padding(10.dp),
                text = if(isSet) messages[INDEX_SETTING_ACTIVE] else messages[INDEX_SETTING_INACTIVE],
                color = colorScheme[INDEX_COLOR_BUTTON_TEXT]
            )
            Button(
                modifier = Modifier
                    .padding(10.dp),
                onClick = {
                    if(indexOpen == index) {
                        settingsViewModel.setIndexOpenSetting(0)
                    }
                    else {
                        settingsViewModel.setIndexOpenSetting(index)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
                    contentColor = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                ),
            ) {
                Text(
                    text= "?",
                    color = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                )
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Transparent
                ),
            enter = expandVertically(animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_IN)),
            exit = shrinkVertically(animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_OUT)),
            visible = indexOpen == index
        ) {
            Text(
                modifier = Modifier
                    .padding(15.dp),
                text = messages[INDEX_SETTING_EXPLANATION],
                color = colorScheme[INDEX_COLOR_BUTTON_TEXT]
            )
        }
    }
}