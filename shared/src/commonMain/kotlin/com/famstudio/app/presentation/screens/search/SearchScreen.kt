package com.famstudio.app.presentation.screens.search

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.famstudio.app.presentation.navigation.Screen
import com.famstudio.app.presentation.screens.order.FAKE_ARTISTS_PUBLIC
import com.famstudio.app.presentation.theme.FamColors
import fam.shared.generated.resources.Res
import fam.shared.generated.resources.ic_search

private val CATEGORIES = listOf(
    "Artists","Paintings","Digital","Sculpture","Photography","Illustration","Mixed Media"
)

private data class SearchArt(val id: String, val url: String, val ratio: Float, val category: String)
private val SEARCH_ARTS = listOf(
    SearchArt("1",  "https://picsum.photos/seed/art1/400/560",  400f/560f, "Paintings"),
    SearchArt("2",  "https://picsum.photos/seed/art2/400/420",  400f/420f, "Photography"),
    SearchArt("3",  "https://picsum.photos/seed/art3/400/500",  400f/500f, "Digital"),
    SearchArt("4",  "https://picsum.photos/seed/art4/400/700",  400f/700f, "Paintings"),
    SearchArt("5",  "https://picsum.photos/seed/art5/400/480",  400f/480f, "Illustration"),
    SearchArt("6",  "https://picsum.photos/seed/art6/800/400",  800f/400f, "Photography"),
    SearchArt("7",  "https://picsum.photos/seed/art7/600/400",  600f/400f, "Digital"),
    SearchArt("8",  "https://picsum.photos/seed/art8/400/560",  400f/560f, "Sculpture"),
    SearchArt("9",  "https://picsum.photos/seed/art9/400/380",  400f/380f, "Mixed Media"),
    SearchArt("10", "https://picsum.photos/seed/art10/400/500", 400f/500f, "Paintings"),
    SearchArt("11", "https://picsum.photos/seed/art11/400/460", 400f/460f, "Illustration"),
    SearchArt("12", "https://picsum.photos/seed/art12/400/540", 400f/540f, "Digital"),
)

// Featured category tiles for empty/browse state
private data class CategoryTile(val name: String, val imageUrl: String)
private val CATEGORY_TILES = listOf(
    CategoryTile("Paintings",     "https://picsum.photos/seed/cat1/400/300"),
    CategoryTile("Digital Art",   "https://picsum.photos/seed/cat2/400/300"),
    CategoryTile("Photography",   "https://picsum.photos/seed/cat3/400/300"),
    CategoryTile("Sculpture",     "https://picsum.photos/seed/cat4/400/300"),
    CategoryTile("Illustration",  "https://picsum.photos/seed/cat5/400/300"),
    CategoryTile("Mixed Media",   "https://picsum.photos/seed/cat6/400/300"),
    CategoryTile("String Art",    "https://picsum.photos/seed/cat7/400/300"),
    CategoryTile("Watercolour",   "https://picsum.photos/seed/cat8/400/300"),
)

