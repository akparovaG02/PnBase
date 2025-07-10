package com.example.pnbase.userprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.pnbase.utils.isNotificationListenerEnabled
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PermissionViewModel(application: Application) : AndroidViewModel(application) {

    private val _isPermissionGranted = MutableStateFlow(false)
    val isPermissionGranted: StateFlow<Boolean> get() = _isPermissionGranted

    fun checkPermission() {
        _isPermissionGranted.value = isNotificationListenerEnabled(getApplication())
    }
}
