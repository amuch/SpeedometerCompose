package net.ddns.muchserver.speedometercompose.preferences

data class UserPreferences(
    val theme: String,
    val indexTheme: Int,
    val standardUnits: Boolean,
    val updateInterval: Long,
)
