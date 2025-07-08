package com.example.pnbase.userprofile.presentation.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pnbase.userprofile.presentation.MediaScreen

@Composable
fun MediaNavGraph(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("showImages") { ShowImages() }
        composable("showVideo") { ShowVideo() }
        composable("showAudio") { ShowAudio() }
        composable ("showFileByFolder") {ShowFileByFolder()}
    }
}