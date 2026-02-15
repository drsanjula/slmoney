package com.slmoney.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.slmoney.app.ui.analytics.AnalyticsScreen
import com.slmoney.app.ui.calendar.BillCalendarScreen
import com.slmoney.app.ui.dashboard.DashboardScreen
import com.slmoney.app.ui.settings.SettingsScreen
import com.slmoney.app.core.preferences.SettingsManager
import javax.inject.Inject
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "dashboard",
        modifier = modifier
    ) {
        composable("dashboard") { 
            DashboardScreen(
                onAddClick = { navController.navigate("manual_entry") },
                onSettingsClick = { navController.navigate("settings") }
            ) 
        }
        composable("analytics") { AnalyticsScreen() }
        composable("calendar") { BillCalendarScreen() }
        composable("settings") { 
            // Better to use a ViewModel here, but for brevity using direct injection check
            val settingsManager = hiltViewModel<com.slmoney.app.ui.dashboard.DashboardViewModel>().run { 
                // We'll need to provide it via ViewModel or local inject
            }
            SettingsScreen(onBack = { navController.popBackStack() }, settingsManager = hiltViewModel<com.slmoney.app.ui.dashboard.SettingsViewModel>().settingsManager)
        }
        composable("manual_entry") { 
            com.slmoney.app.ui.entry.ManualEntryScreen(onBack = { navController.popBackStack() }) 
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
                    label = { Text("Home") },
                    selected = currentRoute == "dashboard",
                    onClick = { navController.navigate("dashboard") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Insights, contentDescription = null) },
                    label = { Text("Insights") },
                    selected = currentRoute == "analytics",
                    onClick = { navController.navigate("analytics") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                    label = { Text("Bills") },
                    selected = currentRoute == "calendar",
                    onClick = { navController.navigate("calendar") }
                )
            }
        }
    ) { innerPadding ->
        MainNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}
