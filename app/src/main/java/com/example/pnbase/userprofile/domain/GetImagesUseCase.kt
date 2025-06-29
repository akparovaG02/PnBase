package com.example.pnbase.userprofile.domain

import com.example.pnbase.userprofile.data.Image
import com.example.pnbase.userprofile.data.ImageProvider

class GetImagesUseCase(
    private val imageProvider: ImageProvider
) {
    operator fun invoke(): List<Image> {
        return imageProvider.getImages()
    }
}