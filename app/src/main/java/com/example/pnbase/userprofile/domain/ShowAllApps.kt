package com.example.pnbase.userprofile.domain

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.example.pnbase.utils.LogFileWriter

class ShowAllApps(private val context: Context) {

    data class AppInfo(
        val name: String,
        val apkPath: String
    )

    fun getAllApps(): List<AppInfo> {
        LogFileWriter.writeLog("APPLOG", "Начало синхронизации списка приложений")

        val packageManager = context.packageManager
        val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        val result = apps.map {
            val name = packageManager.getApplicationLabel(it).toString()
            val path = it.sourceDir
            AppInfo(name, path)
        }

        LogFileWriter.writeLog("APPLOG", "Синхронизация завершена: найдено ${result.size} приложений")

        result.forEach {
            LogFileWriter.writeLog("APPLOG", "Приложение: ${it.name}, путь: ${it.apkPath}")
        }

        return result
    }
}
