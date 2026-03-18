package com.famstudio.app.presentation.screens.order

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.famstudio.app.presentation.screens.cart.OrderItem
import com.famstudio.app.presentation.theme.FamColors


private val ART_STYLES = listOf(
    "Oil Painting","Acrylic","Watercolour","Sketch / Pencil",
    "String Art","Digital Art","Mixed Media","Charcoal","Pastel","Other"
)

// Suggested prices per style (KES)
private val STYLE_PRICES = mapOf(
    "Oil Painting" to 15000L, "Acrylic" to 10000L, "Watercolour" to 8000L,
    "Sketch / Pencil" to 5000L, "String Art" to 12000L, "Digital Art" to 7000L,
    "Mixed Media" to 11000L, "Charcoal" to 6000L, "Pastel" to 7500L, "Other" to 8000L
)

@Composable
fun OrderFlowScreen(artworkId: String, navController: NavHostController) {
    val artist = remember { FAKE_ARTISTS_PUBLIC["a1"]!! }
    var step          by remember { mutableIntStateOf(1) }
    var selectedStyle by remember { mutableStateOf("") }
    var width         by remember { mutableStateOf("") }
    var height        by remember { mutableStateOf("") }
    var unit          by remember { mutableStateOf("cm") }
    var extraDetails  by remember { mutableStateOf("") }
    val colorScheme   = MaterialTheme.colorScheme

    val estimatedTotal = STYLE_PRICES[selectedStyle] ?: 8000L
    val deposit        = estimatedTotal / 2

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
        // Header
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp))
                .background(colorScheme.surfaceVariant)
                .clickable {
                    if (step > 1) step--
                    else navController.navigate(Screen.Home.route) {
                        popUpTo(0) { inclusive = true }; launchSingleTop = true
                    }
                }, contentAlignment = Alignment.Center) {
                Text("←", fontSize = 18.sp, color = colorScheme.onBackground)
            }
            Spacer(Modifier.width(12.dp))
            Text(when(step) { 1 -> "Artist"; 2 -> "Order Details"; else -> "Review & Pay" },
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = colorScheme.onBackground)
            Spacer(Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                (1..3).forEach { s ->
                    Box(modifier = Modifier
                        .size(if (s == step) 24.dp else 8.dp, 8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (s == step) FamColors.PinterestRed
                        else colorScheme.outlineVariant))
                }
            }
        }

        when (step) {
            1 -> ArtistStep(artist, onNext = { step = 2 })
            2 -> DetailsStep(
                selectedStyle, { selectedStyle = it },
                width, { width = it }, height, { height = it },
                unit, { unit = it }, extraDetails, { extraDetails = it },
                estimatedTotal, deposit,
                onNext = { step = 3 }
            )
            3 -> ReviewStep(
                artist, selectedStyle, width, height, unit, extraDetails,
                estimatedTotal, deposit,
                onConfirm = {
                    // Add to orders
                    val orderId = "ord_${selectedStyle.take(3)}_${(100000..999999).random()}"
                    CartState.addOrder(OrderItem(
                        id           = orderId,
                        artTitle     = "Custom ${selectedStyle}",
                        artistName   = artist.name,
                        imageUrl     = artist.avatar,
                        style        = selectedStyle,
                        size         = "$width × $height $unit",
                        totalPrice   = estimatedTotal,
                        depositPaid  = 0L,
                        status       = "Pending"
                    ))
                    // Go to checkout for deposit
                    navController.navigate(Screen.CheckoutDeposit.createRoute(orderId)) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                }
            )
        }
    }
}

