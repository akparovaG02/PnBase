package com.example.pnbase.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.pnbase.R
import com.example.pnbase.adminprofile.presentation.AdminProfile
import com.example.pnbase.auth.presentation.AuthViewModel
import com.example.pnbase.userprofile.presentation.ContactsScreen
import com.example.pnbase.userprofile.presentation.MapsScreen
import com.example.pnbase.userprofile.presentation.MediaScreen
import com.example.pnbase.userprofile.presentation.NotificationScreen
import com.example.pnbase.userprofile.presentation.UserHomeSreen

@Composable
fun BottomBar(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){

    val navItemList = listOf(
        NavItem(stringResource(R.string.home), Icons.Default.Person),
        NavItem(stringResource(R.string.media), Icons.Default.DateRange),
        NavItem(stringResource(R.string.contacts), Icons.Default.Phone),
        NavItem(stringResource(R.string.notification), Icons.Default.Notifications),
        NavItem(stringResource(R.string.map), Icons.Default.LocationOn),
    )

    var selectedIndex by remember {
        mutableStateOf(0)
    }

    Scaffold (
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = index == selectedIndex,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                        },
                        label = { Text(text = navItem.label) }
                    )
                }
            }
        }
    ) {
        ContentScreen(
            modifier = Modifier.padding(it),
            selectedIndex = selectedIndex,
            navController = navController,
            authViewModel = authViewModel
        )
    }


}

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    when (selectedIndex) {
        0 -> UserHomeSreen(modifier, navController, authViewModel)
        1 -> MediaScreen(modifier)
        2 -> ContactsScreen(modifier)
        3 -> NotificationScreen(modifier)
        4 -> MapsScreen(modifier)
    }
}


data class NavItem(
    val label : String,
    val icon : ImageVector
)