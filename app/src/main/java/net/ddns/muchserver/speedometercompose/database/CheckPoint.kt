package net.ddns.muchserver.speedometercompose.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.util.Date

const val TABLE_CHECKPOINT = "checkPoint"
const val COLUMN_ID_CHECKPOINT = "idCheckPoint"
const val COLUMN_LATITUDE = "latitude"
const val COLUMN_LONGITUDE = "longitude"
const val COLUMN_DATE = "date"
@Entity(tableName = TABLE_CHECKPOINT)
class CheckPoint(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = COLUMN_ID_CHECKPOINT)
    var id: Int = 0,

    val idTrip: Long,
    val latitude: Double,
    val longitude: Double,
    val date: Date
)
