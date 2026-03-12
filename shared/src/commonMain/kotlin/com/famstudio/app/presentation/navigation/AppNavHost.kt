package com.famstudio.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.famstudio.app.presentation.screens.home.HomeScreen
import com.famstudio.app.presentation.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route
) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Splash.route) {
            SplashScreen(
                navController = navController,
                isLoggedIn    = { false }   // TODO: replace with Firebase auth check
            )
        }

        // ── Real screens ──────────────────────────────────────────────
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // ── Placeholders — replace one by one ─────────────────────────
        composable(Screen.Login.route)      { LoginScreenPlaceholder(navController) }
        composable(Screen.Register.route)   { RegisterScreenPlaceholder(navController) }
        composable(Screen.Onboarding.route) { OnboardingScreenPlaceholder(navController) }

        // Detail screens (placeholder until Phase 03)
        composable(Screen.ArtDetail.route)        { HomeScreenPlaceholder(navController) }
        composable(Screen.Wallet.route)           { HomeScreenPlaceholder(navController) }
        composable(Screen.Profile.route)          { HomeScreenPlaceholder(navController) }
        composable(Screen.Settings.route)         { HomeScreenPlaceholder(navController) }
        composable(Screen.ArtistDashboard.route)  { HomeScreenPlaceholder(navController) }
        composable(Screen.AdminDashboard.route)   { HomeScreenPlaceholder(navController) }
    }
}