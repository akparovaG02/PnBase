package com.example.pnbase.userprofile.domain

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MyNotificationListenerService : NotificationListenerService() {

    companion object {
        var _notifications = MutableStateFlow<List<StatusBarNotification>>(emptyList())
        val notifications: StateFlow<List<StatusBarNotification>> get() = _notifications
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        activeNotifications.forEach { sbn ->
            println(sbn)
        }
        Log.d("NotifService", "cервис подключен, уведомлений: ${activeNotifications.size}")
            _notifications.value = activeNotifications.toList()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        val current = _notifications.value.toMutableList()
        current.add(sbn)
        _notifications.value = current
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        super.onNotificationRemoved(sbn)
        val current = _notifications.value.toMutableList()
        current.removeIf { it.key == sbn.key }
        _notifications.value = current
    }
}
