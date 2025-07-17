package com.example.pnbase.userprofile.workmanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.impl.utils.ForceStopRunnable.BroadcastReceiver
import com.example.pnbase.module.scheduleAlarm
import com.example.pnbase.utils.LogFileWriter

@SuppressLint("RestrictedApi")
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        LogFileWriter.writeLog("SyncWorker", "Alarm получен — запускаем Worker")

        val request = OneTimeWorkRequestBuilder<DataSyncWorker>().build()
        WorkManager.getInstance(context).enqueue(request)

        // Перезапускаем alarm через 15 секунд
        //scheduleAlarm(context)
    }
}
