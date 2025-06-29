package com.example.pnbase.userprofile.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import com.example.pnbase.userprofile.data.CounterNotificationService
import com.example.pnbase.userprofile.domain.FirebaseService
import com.google.firebase.messaging.FirebaseMessaging


const val TOPIC = "/topics/myTopic2"

class NotificationActivity : ComponentActivity() {

    private val TAG = "NotificationActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                FirebaseService.token = task.result
                Log.d(TAG, "FCM Token: ${task.result}")
            } else {
                Log.w(TAG, "Fetching FCM token failed", task.exception)
            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        setContent {
            MaterialTheme {
                NotificationScreen()
            }
        }
    }
}