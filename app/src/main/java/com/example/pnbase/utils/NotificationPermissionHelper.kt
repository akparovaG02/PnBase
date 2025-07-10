package com.example.pnbase.utils

import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import android.service.notification.NotificationListenerService

fun isNotificationListenerEnabled(context: Context): Boolean{
    val cn = ComponentName(context, NotificationListenerService::class.java)
    val flat = Settings.Secure.getString(context.contentResolver, "enabled_notification_listeners")
    return flat?.contains(cn.flattenToString()) ?: false
}