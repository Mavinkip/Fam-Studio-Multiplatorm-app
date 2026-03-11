package com.famstudio.app.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.famstudio.app.presentation.theme.FamTheme
import com.famstudio.app.presentation.theme.FamColors
import androidx.compose.foundation.layout.Box

@Composable
fun SplashScreenPlaceholder(navController: NavHostController) {
    FamTheme {
        Box(
            Modifier.fillMaxSize().background(FamColors.Background),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "FAM Studio",
                    style = MaterialTheme.typography.displaySmall,
                    color = FamColors.PinterestRed  // Using PinterestRed from your theme
                )
                CircularProgressIndicator(color = FamColors.PinterestRed)
                TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                    Text("→ Go to Login", color = FamColors.TextMuted)
                }
            }
        }
    }
}

@Composable
fun OnboardingScreenPlaceholder(navController: NavHostController) {
    FamTheme {
        PlaceholderScreen("Onboarding") {
            navController.navigate(Screen.Login.route)
        }
    }
}

@Composable
fun LoginScreenPlaceholder(navController: NavHostController) {
    FamTheme {
        PlaceholderScreen("Login Screen") {
            navController.navigate(Screen.Home.route)
        }
    }
}

@Composable
fun RegisterScreenPlaceholder(navController: NavHostController) {
    FamTheme {
        PlaceholderScreen("Register Screen") {
            navController.navigate(Screen.Home.route)
        }
    }
}

@Composable
fun HomeScreenPlaceholder(navController: NavHostController) {
    FamTheme {
        PlaceholderScreen("Home — Art Feed") {}
    }
}

@Composable
private fun PlaceholderScreen(name: String, onNext: () -> Unit) {
    Box(
        Modifier.fillMaxSize().background(FamColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground  // Using theme's onBackground
            )
            Text(
                "Placeholder — build here",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)  // Using theme with alpha
            )
            TextButton(
                onClick = onNext,
                enabled = onNext != {}  // Only enable if there's a next action
            ) {
                Text(
                    "Continue →",
                    color = MaterialTheme.colorScheme.primary  // Using theme's primary color (PinterestRed)
                )
            }
        }
    }
}