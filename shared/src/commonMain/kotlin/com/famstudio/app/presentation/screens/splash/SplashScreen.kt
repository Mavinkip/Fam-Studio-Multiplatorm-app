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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.famstudio.app.presentation.navigation.Screen
import com.famstudio.app.presentation.theme.FamColors
import kotlinx.coroutines.delay

// ── letter-by-letter reveal ───────────────────────────────────────────────
private const val FULL_TEXT   = "FAM STUDIO"
private const val LETTER_DELAY_MS = 120L   // gap between each letter appearing
private const val HOLD_MS         = 800L   // pause after full word shown
private const val NAV_DELAY_MS    = 300L   // fade-out buffer before navigating

@Composable
fun SplashScreen(
    navController: NavHostController,
    isLoggedIn: () -> Boolean = { false }   // swap for real auth check later
) {
    // How many letters are currently visible
    var visibleCount by remember { mutableStateOf(0) }

    // Tagline fade
    val taglineAlpha by animateFloatAsState(
        targetValue  = if (visibleCount == FULL_TEXT.length) 1f else 0f,
        animationSpec = tween(600),
        label        = "tagline"
    )

    // Dot pulse (three dots under text while waiting)
    val infiniteTransition = rememberInfiniteTransition(label = "dots")
    val dot1 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "d1"
    )
    val dot2 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 200), RepeatMode.Reverse),
        label = "d2"
    )
    val dot3 by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600, delayMillis = 400), RepeatMode.Reverse),
        label = "d3"
    )

    // Drive the letter reveal + navigation
    LaunchedEffect(Unit) {
        // Reveal letters one by one
        repeat(FULL_TEXT.length) {
            delay(LETTER_DELAY_MS)
            visibleCount++
        }
        delay(HOLD_MS)
        delay(NAV_DELAY_MS)

        // Navigate based on auth state
        val destination = if (isLoggedIn()) Screen.Home.route else Screen.Login.route
        navController.navigate(destination) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    // ── UI ────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Letter-by-letter title ────────────────────────────────────
            Row {
                FULL_TEXT.forEachIndexed { index, char ->
                    val visible = index < visibleCount
                    val alpha by animateFloatAsState(
                        targetValue  = if (visible) 1f else 0f,
                        animationSpec = tween(200),
                        label        = "letter_$index"
                    )
                    Text(
                        text      = char.toString(),
                        fontSize  = 42.sp,
                        fontWeight= FontWeight.Bold,
                        color     = if (char == ' ') Color.Transparent
                        else FamColors.PinterestRed,
                        modifier  = Modifier.alpha(alpha)
                    )
                }
            }

            // ── Tagline fades in after full word ─────────────────────────
            Text(
                text      = "where art finds its home",
                style     = MaterialTheme.typography.bodyMedium,
                color     = FamColors.TextMuted,
                textAlign = TextAlign.Center,
                modifier  = Modifier
                    .alpha(taglineAlpha)
                    .padding(horizontal = 32.dp)
            )

            Spacer(Modifier.height(48.dp))

            // ── Animated dots ────────────────────────────────────────────
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(dot1, dot2, dot3).forEach { a ->
                    Box(
                        Modifier
                            .size(8.dp)
                            .alpha(a)
                            .background(FamColors.PinterestRed, shape = androidx.compose.foundation.shape.CircleShape)
                    )
                }
            }
        }
    }
}