package net.ddns.muchserver.speedometercompose.composables

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import kotlin.math.min

@Composable
fun SpeedometerAnalog(modifier: Modifier, speedometerViewModel: SpeedometerViewModel, width: Int, height: Int) {
    val screenWidth = width
    val screenHeight = height
    val sideShorter = min(screenWidth, screenHeight)
    val textSizeLocal = (sideShorter / 12).toFloat()

    val backgroundColors = listOf(Color(0xFF000000), Color(0xFF4B5151))

    val textPaint = Paint().asFrameworkPaint().apply{
        isAntiAlias = true
        color = android.graphics.Color.BLACK
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        textSize = textSizeLocal
    }

    Canvas(
        modifier = modifier,
    ) {
//        drawRect(brush = Brush.verticalGradient(backgroundColors))
        drawCircle(
            color = Color(0xFF526525),
            center = Offset(
                x = size.width / 2,
                y = size.height / 2
            ),
            radius = min(size.width, size.height) / 4
        )

        drawIntoCanvas {
            it.nativeCanvas.drawText(
                "$screenWidth $screenHeight",
                0.0f,
                60.0f,
                textPaint
            )
        }

    }

}