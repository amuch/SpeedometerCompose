package net.ddns.muchserver.speedometercompose.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.viewmodel.MenuVisible
import net.ddns.muchserver.speedometercompose.viewmodel.PreferencesViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SettingsViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.TripViewModel

const val MILLISECONDS_ANIMATE_IN = 800
const val MILLISECONDS_ANIMATE_OUT = 500
const val ZOOM_DEFAULT = 11.0f
@Composable
fun MapTab(
    modifier: Modifier,
    activity: MainActivity,
    tripViewModel: TripViewModel,
    speedometerViewModel: SpeedometerViewModel,
    preferencesViewModel: PreferencesViewModel,
    settingsViewModel: SettingsViewModel
) {
//    val paddingValues: PaddingValues
    var settingsVisible by remember { mutableStateOf(MenuVisible.MENU_MAP) }
    settingsViewModel.menuVisible.observe(activity) {
        settingsVisible = it
    }

    val colorScheme: List<Color> by settingsViewModel.colorScheme.observeAsState(settingsViewModel.schemeLight(0))
    val brushBackground = Brush.verticalGradient(
        colors = colorScheme.subList(INDEX_COLOR_PRIMARY, INDEX_COLOR_TERTIARY + 1)
    )

    val modifierBorder = Modifier
        .padding(10.dp)
        .border(2.dp, colorScheme[INDEX_COLOR_BUTTON_BACKGROUND], shape = RoundedCornerShape(10.dp))
    Box(
        modifier = modifier.then(modifierBorder)
    ) {
        Map(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            activity = activity,
            speedometerViewModel = speedometerViewModel,
            preferencesViewModel = preferencesViewModel,
            settingsViewModel = settingsViewModel,
        )
        Button(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp),
            onClick = {
                settingsViewModel.setMenuVisible(MenuVisible.MENU_CONTROLS)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorScheme[INDEX_COLOR_BUTTON_BACKGROUND],
                contentColor = colorScheme[INDEX_COLOR_BUTTON_TEXT]
            )
        ) {
                Text("Menu")
        }

        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxSize()
                .background(
                    brush = brushBackground
                ),
            enter = fadeIn(animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_IN)),
            exit = fadeOut(animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_OUT)),
            visible = settingsVisible != MenuVisible.MENU_MAP
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.Transparent
                    )
            )
            {
                HeaderSettings(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.18f)
                        .background(
                            color = Color.Transparent
                        ),
                    activity = activity,
                    settingsViewModel = settingsViewModel
                )
                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxSize(),
                    enter = fadeIn(animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_IN)),
                    exit = fadeOut(animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_OUT)),
                    visible = settingsVisible == MenuVisible.MENU_CONTROLS
                ) {
                    val scrollState = rememberScrollState()
                    ControlsColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = Color.Transparent
                            )
                            .verticalScroll(scrollState),
                        activity = activity,
                        tripViewModel = tripViewModel,
                        speedometerViewModel = speedometerViewModel,
                        preferencesViewModel = preferencesViewModel,
                        settingsViewModel = settingsViewModel
                    )
                }
                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxSize(),
                    enter = fadeIn(animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_IN)),
                    exit = fadeOut(animationSpec = tween(durationMillis = MILLISECONDS_ANIMATE_OUT)),
                    visible = settingsVisible == MenuVisible.MENU_TRIP
                ) {
                    val scrollState = rememberScrollState()
                    val checkPoints by tripViewModel.checkPoints.observeAsState(listOf())
                    val checkPointsCurrent by tripViewModel.checkPointsCurrent.observeAsState(listOf())
                    val trips by tripViewModel.trips.observeAsState(listOf())
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = Color.Transparent
                            )
                            .verticalScroll(scrollState),
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
            }
        }
    }
}