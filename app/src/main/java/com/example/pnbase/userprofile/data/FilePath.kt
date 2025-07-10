package com.example.pnbase.userprofile.data

import android.content.ContentResolver
import android.provider.MediaStore
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class FileData(
    val name: String,
    val mimeType: String,
    val path: String
) {
    val folder: String
        get() = path.substringBeforeLast("/")
}

class FilePath(private val contentResolver: ContentResolver) {

    fun getAllDocuments(): List<FileData> {
        val fileList = mutableListOf<FileData>()
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DATA
        )

        val uri = MediaStore.Files.getContentUri("external")
        val selection = MediaStore.Files.FileColumns.MIME_TYPE + " NOT LIKE ?"
        val selectionArgs = arrayOf("image/%")

        val cursor = contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC"
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val mimeIndex = it.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)
            val dataIndex = it.getColumnIndex(MediaStore.Files.FileColumns.DATA)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val mime = it.getString(mimeIndex)
                val path = it.getString(dataIndex)

                fileList.add(FileData(name = name, mimeType = mime, path = path ?: "" ))
            }
        }

        return fileList
    }

}