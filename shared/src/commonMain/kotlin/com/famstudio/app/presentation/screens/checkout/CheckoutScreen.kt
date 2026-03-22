package com.famstudio.app.presentation.screens.checkout

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.famstudio.app.presentation.navigation.Screen
import com.famstudio.app.presentation.screens.cart.CartState
import com.famstudio.app.presentation.screens.events.FAKE_EVENTS
import com.famstudio.app.presentation.theme.FamColors

enum class CheckoutType { CART, DEPOSIT, EVENT, BUY_NOW }

private val PAYMENT_METHODS = listOf(
    Triple("📱", "M-Pesa",         "Pay via M-Pesa mobile money"),
    Triple("🏦", "Bank Transfer",  "Pay via bank transfer"),
    Triple("💳", "Card",           "Visa / Mastercard"),
    Triple("💵", "Cash on Pickup", "Pay when you collect"),
)

@Composable
fun CheckoutScreen(
    navController:  NavHostController,
    checkoutType:   CheckoutType = CheckoutType.CART,
    orderId:        String = "",
    eventId:        String = "",
    ticketCount:    Int    = 1,
    artworkTitle:   String = "",
    artworkPrice:   Long   = 0L
) {
    val colorScheme    = MaterialTheme.colorScheme
    var selectedMethod by remember { mutableStateOf("") }
    var phone          by remember { mutableStateOf("") }
    var confirmed      by remember { mutableStateOf(false) }

    // Compute amount + title based on type
    val (pageTitle, orderSummaryTitle, amount) = remember(checkoutType) {
        when (checkoutType) {
            CheckoutType.CART      -> Triple("Checkout", "Cart Items", CartState.cartTotal())
            CheckoutType.DEPOSIT   -> {
                val order = CartState.orders.find { it.id == orderId }
                Triple("Pay Deposit", order?.artTitle ?: "Custom Order", (order?.totalPrice ?: 0L) / 2)
            }
            CheckoutType.EVENT     -> {
                val event = FAKE_EVENTS.find { it.id == eventId }
                Triple("Buy Ticket", event?.title ?: "Event Ticket",
                    (event?.ticketPrice ?: 0L) * ticketCount)
            }
            CheckoutType.BUY_NOW   -> Triple("Buy Now", artworkTitle, artworkPrice)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        // Header
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp))
                .background(colorScheme.surfaceVariant)
                .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center) {
                Text("←", fontSize = 18.sp, color = colorScheme.onBackground)
            }
            Spacer(Modifier.width(12.dp))
            Text(pageTitle, fontSize = 22.sp, fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground)
        }

        if (confirmed) {
            // Success
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(32.dp)) {
                    Text("✅", fontSize = 60.sp)
                    Text("Payment Confirmed!", fontSize = 22.sp,
                        fontWeight = FontWeight.Bold, color = colorScheme.onBackground)
                    Text(when (checkoutType) {
                        CheckoutType.DEPOSIT  -> "Deposit of KES $amount received.\nThe artist will begin work and contact you via email."
                        CheckoutType.EVENT    -> "Your ${ticketCount} ticket(s) for $orderSummaryTitle have been booked!"
                        CheckoutType.BUY_NOW  -> "Your purchase of $orderSummaryTitle is confirmed!\nShipping details will be sent to your email."
                        else                  -> "Your order has been placed.\nShipping details will be sent to your email."
                    }, fontSize = 14.sp, color = colorScheme.onBackground.copy(0.6f),
                        textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(0) { inclusive = true }; launchSingleTop = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape    = RoundedCornerShape(14.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = FamColors.PinterestRed)
                    ) { Text("Back to Home", fontWeight = FontWeight.Bold, color = Color.White) }
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {

                // Order summary card
                Card(shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                    border = BorderStroke(0.5.dp, colorScheme.outlineVariant)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)) {

                        // Type badge
                        Box(modifier = Modifier.clip(RoundedCornerShape(8.dp))
                            .background(FamColors.PinterestRed.copy(0.1f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)) {
                            Text(when (checkoutType) {
                                CheckoutType.DEPOSIT  -> "Custom Order Deposit"
                                CheckoutType.EVENT    -> "Event Ticket"
                                CheckoutType.BUY_NOW  -> "Buy Now"
                                else                  -> "Cart Checkout"
                            }, fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                                color = FamColors.PinterestRed)
                        }

                        Text(orderSummaryTitle, fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold, color = colorScheme.onBackground)

                        when (checkoutType) {
                            CheckoutType.CART -> {
                                CartState.items.forEach { item ->
                                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                        Text(item.title, fontSize = 13.sp,
                                            color = colorScheme.onBackground,
                                            modifier = Modifier.weight(1f))
                                        Text("KES ${item.price}", fontSize = 13.sp,
                                            color = colorScheme.onBackground)
                                    }
                                }
                                HorizontalDivider(color = colorScheme.outlineVariant)
                            }
                            CheckoutType.DEPOSIT -> {
                                val order = CartState.orders.find { it.id == orderId }
                                order?.let {
                                    SummaryRow("Artist", it.artistName, colorScheme)
                                    SummaryRow("Style",  it.style,      colorScheme)
                                    SummaryRow("Size",   it.size,       colorScheme)
                                    HorizontalDivider(color = colorScheme.outlineVariant)
                                    SummaryRow("Full total", "KES ${it.totalPrice}", colorScheme)
                                    Text("Remaining KES ${it.totalPrice - amount} due on completion",
                                        fontSize = 11.sp, color = colorScheme.onBackground.copy(0.45f))
                                }
                            }
                            CheckoutType.EVENT -> {
                                val event = FAKE_EVENTS.find { it.id == eventId }
                                event?.let {
                                    SummaryRow("Date",     it.date,     colorScheme)
                                    SummaryRow("Location", it.location, colorScheme)
                                    SummaryRow("Tickets",  "$ticketCount × KES ${it.ticketPrice}",
                                        colorScheme)
                                    HorizontalDivider(color = colorScheme.outlineVariant)
                                }
                            }
                            CheckoutType.BUY_NOW -> {
                                HorizontalDivider(color = colorScheme.outlineVariant)
                            }
                        }

                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text(if (checkoutType == CheckoutType.DEPOSIT)
                                "50% Deposit due now" else "Total",
                                fontSize = 15.sp, fontWeight = FontWeight.Bold,
                                color = colorScheme.onBackground)
                            Text("KES $amount", fontSize = 18.sp,
                                fontWeight = FontWeight.Bold, color = FamColors.PinterestRed)
                        }
                    }
                }

                // Payment methods
                Text("Choose Payment Method", fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onBackground)

                PAYMENT_METHODS.forEach { (icon, name, desc) ->
                    val sel = selectedMethod == name
                    Row(modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(BorderStroke(if (sel) 2.dp else 1.dp,
                            if (sel) FamColors.PinterestRed else colorScheme.outlineVariant),
                            RoundedCornerShape(14.dp))
                        .background(if (sel) FamColors.PinterestRed.copy(0.06f)
                        else colorScheme.background)
                        .clickable { selectedMethod = name }
                        .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(icon, fontSize = 24.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
                                color = colorScheme.onBackground)
                            Text(desc, fontSize = 12.sp,
                                color = colorScheme.onBackground.copy(0.5f))
                        }
                        if (sel) Text("✓", fontSize = 18.sp, color = FamColors.PinterestRed,
                            fontWeight = FontWeight.Bold)
                    }
                }

                if (selectedMethod == "M-Pesa") {
                    OutlinedTextField(value = phone, onValueChange = { phone = it },
                        label = { Text("M-Pesa Phone Number") },
                        placeholder = { Text("e.g. 0712 345 678") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FamColors.PinterestRed,
                            focusedLabelColor  = FamColors.PinterestRed))
                }

                Button(
                    onClick  = { confirmed = true },
                    enabled  = selectedMethod.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = FamColors.PinterestRed)
                ) {
                    Text("Pay KES $amount", fontSize = 16.sp,
                        fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, colorScheme: ColorScheme) {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
        Text(label, fontSize = 13.sp, color = colorScheme.onBackground.copy(0.5f))
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Medium,
            color = colorScheme.onBackground)
    }
}