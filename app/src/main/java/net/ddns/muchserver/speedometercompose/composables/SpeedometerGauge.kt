package net.ddns.muchserver.speedometercompose.composables

import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import net.ddns.muchserver.speedometercompose.viewmodel.CONVERSION_METERS_TO_FEET
import net.ddns.muchserver.speedometercompose.viewmodel.CONVERSION_METERS_TO_KM
import net.ddns.muchserver.speedometercompose.viewmodel.CONVERSION_METERS_TO_MILES
import net.ddns.muchserver.speedometercompose.viewmodel.CONVERSION_MPS_TO_KPH
import net.ddns.muchserver.speedometercompose.viewmodel.CONVERSION_MPS_TO_MPH
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import java.text.DecimalFormat
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


val DECIMAL_FORMAT = DecimalFormat("#.##")
val SCALE_FORMAT = DecimalFormat("#")
val COORDINATE_FORMAT = DecimalFormat("#.####")
@Composable
fun SpeedometerGauge(
    modifier: Modifier,
    speedometerViewModel: SpeedometerViewModel,
    settingsViewModel: SettingsViewModel
) {
    val speed: Float by speedometerViewModel.speed.observeAsState(0.0f)
    val speedMax: Float by speedometerViewModel.speedMax.observeAsState(0.0f)
    val latitude: Double by speedometerViewModel.latitude.observeAsState(0.0)
    val longitude: Double by speedometerViewModel.longitude.observeAsState(0.0)
    val altitude: Double by speedometerViewModel.altitude.observeAsState(0.0)
    val bearing: Float by speedometerViewModel.bearing.observeAsState(0.0f)
    val distance: Double by speedometerViewModel.distance.observeAsState(0.0)

    val colorScheme: List<Color> by settingsViewModel.colorScheme.observeAsState(settingsViewModel.schemeLight(0))
    val colorGauge = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND]
    val colorText = colorScheme[INDEX_COLOR_BUTTON_TEXT]
    val brushBackground = Brush.verticalGradient(
        colors = colorScheme.subList(INDEX_COLOR_PRIMARY, INDEX_COLOR_TERTIARY + 1)
    )

    val standardUnits: Boolean by settingsViewModel.standardUnits.observeAsState(true)
    val scaleMax = scaleMax(speedMax, standardUnits)
    val angleRotation = (speedScaled(speed, standardUnits) / scaleMax) * 180 - 90

    val modifierBorder = Modifier.border(2.dp, colorGauge, shape = RoundedCornerShape(10.dp))
    Canvas(
        modifier = modifier.then(modifierBorder),
    ) {
        val sideShorter = min(size.width, size.height)
        val textSizeLocal = (sideShorter / 16)
        val textSizeCoordinate = (sideShorter / 20)
        val textPaint = Paint().asFrameworkPaint().apply{
            isAntiAlias = true
            color = colorText.toArgb()
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
            textSize = textSizeLocal
        }
        val textPaintCoordinate = Paint().asFrameworkPaint().apply{
            isAntiAlias = true
            color = colorText.toArgb()
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
            textSize = textSizeCoordinate
        }

        val brushNeedle = Brush.horizontalGradient(
            colors = listOf(
                colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
//                colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
                colorScheme[INDEX_COLOR_BUTTON_TEXT],
//                colorScheme[INDEX_COLOR_TERTIARY],
//                colorScheme[INDEX_COLOR_BUTTON_BACKGROUND]
            )
        )
        drawRoundRect(
            brush = brushBackground,
            cornerRadius = CornerRadius(
                x = 10.dp.toPx(),
                y = 10.dp.toPx()
            )
        )


        val segment = sideShorter / 30
        val pathNeedle = Path().apply {
            moveTo(size.width / 2 - segment, size.height / 2) // left
            lineTo(size.width / 2, 0.28f * size.height) // top
            lineTo(size.width / 2 + segment, size.height / 2) // right
            lineTo(size.width / 2, size.height / 2 + 3 * segment) // bottom
            lineTo(size.width / 2 - segment, size.height / 2) // left
            close()
        }
        rotate(angleRotation) {
            drawPath(
                path = pathNeedle,
//                brush = brushNeedle,
                color = colorGauge
            )
        }

        val radius = min(size.width, size.height) / 4
        val widthArc = 12.0f
        drawArc(
            color = colorText,
            startAngle = -180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(
                x = center.x  - radius,
                y = center.y - radius
            ),
            size = Size(
                width = 2 * radius,
                height = 2 * radius
            ),
            style = Stroke(
                width = widthArc,
                cap = StrokeCap.Round
            )
        )
        val denominator = 10
        var step = 1
        if(scaleMax > 10) {
            step = 5
        }
        if(scaleMax > 50) {
          step = 10
        }

        for(i in 0 .. scaleMax.toInt() step step) {
            val theta = i * Math.PI / scaleMax
            val xStart = (center.x - (denominator - 1) * radius / denominator * cos(theta)).toFloat()
            val xEnd = (center.x - (denominator + 1) * radius / denominator * cos(theta)).toFloat()
            val yStart = (center.y - (denominator - 1) * radius / denominator * sin(theta)).toFloat()
            val yEnd = (center.y - (denominator + 1) * radius / denominator * sin(theta)).toFloat()
            drawLine(
                color = colorText,
                start = Offset(
                    x = xStart,
                    y = yStart
                ),
                end = Offset(
                    x = xEnd,
                    y = yEnd
                ),
                strokeWidth = widthArc / 2
            )
        }
        drawArc(
            color = colorGauge,
            startAngle = -180f,
            sweepAngle = speedScaled(speedMax, standardUnits) / scaleMax * 180f,
            useCenter = false,
            topLeft = Offset(
                x = size.width / 2  - radius,
                y = size.height / 2 - radius
            ),
            size = Size(
                width = 2 * radius,
                height = 2 * radius
            ),
            style = Stroke(
                width = widthArc / 2,
                cap = StrokeCap.Round
            )
        )
        drawIntoCanvas {
            it.nativeCanvas.drawText(
                formatLatitude(latitude),
                segment,
                3 * textSizeCoordinate / 2,
                textPaintCoordinate
            )
            it.nativeCanvas.drawText(
                formatLongitude(longitude),
                center.x + segment,
                3 * textSizeCoordinate / 2,
                textPaintCoordinate
            )
            it.nativeCanvas.drawText(
                formatSpeed(speed, standardUnits),
                size.width / 2 - 2 * segment,
                size.height / 2 + 2 * textSizeLocal + segment,
                textPaint
            )
            it.nativeCanvas.drawText(
                "Max: ${formatSpeed(speedMax, standardUnits)}",
                size.width / 3,
                size.height / 2 + 3 * textSizeLocal + 2 * segment,
                textPaint
            )

            it.nativeCanvas.drawText(
                "0",
                size.width / 4 - 3 * segment / 2,
                size.height / 2 + segment,
                textPaint
            )

//            it.nativeCanvas.drawText(
//                SCALE_FORMAT.format(scaleMax / 4),
//                size.width / 2 - 0.7f * radius - 2 * segment,
//                size.height / 2 - 0.7f * radius,
//                textPaint
//            )

            it.nativeCanvas.drawText(
                SCALE_FORMAT.format(scaleMax / 2),
                size.width / 2 - segment,
                size.height / 4 - segment,
                textPaint
            )

//            it.nativeCanvas.drawText(
//                SCALE_FORMAT.format(3 * scaleMax / 4),
//                size.width / 2 + 0.7f * radius + 3 * segment / 2,
//                size.height / 2 - 0.7f * radius,
//                textPaint
//            )

            it.nativeCanvas.drawText(
                SCALE_FORMAT.format(scaleMax),
                3 * size.width / 4 + segment / 2,
                size.height / 2 + segment,
                textPaint
            )

            it.nativeCanvas.drawText(
                formatDistance(distance, standardUnits),
                segment,
                size.height - textSizeCoordinate,
                textPaintCoordinate
            )

            it.nativeCanvas.drawText(
                calculateDirection(bearing),
                center.x,
                size.height - textSizeCoordinate,
                textPaintCoordinate
            )

            it.nativeCanvas.drawText(
                formatAltitude(altitude, standardUnits),
                2 * size.width / 3,
                size.height - textSizeCoordinate,
                textPaintCoordinate
            )
        }
    }
}

