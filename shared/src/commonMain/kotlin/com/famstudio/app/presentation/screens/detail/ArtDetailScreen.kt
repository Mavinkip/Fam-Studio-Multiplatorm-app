package com.famstudio.app.presentation.screens.detail

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import com.famstudio.app.presentation.screens.cart.CartItem
import com.famstudio.app.presentation.screens.cart.CartState
import com.famstudio.app.presentation.theme.FamColors

data class ArtworkDetail(
    val id:           String,
    val imageUrl:     String,
    val title:        String,
    val artistName:   String,
    val artistId:     String = "a1",
    val artistAvatar: String,
    val price:        String,
    val priceRaw:     Long    = 0L,
    val description:  String  = "",
    val medium:       String  = "Oil on Canvas",
    val dimensions:   String  = "60cm × 80cm",
    val year:         String  = "2024",
    val currency:     String  = "KES"
)

private val FAKE_ARTWORKS = mapOf(
    "1"  to ArtworkDetail("1",  "https://picsum.photos/seed/art1/400/560",  "Whispers of Dawn",   "Amara Osei",    "a1", "https://i.pravatar.cc/100?img=1",  "12,500", 12500, "A breathtaking piece that captures the quiet beauty of early morning light.", "Acrylic on Canvas", "50cm × 70cm", "2024"),
    "2"  to ArtworkDetail("2",  "https://picsum.photos/seed/art2/400/600",  "Silent Waters",      "Zuri Mwangi",   "a2", "https://i.pravatar.cc/100?img=2",  "8,000",  8000,  "An exploration of stillness and reflection.", "Watercolour", "40cm × 60cm", "2023"),
    "3"  to ArtworkDetail("3",  "https://picsum.photos/seed/art6/800/400",  "Golden Horizon",     "Kofi Asante",   "a3", "https://i.pravatar.cc/100?img=3",  "22,000", 22000, "A wide panoramic journey across the African savanna at golden hour.", "Oil on Canvas", "100cm × 60cm", "2024"),
    "4"  to ArtworkDetail("4",  "https://picsum.photos/seed/art3/400/500",  "Urban Pulse",        "Nia Kamara",    "a1", "https://i.pravatar.cc/100?img=4",  "6,500",  6500,  "The energy of city life captured in bold strokes.", "Mixed Media", "45cm × 55cm", "2023"),
    "5"  to ArtworkDetail("5",  "https://picsum.photos/seed/art4/400/700",  "Roots & Wings",      "Emeka Diallo",  "a2", "https://i.pravatar.cc/100?img=5",  "18,000", 18000, "A meditation on heritage and freedom.", "Oil on Canvas", "60cm × 90cm", "2024"),
    "8"  to ArtworkDetail("8",  "https://picsum.photos/seed/art20/800/500", "Crimson Dusk",       "Fatima Al-Nur", "a3", "https://i.pravatar.cc/100?img=6",  "35,000", 35000, "The sky ablaze with the last light of day.", "Oil on Canvas", "120cm × 80cm", "2024"),
    "9"  to ArtworkDetail("9",  "https://picsum.photos/seed/art7/600/400",  "City of Echoes",     "Jomo Kariuki",  "a1", "https://i.pravatar.cc/100?img=7",  "14,500", 14500, "Layers of sound and memory in a single urban landscape.", "Acrylic on Canvas", "80cm × 60cm", "2023"),
    "13" to ArtworkDetail("13", "https://picsum.photos/seed/art19/800/450", "Between Two Worlds", "Amara Osei",    "a1", "https://i.pravatar.cc/100?img=1",  "28,000", 28000, "A liminal space between the familiar and the unknown.", "Oil on Canvas", "100cm × 70cm", "2024"),
)

private fun getArtwork(id: String) =
    FAKE_ARTWORKS[id] ?: ArtworkDetail(id, "https://picsum.photos/seed/$id/400/500",
        "Untitled", "Unknown Artist", "a1", "https://i.pravatar.cc/100?img=8", "5,000", 5000)

