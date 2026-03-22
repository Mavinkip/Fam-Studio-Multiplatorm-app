package com.famstudio.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.famstudio.app.presentation.screens.artist.ArtistProfileScreen
import com.famstudio.app.presentation.screens.auth.ForgotPasswordScreen
import com.famstudio.app.presentation.screens.auth.LoginScreen
import com.famstudio.app.presentation.screens.auth.RegisterScreen
import com.famstudio.app.presentation.screens.cart.CartScreen
import com.famstudio.app.presentation.screens.checkout.CheckoutScreen
import com.famstudio.app.presentation.screens.checkout.CheckoutType
import com.famstudio.app.presentation.screens.detail.ArtDetailScreen
import com.famstudio.app.presentation.screens.events.EventDetailScreen
import com.famstudio.app.presentation.screens.events.EventsScreen
import com.famstudio.app.presentation.screens.home.HomeScreen
import com.famstudio.app.presentation.screens.order.OrderFlowScreen
import com.famstudio.app.presentation.screens.profile.ProfileScreen
import com.famstudio.app.presentation.screens.search.SearchScreen
import com.famstudio.app.presentation.screens.splash.SplashScreen

@Composable
fun AppNavHost(
    navController:    NavHostController = rememberNavController(),
    startDestination: String            = Screen.Splash.route
) {
    MainScaffold(navController = navController) { contentModifier ->
        NavHost(navController = navController, startDestination = startDestination,
            modifier = contentModifier) {

            composable(Screen.Splash.route)     { SplashScreen(navController) }
            composable(Screen.Login.route)      { LoginScreen(navController) }
            composable(Screen.Register.route)   { RegisterScreen(navController) }
            composable(Screen.ForgotPass.route) { ForgotPasswordScreen(navController) }
            composable(Screen.Onboarding.route) { OnboardingScreenPlaceholder(navController) }

            composable(Screen.Home.route)    { HomeScreen(navController) }
            composable(Screen.Search.route)  { SearchScreen(navController) }
            composable(Screen.Cart.route)    { CartScreen(navController) }
            composable(Screen.Events.route)  { EventsScreen(navController) }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
            composable(Screen.EditProfile.route) { HomeScreenPlaceholder(navController) }
            composable(Screen.Wallet.route)  { HomeScreenPlaceholder(navController) }
            composable(Screen.Settings.route){ HomeScreenPlaceholder(navController) }
            composable(Screen.UploadArtwork.route)   { HomeScreenPlaceholder(navController) }
            composable(Screen.ArtistDashboard.route) { HomeScreenPlaceholder(navController) }
            composable(Screen.AdminDashboard.route)  { HomeScreenPlaceholder(navController) }

            // ── Full-screen flows ──────────────────────────────────────
            composable(Screen.Checkout.route) {
                CheckoutScreen(navController = navController, checkoutType = CheckoutType.CART)
            }

            composable(Screen.CheckoutDeposit.route,
                arguments = listOf(navArgument("orderId") { type = NavType.StringType })
            ) { back ->
                CheckoutScreen(
                    navController = navController,
                    checkoutType  = CheckoutType.DEPOSIT,
                    orderId       = back.arguments?.getString("orderId") ?: ""
                )
            }

            composable(Screen.CheckoutBuyNow.route,
                arguments = listOf(
                    navArgument("title") { type = NavType.StringType },
                    navArgument("price") { type = NavType.StringType }
                )
            ) { back ->
                CheckoutScreen(
                    navController = navController,
                    checkoutType  = CheckoutType.BUY_NOW,
                    artworkTitle  = back.arguments?.getString("title") ?: "",
                    artworkPrice  = back.arguments?.getString("price")?.toLongOrNull() ?: 0L
                )
            }

            composable(Screen.CheckoutEvent.route,
                arguments = listOf(
                    navArgument("eventId") { type = NavType.StringType },
                    navArgument("tickets") { type = NavType.StringType }
                )
            ) { back ->
                CheckoutScreen(
                    navController = navController,
                    checkoutType  = CheckoutType.EVENT,
                    eventId       = back.arguments?.getString("eventId") ?: "",
                    ticketCount   = back.arguments?.getString("tickets")?.toIntOrNull() ?: 1
                )
            }

            composable(Screen.ArtDetail.route,
                arguments = listOf(navArgument("artworkId") { type = NavType.StringType })
            ) { back ->
                ArtDetailScreen(back.arguments?.getString("artworkId") ?: "1", navController)
            }

            composable(Screen.ArtistProfile.route,
                arguments = listOf(navArgument("artistId") { type = NavType.StringType })
            ) { back ->
                ArtistProfileScreen(back.arguments?.getString("artistId") ?: "a1", navController)
            }

            composable(Screen.OrderFlow.route,
                arguments = listOf(navArgument("artworkId") { type = NavType.StringType })
            ) { back ->
                OrderFlowScreen(back.arguments?.getString("artworkId") ?: "1", navController)
            }

            composable(Screen.EventDetail.route,
                arguments = listOf(navArgument("eventId") { type = NavType.StringType })
            ) { back ->
                EventDetailScreen(back.arguments?.getString("eventId") ?: "e1", navController)
            }
        }
    }
}