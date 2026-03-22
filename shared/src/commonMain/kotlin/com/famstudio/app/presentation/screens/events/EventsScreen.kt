package com.famstudio.app.presentation.screens.events

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.famstudio.app.presentation.navigation.Screen
import com.famstudio.app.presentation.theme.FamColors

data class ArtEvent(
    val id:          String,
    val title:       String,
    val date:        String,
    val time:        String,
    val location:    String,
    val description: String,
    val organizer:   String,
    val imageUrl:    String,
    val ticketPrice: Long,
    val isPast:      Boolean = false
)

val FAKE_EVENTS = listOf(
    ArtEvent("e1", "Nairobi Art Week 2026",
        "April 5, 2026", "10:00 AM – 6:00 PM",
        "Sarit Centre, Westlands, Nairobi",
        "A celebration of East African contemporary art featuring over 50 artists from Kenya, Uganda, and Tanzania. Featuring live painting sessions, gallery tours, and art auctions.",
        "FAM Studio & Nairobi Art Council",
        "https://picsum.photos/seed/ev1/800/450", 500L),
    ArtEvent("e2", "Colour & Canvas Workshop",
        "April 12, 2026", "9:00 AM – 1:00 PM",
        "GoDown Arts Centre, Nairobi",
        "Learn oil painting techniques from master artist Amara Osei. Materials included. Perfect for beginners and intermediate painters looking to level up.",
        "Amara Osei Studios",
        "https://picsum.photos/seed/ev2/800/450", 1500L),
    ArtEvent("e3", "Digital Art & NFT Summit",
        "April 20, 2026", "11:00 AM – 5:00 PM",
        "iHub, Kilimani, Nairobi",
        "Explore the intersection of traditional African art and digital innovation. Panel discussions, live demos, and a curated digital art showcase.",
        "TechArt Kenya",
        "https://picsum.photos/seed/ev3/800/450", 800L),
    ArtEvent("e4", "String Art Masterclass",
        "May 3, 2026", "2:00 PM – 6:00 PM",
        "Village Market, Gigiri, Nairobi",
        "An intensive string art workshop covering geometric patterns, portrait string art, and 3D string sculptures. All materials provided.",
        "FAM Studio",
        "https://picsum.photos/seed/ev4/800/450", 2000L),
    ArtEvent("e5", "Art & Soul Exhibition",
        "March 10, 2026", "10:00 AM – 8:00 PM",
        "Nairobi National Museum",
        "A retrospective of Kenyan art spanning six decades. This exhibition has concluded.",
        "Nairobi National Museum",
        "https://picsum.photos/seed/ev5/800/450", 300L, isPast = true),
)

@Composable
fun EventsScreen(navController: NavHostController) {
    val colorScheme = MaterialTheme.colorScheme
    val upcoming    = FAKE_EVENTS.filter { !it.isPast }
    val past        = FAKE_EVENTS.filter { it.isPast }

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        // Header
        Text("Events", fontSize = 26.sp, fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp))

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

            // Upcoming
            upcoming.forEach { event ->
                EventCard(event = event, navController = navController, opacity = 1f)
            }

            if (past.isNotEmpty()) {
                Text("Past Events", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onBackground.copy(0.5f),
                    modifier = Modifier.padding(top = 8.dp))
                past.forEach { event ->
                    EventCard(event = event, navController = navController, opacity = 0.5f)
                }
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
fun EventCard(event: ArtEvent, navController: NavHostController, opacity: Float) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier  = Modifier.fillMaxWidth().alpha(opacity)
            .clickable { navController.navigate(Screen.EventDetail.createRoute(event.id)) },
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(0.dp),
        border    = BorderStroke(0.5.dp, colorScheme.outlineVariant)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(event.imageUrl).build(),
                contentDescription = event.title, contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(160.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Column(modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(event.title, fontSize = 16.sp, fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("📅 ${event.date}", fontSize = 12.sp,
                        color = FamColors.PinterestRed, fontWeight = FontWeight.Medium)
                    Text("🕐 ${event.time}", fontSize = 12.sp,
                        color = colorScheme.onBackground.copy(0.6f))
                }
                Text("📍 ${event.location}", fontSize = 12.sp,
                    color = colorScheme.onBackground.copy(0.6f))
                Text(event.description, fontSize = 13.sp,
                    color = colorScheme.onBackground.copy(0.7f),
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text("KES ${event.ticketPrice} / ticket", fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold, color = FamColors.PinterestRed)
                    if (!event.isPast) {
                        OutlinedButton(
                            onClick = { navController.navigate(Screen.EventDetail.createRoute(event.id)) },
                            shape   = RoundedCornerShape(20.dp),
                            border  = BorderStroke(1.dp, FamColors.PinterestRed),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text("Details", fontSize = 13.sp, color = FamColors.PinterestRed)
                        }
                    }
                }
            }
        }
    }
}

