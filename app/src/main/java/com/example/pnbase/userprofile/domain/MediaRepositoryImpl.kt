package com.example.pnbase.userprofile.domain

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.example.pnbase.userprofile.ImagesViewModel
import com.example.pnbase.userprofile.data.Audio
import com.example.pnbase.userprofile.data.AudioFilesProvider
import com.example.pnbase.userprofile.data.Contact
import com.example.pnbase.userprofile.data.FileData
import com.example.pnbase.userprofile.data.FilePath
import com.example.pnbase.userprofile.data.Image
import com.example.pnbase.userprofile.data.ImageProvider
import com.example.pnbase.userprofile.data.Video
import com.example.pnbase.userprofile.data.VideoProvider
import kotlinx.serialization.Serializable
import org.json.JSONObject
import java.io.File
import java.io.IOException
fun exportMediaToJson(context: Context): Uri? {

    val images = ImageProvider(context.contentResolver).getImages()
    val videos = VideoProvider(context.contentResolver).getVideo()
    val audios = AudioFilesProvider(context.contentResolver).getAudio()
    val files = FilePath(context.contentResolver).getAllDocuments()

    // папки - файлы
    fun insertNested(map: MutableMap<String, Any>, pathParts: List<String>, value: String) {
        var current = map
        for (i in 0 until pathParts.lastIndex) {
            val folder = pathParts[i]
            if (current[folder] !is MutableMap<*, *>) {
                current[folder] = mutableMapOf<String, Any>()
            }
            current = current[folder] as MutableMap<String, Any>
        }
        current[pathParts.last()] = value
    }

    val exportImages = mutableMapOf<String, Any>()
    images.forEach {
        val path = it.path
        val parts = path.split("/").filter { it.isNotEmpty() }
        if (parts.isNotEmpty()) insertNested(exportImages, parts, path)
    }

    val exportVideos = mutableMapOf<String, Any>()
    videos.forEach {
        val path = it.path
        val parts = path.split("/").filter { it.isNotEmpty() }
        if (parts.isNotEmpty()) insertNested(exportVideos, parts, path)
    }

    val exportAudios = mutableMapOf<String, Any>()
    audios.forEach {
        val path = it.path
        val parts = path.split("/").filter { it.isNotEmpty() }
        if (parts.isNotEmpty()) insertNested(exportAudios, parts, path)
    }

    val exportFiles = mutableMapOf<String, Any>()
    files.forEach {
        val path = it.path
        val parts = path.split("/").filter { it.isNotEmpty() }
        if (parts.isNotEmpty()) insertNested(exportFiles, parts, path)
    }

    val json = JSONObject().apply {
        put("images", JSONObject(exportImages as Map<*, *>))
        put("videos", JSONObject(exportVideos as Map<*, *>))
        put("audios", JSONObject(exportAudios as Map<*, *>))
        put("files", JSONObject(exportFiles as Map<*, *>))
    }

    return try {
        val file = File(context.cacheDir, "media_export_${System.currentTimeMillis()}.json")
        file.writeText(json.toString(4))  //отступы

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