@Composable
private fun ArtistStep(artist: ArtistInfo, onNext: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(artist.avatar).build(),
                contentDescription = artist.name, contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp).clip(CircleShape).background(FamColors.SurfaceVariant)
            )
            Column {
                Text(artist.name, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground)
                Text(artist.location, fontSize = 13.sp, color = colorScheme.onBackground.copy(0.55f))
            }
        }
        Text(artist.bio, fontSize = 14.sp, lineHeight = 22.sp,
            color = colorScheme.onBackground.copy(0.75f))
        HorizontalDivider(color = colorScheme.outlineVariant)
        listOf("✉  ${artist.email}", "📷  ${artist.instagram}", "📞  ${artist.phone}").forEach {
            Text(it, fontSize = 14.sp, color = colorScheme.onBackground)
        }
        HorizontalDivider(color = colorScheme.outlineVariant)
        Text("This artist accepts custom orders. Continue to specify your requirements.",
            fontSize = 13.sp, color = colorScheme.onBackground.copy(0.55f))
        Button(onClick = onNext, modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FamColors.PinterestRed)) {
            Text("Continue to Order Details", fontWeight = FontWeight.SemiBold, color = Color.White)
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun DetailsStep(
    selectedStyle: String, onStyle: (String) -> Unit,
    width: String, onWidth: (String) -> Unit,
    height: String, onHeight: (String) -> Unit,
    unit: String, onUnit: (String) -> Unit,
    details: String, onDetails: (String) -> Unit,
    estimatedTotal: Long, deposit: Long,
    onNext: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // Reference image upload
        Box(modifier = Modifier.fillMaxWidth().height(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(BorderStroke(1.5.dp, colorScheme.outlineVariant), RoundedCornerShape(16.dp))
            .background(colorScheme.surfaceVariant).clickable {},
            contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("📎", fontSize = 28.sp)
                Text("Upload Reference Image (Optional)", fontSize = 13.sp,
                    color = colorScheme.onBackground.copy(0.55f))
                Text("Tap to pick from gallery", fontSize = 11.sp,
                    color = colorScheme.onBackground.copy(0.35f))
            }
        }

        // Style picker
        Text("Art Style", fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
            color = colorScheme.onBackground)
        ART_STYLES.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { style ->
                    val sel = style == selectedStyle
                    Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))
                        .border(BorderStroke(if (sel) 1.5.dp else 1.dp,
                            if (sel) FamColors.PinterestRed else colorScheme.outlineVariant),
                            RoundedCornerShape(20.dp))
                        .background(if (sel) FamColors.PinterestRed.copy(0.08f)
                        else colorScheme.background)
                        .clickable { onStyle(style) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)) {
                        Text(style, fontSize = 12.sp,
                            color = if (sel) FamColors.PinterestRed
                            else colorScheme.onBackground.copy(0.7f))
                    }
                }
            }
        }

        // Price estimate
        if (selectedStyle.isNotBlank()) {
            Card(shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FamColors.PinterestRed.copy(alpha = 0.07f))) {
                Row(modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Estimated total", fontSize = 12.sp,
                            color = FamColors.TextMuted)
                        Text("KES $estimatedTotal", fontSize = 18.sp,
                            fontWeight = FontWeight.Bold, color = FamColors.PinterestRed)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Deposit (50%)", fontSize = 12.sp, color = FamColors.TextMuted)
                        Text("KES $deposit", fontSize = 18.sp,
                            fontWeight = FontWeight.Bold, color = FamColors.TextPrimary)
                    }
                }
            }
        }

        // Dimensions
        Text("Dimensions", fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
            color = colorScheme.onBackground)
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(value = width, onValueChange = onWidth,
                label = { Text("Width") }, modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FamColors.PinterestRed,
                    focusedLabelColor  = FamColors.PinterestRed))
            Text("×", fontSize = 18.sp)
            OutlinedTextField(value = height, onValueChange = onHeight,
                label = { Text("Height") }, modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FamColors.PinterestRed,
                    focusedLabelColor  = FamColors.PinterestRed))
            Row(modifier = Modifier.clip(RoundedCornerShape(10.dp))
                .border(BorderStroke(1.dp, colorScheme.outlineVariant), RoundedCornerShape(10.dp))) {
                listOf("cm","in").forEach { u ->
                    Box(modifier = Modifier
                        .background(if (u == unit) FamColors.PinterestRed else colorScheme.background)
                        .clickable { onUnit(u) }
                        .padding(horizontal = 10.dp, vertical = 8.dp)) {
                        Text(u, fontSize = 13.sp,
                            color = if (u == unit) Color.White else colorScheme.onBackground)
                    }
                }
            }
        }

        // Extra details
        Text("Additional Details", fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
            color = colorScheme.onBackground)
        OutlinedTextField(value = details, onValueChange = onDetails,
            label = { Text("Describe your vision, colors, mood, special requests...") },
            modifier = Modifier.fillMaxWidth().height(130.dp),
            shape = RoundedCornerShape(12.dp), maxLines = 6,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FamColors.PinterestRed,
                focusedLabelColor  = FamColors.PinterestRed))

        Button(onClick = onNext, modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(14.dp),
            enabled = selectedStyle.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = FamColors.PinterestRed)) {
            Text("Review Order", fontWeight = FontWeight.SemiBold, color = Color.White)
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ReviewStep(
    artist: ArtistInfo, style: String, width: String, height: String,
    unit: String, details: String, total: Long, deposit: Long,
    onConfirm: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text("Review your order", fontSize = 15.sp, color = colorScheme.onBackground.copy(0.6f))

        Card(shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
            border = BorderStroke(0.5.dp, colorScheme.outlineVariant)) {
            Column(modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(artist.avatar).build(),
                        contentDescription = null, contentScale = ContentScale.Crop,
                        modifier = Modifier.size(40.dp).clip(CircleShape))
                    Column {
                        Text("Artist", fontSize = 11.sp, color = colorScheme.onBackground.copy(0.5f))
                        Text(artist.name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                            color = colorScheme.onBackground)
                    }
                }
                HorizontalDivider(color = colorScheme.outlineVariant)
                ReviewRow("Style",   style,                  colorScheme)
                ReviewRow("Size",    "$width × $height $unit", colorScheme)
                if (details.isNotBlank()) ReviewRow("Details", details, colorScheme)
                HorizontalDivider(color = colorScheme.outlineVariant)
                ReviewRow("Total",   "KES $total",           colorScheme)
                ReviewRow("Deposit due now", "KES $deposit", colorScheme)
                Text("Remaining KES ${total - deposit} due when artist confirms completion.",
                    fontSize = 12.sp, color = colorScheme.onBackground.copy(0.45f))
            }
        }

        Text("After placing, you will be directed to pay the 50% deposit. The artist will then begin work and contact you via email.",
            fontSize = 13.sp, color = colorScheme.onBackground.copy(0.55f), lineHeight = 20.sp)

        Button(onClick = onConfirm, modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = FamColors.PinterestRed)) {
            Text("Place Order & Pay Deposit", fontSize = 16.sp,
                fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun ReviewRow(label: String, value: String, colorScheme: ColorScheme) {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        Text(label, fontSize = 13.sp, color = colorScheme.onBackground.copy(0.5f))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium,
            color = colorScheme.onBackground)
    }
}