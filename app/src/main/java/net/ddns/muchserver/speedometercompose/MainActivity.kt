package net.ddns.muchserver.speedometercompose

import android.app.NotificationManager
import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.view.Display
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import net.ddns.muchserver.speedometercompose.composables.DECIMAL_FORMAT
import net.ddns.muchserver.speedometercompose.composables.MainScreen
import net.ddns.muchserver.speedometercompose.repository.THEME_LIGHT
import net.ddns.muchserver.speedometercompose.service.CHANNEL_SPEEDOMETER
import net.ddns.muchserver.speedometercompose.service.ID_FOREGROUND_SERVICE
import net.ddns.muchserver.speedometercompose.service.TEXT_FOREGROUND_SERVICE

import net.ddns.muchserver.speedometercompose.ui.theme.SpeedometerComposeTheme
import net.ddns.muchserver.speedometercompose.viewmodel.PreferenceViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var speedometerViewModel: SpeedometerViewModel
    private lateinit var speedometerViewModelFactory: SpeedometerViewModelFactory

    private lateinit var preferenceViewModel: PreferenceViewModel
    private lateinit var theme: String

    companion object {
        var permissionsGranted = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        speedometerViewModelFactory = SpeedometerViewModelFactory(this)
        speedometerViewModel = ViewModelProvider(this, speedometerViewModelFactory)[SpeedometerViewModel::class.java]

        theme = THEME_LIGHT
        preferenceViewModel = ViewModelProvider(this)[PreferenceViewModel::class.java]
        preferenceViewModel.readFromDataStore.observe(this) { themeSet ->
            theme = themeSet
        }

        val activity = this
//        hideSystemUI()

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
                    MainScreen(activity, speedometerViewModel, preferenceViewModel)
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