@Composable
fun SearchScreen(navController: NavHostController) {
    val colorScheme  = MaterialTheme.colorScheme
    var query        by remember { mutableStateOf("") }
    var activeChip   by remember { mutableStateOf("") }
    var isSearching  by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val focusReq     = remember { FocusRequester() }

    val artists      = FAKE_ARTISTS_PUBLIC.values.toList()
    val showArtists  = activeChip == "Artists" || query.isNotBlank() && artists.any {
        it.name.contains(query, ignoreCase = true)
    }
    val filteredArts = SEARCH_ARTS.filter { art ->
        when {
            query.isNotBlank() -> art.category.contains(query, ignoreCase = true) ||
                    query.length > 2
            activeChip.isNotBlank() && activeChip != "Artists" ->
                art.category.equals(activeChip, ignoreCase = true) ||
                        activeChip == "Paintings" && art.category == "Paintings"
            else -> true
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {

        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

        // ── Search bar ─────────────────────────────────────────────────
        Row(modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            OutlinedTextField(
                value         = query,
                onValueChange = { query = it; isSearching = it.isNotBlank() },
                placeholder   = { Text("Search artists, artworks, styles...",
                    fontSize = 14.sp, color = colorScheme.onBackground.copy(0.4f)) },
                leadingIcon   = { Icon(painter = painterResource(Res.drawable.ic_search),
                    contentDescription = null, tint = MaterialTheme.colorScheme.onBackground.copy(0.5f),
                    modifier = Modifier.size(18.dp)) },
                modifier      = Modifier.weight(1f).focusRequester(focusReq),
                shape         = RoundedCornerShape(28.dp),
                singleLine    = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                colors        = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = FamColors.PinterestRed,
                    unfocusedBorderColor = colorScheme.outlineVariant,
                    cursorColor          = FamColors.PinterestRed
                )
            )
            if (isSearching) {
                Text("Cancel", fontSize = 14.sp, color = FamColors.PinterestRed,
                    modifier = Modifier.clickable {
                        query = ""; isSearching = false; activeChip = ""
                        focusManager.clearFocus()
                    })
            }
        }

        if (!isSearching && query.isBlank()) {
            // ── Browse state: Artists row + Category tiles ─────────────
            LazyVerticalStaggeredGrid(
                columns               = StaggeredGridCells.Fixed(2),
                contentPadding        = PaddingValues(bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalItemSpacing   = 6.dp,
                modifier              = Modifier.fillMaxSize()
            ) {
                // Artists section header
                item(span = StaggeredGridItemSpan.FullLine) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text("Artists", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                            color = colorScheme.onBackground,
                            modifier = Modifier.padding(bottom = 12.dp))
                        // Artist circles — horizontal scroll
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(artists) { artist ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        navController.navigate(Screen.ArtistProfile.createRoute(artist.id))
                                    }) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalPlatformContext.current)
                                            .data(artist.avatar).build(),
                                        contentDescription = artist.name,
                                        contentScale       = ContentScale.Crop,
                                        modifier           = Modifier.size(70.dp).clip(CircleShape)
                                            .background(FamColors.SurfaceVariant)
                                    )
                                    Spacer(Modifier.height(6.dp))
                                    Text(artist.name.split(" ").first(), fontSize = 12.sp,
                                        color = colorScheme.onBackground,
                                        fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        Text("Categories", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                            color = colorScheme.onBackground,
                            modifier = Modifier.padding(bottom = 8.dp))
                    }
                }

                // Category tiles — 2 column grid with image + label overlay
                items(CATEGORY_TILES, key = { it.name }) { tile ->
                    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                        .clickable { activeChip = tile.name; isSearching = true }) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                .data(tile.imageUrl).build(),
                            contentDescription = tile.name,
                            contentScale       = ContentScale.Crop,
                            modifier           = Modifier.fillMaxWidth().aspectRatio(1.4f)
                        )
                        // Dark overlay + label
                        Box(modifier = Modifier.fillMaxWidth().aspectRatio(1.4f)
                            .background(Color.Black.copy(0.35f)))
                        Text(
                            tile.name,
                            fontSize   = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color      = Color.White,
                            modifier   = Modifier.align(Alignment.BottomStart).padding(10.dp)
                        )
                    }
                }
            }
        } else {
            // ── Search / filter results ────────────────────────────────
            LazyVerticalStaggeredGrid(
                columns               = StaggeredGridCells.Fixed(2),
                contentPadding        = PaddingValues(
                    start = 6.dp, end = 6.dp, bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalItemSpacing   = 6.dp,
                modifier              = Modifier.fillMaxSize()
            ) {
                // Category chips
                item(span = StaggeredGridItemSpan.FullLine) {
                    LazyRow(
                        modifier            = Modifier.padding(
                            horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(CATEGORIES) { chip ->
                            val sel = chip == activeChip
                            Box(modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (sel) FamColors.PinterestRed
                                else colorScheme.surfaceVariant)
                                .clickable {
                                    activeChip = if (sel) "" else chip
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)) {
                                Text(chip, fontSize = 13.sp, fontWeight = FontWeight.Medium,
                                    color = if (sel) Color.White
                                    else colorScheme.onBackground)
                            }
                        }
                    }
                }

                // Artist circles when "Artists" chip or name search
                if (showArtists) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                            Text("Artists", fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onBackground,
                                modifier = Modifier.padding(bottom = 10.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                items(artists.filter {
                                    query.isBlank() || it.name.contains(query, ignoreCase = true)
                                }) { artist ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.width(72.dp).clickable {
                                            navController.navigate(
                                                Screen.ArtistProfile.createRoute(artist.id))
                                        }
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                                .data(artist.avatar).build(),
                                            contentDescription = artist.name,
                                            contentScale       = ContentScale.Crop,
                                            modifier           = Modifier.size(64.dp).clip(CircleShape)
                                                .background(FamColors.SurfaceVariant)
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(artist.name.split(" ").first(),
                                            fontSize = 11.sp,
                                            color    = colorScheme.onBackground,
                                            maxLines = 1)
                                    }
                                }
                            }
                        }
                    }
                }

                // Artwork grid
                if (filteredArts.isNotEmpty()) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Text("Artworks", fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                            color = colorScheme.onBackground,
                            modifier = Modifier.padding(
                                start = 16.dp, bottom = 6.dp, top = 4.dp))
                    }
                    items(filteredArts, key = { it.id }) { art ->
                        var pressed by remember { mutableStateOf(false) }
                        val scale by animateFloatAsState(
                            if (pressed) 0.97f else 1f,
                            spring(stiffness = Spring.StiffnessMedium), label = "s")
                        Box(modifier = Modifier.fillMaxWidth().scale(scale)
                            .clip(RoundedCornerShape(12.dp))
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onPress = { pressed = true; tryAwaitRelease(); pressed = false },
                                    onTap   = { navController.navigate(
                                        Screen.ArtDetail.createRoute(art.id)) }
                                )
                            }) {
                            Box(modifier = Modifier.fillMaxWidth().aspectRatio(art.ratio)
                                .background(FamColors.SurfaceVariant))
                            AsyncImage(
                                model = ImageRequest.Builder(LocalPlatformContext.current)
                                    .data(art.url).build(),
                                contentDescription = null, contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxWidth().aspectRatio(art.ratio)
                            )
                        }
                    }
                }
            }
        }
    }
}