private data class MoreCard(val id: String, val url: String, val ratio: Float, val wide: Boolean = false)
private val MORE_FEED = listOf(
    MoreCard("m1", "https://picsum.photos/seed/art24/400/560", 400f/560f),
    MoreCard("m2", "https://picsum.photos/seed/art25/400/420", 400f/420f),
    MoreCard("m3", "https://picsum.photos/seed/art26/800/400", 800f/400f, wide = true),
    MoreCard("m4", "https://picsum.photos/seed/art27/400/600", 400f/600f),
    MoreCard("m5", "https://picsum.photos/seed/art28/400/380", 400f/380f),
    MoreCard("m6", "https://picsum.photos/seed/art29/400/500", 400f/500f),
    MoreCard("m7", "https://picsum.photos/seed/art30/400/460", 400f/460f),
    MoreCard("m8", "https://picsum.photos/seed/art31/800/450", 800f/450f, wide = true),
    MoreCard("m9", "https://picsum.photos/seed/art32/400/540", 400f/540f),
    MoreCard("m10","https://picsum.photos/seed/art33/400/400", 1f),
)

@Composable
fun ArtDetailScreen(artworkId: String, navController: NavHostController) {
    val artwork     = remember(artworkId) { getArtwork(artworkId) }
    val scrollState = rememberScrollState()
    val colorScheme = MaterialTheme.colorScheme
    var showDetails by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)
        .verticalScroll(scrollState)) {

        // ── Image ─────────────────────────────────────────────────────
        Box(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(artwork.imageUrl).build(),
                contentDescription = artwork.title,
                contentScale       = ContentScale.FillWidth,
                modifier           = Modifier.fillMaxWidth()
            )
            Box(modifier = Modifier.fillMaxWidth().height(100.dp)
                .background(Brush.verticalGradient(
                    listOf(Color.Black.copy(0.45f), Color.Transparent))))
            Box(modifier = Modifier.padding(16.dp).size(40.dp).clip(CircleShape)
                .background(Color.Black.copy(0.35f))
                .clickable { navController.popBackStack() }
                .align(Alignment.TopStart),
                contentAlignment = Alignment.Center) {
                Text("←", color = Color.White, fontSize = 20.sp)
            }
        }

        // ── Artist row + Details button ───────────────────────────────
        Row(modifier = Modifier.fillMaxWidth().background(colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {

            // ← Artist row is now CLICKABLE → goes to ArtistProfile
            Row(modifier = Modifier.weight(1f).clip(RoundedCornerShape(12.dp))
                .clickable {
                    navController.navigate(Screen.ArtistProfile.createRoute(artwork.artistId))
                }.padding(end = 8.dp),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(artwork.artistAvatar).build(),
                    contentDescription = artwork.artistName,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.size(42.dp).clip(CircleShape)
                        .background(FamColors.SurfaceVariant)
                )
                Column {
                    Text("Artist", fontSize = 11.sp,
                        color = colorScheme.onBackground.copy(alpha = 0.45f))
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(artwork.artistName, fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold, color = colorScheme.onBackground)
                        Text("→", fontSize = 13.sp,
                            color = colorScheme.onBackground.copy(0.4f))
                    }
                }
            }

            OutlinedButton(onClick = { showDetails = true },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.5.dp, FamColors.PinterestRed),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)) {
                Text("Details", color = FamColors.PinterestRed,
                    fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
        }

        HorizontalDivider(color = colorScheme.outlineVariant, thickness = 0.5.dp,
            modifier = Modifier.padding(horizontal = 16.dp))

        Text("More to explore", fontSize = 17.sp, fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 10.dp))

        MasonryGrid(items = MORE_FEED, navController = navController)
        Spacer(Modifier.height(32.dp))
    }

    if (showDetails) {
        DetailsSheet(artwork = artwork, onDismiss = { showDetails = false },
            navController = navController)
    }
}

