package com.genexus.superapps.bankx.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.genexus.superapps.bankx.ui.screens.Screen
import com.genexus.superapps.bankx.ui.screens.caching.CachedMiniAppListHomeContent
import com.genexus.superapps.bankx.ui.screens.main.MiniAppListHomeContent
import com.genexus.superapps.bankx.viewmodel.caching.CacheViewModel
import com.genexus.superapps.bankx.viewmodel.main.MainViewModel

@Composable
fun BottomNavController() {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = viewModel()
    val cacheViewModel: CacheViewModel = viewModel()
    Scaffold(
        bottomBar = {
            BottomNavigation(elevation = 10.dp) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                LaunchedEffect(key1 = currentDestination) {
                    when (currentDestination?.route) {
                        Screen.Main.route -> mainViewModel.refresh()
                        Screen.Cache.route -> cacheViewModel.refresh()
                    }
                }
                
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Main.route,
            Modifier.padding(innerPadding)
        ) {
            composable(Screen.Main.route) { MiniAppListHomeContent(mainViewModel) }
            composable(Screen.Cache.route) { CachedMiniAppListHomeContent(cacheViewModel) }
        }
    }
}

val items = listOf(
    Screen.Main,
    Screen.Cache,
)
