package com.example.pnbase.module

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.pnbase.userprofile.workmanager.AlarmReceiver
import com.example.pnbase.userprofile.workmanager.DataSyncWorker
import com.example.pnbase.utils.LogFileWriter
import java.time.Duration
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
       // scheduleAlarm(this)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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

        val dataRequest = PeriodicWorkRequestBuilder<DataSyncWorker>(
            2, TimeUnit.DAYS
        ).build()
        // надо потом поменять время!!!
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "synk_work",
            ExistingPeriodicWorkPolicy.KEEP,
            dataRequest
        )

    }


}
@SuppressLint("ScheduleExactAlarm")
fun scheduleAlarm(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val intervalMillis = 60 * 60 * 1000L

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        intervalMillis,
        pendingIntent
    )


    LogFileWriter.writeLog("SyncWorker", "AlarmManager запланирован через на каждый день")
}
