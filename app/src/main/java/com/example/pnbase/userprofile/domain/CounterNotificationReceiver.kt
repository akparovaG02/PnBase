package com.example.pnbase.userprofile.domain

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.pnbase.userprofile.data.CounterNotificationService

class CounterNotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val service = CounterNotificationService(context)
        service.showNotification(++Counter.value)
    }
}

object Counter {
    var value = 0
}