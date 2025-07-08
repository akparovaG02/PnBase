package com.example.pnbase.userprofile.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pnbase.userprofile.FileViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun ShowFileByFolder() {
    val viewModel = getViewModel<FileViewModel>()
    val foldersWithFiles by viewModel.groupedFiles.collectAsState()
    var selectedFolder by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadFile()
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        // Кнопки-папки
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(foldersWithFiles.keys.toList()) { folder ->
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
            val files = foldersWithFiles[selectedFolder]
            if (!files.isNullOrEmpty()) {
                items(files) { file ->
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)) {
                        Text("📄 ${file.name}", fontWeight = FontWeight.Bold)
                        Text("Тип: ${file.mimeType}", fontSize = 12.sp)
                        Text("Путь: ${file.path}", fontSize = 12.sp)
                    }
                    Divider()
                }
            } else {
                item {
                    Text("Нет файлов для отображения", fontSize = 14.sp)
                }
            }
        }
    }
}


@Composable
fun ShowFile() {
    val viewModel = getViewModel<FileViewModel>()
    val files by viewModel.file.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFile()
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(files) { file ->
            androidx.compose.foundation.layout.Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("📄 ${file.name}", fontWeight = FontWeight.Bold)
                Text("Тип: ${file.mimeType}", fontSize = 12.sp)
                Text("Путь: ${file.path}", fontSize = 12.sp)
            }
            Divider()
        }
    }
}
