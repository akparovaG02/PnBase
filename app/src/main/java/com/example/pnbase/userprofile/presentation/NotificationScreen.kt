package com.example.pnbase.userprofile.presentation

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pnbase.R
import com.example.pnbase.userprofile.data.CounterNotificationService
import com.example.pnbase.userprofile.data.NotificationData
import com.example.pnbase.userprofile.data.PushNotification
import com.example.pnbase.userprofile.domain.Counter
import com.example.pnbase.userprofile.domain.FirebaseService
import com.example.pnbase.userprofile.domain.RetrofitInstance
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.mp.KoinPlatform.getKoin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(modifier: Modifier = Modifier) {
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var recipientToken by remember { mutableStateOf(FirebaseService.token ?: "") }

    val context = LocalContext.current.applicationContext
  /*  val service = remember {
        getKoin().get<CounterNotificationService> { parametersOf(context) }
    }*/

    LaunchedEffect(Unit) {
        FirebaseService.sharedPref = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            FirebaseService.token = it
            recipientToken = it
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.send_push_notification),
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.title)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text(stringResource(R.string.message)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = recipientToken,
            onValueChange = { recipientToken = it },
            label = { Text(stringResource(R.string.recipient_token_or_leave_empty_for_topic)) },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
                    val notification = PushNotification(
                        NotificationData(title, message),
                        recipientToken
                    )
                    sendNotification(notification)
                } else {
                    Log.w(TAG, context.getString(R.string.please_fill_all_fields))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(12.dp)
        ) {
            Text(stringResource(R.string.send_notification))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(
                R.string.your_fcm_token,
                FirebaseService.token ?: stringResource(R.string.fetching)
            ),
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

       /*Box(modifier = Modifier.fillMaxSize()) {
            Button(onClick = {
                service.showNotification(Counter.value)
            }) {
                Text(text = "Show notification")
            }
        }*/
    }
}


private fun sendNotification(notification: PushNotification) =
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful) {
                Log.d(TAG, "Response: ${Gson().toJson(response)}")
            } else {
                Log.e(TAG, response.errorBody()?.string() ?: "Unknown error")
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }