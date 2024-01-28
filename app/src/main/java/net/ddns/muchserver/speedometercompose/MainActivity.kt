package net.ddns.muchserver.speedometercompose

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.*
import net.ddns.muchserver.speedometercompose.service.ServiceForeground

import net.ddns.muchserver.speedometercompose.ui.theme.SpeedometerComposeTheme
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel

class MainActivity : ComponentActivity() {
    private lateinit var speedometerViewModel: SpeedometerViewModel
    private val coroutineScopeMain = CoroutineScope(Dispatchers.Main)

    companion object {
        var permissionsGranted = false
        var speed: Float = 0.0f
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        speedometerViewModel = SpeedometerViewModel(this)
        hideSystemUI()

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
                    var speedLocal by remember { mutableStateOf(speed) }
                    TopLevel(speedLocal, speedometerViewModel)
                }
            }
        }
    }

    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted ->
        if(isGranted) {
            println("permission granted")
            permissionsGranted = true
        }
        else {
            Toast.makeText(applicationContext, "Needs Location", Toast.LENGTH_LONG).show()
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

@Composable
fun TopLevel(speed: Float, speedometerViewModel: SpeedometerViewModel) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        SpeedometerDigital(speedometerViewModel)
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
        ) {
            ToggleLocationUpdates(
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(5.dp),
                speedometerViewModel = speedometerViewModel
            )
            ToggleService(
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(5.dp)
            )
        }
    }
}

@Composable
fun ToggleLocationUpdates(modifier: Modifier, speedometerViewModel: SpeedometerViewModel) {
    val requestingUpdates = SpeedometerViewModel.requestingUpdates
    Button(
        onClick = {
            if(requestingUpdates) {
                speedometerViewModel.stopLocationUpdates()
            }
            else {
                if(MainActivity.permissionsGranted) {
                    speedometerViewModel.requestLocationUpdates()
                }
            }
        },
        modifier = modifier
    ) {
        Text(
            text = if(requestingUpdates) "Stop Updates" else "Start Updates"
        )
    }
}

@Composable
fun ToggleService(modifier: Modifier) {
    val context = LocalContext.current
    val serviceRunning = ServiceForeground.serviceRunning
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
            text = if(serviceRunning) "Stop Service" else "Start Service"
        )
    }
}

@Composable
fun SpeedometerDigital(speedometerViewModel: SpeedometerViewModel) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val sideShorter = kotlin.math.min(screenWidth, screenHeight)
    val textSizeLocal = (sideShorter / 12).toFloat()

    val textPaint = Paint().asFrameworkPaint().apply{
        isAntiAlias = true
        color = android.graphics.Color.BLACK
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        textSize = textSizeLocal
    }

    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            drawIntoCanvas {
                it.nativeCanvas.drawText(
                    "Speed: ${SpeedometerViewModel.speed}",
                    (screenWidth / 2).toFloat(),
                    (screenHeight / 2).toFloat(),
                    textPaint
                )

                it.nativeCanvas.drawText(
                    "Max Speed: ${SpeedometerViewModel.speedMax}",
                    (screenWidth / 2).toFloat(),
                    (2 * screenHeight / 3).toFloat(),
                    textPaint
                )
            }
        })
}

// https://github.com/jurajkusnier/compose-speed-test
@Composable
fun SpeedometerAnalog(viewModel: SpeedometerViewModel) {
    Canvas(modifier = Modifier.fillMaxSize()) {
//        drawLines()


    }
}

fun DrawScope.drawLines(angleSize: Float, lines: Int) {
    val oneRotation = angleSize / lines
    val startingAngle = 90 - angleSize / 2
    val shortLine = size.width / 20
    val longLine = size.width / 10

    for(i in 0..lines) {
        rotate(i * oneRotation + startingAngle) {
            drawLine(
                color = Color.White,
                start = Offset(
                    x = if(i % 5 == 0)
                            longLine
                        else
                            shortLine,
                    y = size.height / 2
                ),
                end = Offset(4f, size.height / 2)
            )
        }
    }
}

fun DrawScope.drawArc(angleSize: Float, progress: Float) {
    val startAngle = 270 - angleSize / 2
    val sweepAngle = angleSize * progress

    drawArc(
        color = Color.White,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = 1f)
    )
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SpeedometerComposeTheme {
        Greeting("Android")
    }
}