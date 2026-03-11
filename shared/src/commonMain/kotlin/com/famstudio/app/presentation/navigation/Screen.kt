package com.famstudio.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// ── All screens in the app ─────────────────────────────────────────────────
sealed class Screen(val route: String) {
    // Auth
    data object Splash       : Screen("splash")
    data object Onboarding   : Screen("onboarding")
    data object Login        : Screen("login")
    data object Register     : Screen("register")
    data object ForgotPass   : Screen("forgot_password")

    // Client
    data object Home         : Screen("home")
    data object ArtDetail    : Screen("art_detail/{artworkId}") {
        fun createRoute(artworkId: String) = "art_detail/$artworkId"
    }
    data object SavedArt     : Screen("saved_art")
    data object ClientOrders : Screen("client_orders")
    data object OrderDetail  : Screen("order_detail/{orderId}") {
        fun createRoute(orderId: String) = "order_detail/$orderId"
    }

    // Artist
    data object ArtistDashboard  : Screen("artist_dashboard")
    data object MyArtworks       : Screen("my_artworks")
    data object UploadArtwork    : Screen("upload_artwork")
    data object ArtistOrders     : Screen("artist_orders")
    data object ArtistProfile    : Screen("artist_profile/{artistId}") {
        fun createRoute(artistId: String) = "artist_profile/$artistId"
    }

    // Shared
    data object Wallet       : Screen("wallet")
    data object Events       : Screen("events")
    data object Notifications: Screen("notifications")
    data object Profile      : Screen("profile")
    data object Settings     : Screen("settings")

    // Admin
    data object AdminDashboard   : Screen("admin_dashboard")
    data object UserManagement   : Screen("user_management")
    data object ContentManagement: Screen("content_management")
}

// ── Nav Host ───────────────────────────────────────────────────────────────
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route)        { SplashScreenPlaceholder(navController) }
        composable(Screen.Onboarding.route)    { OnboardingScreenPlaceholder(navController) }
        composable(Screen.Login.route)         { LoginScreenPlaceholder(navController) }
        composable(Screen.Register.route)      { RegisterScreenPlaceholder(navController) }
        composable(Screen.Home.route)          { HomeScreenPlaceholder(navController) }
        composable(Screen.ArtDetail.route)     { HomeScreenPlaceholder(navController) }
        composable(Screen.Wallet.route)        { HomeScreenPlaceholder(navController) }
        composable(Screen.Profile.route)       { HomeScreenPlaceholder(navController) }
        composable(Screen.Settings.route)      { HomeScreenPlaceholder(navController) }
        composable(Screen.ArtistDashboard.route) { HomeScreenPlaceholder(navController) }
        composable(Screen.AdminDashboard.route)  { HomeScreenPlaceholder(navController) }
    }
}
