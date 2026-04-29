package com.loyalty.app.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.loyalty.app.ui.home.HomeScreen
import com.loyalty.app.ui.profile.ProfileScreen
import com.loyalty.app.ui.rewards.RewardsScreen
import com.loyalty.app.ui.theme.*

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "home_tab",
        title = "MI TARJETA",
        icon = Icons.Default.QrCode
    )

    object Rewards : BottomBarScreen(
        route = "rewards_tab",
        title = "PREMIOS",
        icon = Icons.Default.CardGiftcard
    )

    object Profile : BottomBarScreen(
        route = "profile_tab",
        title = "PERFIL",
        icon = Icons.Default.Person
    )
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        },
        containerColor = Zinc950
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = BottomBarScreen.Home.route
            ) {
                composable(BottomBarScreen.Home.route) {
                    HomeScreen()
                }
                composable(BottomBarScreen.Rewards.route) {
                    RewardsScreen()
                }
                composable(BottomBarScreen.Profile.route) {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Rewards,
        BottomBarScreen.Profile,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = Zinc900,
        contentColor = Zinc700,
        tonalElevation = 0.dp,
        modifier = Modifier.background(Zinc900)
    ) {
        screens.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    val iconColor = if (selected) Amber400 else Zinc700
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (selected) Amber400.copy(alpha = 0.1f) else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = screen.title,
                            tint = iconColor,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = screen.title,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (selected) Amber400 else Zinc700
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Amber400,
                    unselectedIconColor = Zinc700,
                    selectedTextColor = Amber400,
                    unselectedTextColor = Zinc700
                )
            )
        }
    }
}
