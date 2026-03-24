package com.famstudio.app.presentation.screens.profile

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
import com.famstudio.app.presentation.screens.cart.CartState
import com.famstudio.app.presentation.screens.cart.OrderDetailCard
import com.famstudio.app.presentation.screens.cart.OrderItem
import com.famstudio.app.presentation.theme.FamColors

// ── Data ─────────────────────────────────────────────────────────────────────
data class UserProfile(
    val name:     String = "Jane Client",
    val email:    String = "jane@example.com",
    val phone:    String = "+254 700 000 000",
    val location: String = "Nairobi, Kenya",
    val role:     String = "Client",
    val since:    String = "March 2026",
    val avatar:   String = "https://i.pravatar.cc/150?img=10"
)

// ── Profile Screen ────────────────────────────────────────────────────────────
@Composable
fun ProfileScreen(navController: NavHostController) {
    val colorScheme  = MaterialTheme.colorScheme
    var selectedTab  by remember { mutableIntStateOf(0) }
    var profile      by remember { mutableStateOf(UserProfile()) }
    var showEditSheet by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))

        // ── Avatar header ──────────────────────────────────────────────
        Column(
            modifier = Modifier.fillMaxWidth()
                .background(colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Avatar — tap to edit profile
            Box(
                modifier = Modifier.clickable { showEditSheet = true },
                contentAlignment = Alignment.BottomEnd
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(profile.avatar).build(),
                    contentDescription = "Avatar",
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.size(88.dp).clip(CircleShape)
                        .background(FamColors.SurfaceVariant)
                )
                // Camera badge
                Box(
                    modifier = Modifier.size(28.dp).clip(CircleShape)
                        .background(FamColors.PinterestRed)
                        .border(BorderStroke(2.dp, colorScheme.background), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📷", fontSize = 12.sp)
                }
            }

            Text(profile.name, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground)
            Text(profile.email, fontSize = 13.sp,
                color = colorScheme.onBackground.copy(0.55f))
        }

        // ── Tabs ───────────────────────────────────────────────────────
        val tabs = listOf("Profile", "Orders", "Cart")
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor   = colorScheme.background,
            contentColor     = FamColors.PinterestRed,
            indicator        = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color    = FamColors.PinterestRed)
            }
        ) {
            tabs.forEachIndexed { i, title ->
                Tab(selected = selectedTab == i, onClick = { selectedTab = i },
                    text = {
                        Text(title, fontSize = 14.sp,
                            fontWeight = if (selectedTab == i) FontWeight.Bold
                            else FontWeight.Normal,
                            color = if (selectedTab == i) FamColors.PinterestRed
                            else colorScheme.onBackground.copy(0.5f))
                    })
            }
        }

        when (selectedTab) {
            0 -> ProfileTab(profile, colorScheme, onEdit = { showEditSheet = true })
            1 -> OrdersTab(colorScheme)
            2 -> CartTab(navController, colorScheme)
        }
    }

    // ── Edit Profile Sheet ─────────────────────────────────────────────
    if (showEditSheet) {
        EditProfileSheet(
            profile   = profile,
            onDismiss = { showEditSheet = false },
            onSave    = { updated -> profile = updated; showEditSheet = false }
        )
    }
}

// ── Edit Profile Bottom Sheet ─────────────────────────────────────────────────
@Composable
private fun EditProfileSheet(
    profile:   UserProfile,
    onDismiss: () -> Unit,
    onSave:    (UserProfile) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    var name     by remember { mutableStateOf(profile.name) }
    var phone    by remember { mutableStateOf(profile.phone) }
    var location by remember { mutableStateOf(profile.location) }

    Dialog(
        onDismissRequest = onDismiss,
        properties       = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Dim background
            Box(modifier = Modifier.fillMaxSize()
                .background(Color.Black.copy(0.5f))
                .clickable { onDismiss() })

            // Sheet
            Column(
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(colorScheme.background)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Handle
                Box(modifier = Modifier.width(40.dp).height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(colorScheme.outlineVariant)
                    .align(Alignment.CenterHorizontally))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text("Edit Profile", fontSize = 18.sp,
                        fontWeight = FontWeight.Bold, color = colorScheme.onBackground)
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = colorScheme.onBackground.copy(0.5f))
                    }
                }

                // Change avatar
                Box(modifier = Modifier.align(Alignment.CenterHorizontally)
                    .clickable { /* TODO: image picker */ },
                    contentAlignment = Alignment.BottomEnd) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data(profile.avatar).build(),
                        contentDescription = null,
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier.size(80.dp).clip(CircleShape)
                            .background(FamColors.SurfaceVariant)
                    )
                    Box(modifier = Modifier.size(26.dp).clip(CircleShape)
                        .background(FamColors.PinterestRed)
                        .border(BorderStroke(2.dp, colorScheme.background), CircleShape),
                        contentAlignment = Alignment.Center) {
                        Text("📷", fontSize = 11.sp)
                    }
                }
                Text("Tap photo to change", fontSize = 11.sp,
                    color = colorScheme.onBackground.copy(0.4f),
                    modifier = Modifier.align(Alignment.CenterHorizontally))

                // Fields
                EditField("Full Name", name, { name = it }, colorScheme)
                EditField("Phone", phone, { phone = it }, colorScheme)
                EditField("Location", location, { location = it }, colorScheme)

                // Save
                Button(
                    onClick  = { onSave(profile.copy(name = name, phone = phone, location = location)) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = FamColors.PinterestRed)
                ) { Text("Save Changes", fontWeight = FontWeight.Bold, color = Color.White) }

                Spacer(Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
private fun EditField(label: String, value: String, onValue: (String) -> Unit,
                      colorScheme: ColorScheme) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValue,
        label         = { Text(label) },
        modifier      = Modifier.fillMaxWidth(),
        shape         = RoundedCornerShape(12.dp),
        singleLine    = true,
        colors        = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = FamColors.PinterestRed,
            focusedLabelColor  = FamColors.PinterestRed)
    )
}

