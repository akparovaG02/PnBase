package com.example.pnbase.userprofile.domain

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.core.app.NotificationCompat
import com.example.pnbase.R
import com.example.pnbase.userprofile.data.LocationClient
import com.example.pnbase.utils.LogFileWriter
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private var locationJob: Job? = null

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        LogFileWriter.writeLog("LocationService", "Служба создана")
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                LogFileWriter.writeLog("LocationService", "Получена команда START")
                start()
            }
            ACTION_STOP -> {
                LogFileWriter.writeLog("LocationService", "Получена команда STOP")
                stop()
            }
        }
        return START_STICKY
    }

    private fun start() {
        LogFileWriter.writeLog("LocationService", "Служба запущена")
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationJob = locationClient
            .getLocationUpdates(10000L)
            .catch { e ->
                LogFileWriter.writeLog("LocationService", "Ошибка получения локации: ${e.message}")
                e.printStackTrace()
            }
            .onEach { location ->
                val lat = location.latitude
                val long = location.longitude

                LogFileWriter.writeLog("LocationService", "Обновлена локация: lat=$lat, long=$long")

                val updatedNotification = notification
                    .setContentText("Location: ($lat, $long)")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)

                notificationManager.notify(1, updatedNotification.build())
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun stop() {
        LogFileWriter.writeLog("LocationService", "Служба остановлена")
        locationJob?.cancel()
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        LogFileWriter.writeLog("LocationService", "Служба уничтожена")
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}
