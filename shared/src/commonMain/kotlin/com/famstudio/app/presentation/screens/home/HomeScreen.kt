package com.famstudio.app.presentation.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.famstudio.app.presentation.navigation.Screen
import com.famstudio.app.presentation.theme.FamColors

// ── Fake data until Firebase is connected ─────────────────────────────────
data class ArtCard(
    val id: String,
    val imageUrl: String,
    val aspectRatio: Float,   // height / width  — drives card height
    val isSaved: Boolean = false
)

private val FAKE_FEED = listOf(
    ArtCard("1",  "https://picsum.photos/seed/art1/400/560",  1.4f),
    ArtCard("2",  "https://picsum.photos/seed/art2/400/300",  0.75f),
    ArtCard("3",  "https://picsum.photos/seed/art3/400/500",  1.25f),
    ArtCard("4",  "https://picsum.photos/seed/art4/400/400",  1.0f),
    ArtCard("5",  "https://picsum.photos/seed/art5/400/600",  1.5f),
    ArtCard("6",  "https://picsum.photos/seed/art6/400/350",  0.875f),
    ArtCard("7",  "https://picsum.photos/seed/art7/400/480",  1.2f),
    ArtCard("8",  "https://picsum.photos/seed/art8/400/320",  0.8f),
    ArtCard("9",  "https://picsum.photos/seed/art9/400/550",  1.375f),
    ArtCard("10", "https://picsum.photos/seed/art10/400/420", 1.05f),
    ArtCard("11", "https://picsum.photos/seed/art11/400/380", 0.95f),
    ArtCard("12", "https://picsum.photos/seed/art12/400/500", 1.25f),
)

private val CATEGORIES = listOf(
    "For You", "Paintings", "Sculpture", "Digital", "Photography", "Illustration", "Mixed Media"
)

// ── Home Screen ────────────────────────────────────────────────────────────
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedCategory by remember { mutableStateOf("For You") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Category tabs ─────────────────────────────────────────────
            CategoryTabs(
                categories     = CATEGORIES,
                selected       = selectedCategory,
                onSelect       = { selectedCategory = it }
            )

            // ── Masonry grid ──────────────────────────────────────────────
            LazyVerticalStaggeredGrid(
                columns            = StaggeredGridCells.Fixed(2),
                modifier           = Modifier.fillMaxSize(),
                contentPadding     = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing   = 8.dp
            ) {
                items(FAKE_FEED, key = { it.id }) { card ->
                    ArtCardItem(
                        card  = card,
                        onTap = { navController.navigate(Screen.ArtDetail.createRoute(card.id)) }
                    )
                }
            }
        }
    }
}

// ── Category tabs ──────────────────────────────────────────────────────────
@Composable
private fun CategoryTabs(
    categories: List<String>,
    selected:   String,
    onSelect:   (String) -> Unit
) {
    Column {
        ScrollableTabRow(
            selectedTabIndex   = categories.indexOf(selected),
            containerColor     = Color.White,
            contentColor       = FamColors.PinterestRed,
            edgePadding        = 8.dp,
            indicator          = { tabPositions ->
                val index = categories.indexOf(selected)
                if (index >= 0 && index < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[index]),
                        color    = FamColors.PinterestRed
                    )
                }
            },
            divider = {}
        ) {
            categories.forEach { cat ->
                Tab(
                    selected = cat == selected,
                    onClick  = { onSelect(cat) },
                    text     = {
                        Text(
                            cat,
                            fontSize   = 14.sp,
                            fontWeight = if (cat == selected) FontWeight.Bold else FontWeight.Normal,
                            color      = if (cat == selected) FamColors.PinterestRed
                            else FamColors.TextMuted
                        )
                    }
                )
            }
        }
        Divider(color = FamColors.Border, thickness = 0.5.dp)
    }
}

// ── Art card ───────────────────────────────────────────────────────────────
@Composable
private fun ArtCardItem(
    card:  ArtCard,
    onTap: () -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue   = if (pressed) 0.96f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label         = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress   = { pressed = true; tryAwaitRelease(); pressed = false },
                    onTap     = { onTap() }
                )
            }
    ) {
        // ── Image ──────────────────────────────────────────────────────
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(card.imageUrl)
                .build(),
            contentDescription = null,
            contentScale       = ContentScale.Crop,
            modifier           = Modifier
                .fillMaxWidth()
                .aspectRatio(1f / card.aspectRatio)
        )

        // ── Dominant colour placeholder shown while loading ────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f / card.aspectRatio)
                .background(FamColors.SurfaceVariant)
        )
    }
}