@Composable
private fun MasonryGrid(items: List<MoreCard>, navController: NavHostController) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.filter { it.wide }.forEach { card ->
            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                .clickable { navController.navigate(Screen.ArtDetail.createRoute(card.id)) }) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(card.url).build(),
                    contentDescription = null, contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().aspectRatio(card.ratio)
                )
            }
        }
        items.filter { !it.wide }.chunked(2).forEach { pair ->
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                pair.forEach { card ->
                    var pressed by remember { mutableStateOf(false) }
                    val scale by animateFloatAsState(if (pressed) 0.97f else 1f,
                        spring(stiffness = Spring.StiffnessMedium), label = "s")
                    Box(modifier = Modifier.weight(1f).scale(scale)
                        .clip(RoundedCornerShape(12.dp))
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = { pressed = true; tryAwaitRelease(); pressed = false },
                                onTap   = { navController.navigate(Screen.ArtDetail.createRoute(card.id)) }
                            )
                        }) {
                        Box(modifier = Modifier.fillMaxWidth().aspectRatio(card.ratio)
                            .background(FamColors.SurfaceVariant))
                        AsyncImage(
                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                .data(card.url).build(),
                            contentDescription = null, contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxWidth().aspectRatio(card.ratio)
                        )
                    }
                }
                if (pair.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun DetailsSheet(
    artwork:       ArtworkDetail,
    onDismiss:     () -> Unit,
    navController: NavHostController
) {
    val colorScheme = MaterialTheme.colorScheme
    var addedToCart by remember { mutableStateOf(CartState.items.any { it.id == artwork.id }) }

    Dialog(onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false,
            dismissOnClickOutside = true)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.5f))
                .clickable { onDismiss() })
            Column(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(colorScheme.background).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)) {

                Box(modifier = Modifier.width(40.dp).height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colorScheme.outlineVariant)
                    .align(Alignment.CenterHorizontally))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(artwork.title, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground, modifier = Modifier.weight(1f))
                    Box(modifier = Modifier.size(32.dp).clip(CircleShape)
                        .background(colorScheme.surfaceVariant).clickable { onDismiss() },
                        contentAlignment = Alignment.Center) {
                        Text("✕", fontSize = 14.sp, color = colorScheme.onBackground)
                    }
                }

                Text(artwork.description, fontSize = 14.sp,
                    color = colorScheme.onBackground.copy(0.7f), lineHeight = 22.sp)
                HorizontalDivider(color = colorScheme.outlineVariant)
                DetailRow("Medium",     artwork.medium,     colorScheme)
                DetailRow("Dimensions", artwork.dimensions, colorScheme)
                DetailRow("Year",       artwork.year,       colorScheme)
                HorizontalDivider(color = colorScheme.outlineVariant)

                Text("${artwork.currency} ${artwork.price}", fontSize = 28.sp,
                    fontWeight = FontWeight.Bold, color = FamColors.PinterestRed)

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = {
                        onDismiss()
                        navController.navigate(Screen.OrderFlow.createRoute(artwork.id))
                    }, modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.5.dp, FamColors.PinterestRed)) {
                        Text("Order", color = FamColors.PinterestRed,
                            fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Button(onClick = {
                        CartState.addToCart(CartItem(
                            id = artwork.id, title = artwork.title,
                            artistName = artwork.artistName, artistId = artwork.artistId,
                            medium = artwork.medium, imageUrl = artwork.imageUrl,
                            price = artwork.priceRaw, currency = artwork.currency
                        ))
                        addedToCart = true
                        onDismiss()
                        // ← Fix: navigate to Cart WITHOUT replacing Home in back stack
                        navController.navigate(Screen.Cart.route) {
                            launchSingleTop = true
                            // Do NOT popUpTo Home — keep back stack intact
                        }
                    }, modifier = Modifier.weight(1f).height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (addedToCart) Color(0xFF2E7D32)
                            else FamColors.PinterestRed,
                            contentColor   = Color.White)) {
                        Text(if (addedToCart) "In Cart ✓" else "Buy Now",
                            fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, colorScheme: ColorScheme) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 13.sp, color = colorScheme.onBackground.copy(0.5f))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium,
            color = colorScheme.onBackground)
    }
}