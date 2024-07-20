package net.ddns.muchserver.speedometercompose.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.ddns.muchserver.speedometercompose.database.CheckPoint
import net.ddns.muchserver.speedometercompose.database.Trip
import net.ddns.muchserver.speedometercompose.database.TripRepository
import net.ddns.muchserver.speedometercompose.database.TripRoomDatabase
import java.util.Date

class TripViewModel(application: Application): ViewModel() {
    val trip: MutableLiveData<Trip>
    val trips: LiveData<List<Trip>>
    val checkPoints: LiveData<List<CheckPoint>>
    val checkPointsCurrent: LiveData<List<CheckPoint>>
    val tripInProcess: MutableLiveData<Boolean> = MutableLiveData(false)
    private val repository: TripRepository

    init {
        val tripRoomDatabase = TripRoomDatabase.getInstance(application)
        val tripDao = tripRoomDatabase.tripDao()
        val checkPointDao = tripRoomDatabase.checkPointDao()

        repository = TripRepository(tripDao, checkPointDao)
        trip = repository.trip
        trips = repository.trips
        checkPoints = repository.checkPoints
        checkPointsCurrent = repository.checkPointsCurrent
    }

    fun insertTrip(trip: Trip) {
        repository.insertTrip(trip)
    }

    fun startTrip() {
        val date = Date()
        val trip = Trip(date.toString())
        insertTrip(trip)
        tripInProcess.value = true
        findTrip(trip.name)
        println("Start Trip")
    }

    fun stopTrip() {
        tripInProcess.value = false
        println("Stop Trip")
    }
    fun insertCheckPoint(checkPoint: CheckPoint) {
        repository.insertCheckPoint(checkPoint)
    }

    fun deleteTrip(idTrip: Long) {
        repository.deleteTrip(idTrip)
    }

    fun deleteCheckPoint(idCheckPoint: Long) {
        repository.deleteCheckPoint(idCheckPoint)
    }

    fun findTrip(idTrip: Long) {
        repository.findTrip(idTrip)
    }
    fun findTrip(name: String) {
        repository.findTrip(name)
    }

    fun findCheckPoint(idCheckPoint: Long) {
        repository.findCheckPoint(idCheckPoint)
    }
}