package com.example.pnbase.module

import android.content.Context
import com.example.pnbase.userprofile.data.CounterNotificationService
import org.koin.dsl.module

val notificationModule = module {
    single { (context: Context) -> CounterNotificationService(context) }
}
