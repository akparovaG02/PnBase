
package com.example.pnbase.userprofile.presentation

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pnbase.R
import androidx.navigation.compose.rememberNavController
import com.example.pnbase.userprofile.presentation.components.MediaNavGraph
import com.example.pnbase.userprofile.domain.exportMediaToJson

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MediaScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        bottomBar = {
            Button(
                onClick = {
                    val uri = exportMediaToJson( context)
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
                Icon(painterResource(R.drawable.file_upload), contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.export))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Text(stringResource(R.string.media), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { navController.navigate("showImages") }) {
                    Text(stringResource(R.string.show_images))
                }
                Button(onClick = { navController.navigate("showVideo") }) {
                    Text(stringResource(R.string.show_video))
                }
                Button(onClick = { navController.navigate("showAudio") }) {
                    Text(stringResource(R.string.show_audio))
                }
                Button(onClick = { navController.navigate("showFileByFolder") }) {
                    Text("showFile")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                MediaNavGraph(navController = navController, startDestination = "showImages")
            }

            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}