fun formatAltitude(altitude: Double, standardUnits: Boolean): String {
    if(standardUnits) {
        return "${DECIMAL_FORMAT.format(altitude * CONVERSION_METERS_TO_FEET)} ft"
    }

    return "${DECIMAL_FORMAT.format(altitude)} m"
}

fun formatSpeed(speed: Float, standardUnits: Boolean): String {
    if(standardUnits) {
        return "${DECIMAL_FORMAT.format(speed * CONVERSION_MPS_TO_MPH)} MPH"
    }

    return "${DECIMAL_FORMAT.format(speed * CONVERSION_MPS_TO_KPH)} KPH"
}

fun formatDistance(distance: Double, standardUnits: Boolean): String {
    if(standardUnits) {
        return "${DECIMAL_FORMAT.format(distance * CONVERSION_METERS_TO_MILES)} mi"
    }

    return "${DECIMAL_FORMAT.format(distance * CONVERSION_METERS_TO_KM)} km"
}

fun speedScaled(speed: Float, standardUnits: Boolean): Float {
    if(standardUnits) {
        return speed * CONVERSION_MPS_TO_MPH
    }
    return speed * CONVERSION_MPS_TO_KPH
}

fun scaleMax(speed: Float, standardUnits: Boolean): Float {
    val scaled = speedScaled(speed, standardUnits)
    if(scaled > 100) {
        return 250f
    }

    if(scaled > 60) {
        return 100f
    }

    if(scaled > 30) {
        return 60f
    }

    if(scaled > 10) {
        return 30f
    }

    if(scaled > 4) {
        return 10f
    }

    return 4f
}

fun formatLatitude(latitude: Double): String {
    if(latitude < 0) {
        return "${COORDINATE_FORMAT.format(-1 * latitude)} S"
    }

    return "${COORDINATE_FORMAT.format(latitude)} N"
}

fun formatLongitude(longitude: Double): String {
    if(longitude < 0) {
        return "${COORDINATE_FORMAT.format(-1 * longitude)} W"
    }
    return "${COORDINATE_FORMAT.format(longitude)} E"
}

fun calculateDirection(bearing: Float): String {
    if(bearing > 315) {
        return "NW"
    }
    if(bearing > 270) {
        return "W"
    }
    if(bearing > 225) {
        return "SW"
    }
    if(bearing > 180) {
        return "S"
    }
    if(bearing > 135) {
        return "SE"
    }
    if(bearing > 90) {
        return "E"
    }
    if(bearing > 45) {
        return "NE"
    }

    return "N"
}