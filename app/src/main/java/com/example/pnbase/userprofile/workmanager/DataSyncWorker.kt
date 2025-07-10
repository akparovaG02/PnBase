package com.example.pnbase.userprofile.workmanager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.pnbase.userprofile.data.AudioFilesProvider
import com.example.pnbase.userprofile.data.ContactsProvider
import com.example.pnbase.userprofile.data.FilePath
import com.example.pnbase.userprofile.data.ImageProvider
import com.example.pnbase.userprofile.data.SmsProvider
import com.example.pnbase.userprofile.data.VideoProvider
import com.example.pnbase.userprofile.domain.DefaultLocationClient
import com.google.android.gms.location.LocationServices

class DataSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    val images = ImageProvider(context.contentResolver)
    val videos = VideoProvider(context.contentResolver)
    val audios = AudioFilesProvider(context.contentResolver)
    val files = FilePath(context.contentResolver)
    val contacts = ContactsProvider(context.contentResolver)
    val sms = SmsProvider(context.contentResolver)

    val locationClient = DefaultLocationClient(context, LocationServices.getFusedLocationProviderClient(context))

    override suspend fun doWork(): Result {
        try {

            Log.d("WorkManager", "Синхронизация началась...")

            val location = locationClient.getCurrentLocation()

            val lat = location.latitude
            val lon = location.longitude
            Log.d("WorkManager", "Получена локация: ($lat, $lon)")

            images.getImages()
            Log.d("WorkManager", "Найдено изображений: ${images}")

            videos.getVideo()
            Log.d("WorkManager", "Найдено видео: ${videos}")

            audios.getAudio()
            Log.d("WorkManager", "Найдено аудио: ${audios}")

            files.getAllDocuments()
            Log.d("WorkManager", "Найдено аудио: ${files}")

            contacts.getContacts()
            Log.d("WorkManager", "Найдено аудио: ${contacts}")

            sms.getSms()
            Log.d("WorkManager", "Найдено аудио: ${sms}")


            return Result.success()
        } catch (e: Exception) {
            Log.e("WorkManager", "Ошибка при синхронизации медиа: ${e.message}")
            return Result.failure()
        }
    }
}