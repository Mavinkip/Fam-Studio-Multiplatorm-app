package com.famstudio.app.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.famstudio.app.presentation.theme.FamColors
import fam.shared.generated.resources.Res
import fam.shared.generated.resources.ic_cart
import fam.shared.generated.resources.ic_events
import fam.shared.generated.resources.ic_home
import fam.shared.generated.resources.ic_profile
import fam.shared.generated.resources.ic_search
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class NavItem(
    val route:    String,
    val iconRes:  DrawableResource,
    val label:    String
)

private val NAV_ITEMS = listOf(
    NavItem(Screen.Home.route,    Res.drawable.ic_home,    "Home"),
    NavItem(Screen.Search.route,  Res.drawable.ic_search,  "Search"),
    NavItem(Screen.Cart.route,    Res.drawable.ic_cart,    "Cart"),
    NavItem(Screen.Events.route,  Res.drawable.ic_events,  "Events"),
    NavItem(Screen.Profile.route, Res.drawable.ic_profile, "Profile"),
)

private val NO_NAV_PREFIXES = setOf(
    Screen.Splash.route,
    Screen.Login.route,
    Screen.Register.route,
    Screen.ForgotPass.route,
    Screen.ArtDetail.route,
    Screen.OrderFlow.route,
    Screen.Checkout.route,
    Screen.CheckoutDeposit.route,
    Screen.CheckoutBuyNow.route,
    Screen.CheckoutEvent.route,
    Screen.ArtistProfile.route,
    Screen.EventDetail.route,
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

    val onNavScreen = currentRoute != null &&
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
            BottomNavBar(currentRoute, navController, isDark)
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

    Column(modifier = Modifier.fillMaxWidth().background(bgColor)) {
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
                NavBarItem(item, isSelected, isDark, navController)
            }
        }
    }
}

@Composable
private fun NavBarItem(
    item:          NavItem,
    isSelected:    Boolean,
    isDark:        Boolean,
    navController: NavHostController
) {
    val selectedColor   = FamColors.PinterestRed
    val unselectedColor = if (isDark) Color(0xFF888888) else Color(0xFF666666)
    val iconColor       = if (isSelected) selectedColor else unselectedColor

    // Read route HERE inside @Composable — never inside click lambda
    val backStack    by navController.currentBackStackEntryAsState()
    val currentRoute  = backStack?.destination?.route

    val scaleAnim by animateFloatAsState(
        targetValue   = if (isSelected) 1.1f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label         = "scale"
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                if (item.route == Screen.Home.route) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                } else if (currentRoute != item.route) {
                    navController.navigate(item.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState    = true
                    }
                }
            }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter            = painterResource(item.iconRes),
            contentDescription = item.label,
            tint               = iconColor,
            modifier           = Modifier.size((26 * scaleAnim).dp)
        )
        Box(
            modifier = Modifier
                .size(width = 16.dp, height = 3.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(if (isSelected) selectedColor else Color.Transparent)
        )
    }
}