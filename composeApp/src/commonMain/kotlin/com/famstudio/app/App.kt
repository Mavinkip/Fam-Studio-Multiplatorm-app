package com.famstudio.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.famstudio.app.presentation.navigation.AppNavHost
import com.famstudio.app.presentation.theme.FamTheme

@Composable
fun App() {
    // FamTheme reads isSystemInDarkTheme() automatically
    // Background is set by MaterialTheme.colorScheme.background
    // No hardcoded white — adapts to dark/light
    FamTheme {
        val navController = rememberNavController()
        AppNavHost(navController = navController)
    }
}