// ── Event Detail Screen ────────────────────────────────────────────────────
@Composable
fun EventDetailScreen(eventId: String, navController: NavHostController) {
    val event       = FAKE_EVENTS.find { it.id == eventId } ?: FAKE_EVENTS.first()
    val colorScheme = MaterialTheme.colorScheme
    var tickets     by remember { mutableIntStateOf(1) }

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            // Image + back
            Box(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(event.imageUrl).build(),
                    contentDescription = event.title, contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(240.dp)
                )
                Box(modifier = Modifier.padding(16.dp).size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(0.4f))
                    .clickable { navController.popBackStack() }
                    .align(Alignment.TopStart),
                    contentAlignment = Alignment.Center) {
                    Text("←", color = Color.White, fontSize = 18.sp)
                }
            }

            Column(modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(event.title, fontSize = 22.sp, fontWeight = FontWeight.Bold,
                    color = colorScheme.onBackground)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoChip("📅 ${event.date}", colorScheme)
                    InfoChip("🕐 ${event.time}", colorScheme)
                }
                InfoChip("📍 ${event.location}", colorScheme)
                HorizontalDivider(color = colorScheme.outlineVariant)
                Text(event.description, fontSize = 14.sp, lineHeight = 22.sp,
                    color = colorScheme.onBackground.copy(0.75f))
                HorizontalDivider(color = colorScheme.outlineVariant)
                Text("Organised by ${event.organizer}", fontSize = 13.sp,
                    color = colorScheme.onBackground.copy(0.55f))
                // Map placeholder
                Box(modifier = Modifier.fillMaxWidth().height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center) {
                    Text("🗺  Map — ${event.location}", fontSize = 13.sp,
                        color = colorScheme.onBackground.copy(0.5f))
                }
                HorizontalDivider(color = colorScheme.outlineVariant)
                // Ticket quantity
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text("Tickets", fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                        color = colorScheme.onBackground)
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp))
                            .background(colorScheme.surfaceVariant)
                            .clickable { if (tickets > 1) tickets-- },
                            contentAlignment = Alignment.Center) {
                            Text("−", fontSize = 18.sp, color = colorScheme.onBackground)
                        }
                        Text("$tickets", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                            color = colorScheme.onBackground)
                        Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp))
                            .background(colorScheme.surfaceVariant)
                            .clickable { tickets++ },
                            contentAlignment = Alignment.Center) {
                            Text("+", fontSize = 18.sp, color = colorScheme.onBackground)
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Price per ticket", fontSize = 13.sp,
                        color = colorScheme.onBackground.copy(0.55f))
                    Text("KES ${event.ticketPrice}", fontSize = 13.sp,
                        color = colorScheme.onBackground)
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total", fontSize = 15.sp, fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground)
                    Text("KES ${event.ticketPrice * tickets}", fontSize = 18.sp,
                        fontWeight = FontWeight.Bold, color = FamColors.PinterestRed)
                }
            }
        }
        // Buy Ticket button
        if (!event.isPast) {
            Button(
                onClick  = {
                    navController.navigate(
                        Screen.CheckoutEvent.createRoute(event.id, tickets.toString())
                    )
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(54.dp),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = FamColors.PinterestRed)
            ) {
                Text("Buy Ticket · KES ${event.ticketPrice * tickets}",
                    fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
private fun InfoChip(text: String, colorScheme: ColorScheme) {
    Box(modifier = Modifier.clip(RoundedCornerShape(20.dp))
        .background(colorScheme.surfaceVariant)
        .padding(horizontal = 12.dp, vertical = 6.dp)) {
        Text(text, fontSize = 12.sp, color = colorScheme.onBackground)
    }
}