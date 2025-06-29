package com.example.pnbase.module

import com.example.pnbase.userprofile.data.VideoProvider
import com.example.pnbase.userprofile.VideoViewModel
import com.example.pnbase.userprofile.domain.GetVideoUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.pnbase.userprofile.AudioViewModel
import com.example.pnbase.userprofile.data.AudioFilesProvider
import com.example.pnbase.userprofile.domain.GetAudioUseCase
import com.example.pnbase.userprofile.ImagesViewModel
import com.example.pnbase.userprofile.data.ImageProvider
import com.example.pnbase.userprofile.domain.GetImagesUseCase

val mediaModule = module {
    single { ImageProvider(androidContext().contentResolver) }

    factory { GetImagesUseCase(get()) }

    viewModel { ImagesViewModel(get()) }
}
val audioModule = module {
    single { AudioFilesProvider(androidContext().contentResolver) }

    factory { GetAudioUseCase(get()) }

    viewModel { AudioViewModel(get()) }
}
val videoModule = module {
    single { VideoProvider(androidContext().contentResolver) }

    factory { GetVideoUseCase(get()) }

    viewModel { VideoViewModel(get()) }
}