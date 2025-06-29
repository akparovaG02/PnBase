package com.example.pnbase.userprofile.domain

import com.example.pnbase.userprofile.data.Video
import com.example.pnbase.userprofile.data.VideoProvider

class GetVideoUseCase(
    private val videoProvider: VideoProvider
) {
    operator fun invoke(): List<Video> {
        return videoProvider.getVideo()
    }
}