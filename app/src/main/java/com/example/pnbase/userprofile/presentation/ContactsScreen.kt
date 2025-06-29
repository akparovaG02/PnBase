package com.example.pnbase.userprofile.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import com.example.pnbase.ui.theme.PurpleGrey80

@Composable
fun ContactsScreen(modifier: Modifier = Modifier) {
    val viewModel = getViewModel<ContactsViewModel>()
    val contacts by viewModel.contacts.collectAsState()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.loadContacts()
        } else {
            Toast.makeText(context,
                context.getString(R.string.permission_to_read_contacts), Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_CONTACTS
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            viewModel.loadContacts()
        } else {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.contacts), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))


        if (contacts.isEmpty()) {
            Text(stringResource(R.string.no_contacts_found))
        } else {
            LazyColumn {
                items(contacts) { contact ->
                    ContactItem(
                        name = contact.name,
                        subtitle = contact.phoneNumber,
                        time = ""
                    )
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