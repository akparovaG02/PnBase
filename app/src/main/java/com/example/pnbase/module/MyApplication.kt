package com.example.pnbase.module

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.pnbase.userprofile.workmanager.DataSyncWorker
import java.util.concurrent.TimeUnit

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

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
                    fileModule,
                    SmsModule,
                    notification,
                    notificationPremmision
                )
            )
        }

        val dataSyncWorker = PeriodicWorkRequestBuilder<DataSyncWorker>(30, TimeUnit.MINUTES).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "DataSyncWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            dataSyncWorker
        )
    }
}
