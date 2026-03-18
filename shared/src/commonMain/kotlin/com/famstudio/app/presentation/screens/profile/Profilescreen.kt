package com.famstudio.app.presentation.screens.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import com.famstudio.app.presentation.theme.FamColors

data class OrderRecord(
    val id: String, val artTitle: String, val artistName: String,
    val imageUrl: String, val style: String, val size: String, val status: String
)

private val FAKE_ORDERS = listOf(
    OrderRecord("o1", "Whispers of Dawn", "Amara Osei",
        "https://picsum.photos/seed/art1/400/560", "Oil Painting", "50×70 cm", "In Progress"),
    OrderRecord("o2", "Urban Pulse", "Nia Kamara",
        "https://picsum.photos/seed/art3/400/500", "Acrylic", "45×55 cm", "Pending"),
)

@Composable
fun ProfileScreen(navController: NavHostController) {
    val colorScheme = MaterialTheme.colorScheme
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Profile", "Orders", "Cart")

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
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
            indicator      = { tabPositions ->
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
            1 -> OrdersTab(colorScheme)
            2 -> CartTab(navController, colorScheme)
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

@Composable
private fun OrdersTab(colorScheme: ColorScheme) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        .padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (FAKE_ORDERS.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No orders yet", color = colorScheme.onBackground.copy(0.5f))
            }
        } else {
            FAKE_ORDERS.forEach { order -> OrderCard(order, colorScheme) }
        }
    }
}

@Composable
private fun OrderCard(order: OrderRecord, colorScheme: ColorScheme) {
    Card(shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(0.5.dp, colorScheme.outlineVariant)) {
        Row(modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(order.imageUrl).build(),
                contentDescription = null, contentScale = ContentScale.Crop,
                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(10.dp))
                    .background(FamColors.SurfaceVariant)
            )
            Column(modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(order.artTitle, fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onBackground)
                Text(order.artistName, fontSize = 13.sp,
                    color = colorScheme.onBackground.copy(0.6f))
                Text("${order.style} · ${order.size}", fontSize = 12.sp,
                    color = colorScheme.onBackground.copy(0.5f))
                Box(modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    .background(when(order.status) {
                        "Complete"    -> Color(0xFF2E7D32)
                        "In Progress" -> Color(0xFFE65100)
                        else          -> colorScheme.outlineVariant
                    }).padding(horizontal = 8.dp, vertical = 3.dp)) {
                    Text(order.status, fontSize = 11.sp, color = Color.White,
                        fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun CartTab(navController: NavHostController, colorScheme: ColorScheme) {
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