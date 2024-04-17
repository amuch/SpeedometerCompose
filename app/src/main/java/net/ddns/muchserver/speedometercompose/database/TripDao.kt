package net.ddns.muchserver.speedometercompose.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface TripDao {

    @Insert
    fun insertTrip(trip: Trip)
}