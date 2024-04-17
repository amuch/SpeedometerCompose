package net.ddns.muchserver.speedometercompose.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

const val VERSION_DATABASE = 1
const val NAME_DATABASE = "tripDatabase"
@Database(entities = [CheckPoint::class, Trip::class], version = VERSION_DATABASE)
@TypeConverters(Converters::class)
abstract class TripRoomDatabase: RoomDatabase() {

    abstract fun checkPointDao(): CheckPointDao
    abstract fun tripDao(): TripDao

    companion object {
        private var INSTANCE: TripRoomDatabase? = null

        fun getInstance(context: Context): TripRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TripRoomDatabase::class.java,
                        NAME_DATABASE
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}