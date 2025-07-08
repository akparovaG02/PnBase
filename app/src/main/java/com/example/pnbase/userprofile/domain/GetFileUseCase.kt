package com.example.pnbase.userprofile.domain

import com.example.pnbase.userprofile.data.FileData
import com.example.pnbase.userprofile.data.FilePath

class GetFileUseCase(
    private val filePath: FilePath
) {
    operator fun invoke(): List<FileData> {
        return filePath.getAllDocuments()
    }
}