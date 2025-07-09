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

fun exportMediaToJson(
    context: Context
): Uri? {

    val images = ImageProvider(context.contentResolver).getImages()
    val videos = VideoProvider(context.contentResolver).getVideo()
    val audios = AudioFilesProvider(context.contentResolver).getAudio()
    val files = FilePath(context.contentResolver).getAllDocuments()

    val exportImages = mutableMapOf<String, Any>()
    images.forEach { exportImages[it.name] = it.uri.path.orEmpty() }

    val exportVideos = mutableMapOf<String, String>()
    videos.forEach { exportVideos[it.name] = it.uri.path.orEmpty() }

    val exportAudios = mutableMapOf<String, String>()
    audios.forEach { exportAudios[it.name] = it.uri.path.orEmpty() }

    val exportFiles = mutableMapOf<String, String>()
    files.forEach { exportFiles[it.name] = it.path }
    /*
    val file = File("0")
    file.listFiles()
    val file1 = File("0/1")
    file1.listFiles()
*/

    val json = JSONObject().apply {
        put("images", JSONObject(exportImages as Map<*, *>))
        put("videos", JSONObject(exportVideos as Map<*, *>))
        put("audios", JSONObject(exportAudios as Map<*, *>))
        put("files", JSONObject(exportFiles as Map<*, *>))
    }


    return try {
        val file = File(context.cacheDir, "media_export_${System.currentTimeMillis()}.json")
        file.writeText(json.toString(4))


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
