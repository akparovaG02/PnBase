package com.example.pnbase.userprofile.data

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val id: Long,
    val name: String,
    val uri: Uri
)

class ImageProvider(private val contentResolver: ContentResolver) {

    fun getImages(): List<Image> {
        val images = mutableListOf<Image>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null, null, null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                images.add(Image(id, name, uri))
            }
        }
        return images
    }
}