package com.example.pnbase.userprofile.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pnbase.R
import com.example.pnbase.ui.theme.PurpleGrey40
import com.example.pnbase.userprofile.ContactsViewModel
import org.koin.androidx.compose.getViewModel
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.example.pnbase.userprofile.SMSViewModel
import com.example.pnbase.userprofile.domain.exportContactToJson

@Composable
fun ContactsScreen(modifier: Modifier = Modifier) {
    val viewModel = getViewModel<ContactsViewModel>()
    val contacts by viewModel.contacts.collectAsState()

    val smsViewModel = getViewModel<SMSViewModel>()
    val smsList by smsViewModel.sms.collectAsState()

    val context = LocalContext.current

    var selectedType by remember { mutableStateOf("contacts") }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (selectedType == "contacts") viewModel.loadContacts()
            else smsViewModel.loadSms()
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.permission_to_read_contacts),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(selectedType) {
        val permission = if (selectedType == "contacts") Manifest.permission.READ_CONTACTS
        else Manifest.permission.READ_SMS

        val check = ContextCompat.checkSelfPermission(context, permission)
        if (check == PackageManager.PERMISSION_GRANTED) {
            if (selectedType == "contacts") viewModel.loadContacts()
            else smsViewModel.loadSms()
        } else {
            permissionLauncher.launch(permission)
        }
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            Button(
                onClick = {
                    val uri = exportContactToJson( context)
                    uri?.let {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_STREAM, it)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(
                            Intent.createChooser(
                                shareIntent,
                                context.getString(R.string.share_media_info)
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                androidx.compose.material.Icon(painterResource(R.drawable.file_upload), contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                androidx.compose.material.Text(stringResource(R.string.export))
            }
        }
    ) { padding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Выберите, что показать:", fontWeight = FontWeight.Bold)

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedType == "contacts",
                    onClick = { selectedType = "contacts" }
                )
                Text("Контакты", Modifier.clickable { selectedType = "contacts" })

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = selectedType == "sms",
                    onClick = { selectedType = "sms" }
                )
                Text("SMS", Modifier.clickable { selectedType = "sms" })
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                if (selectedType == "contacts") {
                    if (contacts.isEmpty()) {
                        item { Text("Контакты не найдены") }
                    } else {
                        items(contacts) { contact ->
                            ContactItem(
                                name = contact.name,
                                subtitle = contact.phoneNumber,
                                time = ""
                            )
                        }
                    }
                } else {
                    if (smsList.isEmpty()) {
                        item { Text("СМС не найдены") }
                    } else {
                        items(smsList) { sms ->
                            SmsItem(
                                name = sms.address,
                                subtitle = sms.body,
                                time = sms.date
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ContactItem(name: String, subtitle: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(name, fontWeight = FontWeight.Bold)
            Text(subtitle )
        }
        if (time.isNotEmpty()) {
            Text(time )
        }
    }
}
@Composable
fun SmsItem(name: String, subtitle: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(2.dp)) {
            Text(name, fontWeight = FontWeight.Bold)
            Text(subtitle )
        }
        if (time.isNotEmpty()) {
            Text(time )
        }
    }
}