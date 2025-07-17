package com.example.pnbase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.pnbase.userprofile.workmanager.DataSyncWorker
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FirebaseNotification","Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        performActionOnNotification(message)

        message.notification?.let {
            showNotification(message)
        }

        if (message.data.isEmpty()) {
            handleDataMessage()
        }
    }

    private fun performActionOnNotification(message: RemoteMessage) {
        val workRequest = OneTimeWorkRequestBuilder<DataSyncWorker>().build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
        Log.d("FirebaseNotification", "Уведомление получено: ${message.notification?.title}")
    }

    private fun handleDataMessage(){
        Log.d("FirebaseNotification", "handleMassage: ")
    }

    fun showNotification(message: RemoteMessage) {

        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)

        val channelId = "Default"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_stat_ic_notification)
            .setContentTitle(message.notification?.title ?: "Уведомление")
            .setContentText(message.notification?.body ?: "Сообщение от сервера")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelName = "Firebase Messaging"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        manager.notify(Random.nextInt(), notificationBuilder.build())
    }
}