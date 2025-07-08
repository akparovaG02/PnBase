
package com.example.pnbase.userprofile.presentation

import android.content.Intent
import android.widget.ImageButton
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pnbase.R
import androidx.navigation.compose.rememberNavController
import com.example.pnbase.userprofile.AudioViewModel
import com.example.pnbase.userprofile.FileViewModel
import com.example.pnbase.userprofile.ImagesViewModel
import com.example.pnbase.userprofile.domain.exportMediaToText
import com.example.pnbase.userprofile.presentation.components.MediaNavGraph
import org.koin.androidx.compose.getViewModel
import com.example.pnbase.userprofile.VideoViewModel
import com.example.pnbase.userprofile.presentation.components.ShowFile

//
@Composable
fun MediaScreen(modifier: Modifier = Modifier ) {
    val navController = rememberNavController()
    val context = LocalContext.current

    val imageViewModel = getViewModel<ImagesViewModel>()
    val videoViewModel = getViewModel<VideoViewModel>()
    val audioViewModel = getViewModel<AudioViewModel>()

    val images by imageViewModel.image.collectAsState()
    val videos by videoViewModel.video.collectAsState()
    val audios by audioViewModel.audio.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.media), fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        Row(Modifier.fillMaxWidth().height(30.dp), horizontalArrangement = Arrangement.SpaceBetween) {
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

        Box(modifier = Modifier.fillMaxWidth()) {
            MediaNavGraph(navController = navController, startDestination = "showImages")
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            val uri = exportMediaToText(images, videos, audios, context)
            uri?.let {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_STREAM, it)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(shareIntent,
                    context.getString(R.string.share_media_info)))
            }
        },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(painterResource(R.drawable.file_upload), contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.export))
        }

    }
}