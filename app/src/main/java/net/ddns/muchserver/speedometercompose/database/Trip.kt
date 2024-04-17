package net.ddns.muchserver.speedometercompose.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_TRIP = "trip"
const val COLUMN_ID_TRIP = "idTrip"
const val COLUMN_CHECKPOINT_LIST = "checkPointList"

@Entity(tableName = TABLE_TRIP)
class Trip {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID_TRIP)
    var id: Int = 0

//    @ColumnInfo(name = COLUMN_CHECKPOINT_LIST)
//    val checkPoints = ArrayList<CheckPoint>()

}