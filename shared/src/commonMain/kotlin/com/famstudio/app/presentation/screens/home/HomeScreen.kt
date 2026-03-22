package com.famstudio.app.presentation.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.famstudio.app.presentation.navigation.NavBarState
import com.famstudio.app.presentation.navigation.Screen
import com.famstudio.app.presentation.theme.FamColors

data class ArtCard(
    val id: String, val imageUrl: String, val aspectRatio: Float,
    val fullWidth: Boolean = false
)

private val FAKE_FEED = listOf(
    ArtCard("1",  "https://picsum.photos/seed/art1/400/560",  400f/560f),
    ArtCard("2",  "https://picsum.photos/seed/art2/400/600",  400f/600f),
    ArtCard("3",  "https://picsum.photos/seed/art6/800/400",  800f/400f, fullWidth = true),
    ArtCard("4",  "https://picsum.photos/seed/art3/400/500",  400f/500f),
    ArtCard("5",  "https://picsum.photos/seed/art4/400/700",  400f/700f),
    ArtCard("6",  "https://picsum.photos/seed/art5/400/480",  400f/480f),
    ArtCard("7",  "https://picsum.photos/seed/art14/400/550", 400f/550f),
    ArtCard("8",  "https://picsum.photos/seed/art20/800/500", 800f/500f, fullWidth = true),
    ArtCard("9",  "https://picsum.photos/seed/art7/600/400",  600f/400f),
    ArtCard("10", "https://picsum.photos/seed/art8/700/400",  700f/400f),
    ArtCard("11", "https://picsum.photos/seed/art9/400/620",  400f/620f),
    ArtCard("12", "https://picsum.photos/seed/art16/400/420", 400f/420f),
    ArtCard("13", "https://picsum.photos/seed/art19/800/450", 800f/450f, fullWidth = true),
    ArtCard("14", "https://picsum.photos/seed/art10/400/400", 1f),
    ArtCard("15", "https://picsum.photos/seed/art11/400/400", 1f),
    ArtCard("16", "https://picsum.photos/seed/art12/400/520", 400f/520f),
    ArtCard("17", "https://picsum.photos/seed/art13/400/460", 400f/460f),
    ArtCard("18", "https://picsum.photos/seed/art17/400/580", 400f/580f),
    ArtCard("19", "https://picsum.photos/seed/art18/800/420", 800f/420f, fullWidth = true),
    ArtCard("20", "https://picsum.photos/seed/art15/400/640", 400f/640f),
    ArtCard("21", "https://picsum.photos/seed/art21/400/500", 400f/500f),
    ArtCard("22", "https://picsum.photos/seed/art22/650/380", 650f/380f),
    ArtCard("23", "https://picsum.photos/seed/art23/400/560", 400f/560f),
)

private val CATEGORIES = listOf(
    "For You", "Paintings", "Sculpture", "Digital",
    "Photography", "Illustration", "Mixed Media"
)

@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedCategory by remember { mutableStateOf("For You") }
    val gridState = rememberLazyStaggeredGridState()
    var lastScrollOffset by remember { mutableIntStateOf(0) }
    val colorScheme = MaterialTheme.colorScheme

    // YouTube-style scroll hide/show
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.firstVisibleItemScrollOffset to gridState.firstVisibleItemIndex }
            .collect { (offset, index) ->
                val currentTotal = index * 1000 + offset
                val delta = currentTotal - lastScrollOffset
                when {
                    delta > 20  -> NavBarState.isVisible = false
                    delta < -20 -> NavBarState.isVisible = true
                }
                lastScrollOffset = currentTotal
            }
    }

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {

        // ── Category tabs — with proper status bar spacing ─────────────
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

        ScrollableTabRow(
            selectedTabIndex = CATEGORIES.indexOf(selectedCategory).coerceAtLeast(0),
            containerColor   = colorScheme.background,
            contentColor     = FamColors.PinterestRed,
            edgePadding      = 8.dp,
            indicator        = { tabPositions ->
                val idx = CATEGORIES.indexOf(selectedCategory).coerceAtLeast(0)
                if (idx < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color    = FamColors.PinterestRed
                    )
                }
            },
            divider = {}
        ) {
            CATEGORIES.forEach { cat ->
                Tab(
                    selected = cat == selectedCategory,
                    onClick  = { selectedCategory = cat },
                    text = {
                        Text(
                            cat,
                            fontSize   = 14.sp,
                            fontWeight = if (cat == selectedCategory) FontWeight.Bold
                            else FontWeight.Normal,
                            color      = if (cat == selectedCategory) FamColors.PinterestRed
                            else colorScheme.onBackground.copy(0.55f)
                        )
                    }
                )
            }
        }
        HorizontalDivider(color = colorScheme.outlineVariant, thickness = 0.5.dp)

        // ── Masonry grid ───────────────────────────────────────────────
        LazyVerticalStaggeredGrid(
            state                 = gridState,
            columns               = StaggeredGridCells.Fixed(2),
            modifier              = Modifier.fillMaxSize(),
            contentPadding        = PaddingValues(
                start = 6.dp, end = 6.dp, top = 6.dp, bottom = 80.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalItemSpacing   = 6.dp
        ) {
            items(
                items = FAKE_FEED,
                key   = { it.id },
                span  = { card ->
                    if (card.fullWidth) StaggeredGridItemSpan.FullLine
                    else StaggeredGridItemSpan.SingleLane
                }
            ) { card ->
                ArtCardItem(card = card,
                    onTap = { navController.navigate(Screen.ArtDetail.createRoute(card.id)) })
            }
        }
    }
}

@Composable
private fun ArtCardItem(card: ArtCard, onTap: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        if (pressed) 0.97f else 1f,
        spring(stiffness = Spring.StiffnessMedium), label = "s")
    Box(modifier = Modifier.fillMaxWidth().scale(scale)
        .clip(RoundedCornerShape(12.dp))
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = { pressed = true; tryAwaitRelease(); pressed = false },
                onTap   = { onTap() }
            )
        }) {
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(card.aspectRatio)
            .background(FamColors.SurfaceVariant))
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(card.imageUrl).build(),
            contentDescription = null, contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().aspectRatio(card.aspectRatio)
        )
    }
}