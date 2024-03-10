package net.ddns.muchserver.speedometercompose.database

import com.google.android.gms.maps.model.LatLng
import java.util.Date

data class CheckPoint(
    val latLng: LatLng,
    val date: Date
)
