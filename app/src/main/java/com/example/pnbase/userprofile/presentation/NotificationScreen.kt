package com.example.pnbase.userprofile.presentation

import android.Manifest
import android.app.Notification
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pnbase.RequestNotificationPermissionDialog
import com.example.pnbase.userprofile.NotificationViewModel
import com.example.pnbase.userprofile.PermissionViewModel
import com.example.pnbase.userprofile.domain.MyNotificationListenerService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import org.koin.androidx.compose.getViewModel
import kotlin.math.log

@Composable
fun NotificationScreen(modifier: Modifier) {
    val viewModel = getViewModel<PermissionViewModel>()
    val isGranted by viewModel.isPermissionGranted.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkPermission()
    }
    FirebaseNotification()
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        ShowNotificaion()
        Spacer(modifier = modifier.padding(16.dp))
        NotificationPermissionScreen()
    }
}

@Composable
fun ShowNotificaion(){
    val notifications = MyNotificationListenerService.notifications.collectAsState()
    Text("Всего уведомлений: ${notifications.value.size}")

    LazyColumn {
        items(notifications.value) { sbn ->
            val extras = sbn.notification.extras
            val title = extras.getString(Notification.EXTRA_TITLE) ?: "not Found"
            val text = extras.getString(Notification.EXTRA_TEXT) ?: "Not Found"
            Text(text = "$title: $text")
            Log.d("NotifService", "Новое уведомление: ${title ?: "no title"} / ${text ?: "no text"}")

        }

    }

}

@Composable
fun NotificationPermissionScreen(){
    val context = LocalContext.current

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        Arrangement.Top
    ) {
        Text("Чтобы показать список уведемление дайте разрешение")
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            context.startActivity(intent)
        }){
            Text("Открыть настройки")
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FirebaseNotification(modifier: Modifier = Modifier){
    val openDialog = remember { mutableStateOf(false)}

    val notificationPermissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    if (openDialog.value) {
        RequestNotificationPermissionDialog(
            openDialog = openDialog,
            permissionState = notificationPermissionState
        )
    }

    LaunchedEffect(key1 = Unit) {
        if (notificationPermissionState.status.isGranted || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
            Firebase.messaging.subscribeToTopic("Tutoriol")
        } else openDialog.value = true
    }

    Column (
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Firebase Cloud Messaging")
    }

}