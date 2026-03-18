package com.famstudio.app.presentation.screens.artist

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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

private data class ArtistWork(val id: String, val url: String, val ratio: Float)
private val ARTIST_WORKS = listOf(
    ArtistWork("1",  "https://picsum.photos/seed/art1/400/560",  400f/560f),
    ArtistWork("2",  "https://picsum.photos/seed/art2/400/420",  400f/420f),
    ArtistWork("13", "https://picsum.photos/seed/art19/800/450", 800f/450f),
    ArtistWork("5",  "https://picsum.photos/seed/art4/400/700",  400f/700f),
    ArtistWork("8",  "https://picsum.photos/seed/art20/800/500", 800f/500f),
    ArtistWork("9",  "https://picsum.photos/seed/art7/600/400",  600f/400f),
)

@Composable
fun ArtistProfileScreen(artistId: String, navController: NavHostController) {
    val artist      = FAKE_ARTISTS_PUBLIC[artistId] ?: FAKE_ARTISTS_PUBLIC["a1"]!!
    val colorScheme = MaterialTheme.colorScheme

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp))
                .background(colorScheme.surfaceVariant)
                .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center) {
                Text("←", fontSize = 18.sp, color = colorScheme.onBackground)
            }
        }

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            // ← Fix 1: use start/end/bottom separately, not horizontal+bottom
            contentPadding = PaddingValues(start = 6.dp, end = 6.dp, bottom = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalItemSpacing   = 6.dp
        ) {
            // ← Fix 2: span = StaggeredGridItemSpan.FullLine directly, not a lambda
            item(span = StaggeredGridItemSpan.FullLine) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                .data(artist.avatar).build(),
                            contentDescription = artist.name,
                            contentScale       = ContentScale.Crop,
                            modifier           = Modifier.size(80.dp).clip(CircleShape)
                                .background(FamColors.SurfaceVariant)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(artist.name, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                                color = colorScheme.onBackground)
                            Text(artist.location, fontSize = 13.sp,
                                color = colorScheme.onBackground.copy(0.55f))
                        }
                    }
                    Text(artist.bio, fontSize = 14.sp, lineHeight = 22.sp,
                        color = colorScheme.onBackground.copy(0.75f))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ContactChip("✉ Email",            colorScheme)
                        ContactChip("📷 ${artist.instagram}", colorScheme)
                        ContactChip("📞 Call",             colorScheme)
                    }
                    HorizontalDivider(color = colorScheme.outlineVariant)
                    Text("Artworks", fontSize = 16.sp, fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground)
                }
            }

            items(ARTIST_WORKS, key = { it.id }) { work ->
                var pressed by remember { mutableStateOf(false) }
                val scale by androidx.compose.animation.core.animateFloatAsState(
                    if (pressed) 0.97f else 1f,
                    androidx.compose.animation.core.spring(), label = "s")
                Box(modifier = Modifier.fillMaxWidth().scale(scale)
                    .clip(RoundedCornerShape(12.dp))
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { pressed = true; tryAwaitRelease(); pressed = false },
                            onTap   = { navController.navigate(Screen.ArtDetail.createRoute(work.id)) }
                        )
                    }) {
                    Box(modifier = Modifier.fillMaxWidth().aspectRatio(work.ratio)
                        .background(FamColors.SurfaceVariant))
                    AsyncImage(
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(work.url).build(),
                        contentDescription = null, contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().aspectRatio(work.ratio)
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactChip(label: String, colorScheme: ColorScheme) {
    Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))
        .border(BorderStroke(1.dp, colorScheme.outlineVariant), RoundedCornerShape(20.dp))
        .background(colorScheme.surfaceVariant)
        .clickable {}
        .padding(horizontal = 12.dp, vertical = 6.dp)) {
        Text(label, fontSize = 12.sp, color = colorScheme.onBackground)
    }
}