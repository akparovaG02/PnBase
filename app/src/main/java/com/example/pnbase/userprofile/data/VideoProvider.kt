package com.example.pnbase.userprofile.data

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val id: Long,
    val name: String,
    val uri: Uri,
    val path: String
)


class VideoProvider(private val contentResolver: ContentResolver) {

    fun getVideo(): List<Video> {
        val videos = mutableListOf<Video>()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATA
        )
        contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null, null, null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Video.Media._ID)
            val nameColumn = cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME)
            val dataIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                val path = if (dataIndex != -1) cursor.getString(dataIndex) else ""

                videos.add(Video(id, name, uri, path ))
            }
        }
        return videos
    }
}
