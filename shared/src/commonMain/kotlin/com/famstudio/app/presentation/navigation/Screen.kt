package com.famstudio.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.famstudio.app.presentation.screens.splash.SplashScreen

// ── All screens ────────────────────────────────────────────────────────────
sealed class Screen(val route: String) {
    data object Splash       : Screen("splash")
    data object Onboarding   : Screen("onboarding")
    data object Login        : Screen("login")
    data object Register     : Screen("register")
    data object ForgotPass   : Screen("forgot_password")
    data object Home         : Screen("home")
    data object ArtDetail    : Screen("art_detail/{artworkId}") {
        fun createRoute(id: String) = "art_detail/$id"
    }
    data object SavedArt     : Screen("saved_art")
    data object ClientOrders : Screen("client_orders")
    data object OrderDetail  : Screen("order_detail/{orderId}") {
        fun createRoute(id: String) = "order_detail/$id"
    }
    data object ArtistDashboard   : Screen("artist_dashboard")
    data object MyArtworks        : Screen("my_artworks")
    data object UploadArtwork     : Screen("upload_artwork")
    data object ArtistOrders      : Screen("artist_orders")
    data object ArtistProfile     : Screen("artist_profile/{artistId}") {
        fun createRoute(id: String) = "artist_profile/$id"
    }
    data object Wallet        : Screen("wallet")
    data object Events        : Screen("events")
    data object Notifications : Screen("notifications")
    data object Profile       : Screen("profile")
    data object Settings      : Screen("settings")
    data object AdminDashboard    : Screen("admin_dashboard")
    data object UserManagement    : Screen("user_management")
    data object ContentManagement : Screen("content_management")
}

