package com.famstudio.app.presentation.screens.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.famstudio.app.presentation.navigation.Screen
import com.famstudio.app.presentation.screens.cart.CartState
import com.famstudio.app.presentation.screens.cart.OrderDetailCard
import com.famstudio.app.presentation.theme.FamColors


@Composable
fun ProfileScreen(navController: NavHostController) {
    val colorScheme = MaterialTheme.colorScheme
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Profile", "Orders", "Cart")

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        Column(modifier = Modifier.fillMaxWidth().background(colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data("https://i.pravatar.cc/150?img=10").build(),
                    contentDescription = "Avatar", contentScale = ContentScale.Crop,
                    modifier = Modifier.size(80.dp).clip(CircleShape)
                        .background(FamColors.SurfaceVariant)
                )
                Box(modifier = Modifier.size(24.dp).clip(CircleShape)
                    .background(FamColors.PinterestRed)
                    .align(Alignment.BottomEnd).clickable {},
                    contentAlignment = Alignment.Center) {
                    Text("✎", fontSize = 12.sp, color = Color.White)
                }
            }
            Text("Jane Client", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground)
            Text("jane@example.com", fontSize = 13.sp,
                color = colorScheme.onBackground.copy(0.55f))
            OutlinedButton(onClick = { navController.navigate(Screen.EditProfile.route) },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, colorScheme.outlineVariant)) {
                Text("Edit Profile", fontSize = 13.sp, color = colorScheme.onBackground)
            }
        }

        TabRow(selectedTabIndex = selectedTab,
            containerColor = colorScheme.background,
            contentColor   = FamColors.PinterestRed,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color    = FamColors.PinterestRed)
            }) {
            tabs.forEachIndexed { i, title ->
                Tab(selected = selectedTab == i, onClick = { selectedTab = i },
                    text = {
                        Text(title, fontSize = 14.sp,
                            fontWeight = if (selectedTab == i) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == i) FamColors.PinterestRed
                            else colorScheme.onBackground.copy(0.5f))
                    })
            }
        }

        when (selectedTab) {
            0 -> ProfileTab(colorScheme)
            1 -> ProfileOrdersTab(colorScheme)
            2 -> ProfileCartTab(navController, colorScheme)
        }
    }
}

@Composable
private fun ProfileTab(colorScheme: ColorScheme) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        .padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        ProfileField("Full Name",    "Jane Client",       colorScheme)
        ProfileField("Email",        "jane@example.com",  colorScheme)
        ProfileField("Phone",        "+254 700 000 000",  colorScheme)
        ProfileField("Location",     "Nairobi, Kenya",    colorScheme)
        HorizontalDivider(color = colorScheme.outlineVariant)
        Text("Account", fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
            color = colorScheme.onBackground)
        ProfileField("Role",         "Client",            colorScheme)
        ProfileField("Member Since", "March 2026",        colorScheme)
        HorizontalDivider(color = colorScheme.outlineVariant)
        OutlinedButton(onClick = {},
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape    = RoundedCornerShape(12.dp),
            border   = BorderStroke(1.dp, MaterialTheme.colorScheme.error)) {
            Text("Sign Out", color = MaterialTheme.colorScheme.error, fontSize = 15.sp)
        }
    }
}

// Uses real CartState.orders — same data as Cart screen Orders tab
@Composable
private fun ProfileOrdersTab(colorScheme: ColorScheme) {
    val orders = CartState.orders
    if (orders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("🎨", fontSize = 40.sp)
                Text("No orders yet", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onBackground)
                Text("Order a custom artwork from an artist",
                    fontSize = 13.sp, color = colorScheme.onBackground.copy(0.5f))
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            orders.forEach { order -> OrderDetailCard(order, colorScheme) }
        }
    }
}

@Composable
private fun ProfileCartTab(navController: NavHostController, colorScheme: ColorScheme) {
    val items = CartState.items
    if (items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("🛒", fontSize = 40.sp)
                Text("Cart is empty", color = colorScheme.onBackground.copy(0.5f))
                TextButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                    Text("Go to Cart", color = FamColors.PinterestRed)
                }
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items.forEach { item ->
                Card(shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                    border = BorderStroke(0.5.dp, colorScheme.outlineVariant)) {
                    Row(modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment     = Alignment.CenterVertically) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                .data(item.imageUrl).build(),
                            contentDescription = null, contentScale = ContentScale.Crop,
                            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp))
                                .background(FamColors.SurfaceVariant)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onBackground)
                            Text(item.artistName, fontSize = 12.sp,
                                color = colorScheme.onBackground.copy(0.6f))
                        }
                        // ← Fix 3: use toString() instead of String.format
                        Text("KES ${item.price}", fontSize = 13.sp,
                            fontWeight = FontWeight.Bold, color = FamColors.PinterestRed)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { navController.navigate(Screen.Cart.route) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FamColors.PinterestRed)) {
                Text("View Full Cart", color = Color.White)
            }
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String, colorScheme: ColorScheme) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, fontSize = 11.sp, color = colorScheme.onBackground.copy(0.45f))
        Text(value, fontSize = 15.sp, color = colorScheme.onBackground)
        HorizontalDivider(color = colorScheme.outlineVariant, thickness = 0.5.dp,
            modifier = Modifier.padding(top = 8.dp))
    }
}