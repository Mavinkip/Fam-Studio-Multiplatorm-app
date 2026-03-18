package com.famstudio.app.presentation.screens.cart

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.famstudio.app.presentation.navigation.Screen
import com.famstudio.app.presentation.theme.FamColors

// ── Data models ───────────────────────────────────────────────────────────
data class CartItem(
    val id: String, val title: String, val artistName: String,
    val artistId: String = "a1", val medium: String,
    val imageUrl: String, val price: Long, val currency: String = "KES"
)

data class OrderItem(
    val id: String, val artTitle: String, val artistName: String,
    val imageUrl: String, val style: String, val size: String,
    val totalPrice: Long, val depositPaid: Long = 0L,
    val status: String = "Pending",   // Pending | In Progress | Done - Awaiting Confirmation | Complete
    val currency: String = "KES"
)

// ── Global state ──────────────────────────────────────────────────────────
object CartState {
    val items  = mutableStateListOf<CartItem>()
    val orders = mutableStateListOf<OrderItem>()

    fun addToCart(item: CartItem) { if (items.none { it.id == item.id }) items.add(item) }
    fun removeFromCart(id: String) { items.removeAll { it.id == id } }
    fun cartTotal(): Long = items.sumOf { it.price }

    fun addOrder(order: OrderItem) { if (orders.none { it.id == order.id }) orders.add(order) }
}

// ── Cart Screen ───────────────────────────────────────────────────────────
@Composable
fun CartScreen(navController: NavHostController) {
    val colorScheme = MaterialTheme.colorScheme
    var selectedTab  by remember { mutableIntStateOf(0) }
    var previewItem  by remember { mutableStateOf<CartItem?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {

        // Header
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp))
                .background(colorScheme.surfaceVariant)
                .clickable {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }, contentAlignment = Alignment.Center) {
                Text("←", fontSize = 18.sp, color = colorScheme.onBackground)
            }
            Spacer(Modifier.width(12.dp))
            Text("My Cart", fontSize = 24.sp, fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground)
        }

        // Tabs
        TabRow(selectedTabIndex = selectedTab,
            containerColor = colorScheme.background,
            contentColor   = FamColors.PinterestRed,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color    = FamColors.PinterestRed)
            }) {
            listOf("Cart (${CartState.items.size})", "Orders (${CartState.orders.size})").forEachIndexed { i, title ->
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
            0 -> CartTab(navController, colorScheme, onPreview = { previewItem = it })
            1 -> OrdersTab(colorScheme)
        }
    }

    previewItem?.let { ImagePreviewDialog(it) { previewItem = null } }
}

// ── Cart tab ──────────────────────────────────────────────────────────────
@Composable
private fun CartTab(
    navController: NavHostController,
    colorScheme:   ColorScheme,
    onPreview:     (CartItem) -> Unit
) {
    val items = CartState.items
    if (items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("🛒", fontSize = 48.sp)
                Text("Your cart is empty", fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold, color = colorScheme.onBackground)
                Button(onClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }; launchSingleTop = true
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = FamColors.PinterestRed)) {
                    Text("Browse Art", color = Color.White)
                }
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items.forEach { item ->
                    CartItemCard(item = item,
                        onRemove  = { CartState.removeFromCart(item.id) },
                        onPreview = { onPreview(item) })
                }
                Spacer(Modifier.height(8.dp))
            }
            // Summary
            Column(modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(colorScheme.surface).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("Shipping", fontSize = 14.sp, color = colorScheme.onBackground.copy(0.6f))
                    Text("Included", fontSize = 14.sp, color = colorScheme.onBackground.copy(0.6f))
                }
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("TOTAL", fontSize = 15.sp, fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground)
                    Text("KES ${CartState.cartTotal()}", fontSize = 18.sp,
                        fontWeight = FontWeight.Bold, color = FamColors.PinterestRed)
                }
                Button(
                    onClick  = { navController.navigate(Screen.Checkout.route) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = FamColors.PinterestRed)
                ) { Text("Checkout", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White) }
            }
        }
    }
}

// ── Orders tab ────────────────────────────────────────────────────────────
@Composable
fun OrdersTab(colorScheme: ColorScheme) {
    val orders = CartState.orders
    if (orders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("🎨", fontSize = 48.sp)
                Text("No custom orders yet", fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold, color = colorScheme.onBackground)
                Text("Order a custom artwork from an artist", fontSize = 13.sp,
                    color = colorScheme.onBackground.copy(0.5f))
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            orders.forEach { order -> OrderCard(order, colorScheme) }
        }
    }
}

