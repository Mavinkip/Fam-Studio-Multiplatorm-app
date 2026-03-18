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
import androidx.compose.ui.graphics.Color
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
    val id:          String,
    val imageUrl:    String,
    val aspectRatio: Float,
    val fullWidth:   Boolean = false
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
    "For You", "Paintings", "Sculpture", "Digital", "Photography", "Illustration", "Mixed Media"
)

@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedCategory by remember { mutableStateOf("For You") }

    // ── YouTube-style scroll-hide nav bar ─────────────────────────────
    val gridState = rememberLazyStaggeredGridState()
    var lastScrollOffset by remember { mutableIntStateOf(0) }

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.firstVisibleItemScrollOffset to gridState.firstVisibleItemIndex }
            .collect { (offset, index) ->
                val currentTotal = index * 1000 + offset
                val delta = currentTotal - lastScrollOffset
                when {
                    delta > 20  -> NavBarState.isVisible = false  // scrolling down — hide
                    delta < -20 -> NavBarState.isVisible = true   // scrolling up — show
                }
                lastScrollOffset = currentTotal
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CategoryTabs(
            categories = CATEGORIES,
            selected   = selectedCategory,
            onSelect   = { selectedCategory = it }
        )

        LazyVerticalStaggeredGrid(
            state                 = gridState,
            columns               = StaggeredGridCells.Fixed(2),
            modifier              = Modifier.fillMaxSize(),
            contentPadding        = PaddingValues(
                start  = 6.dp,
                end    = 6.dp,
                top    = 6.dp,
                bottom = 80.dp   // space so last items aren't hidden behind nav bar
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
                ArtCardItem(
                    card  = card,
                    onTap = { navController.navigate(Screen.ArtDetail.createRoute(card.id)) }
                )
            }
        }
    }
}

@Composable
private fun CategoryTabs(categories: List<String>, selected: String, onSelect: (String) -> Unit) {
    ScrollableTabRow(
        selectedTabIndex = categories.indexOf(selected),
        containerColor   = Color.White,
        contentColor     = FamColors.PinterestRed,
        edgePadding      = 8.dp,
        indicator        = { tabPositions ->
            val index = categories.indexOf(selected)
            if (index in tabPositions.indices) {
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
                text = {
                    Text(
                        cat,
                        fontSize   = 14.sp,
                        fontWeight = if (cat == selected) FontWeight.Bold else FontWeight.Normal,
                        color      = if (cat == selected) FamColors.PinterestRed else FamColors.TextMuted
                    )
                }
            )
        }
    }
    HorizontalDivider(color = FamColors.Border, thickness = 0.5.dp)
}

@Composable
private fun ArtCardItem(card: ArtCard, onTap: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue   = if (pressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
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
                    onTap   = { onTap() }
                )
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(card.aspectRatio)
                .background(FamColors.SurfaceVariant)
        )
        AsyncImage(
            model              = ImageRequest.Builder(LocalPlatformContext.current)
                .data(card.imageUrl).build(),
            contentDescription = null,
            contentScale       = ContentScale.Fit,
            modifier           = Modifier
                .fillMaxWidth()
                .aspectRatio(card.aspectRatio)
        )
    }
}