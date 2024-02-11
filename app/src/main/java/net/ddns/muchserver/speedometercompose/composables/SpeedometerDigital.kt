package net.ddns.muchserver.speedometercompose.composables

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import java.text.DecimalFormat



@Composable
fun SpeedometerDigital(modifier: Modifier, speedometerViewModel: SpeedometerViewModel) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val orientation = if(screenWidth > screenHeight) "Landscape" else "Portrait"
    val sideShorter = kotlin.math.min(screenWidth, screenHeight)
    val textSizeLocal = (sideShorter / 12).toFloat()
    val speed: Float by speedometerViewModel.speed.observeAsState(0.0f)
    val speedMax: Float by speedometerViewModel.speedMax.observeAsState(0.0f)

    val textPaint = Paint().asFrameworkPaint().apply{
        isAntiAlias = true
        color = android.graphics.Color.BLACK
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        textSize = textSizeLocal
    }

    Canvas(
        modifier = modifier,
//        onDraw = {
//            drawIntoCanvas {
//                it.nativeCanvas.drawText(
//                    "$orientation Width: $screenWidth Height: $screenHeight",
//                    (screenWidth / 3).toFloat(),
//                    (screenHeight / 10).toFloat(),
//                    textPaint
//                )
//
//                it.nativeCanvas.drawText(
//                    "Speed: ${DECIMAL_FORMAT.format(speed)}",
//                    (screenWidth / 2).toFloat(),
//                    (screenHeight / 2).toFloat(),
//                    textPaint
//                )
//
//                it.nativeCanvas.drawText(
//                    "Max Speed: ${DECIMAL_FORMAT.format(speedMax)}",
//                    (screenWidth / 2).toFloat(),
//                    (2 * screenHeight / 3).toFloat(),
//                    textPaint
//                )
//            }
//        }
    ) {

    }
}