@Composable
fun OrderCard(order: OrderItem, colorScheme: ColorScheme) {
    val statusColor = when (order.status) {
        "Complete"                   -> Color(0xFF2E7D32)
        "In Progress"                -> Color(0xFFE65100)
        "Done - Awaiting Confirmation" -> Color(0xFF1565C0)
        else                         -> colorScheme.outlineVariant
    }
    Card(shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(0.5.dp, colorScheme.outlineVariant)) {
        Row(modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(order.imageUrl).build(),
                contentDescription = null, contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(10.dp))
                    .background(FamColors.SurfaceVariant)
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(order.artTitle, fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onBackground)
                Text(order.artistName, fontSize = 13.sp, color = colorScheme.onBackground.copy(0.6f))
                Text("${order.style} · ${order.size}", fontSize = 12.sp,
                    color = colorScheme.onBackground.copy(0.5f))
                Spacer(Modifier.height(4.dp))
                // Payment progress
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text("Deposit paid", fontSize = 11.sp, color = colorScheme.onBackground.copy(0.45f))
                        Text("KES ${order.depositPaid}", fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold, color = FamColors.PinterestRed)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Total", fontSize = 11.sp, color = colorScheme.onBackground.copy(0.45f))
                        Text("KES ${order.totalPrice}", fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold, color = colorScheme.onBackground)
                    }
                }
                Spacer(Modifier.height(2.dp))
                Box(modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    .background(statusColor).padding(horizontal = 8.dp, vertical = 3.dp)) {
                    Text(order.status, fontSize = 11.sp, color = Color.White,
                        fontWeight = FontWeight.SemiBold)
                }
                // Confirm completion button
                if (order.status == "Done - Awaiting Confirmation") {
                    Spacer(Modifier.height(4.dp))
                    Button(onClick = { /* TODO: mark complete, trigger final payment */ },
                        modifier = Modifier.fillMaxWidth().height(36.dp),
                        shape    = RoundedCornerShape(10.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))) {
                        Text("Confirm & Pay Remaining KES ${order.totalPrice - order.depositPaid}",
                            fontSize = 12.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemCard(item: CartItem, onRemove: () -> Unit, onPreview: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    Card(shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp),
        border = BorderStroke(0.5.dp, colorScheme.outlineVariant)) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(item.imageUrl).build(),
                contentDescription = item.title, contentScale = ContentScale.Crop,
                modifier = Modifier.size(width = 100.dp, height = 120.dp)
                    .clip(RoundedCornerShape(10.dp)).background(FamColors.SurfaceVariant)
                    .clickable { onPreview() }
            )
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(item.title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onBackground)
                Text(item.artistName, fontSize = 13.sp, color = colorScheme.onBackground.copy(0.6f))
                Text(item.medium, fontSize = 12.sp, color = FamColors.PinterestRed)
                Spacer(Modifier.height(6.dp))
                Text("ARTWORK TOTAL", fontSize = 10.sp, fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp, color = colorScheme.onBackground.copy(0.45f))
                Text("${item.currency} ${item.price}", fontSize = 16.sp,
                    fontWeight = FontWeight.Bold, color = colorScheme.onBackground)
                // ← "Order Custom" button REMOVED
            }
            Box(modifier = Modifier.size(28.dp).clip(RoundedCornerShape(8.dp))
                .background(colorScheme.surfaceVariant).clickable(onClick = onRemove),
                contentAlignment = Alignment.Center) {
                Text("✕", fontSize = 12.sp, color = colorScheme.onBackground.copy(0.6f))
            }
        }
    }
}

@Composable
private fun ImagePreviewDialog(item: CartItem, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.88f))
            .clickable { onDismiss() }) {
            Column(modifier = Modifier.align(Alignment.Center).padding(24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
                .clickable(enabled = false) {}) {
                Row(modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.End) {
                    Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onDismiss() }, contentAlignment = Alignment.Center) {
                        Text("✕", fontSize = 14.sp)
                    }
                }
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(item.imageUrl).build(),
                    contentDescription = item.title, contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Column(modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(item.title, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground)
                    Text("${item.artistName}, ${item.medium}", fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.6f))
                    Text("${item.currency} ${item.price}", fontSize = 16.sp,
                        fontWeight = FontWeight.Bold, color = FamColors.PinterestRed)
                }
            }
        }
    }
}