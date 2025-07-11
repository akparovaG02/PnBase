package com.example.pnbase.utils

import android.util.Log
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

object LogFileWriter {
    private const val FILE_NAME = "/sdcard/logs.txt"
    private val loggedMessages = mutableSetOf<String>()

    fun writeLog(tag: String, message: String) {
        val formatTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val timeShow = formatTime.format(Date())

        val logEntry = " ${timeShow }, [$tag] $message"
        Log.d(tag, message)



        if(!loggedMessages.contains(logEntry)){
            loggedMessages.add(logEntry)

            try {
                val file = File(FILE_NAME)
                file.appendText("$logEntry\n")
            } catch (e: IOException) {
                writeLog("APPLOG", "Ошибка записи лога в файл: ${e.message}")
            }
        }
    }
}
