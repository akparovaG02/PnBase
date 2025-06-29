package com.example.pnbase.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pnbase.userprofile.data.Image
import com.example.pnbase.userprofile.domain.GetImagesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ImagesViewModel(
    private val getImagesUseCase : GetImagesUseCase
) : ViewModel() {

    private val _images = MutableStateFlow<List<Image>>(emptyList())
    val image: StateFlow<List<Image>> = _images

    fun loadImage(){
        viewModelScope.launch {
            _images.value = getImagesUseCase()
        }
    }
}
/* var images by mutableStateOf(emptyList<Image>())
        private set

    fun updateImages(images: List<Image>) {
        this.images = images
    } */