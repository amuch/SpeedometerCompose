package net.ddns.muchserver.speedometercompose

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import net.ddns.muchserver.speedometercompose.composables.MainScreen
import net.ddns.muchserver.speedometercompose.preferences.THEME_LIGHT

import net.ddns.muchserver.speedometercompose.ui.theme.SpeedometerComposeTheme
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var speedometerViewModel: SpeedometerViewModel
    private lateinit var speedometerViewModelFactory: SpeedometerViewModelFactory

    private lateinit var preferencesViewModel: PreferencesViewModel
    private lateinit var theme: String
    private var indexTheme: Int = 0
    private var standardUnits: Boolean = true

    private lateinit var settingsViewModel: SettingsViewModel
    companion object {
        var permissionsGranted = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeViewModels()
        val activity = this

        activityResultLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            activityResultLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
//        }

        setContent {
            SpeedometerComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(activity, speedometerViewModel, preferencesViewModel, settingsViewModel)
                }
            }
        }
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted ->
        if(isGranted) {
            permissionsGranted = true
        }
        else {
            Toast.makeText(applicationContext, R.string.location_services_denied, Toast.LENGTH_LONG).show()
        }
    }

    private fun initializeViewModels() {
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        settingsViewModel.settingsVisible.observe(this) {}
        settingsViewModel.screenAwake.observe(this) {
            if(it) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
        settingsViewModel.colorScheme.observe(this){}

        theme = THEME_LIGHT
        preferencesViewModel = ViewModelProvider(this)[PreferencesViewModel::class.java]
        preferencesViewModel.readFromDataStore.observe(this) { preferences ->
            theme = preferences.theme
            indexTheme = preferences.indexTheme
            settingsViewModel.setColorScheme(theme, indexTheme)
            standardUnits = preferences.standardUnits
            settingsViewModel.standardUnits.value = standardUnits
        }

        speedometerViewModelFactory = SpeedometerViewModelFactory(this)
        speedometerViewModel = ViewModelProvider(this, speedometerViewModelFactory)[SpeedometerViewModel::class.java]
    }

    private fun hideSystemUI() {
        actionBar?.hide()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        else {
            window.insetsController?.apply {
                hide(WindowInsets.Type.systemBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}