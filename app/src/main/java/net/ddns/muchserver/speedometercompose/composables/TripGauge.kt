package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.TripViewModel

@Composable
fun TripGauge(
    modifier: Modifier,
    tripViewModel: TripViewModel,
    speedometerViewModel: SpeedometerViewModel,
    settingsViewModel: SettingsViewModel
) {
    val checkPoints by tripViewModel.checkPoints.observeAsState(listOf())
    val checkPointsCurrent by tripViewModel.checkPointsCurrent.observeAsState(listOf())
    val trips by tripViewModel.trips.observeAsState(listOf())
    val colorScheme: List<Color> by settingsViewModel.colorScheme.observeAsState(settingsViewModel.schemeLight(0))

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.then(
            Modifier
                .fillMaxSize()
                .background(
                    color = Color.Transparent
                )
                .verticalScroll(scrollState)
        ),
    ) {
        ButtonToggleTrip(
            modifier = Modifier
                .padding(10.dp),
            tripViewModel = tripViewModel,
            speedometerViewModel = speedometerViewModel,
            settingsViewModel = settingsViewModel
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Transparent
                )
        ) {
            for(trip in trips) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.Transparent
                        )
                        .padding(10.dp),
                ){
                    Button(
                        onClick = {
                            tripViewModel.findTrip(trip.id)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
                            contentColor = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                        )
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(10.dp),
                            text = "${trip.id}",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Text(
                        text = "${trip.id} ${trip.name}",
                        color = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                    )
                    Button(
                        onClick = {
                            tripViewModel.deleteTrip(trip.id)
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
                            contentColor = colorScheme[INDEX_COLOR_BUTTON_TEXT]
                        )
                    ) {
                        Text("X")
                    }
                }
            }
        }
        Button(
            modifier = Modifier
                .padding(10.dp),
            onClick = {
//                                for(checkPoint in checkPoints) {
//                                    println("${checkPoint.latitude} ${checkPoint.longitude} ${checkPoint.date}")
//                                }
                for(trip in trips) {
                    println(trip.name)
                    for(checkPoint in checkPoints) {
                        if(checkPoint.idTrip == trip.id) {
                            println("\t ${checkPoint.latitude}, ${checkPoint.longitude}")
                        }
                    }
                }
            }
        ) {
            Text("Print Trips")
        }
        Button(
            modifier = Modifier
                .padding(10.dp),
            onClick = {
                println("Size ${checkPointsCurrent.size}")
                for(checkPointCurrent in checkPointsCurrent) {
                    println("${checkPointCurrent.latitude} ${checkPointCurrent.longitude} ${checkPointCurrent.date}")
                }
            }
        ) {
            Text("Print CheckPoints")
        }
    }
}