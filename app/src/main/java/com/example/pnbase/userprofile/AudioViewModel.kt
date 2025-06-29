package com.example.pnbase.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pnbase.userprofile.data.Audio
import com.example.pnbase.userprofile.domain.GetAudioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AudioViewModel (
    private val getAudioUseCase: GetAudioUseCase
) : ViewModel() {

    private val _audio = MutableStateFlow<List<Audio>>(emptyList())
    val audio: StateFlow<List<Audio>> = _audio

    fun loadAudio(){
        viewModelScope.launch {
            _audio.value = getAudioUseCase()
        }
    }
}