package com.example.pnbase.module

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.pnbase.userprofile.data.CounterNotificationService

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        creatNotification()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Запуск Koin
        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    appModule,
                    contactModule,
                    mediaModule,
                    videoModule,
                    audioModule,
                    notificationModule,
                    fileModule,
                    SmsModule
                )
            )
        }
    }

    private fun creatNotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CounterNotificationService.COUNTER_CHANNEL_ID,
                "Counter",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Used for the increment counter notifications"

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
