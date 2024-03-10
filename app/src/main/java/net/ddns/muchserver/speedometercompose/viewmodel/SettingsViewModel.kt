package net.ddns.muchserver.speedometercompose.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.ddns.muchserver.speedometercompose.preferences.THEME_DARK
import java.lang.StrictMath.floor

const val HUE_BLUE = 210.0f
const val HUE_BLUE_DARK = 250.0f
const val HUE_CYAN = 190.0f
const val HUE_GREEN = 115.0f
const val HUE_MAGENTA = 295.0f
const val HUE_ORANGE = 30.0f
const val HUE_RED = 10.0f
const val HUE_YELLOW = 60.0f;
class SettingsViewModel: ViewModel() {
    val settingsVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val closeSettings = { settingsVisible.value = false }
    val openSettings = { settingsVisible.value = true }

    val screenAwake: MutableLiveData<Boolean> = MutableLiveData(false)
    val screenOn = { screenAwake.value = true }
    val screenOff = { screenAwake.value = false }

    val indexOpenSetting: MutableLiveData<Int> = MutableLiveData(0)
    val setIndexOpenSetting = {index: Int -> indexOpenSetting.value = index}

    val standardUnits: MutableLiveData<Boolean> = MutableLiveData(true)

    val colorScheme: MutableLiveData<List<Color>> = MutableLiveData(schemeLight(0))
    val indexScheme: MutableLiveData<Int> = MutableLiveData(0)
    val hueCurrent: MutableLiveData<Float> = MutableLiveData(HUE_ORANGE)
    val hueTrip: MutableLiveData<Float> = MutableLiveData(HUE_BLUE)

    val setColorScheme = { theme: String, indexTheme: Int ->
        if(theme == THEME_DARK) {
            colorScheme.value = schemeDark(indexTheme)
            hueCurrent.value = HUE_RED
            hueTrip.value = HUE_CYAN
        }
        else  {
            colorScheme.value = schemeLight(indexTheme)
            hueCurrent.value = HUE_BLUE_DARK
            hueTrip.value = HUE_YELLOW
        }
    }

    val setIndexScheme = { indexTheme: Float ->
        val floor = floor(indexTheme.toDouble()).toInt()
        if(floor != indexScheme.value!!) {
            println("Set index: $floor")
            indexScheme.value = floor
//            setColorScheme(colorScheme.value!!, floor)
        }
    }

    fun schemeDark(index: Int): List<Color> {
        when(index) {
            0 -> {
                return listOf(
                    Color(0xFF020312),
                    Color(0xFF020312),
                    Color(0xFF020312),
                    Color(0xFFB3081F),
                    Color(0xFFFCFBF8),
                )
            }
            1 -> {
                return listOf(
                    Color(0xFF1C1E1B),
                    Color(0xFF1C1E1B),
                    Color(0xFF585D54),
                    Color(0xFF32519B),
                    Color(0xFFEDDCA6),
                )
            }
            2 -> {
                return listOf(
                    Color(0xFF000000),
                    Color(0xFF0F1A38),
                    Color(0xFF0F1A38),
                    Color(0xFF5A6486),
                    Color(0xFFFFFFFF),
                )
            }
            3 -> {
                return listOf(
                    Color(0xFF000000),
                    Color(0xFF000000),
                    Color(0xFF4F4F4F),
                    Color(0xFFFFD700),
                    Color(0xFF9E9C9E),
                )
            }
            else -> {
                return listOf(
                    Color(0xFF000000),
                    Color(0xFF000000),
                    Color(0xFFB8B6B5),
                    Color(0xFF8BC819),
                    Color(0xFFFCFCFC),
                )
            }
        }
    }

    fun schemeLight(index: Int): List<Color> {
        when(index) {
            0 -> {
                return listOf(
                    Color(0xFFE9F0F2),
                    Color(0xFFFFFFFF),
                    Color(0xFFFFFFFF),
                    Color(0xFF325A73),
                    Color(0xFF1C1C1C),
                )
            }
            1 -> {
                return listOf(
                    Color(0xFF41ABFF),
                    Color(0xFFFFFFFF),
                    Color(0xFFFFFFFF),
                    Color(0xFF006CFF),
                    Color(0xFF001835),
                )
            }
            2 -> {
                return listOf(
                    Color(0xFFFFFCF3),
                    Color(0xFFFFFCF3),
                    Color(0xFFEEE4AD),
                    Color(0xFF879070),
                    Color(0xFF2E2E2C),
                )
            }
            3 -> {
                return listOf(
                    Color(0xFFDDFFDD),
                    Color(0xFFEEEEEE),
                    Color(0xFFEEEEEE),
                    Color(0xFF2F7F46),
                    Color(0xFF16210F),
                )
            }
            else -> {
                return listOf(
                    Color(0xFFFFFFFF),
                    Color(0xFFD9D9D9),
                    Color(0xFFD9D9D9),
                    Color(0xFFF1731C),
                    Color(0xFF585858),
                )
            }
        }
    }
}