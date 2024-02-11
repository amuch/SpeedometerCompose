package net.ddns.muchserver.speedometercompose.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import net.ddns.muchserver.speedometercompose.R
import net.ddns.muchserver.speedometercompose.composables.DECIMAL_FORMAT

const val TITLE_FOREGROUND_SERVICE = "Speedometer running"
const val TEXT_FOREGROUND_SERVICE = "Max Speed"
const val ID_FOREGROUND_SERVICE = 1001

class ServiceForeground: Service() {

    companion object {
        var serviceRunning by mutableStateOf(false)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stop()
            Actions.UPDATE.toString() -> updateNotification(intent.getFloatExtra("speedMax", 0.0f))
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification = NotificationCompat.Builder(this, CHANNEL_SPEEDOMETER)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(TITLE_FOREGROUND_SERVICE)
            .setContentText("")
            .build()

        startForeground(ID_FOREGROUND_SERVICE, notification)
        serviceRunning = true
    }

    private fun stop() {
        stopSelf()
        serviceRunning = false
    }

    fun updateNotification(speedMax: Float) {
        val notification = NotificationCompat.Builder(this, CHANNEL_SPEEDOMETER)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(TITLE_FOREGROUND_SERVICE)
            .setContentText("$TEXT_FOREGROUND_SERVICE ${DECIMAL_FORMAT.format(speedMax)} MPH")
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(ID_FOREGROUND_SERVICE, notification)
    }

    enum class Actions {
        START,
        STOP,
        UPDATE
    }
}