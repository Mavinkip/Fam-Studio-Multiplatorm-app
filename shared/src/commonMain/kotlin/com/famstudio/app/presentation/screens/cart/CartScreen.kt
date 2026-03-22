package com.famstudio.app.presentation.screens.cart

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
    val id:           String,
    val artTitle:     String,
    val artistName:   String,
    val artistAvatar: String,
    val imageUrl:     String,
    val style:        String,
    val size:         String,
    val extraDetails: String  = "",
    val totalPrice:   Long,
    val depositPaid:  Long    = 0L,
    val status:       String  = "Pending",
    val currency:     String  = "KES"
)

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
    var selectedTab by remember { mutableIntStateOf(0) }
    var previewItem by remember { mutableStateOf<CartItem?>(null) }

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
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
            listOf(
                "Cart (${CartState.items.size})",
                "Orders (${CartState.orders.size})"
            ).forEachIndexed { i, title ->
                Tab(selected = selectedTab == i, onClick = { selectedTab = i },
                    text = {
                        Text(title, fontSize = 14.sp,
                            fontWeight = if (selectedTab == i) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == i) FamColors.PinterestRed
                            else colorScheme.onBackground.copy(0.5f))
                    })
            }
        }

        // Tab content — weight so summary pins to bottom
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> CartTab(navController, colorScheme, onPreview = { previewItem = it })
                1 -> OrdersTab(colorScheme)
            }
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
            // Summary pinned at bottom
            Column(modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                .background(colorScheme.surface)
                .padding(horizontal = 20.dp, vertical = 16.dp),
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
                Button(onClick = { navController.navigate(Screen.Checkout.route) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = FamColors.PinterestRed)) {
                    Text("Checkout", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
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
fun OrderDetailCard(order: OrderItem, colorScheme: ColorScheme) {
    val statusColor = when (order.status) {
        "Complete"                     -> Color(0xFF2E7D32)
        "In Progress"                  -> Color(0xFFE65100)
        "Done - Awaiting Confirmation" -> Color(0xFF1565C0)
        else                           -> Color(0xFF757575)
    }

    Card(shape = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border    = BorderStroke(0.5.dp, colorScheme.outlineVariant)) {
        Column(modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {

            // Artist + artwork header
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(order.artistAvatar).build(),
                    contentDescription = null, contentScale = ContentScale.Crop,
                    modifier = Modifier.size(48.dp).clip(CircleShape)
                        .background(FamColors.SurfaceVariant)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(order.artTitle, fontSize = 16.sp, fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground)
                    Text("by ${order.artistName}", fontSize = 13.sp,
                        color = colorScheme.onBackground.copy(0.6f))
                }
                // Status badge
                Box(modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    .background(statusColor).padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Text(order.status, fontSize = 11.sp, color = Color.White,
                        fontWeight = FontWeight.SemiBold)
                }
            }

            HorizontalDivider(color = colorScheme.outlineVariant)

            // Order details
            OrderRow("Style",   order.style,    colorScheme)
            OrderRow("Size",    order.size,     colorScheme)
            if (order.extraDetails.isNotBlank()) {
                OrderRow("Details", order.extraDetails, colorScheme)
            }

            HorizontalDivider(color = colorScheme.outlineVariant)

            // Payment
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Column {
                    Text("Deposit paid", fontSize = 11.sp,
                        color = colorScheme.onBackground.copy(0.45f))
                    Text("KES ${order.depositPaid}", fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold, color = FamColors.PinterestRed)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Total", fontSize = 11.sp,
                        color = colorScheme.onBackground.copy(0.45f))
                    Text("KES ${order.totalPrice}", fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold, color = colorScheme.onBackground)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Remaining", fontSize = 11.sp,
                        color = colorScheme.onBackground.copy(0.45f))
                    Text("KES ${order.totalPrice - order.depositPaid}", fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onBackground.copy(0.7f))
                }
            }

            // Confirm + pay button when artist marks done
            if (order.status == "Done - Awaiting Confirmation") {
                Button(onClick = { /* TODO: mark complete + final payment */ },
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    shape    = RoundedCornerShape(10.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))) {
                    Text("Confirm & Pay Remaining KES ${order.totalPrice - order.depositPaid}",
                        fontSize = 13.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun OrderRow(label: String, value: String, colorScheme: ColorScheme) {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top) {
        Text(label, fontSize = 13.sp, color = colorScheme.onBackground.copy(0.5f),
            modifier = Modifier.width(70.dp))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium,
            color = colorScheme.onBackground, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun CartItemCard(item: CartItem, onRemove: () -> Unit, onPreview: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    Card(shape = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp),
        border    = BorderStroke(0.5.dp, colorScheme.outlineVariant)) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment     = Alignment.Top) {
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