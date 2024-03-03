package net.ddns.muchserver.speedometercompose.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.ddns.muchserver.speedometercompose.repository.THEME_DARK

class SettingsViewModel: ViewModel() {

    val settingsVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val closeSettings = { settingsVisible.value = false }
    val openSettings = { settingsVisible.value = true }

    val screenAwake: MutableLiveData<Boolean> = MutableLiveData(false)
    val screenOn = { screenAwake.value = true }
    val screenOff = { screenAwake.value = false }

    val indexOpenSetting: MutableLiveData<Int> = MutableLiveData(0)
    val setIndexOpenSetting = {index: Int -> indexOpenSetting.value = index}


    val darkTheme = listOf(
        Color(0xFF000000),
        Color(0xFF000000),
        Color(0xFFB8B6B5),
        Color(0xFF8BC819),
        Color(0xFFFCFCFC),
    )
    val lightTheme = listOf(
        Color(0xFFFFFFFF),
        Color(0xFFD9D9D9),
        Color(0xFFD9D9D9),
        Color(0xFFF1731C),
        Color(0xFF585858),
    )

    val colorScheme: MutableLiveData<List<Color>> = MutableLiveData(lightTheme)
    val setColorScheme = { theme: String, indexTheme: Int ->
        if(theme == THEME_DARK) {
            colorScheme.value = darkTheme
        }
        else  {
            colorScheme.value = lightTheme
        }
    }
}