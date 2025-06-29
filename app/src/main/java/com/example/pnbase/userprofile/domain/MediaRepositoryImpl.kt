package com.example.pnbase.userprofile.domain

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.pnbase.userprofile.data.Audio
import com.example.pnbase.userprofile.data.Image
import com.example.pnbase.userprofile.data.Video
import java.io.File
import java.io.IOException

fun exportMediaToText(
    images: List<Image>,
    videos: List<Video>,
    audios: List<Audio>,
    context: Context
): Uri? {
    val exportText = buildString {
        appendLine("=== Images ===")
        images.forEach {
            appendLine("ID: ${it.id} | Name: ${it.name} | URI: ${it.uri}")
        }

        appendLine("\n=== Videos ===")
        videos.forEach {
            appendLine("ID: ${it.id} | Name: ${it.name} | URI: ${it.uri}")
        }

        appendLine("\n=== Audio ===")
        audios.forEach {
            appendLine("ID: ${it.id} | Name: ${it.name} | URI: ${it.uri}")
        }
    }

    return try {
        val file = File(context.cacheDir, "media_export_${System.currentTimeMillis()}.txt")
        file.writeText(exportText)

        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
