package com.example.pnbase.userprofile.domain

import com.example.pnbase.userprofile.data.Audio
import com.example.pnbase.userprofile.data.AudioFilesProvider

class GetAudioUseCase (
    private val audioProvider: AudioFilesProvider
) {
    operator fun invoke(): List<Audio> {
        return audioProvider.getAudio()
    }
}