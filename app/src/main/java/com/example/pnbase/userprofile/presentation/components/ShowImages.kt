package com.example.pnbase.userprofile.presentation.components

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.pnbase.userprofile.ImagesViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun ShowImages() {
    val viewModel = getViewModel<ImagesViewModel>()
    val groupedImages by viewModel.groupedImages.collectAsState()
    var selectedFolder by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.loadImage()
        } else {
            Toast.makeText(context, "Нет доступа к фото", Toast.LENGTH_SHORT).show()
        }
    }

    // Запрос разрешения
    LaunchedEffect(Unit) {
        val permissionCheck = ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_MEDIA_IMAGES
        )
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            viewModel.loadImage()
        } else {
            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(groupedImages.keys.toList()) { folder ->
                Button(
                    onClick = { selectedFolder = folder },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text(folder.substringAfterLast("/"))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            val images = groupedImages[selectedFolder]
            if (!images.isNullOrEmpty()) {
                items(images) { image ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = image.uri,
                            contentDescription = null,
                            modifier = Modifier.height(200.dp).fillMaxWidth()
                        )
                        Text(text = image.name)
                    }
                    Divider()
                }
            } else {
                item {
                    Text("Нет изображений для отображения")
                }
            }
        }
    }
}