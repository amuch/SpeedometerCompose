package net.ddns.muchserver.speedometercompose.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel: ViewModel() {
    companion object {

    }
    val settingsVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val closeSettings = { settingsVisible.value = false }
    val openSettings = { settingsVisible.value = true }

    val screenAwake: MutableLiveData<Boolean> = MutableLiveData(false)
    val screenOn = { screenAwake.value = true }
    val screenOff = { screenAwake.value = false }
}