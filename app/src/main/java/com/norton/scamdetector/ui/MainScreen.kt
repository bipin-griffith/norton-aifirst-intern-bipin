package com.norton.scamdetector.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.norton.scamdetector.ui.screen.HomeScreen
import com.norton.scamdetector.ui.screen.ProfileScreen
import com.norton.scamdetector.ui.screen.ScamDetectorScreen
import com.norton.scamdetector.ui.theme.NortonColors
import com.norton.scamdetector.ui.viewmodel.ScamDetectorViewModel

private sealed class NavTab(val route: String, val icon: ImageVector, val label: String) {
    object Home : NavTab("home", Icons.Filled.Home, "Home")
    object Scan : NavTab("scan", Icons.Filled.Shield, "Scan")
    object Profile : NavTab("profile", Icons.Filled.Person, "Profile")
}

private val navTabs = listOf(NavTab.Home, NavTab.Scan, NavTab.Profile)

@Composable
fun MainScreen(viewModel: ScamDetectorViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { NortonBottomBar(navController = navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavTab.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavTab.Home.route) {
                HomeScreen(viewModel = viewModel)
            }
            composable(NavTab.Scan.route) {
                ScamDetectorScreen(viewModel = viewModel)
            }
            composable(NavTab.Profile.route) {
                ProfileScreen()
            }
        }
    }
}

@Composable
private fun NortonBottomBar(navController: NavController) {
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route ?: NavTab.Home.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(NortonColors.BottomNavBackground)
            .navigationBarsPadding()
            .height(60.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            navTabs.forEach { tab ->
                val selected = currentRoute == tab.route
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            navController.navigate(tab.route) {
                                popUpTo(NavTab.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        tint = if (selected) NortonColors.PrimaryYellow
                               else Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected) NortonColors.PrimaryYellow
                                else Color.Transparent
                            )
                    )
                }
            }
        }
    }
}