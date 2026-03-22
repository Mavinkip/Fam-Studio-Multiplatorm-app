package com.famstudio.app.presentation.navigation

sealed class Screen(val route: String) {
    data object Splash            : Screen("splash")
    data object Onboarding        : Screen("onboarding")
    data object Login             : Screen("login")
    data object Register          : Screen("register")
    data object ForgotPass        : Screen("forgot_password")
    data object Home              : Screen("home")
    data object Search            : Screen("search")
    data object Cart              : Screen("cart")
    data object Checkout          : Screen("checkout")
    data object CheckoutDeposit   : Screen("checkout_deposit/{orderId}") {
        fun createRoute(id: String) = "checkout_deposit/$id"
    }
    data object CheckoutBuyNow    : Screen("checkout_buynow/{title}/{price}") {
        fun createRoute(title: String, price: Long) = "checkout_buynow/$title/$price"
    }
    data object CheckoutEvent     : Screen("checkout_event/{eventId}/{tickets}") {
        fun createRoute(eventId: String, tickets: String) = "checkout_event/$eventId/$tickets"
    }
    data object ArtDetail         : Screen("art_detail/{artworkId}") {
        fun createRoute(id: String) = "art_detail/$id"
    }
    data object ArtistProfile     : Screen("artist_profile/{artistId}") {
        fun createRoute(id: String) = "artist_profile/$id"
    }
    data object OrderFlow         : Screen("order_flow/{artworkId}") {
        fun createRoute(id: String) = "order_flow/$id"
    }
    data object EventDetail       : Screen("event_detail/{eventId}") {
        fun createRoute(id: String) = "event_detail/$id"
    }
    data object SavedArt          : Screen("saved_art")
    data object ClientOrders      : Screen("client_orders")
    data object ArtistDashboard   : Screen("artist_dashboard")
    data object MyArtworks        : Screen("my_artworks")
    data object UploadArtwork     : Screen("upload_artwork")
    data object ArtistOrders      : Screen("artist_orders")
    data object Wallet            : Screen("wallet")
    data object Events            : Screen("events")
    data object Notifications     : Screen("notifications")
    data object Profile           : Screen("profile")
    data object EditProfile       : Screen("edit_profile")
    data object Settings          : Screen("settings")
    data object AdminDashboard    : Screen("admin_dashboard")
    data object UserManagement    : Screen("user_management")
    data object ContentManagement : Screen("content_management")
}