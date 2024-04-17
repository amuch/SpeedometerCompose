package net.ddns.muchserver.speedometercompose.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import net.ddns.muchserver.speedometercompose.database.CheckPoint
import net.ddns.muchserver.speedometercompose.database.TripDao
import net.ddns.muchserver.speedometercompose.database.TripRepository
import net.ddns.muchserver.speedometercompose.database.TripRoomDatabase

class TripViewModel(application: Application): ViewModel() {
    val checkPoints: LiveData<List<CheckPoint>>
    private val repository: TripRepository

    init {
        val tripRoomDatabase = TripRoomDatabase.getInstance(application)
        val tripDao = tripRoomDatabase.tripDao()
        val checkPointDao = tripRoomDatabase.checkPointDao()

        repository = TripRepository(checkPointDao)
        checkPoints = repository.checkPoints
    }


    fun insertCheckPoint(checkPoint: CheckPoint) {
        repository.insertCheckPoint(checkPoint)
    }

    fun deleteCheckPoint(idCheckPoint: Long) {
        repository.deleteCheckPoint(idCheckPoint)
    }

    fun findCheckPoint(idCheckPoint: Long) {
        repository.findCheckPoint(idCheckPoint)
    }
}