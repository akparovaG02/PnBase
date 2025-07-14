package com.example.pnbase.userprofile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.pnbase.auth.presentation.AuthViewModel
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.pnbase.userprofile.domain.ShowAllApps
import com.example.pnbase.userprofile.workmanager.DataSyncWorker
import java.time.Duration
import java.util.concurrent.TimeUnit


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserHomeSreen(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    MainScreen()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val appFetcher = remember { ShowAllApps(context) }
    var apps by remember { mutableStateOf<List<ShowAllApps.AppInfo>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(26.dp)
    ) {
        Button(onClick = {
            val intent = Intent( Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
            context.startActivity(intent)
        }) {
            Text("Открыть настройки")
        }
        Button(
            onClick = {
                apps = appFetcher.getAllApps()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Получить список всех приложении")
        }

        Spacer(modifier = Modifier.height(8.dp))
        // надо потом поменять время
        Button(
            onClick = {
                val request = PeriodicWorkRequestBuilder<DataSyncWorker>(
                    2, TimeUnit.DAYS
                ).build()

                WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                    "DataSyncWork",
                    ExistingPeriodicWorkPolicy.KEEP,
                    request
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Синхронизировать медиа и локацию")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "${apps.size} apps are installed")

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(apps) { app ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(text = "Name: ${app.name}", style = MaterialTheme.typography.bodyLarge)
                    Text(text = "Path: ${app.apkPath}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

