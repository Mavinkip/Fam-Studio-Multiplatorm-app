package com.famstudio.app.presentation.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.famstudio.app.data.auth.AuthRepository
import com.famstudio.app.presentation.navigation.Screen
import com.famstudio.app.presentation.theme.FamColors
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

private const val FULL_TEXT       = "FAM STUDIO"
private const val LETTER_DELAY_MS = 120L
private const val HOLD_MS         = 600L

@Composable
fun SplashScreen(navController: NavHostController) {
    // Inject real auth repository — no more hardcoded false
    val authRepo: AuthRepository = koinInject()

    var visibleCount by remember { mutableStateOf(0) }

    val taglineAlpha by animateFloatAsState(
        targetValue   = if (visibleCount == FULL_TEXT.length) 1f else 0f,
        animationSpec = tween(600),
        label         = "tagline"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "dots")
    val dot1 by infiniteTransition.animateFloat(0.2f, 1f,
        infiniteRepeatable(tween(600), RepeatMode.Reverse), label = "d1")
    val dot2 by infiniteTransition.animateFloat(0.2f, 1f,
        infiniteRepeatable(tween(600, delayMillis = 200), RepeatMode.Reverse), label = "d2")
    val dot3 by infiniteTransition.animateFloat(0.2f, 1f,
        infiniteRepeatable(tween(600, delayMillis = 400), RepeatMode.Reverse), label = "d3")

    LaunchedEffect(Unit) {
        repeat(FULL_TEXT.length) { delay(LETTER_DELAY_MS); visibleCount++ }
        delay(HOLD_MS)

        // Check real auth state
        val destination = if (authRepo.isLoggedIn()) Screen.Home.route
        else Screen.Login.route

        navController.navigate(destination) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row {
                FULL_TEXT.forEachIndexed { index, char ->
                    val alpha by animateFloatAsState(
                        targetValue   = if (index < visibleCount) 1f else 0f,
                        animationSpec = tween(200), label = "l$index"
                    )
                    Text(
                        text       = char.toString(),
                        fontSize   = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color      = if (char == ' ') Color.Transparent else FamColors.PinterestRed,
                        modifier   = Modifier.alpha(alpha)
                    )
                }
            }

        }
    }
}