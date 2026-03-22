package com.famstudio.app.presentation.screens.artist

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.famstudio.app.presentation.navigation.Screen
import com.famstudio.app.presentation.screens.order.FAKE_ARTISTS_PUBLIC
import com.famstudio.app.presentation.theme.FamColors

private data class ArtWork(val id: String, val url: String, val ratio: Float)

private val WORKS = listOf(
    ArtWork("1",  "https://picsum.photos/seed/art1/400/560",  400f/560f),
    ArtWork("2",  "https://picsum.photos/seed/art2/400/420",  400f/420f),
    ArtWork("5",  "https://picsum.photos/seed/art4/400/700",  400f/700f),
    ArtWork("9",  "https://picsum.photos/seed/art7/600/400",  600f/400f),
    ArtWork("4",  "https://picsum.photos/seed/art3/400/500",  400f/500f),
    ArtWork("13", "https://picsum.photos/seed/art19/400/300", 400f/300f),
    ArtWork("8",  "https://picsum.photos/seed/art20/400/500", 400f/500f),
    ArtWork("3",  "https://picsum.photos/seed/art6/400/400",  1f),
)

@Composable
fun ArtistProfileScreen(artistId: String, navController: NavHostController) {
    val artist      = FAKE_ARTISTS_PUBLIC[artistId] ?: FAKE_ARTISTS_PUBLIC["a1"]!!
    val colorScheme = MaterialTheme.colorScheme

    LazyVerticalStaggeredGrid(
        columns               = StaggeredGridCells.Fixed(2),
        contentPadding        = PaddingValues(start = 6.dp, end = 6.dp, bottom = 32.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalItemSpacing   = 6.dp,
        modifier              = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        // Status bar spacing
        item(span = StaggeredGridItemSpan.FullLine) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        }
        // ── Profile header (full width) ───────────────────────────────
        item(span = StaggeredGridItemSpan.FullLine) {
            Column(
                modifier            = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Back
                Box(
                    modifier = Modifier.size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(colorScheme.surfaceVariant)
                        .clickable { navController.popBackStack() },
                    contentAlignment = Alignment.Center
                ) { Text("←", fontSize = 18.sp, color = colorScheme.onBackground) }

                // Avatar + name
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AsyncImage(
                        model              = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(artist.avatar).build(),
                        contentDescription = artist.name,
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier.size(80.dp).clip(CircleShape)
                            .background(FamColors.SurfaceVariant)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            artist.name, fontSize = 22.sp,
                            fontWeight = FontWeight.Bold, color = colorScheme.onBackground
                        )
                        Text(
                            artist.location, fontSize = 13.sp,
                            color = colorScheme.onBackground.copy(0.55f)
                        )
                    }
                }

                // Bio
                Text(
                    artist.bio, fontSize = 14.sp,
                    lineHeight = 22.sp, color = colorScheme.onBackground.copy(0.75f)
                )

                // Contact chips
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Chip("✉  Email",               colorScheme)
                    Chip("📷  ${artist.instagram}", colorScheme)
                    Chip("📞  Call",                colorScheme)
                }

                HorizontalDivider(color = colorScheme.outlineVariant)

                // Order button
                Button(
                    onClick  = {
                        navController.navigate(Screen.OrderFlow.createRoute("new"))
                    },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = FamColors.PinterestRed,
                        contentColor   = Color.White
                    )
                ) {
                    Text(
                        "Continue to Order Details",
                        fontSize = 15.sp, fontWeight = FontWeight.SemiBold
                    )
                }

                HorizontalDivider(color = colorScheme.outlineVariant)

                Text(
                    "Artworks by ${artist.name}",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color      = colorScheme.onBackground
                )
            }
        }

        // ── Artwork grid ──────────────────────────────────────────────
        items(WORKS, key = { it.id }) { work ->
            var pressed by remember { mutableStateOf(false) }
            val scale   by androidx.compose.animation.core.animateFloatAsState(
                targetValue   = if (pressed) 0.97f else 1f,
                animationSpec = androidx.compose.animation.core.spring(),
                label         = "scale"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(scale)
                    .clip(RoundedCornerShape(12.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { pressed = true; tryAwaitRelease(); pressed = false },
                            onTap   = {
                                navController.navigate(Screen.ArtDetail.createRoute(work.id))
                            }
                        )
                    }
            ) {
                // Placeholder colour while loading
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(work.ratio)
                        .background(FamColors.SurfaceVariant)
                )
                AsyncImage(
                    model              = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(work.url).build(),
                    contentDescription = null,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier
                        .fillMaxWidth()
                        .aspectRatio(work.ratio)
                )
            }
        }
    }
}

@Composable
private fun Chip(label: String, colorScheme: ColorScheme) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(BorderStroke(1.dp, colorScheme.outlineVariant), RoundedCornerShape(20.dp))
            .background(colorScheme.surfaceVariant)
            .clickable {}
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, fontSize = 12.sp, color = colorScheme.onBackground)
    }
}