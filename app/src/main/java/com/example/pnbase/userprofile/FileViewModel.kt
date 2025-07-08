package com.example.pnbase.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pnbase.userprofile.data.FileData
import com.example.pnbase.userprofile.domain.GetFileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FileViewModel (
    private val getFileUseCase: GetFileUseCase
): ViewModel() {
    private val _file = MutableStateFlow<List<FileData>>(emptyList())
    val file: StateFlow<List<FileData>> = _file

    fun loadFile(){
        viewModelScope.launch {
            _file.value = getFileUseCase()
        }
    }

    val groupedFiles: StateFlow<Map<String, List<FileData>>> =
        file.map { list ->
            list.groupBy { it.folder }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

}