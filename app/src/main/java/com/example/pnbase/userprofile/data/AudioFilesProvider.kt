package com.example.pnbase.userprofile.data

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import kotlinx.serialization.Serializable

@Serializable
data class Audio(
    val id: Long,
    val name: String,
    val uri: Uri,
    val path: String
)


class AudioFilesProvider(private val contentResolver: ContentResolver) {

    fun getAudio(): List<Audio> {
        val audios = mutableListOf<Audio>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DATA
        )
        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null, null, null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val nameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
            val dataColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATA)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                val path = if (dataColumn != -1) cursor.getString(dataColumn) else ""

                audios.add(Audio(id, name, uri, path ))
            }
        }
        return audios
    }

}