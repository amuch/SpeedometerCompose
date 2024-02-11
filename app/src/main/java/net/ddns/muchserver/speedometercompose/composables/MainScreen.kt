package net.ddns.muchserver.speedometercompose.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.ddns.muchserver.speedometercompose.MainActivity
import net.ddns.muchserver.speedometercompose.viewmodel.PreferenceViewModel
import net.ddns.muchserver.speedometercompose.viewmodel.SpeedometerViewModel
import java.text.DecimalFormat


val DECIMAL_FORMAT = DecimalFormat("#.##")

// 1 portrait 2 landscape
@Composable
fun MainScreen(activity: MainActivity, speedometerViewModel: SpeedometerViewModel, preferenceViewModel: PreferenceViewModel) {


    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation

    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    if(Configuration.ORIENTATION_PORTRAIT == orientation) {
        LayoutPortrait(activity, speedometerViewModel, preferenceViewModel)
    }
    else {
        LayoutLandscape(activity, speedometerViewModel, preferenceViewModel)
    }
}