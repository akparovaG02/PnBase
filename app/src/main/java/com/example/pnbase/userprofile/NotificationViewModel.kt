package com.example.pnbase.userprofile

import android.service.notification.StatusBarNotification
import androidx.lifecycle.ViewModel
import com.example.pnbase.userprofile.domain.MyNotificationListenerService
import kotlinx.coroutines.flow.StateFlow

class NotificationViewModel : ViewModel() {
    val notifications: StateFlow<List<StatusBarNotification>> =
        MyNotificationListenerService._notifications
}
