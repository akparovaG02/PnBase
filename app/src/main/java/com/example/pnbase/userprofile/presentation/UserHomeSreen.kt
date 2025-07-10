package com.example.pnbase.userprofile.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.pnbase.auth.presentation.AuthViewModel
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pnbase.userprofile.domain.ShowAllApps


@Composable
fun UserHomeSreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
){
    MainScreen()
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val appFetcher = remember { ShowAllApps(context) }
    var apps by remember { mutableStateOf<List<ShowAllApps.AppInfo>>(emptyList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(
            onClick = {
                apps = appFetcher.getAllApps()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Installed Apps")
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