// ── Profile Tab ───────────────────────────────────────────────────────────────
@Composable
private fun ProfileTab(profile: UserProfile, colorScheme: ColorScheme, onEdit: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        .padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

        ProfileField("Full Name",    profile.name,     colorScheme)
        ProfileField("Email",        profile.email,    colorScheme)
        ProfileField("Phone",        profile.phone,    colorScheme)
        ProfileField("Location",     profile.location, colorScheme)
        HorizontalDivider(color = colorScheme.outlineVariant)
        Text("Account", fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
            color = colorScheme.onBackground)
        ProfileField("Role",         profile.role,  colorScheme)
        ProfileField("Member Since", profile.since, colorScheme)
        HorizontalDivider(color = colorScheme.outlineVariant)

        // Edit Profile - now a subtle text button, not the main CTA
        OutlinedButton(
            onClick  = onEdit,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape    = RoundedCornerShape(12.dp),
            border   = BorderStroke(1.dp, colorScheme.outlineVariant)
        ) {
            Text("✎  Edit Profile", fontSize = 14.sp,
                color = colorScheme.onBackground)
        }

        HorizontalDivider(color = colorScheme.outlineVariant)

        // Sign out
        OutlinedButton(
            onClick  = { /* TODO: sign out → navigate to Login */ },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape    = RoundedCornerShape(12.dp),
            border   = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
        ) {
            Text("Sign Out", color = MaterialTheme.colorScheme.error, fontSize = 15.sp)
        }
    }
}

// ── Orders Tab ────────────────────────────────────────────────────────────────
@Composable
private fun OrdersTab(colorScheme: ColorScheme) {
    val orders = CartState.orders
    if (orders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("🎨", fontSize = 40.sp)
                Text("No custom orders yet", fontSize = 16.sp,
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

// ── Cart Tab ──────────────────────────────────────────────────────────────────
@Composable
private fun CartTab(navController: NavHostController, colorScheme: ColorScheme) {
    val items = CartState.items
    if (items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("🛒", fontSize = 40.sp)
                Text("Cart is empty", color = colorScheme.onBackground.copy(0.5f))
                TextButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                    Text("Go to Cart", color = FamColors.PinterestRed)
                }
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items.forEach { item ->
                Card(shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                    border = BorderStroke(0.5.dp, colorScheme.outlineVariant)) {
                    Row(modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment     = Alignment.CenterVertically) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                .data(item.imageUrl).build(),
                            contentDescription = null, contentScale = ContentScale.Crop,
                            modifier = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp))
                                .background(FamColors.SurfaceVariant)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.title, fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold, color = colorScheme.onBackground)
                            Text(item.artistName, fontSize = 12.sp,
                                color = colorScheme.onBackground.copy(0.6f))
                        }
                        Text("KES ${item.price}", fontSize = 13.sp,
                            fontWeight = FontWeight.Bold, color = FamColors.PinterestRed)
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = { navController.navigate(Screen.Cart.route) },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FamColors.PinterestRed)) {
                Text("View Full Cart", color = Color.White)
            }
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String, colorScheme: ColorScheme) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, fontSize = 11.sp, color = colorScheme.onBackground.copy(0.45f))
        Text(value, fontSize = 15.sp, color = colorScheme.onBackground)
        HorizontalDivider(color = colorScheme.outlineVariant, thickness = 0.5.dp,
            modifier = Modifier.padding(top = 8.dp))
    }
}