package net.ddns.muchserver.speedometercompose.database

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimeStamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimeStamp(date: Date): Long {
        return date.time
    }

}