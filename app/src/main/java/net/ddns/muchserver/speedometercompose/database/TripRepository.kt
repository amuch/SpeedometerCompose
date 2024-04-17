package net.ddns.muchserver.speedometercompose.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TripRepository(private val checkPointDao: CheckPointDao) {
    val checkPoints: LiveData<List<CheckPoint>> = checkPointDao.getAllCheckPoints()

    val checkPoint = MutableLiveData<CheckPoint>()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun insertCheckPoint(checkPoint: CheckPoint) {
        println("Add checkpoint")
        coroutineScope.launch(Dispatchers.IO) {
            checkPointDao.insertCheckPoint(checkPoint)
        }
    }

    fun deleteCheckPoint(idCheckPoint: Long) {
        coroutineScope.launch(Dispatchers.IO) {
            checkPointDao.deleteCheckPoint(idCheckPoint)
        }
    }

    fun findCheckPoint(idCheckPoint: Long) {
        coroutineScope.launch(Dispatchers.Main) {
            checkPoint.value = asyncFind(idCheckPoint).await()
        }
    }

    private fun asyncFind(idCheckPoint: Long): Deferred<CheckPoint?> =
        coroutineScope.async(Dispatchers.IO) {
            return@async checkPointDao.findCheckPoint(idCheckPoint)
        }
}