package com.example.pnbase.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pnbase.userprofile.data.Video
import com.example.pnbase.userprofile.domain.GetVideoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VideoViewModel(
    private val getVideoUseCase: GetVideoUseCase
): ViewModel() {
    private val _video = MutableStateFlow<List<Video>>(emptyList())
    val video: StateFlow<List<Video>> = _video

    fun loadVideo(){
        viewModelScope.launch {
            _video.value = getVideoUseCase()
        }
    }
}