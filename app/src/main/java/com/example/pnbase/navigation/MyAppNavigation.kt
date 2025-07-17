package com.example.pnbase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pnbase.adminprofile.presentation.AdminProfile
import com.example.pnbase.auth.presentation.AuthState
import com.example.pnbase.auth.presentation.AuthViewModel
import com.example.pnbase.auth.presentation.login.LoginScreen
import com.example.pnbase.auth.presentation.register.RegisterScreen
import com.example.pnbase.userprofile.presentation.UserHomeSreen


@Composable
fun MyAppNavigation(modifier: Modifier = Modifier,authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(modifier, navController, authViewModel)
        }
        composable("signup") {
            RegisterScreen(modifier, navController, authViewModel)
        }
        composable("home") {
            BottomBar(modifier, navController, authViewModel)
        }
        composable("userHome") {
            UserHomeSreen(modifier, navController, authViewModel)
        }
        composable("adminHome") {
            AdminProfile(modifier, navController, authViewModel)
        }
    }
}
