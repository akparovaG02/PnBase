package com.example.pnbase

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermissionDialog(
    modifier: Modifier = Modifier,
    openDialog: MutableState<Boolean>,
    permissionState: PermissionState
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                TextButton(onClick = {
                    openDialog.value = false
                    permissionState.launchPermissionRequest()
                }) {
                    Text("OK")
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_stat_ic_notification),
                    contentDescription = null
                )
            },
            title = {
                Text("Notification Permission")
            },
            text = {
                Text("This app requires Notification permission to show notifications.")
            }
        )
    }
}

