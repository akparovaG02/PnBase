package com.example.pnbase.userprofile.domain

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.example.pnbase.userprofile.data.ContactsProvider
import com.example.pnbase.userprofile.data.SmsProvider
import com.example.pnbase.utils.LogFileWriter
import org.json.JSONObject
import java.io.File
import java.io.IOException

fun exportContactToJson(context: Context): Uri? {
    val TAG = "APPLOG"

    LogFileWriter.writeLog(TAG, "Начата синхронизация контактов и SMS")

    val contacts = ContactsProvider(context.contentResolver).getContacts()
    val sms = SmsProvider(context.contentResolver).getSms()

    LogFileWriter.writeLog(TAG, "Получено ${contacts.size} контактов и ${sms.size} SMS")

    val exportContacts = mutableMapOf<String, String>()
    contacts.forEach { exportContacts[it.name] = it.phoneNumber }

    val exportSms = mutableMapOf<String, String>()
    sms.forEach { exportSms[it.address] = it.id }

    val json = JSONObject().apply {
        put("contacts", JSONObject(exportContacts as Map<*, *>))
        put("sms", JSONObject(exportSms as Map<*, *>))
    }

    return try {
        val fileName = "phone_export_${System.currentTimeMillis()}.json"
        val file = File(context.cacheDir, fileName)
        file.writeText(json.toString(2))

        LogFileWriter.writeLog(TAG, "Данные экспортированы в файл: $fileName")

        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    } catch (e: IOException) {
        LogFileWriter.writeLog(TAG, "Ошибка при экспорте данных")
        null
    }
}
