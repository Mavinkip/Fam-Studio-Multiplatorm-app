package com.famstudio.app.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil3.compose.AsyncImage
import com.famstudio.app.presentation.theme.FamColors

// ── Nav items — using emoji fallback until SVG resources are wired ─────────
// SVG files must be in: composeApp/src/commonMain/composeResources/drawable/
// named: ic_home.svg, ic_search.svg, ic_cart.svg, ic_events.svg, ic_profile.svg

data class NavItem(val route: String, val icon: String, val label: String)

private val NAV_ITEMS = listOf(
    NavItem(Screen.Home.route,    "⌂",  "Home"),
    NavItem(Screen.Search.route,  "⊙",  "Search"),
    NavItem(Screen.Cart.route,    "⊞",  "Cart"),
    NavItem(Screen.Events.route,  "◈",  "Events"),
    NavItem(Screen.Profile.route, "◉",  "Profile"),
)

private val NO_NAV_PREFIXES = setOf(
    Screen.Splash.route,
    Screen.Login.route,
    Screen.Register.route,
    Screen.ForgotPass.route,
    Screen.ArtDetail.route,
)

object NavBarState {
    var isVisible by mutableStateOf(true)
}

@Composable
fun MainScaffold(
    navController: NavHostController,
    content:       @Composable (Modifier) -> Unit
) {
    val isDark       = isSystemInDarkTheme()
    val backStack    by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    val onNavScreen  = currentRoute != null &&
            NO_NAV_PREFIXES.none { currentRoute.startsWith(it.substringBefore("{")) }

    LaunchedEffect(currentRoute) {
        if (onNavScreen) NavBarState.isVisible = true
    }

    val showBar = onNavScreen && NavBarState.isVisible

    Box(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {

        content(Modifier)

        AnimatedVisibility(
            visible  = showBar,
            enter    = slideInVertically(initialOffsetY = { it }) + fadeIn(tween(200)),
            exit     = slideOutVertically(targetOffsetY = { it }) + fadeOut(tween(200)),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavBar(
                currentRoute  = currentRoute,
                navController = navController,
                isDark        = isDark
            )
        }
    }
}

@Composable
private fun BottomNavBar(
    currentRoute:  String?,
    navController: NavHostController,
    isDark:        Boolean
) {
    val bgColor      = if (isDark) Color(0xFF121212) else Color.White
    val dividerColor = if (isDark) Color(0xFF2A2A2A) else Color(0xFFEEEEEE)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
    ) {
        HorizontalDivider(color = dividerColor, thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            NAV_ITEMS.forEach { item ->
                val isSelected = currentRoute == item.route
                NavBarItem(
                    item       = item,
                    isSelected = isSelected,
                    isDark     = isDark,
                    onClick    = {
                        if (!isSelected) {
                            navController.navigate(item.route) {
                                popUpTo(Screen.Home.route) { saveState = true }
                                launchSingleTop = true
                                restoreState    = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun NavBarItem(
    item:       NavItem,
    isSelected: Boolean,
    isDark:     Boolean,
    onClick:    () -> Unit
) {
    val selectedColor   = FamColors.PinterestRed
    val unselectedColor = if (isDark) Color(0xFF888888) else Color(0xFF666666)
    val iconColor       = if (isSelected) selectedColor else unselectedColor

    val scaleAnim by animateFloatAsState(
        targetValue   = if (isSelected) 1.1f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label         = "scale"
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Box(
            modifier         = Modifier.size(28.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text     = item.icon,
                fontSize = (22 * scaleAnim).sp,
                color    = iconColor
            )
        }
        Text(
            text       = item.label,
            fontSize   = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color      = iconColor
        )
        // Selected dot indicator
        Box(
            modifier = Modifier
                .size(width = 16.dp, height = 3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(
                    if (isSelected) selectedColor else Color.Transparent
                )
        )
    }
}