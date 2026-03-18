package com.famstudio.app.presentation.screens.checkout

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.famstudio.app.presentation.navigation.Screen
import com.famstudio.app.presentation.screens.cart.CartState
import com.famstudio.app.presentation.theme.FamColors

private val PAYMENT_METHODS = listOf(
    Triple("📱", "M-Pesa",        "Pay via M-Pesa mobile money"),
    Triple("🏦", "Bank Transfer", "Pay via bank transfer"),
    Triple("💳", "Card",          "Visa / Mastercard"),
    Triple("💵", "Cash on Pickup","Pay when you collect"),
)

@Composable
fun CheckoutScreen(
    navController:  NavHostController,
    isDepositOnly:  Boolean = false,   // true = order deposit, false = full cart checkout
    orderId:        String  = ""
) {
    val colorScheme     = MaterialTheme.colorScheme
    var selectedMethod  by remember { mutableStateOf("") }
    var phoneNumber     by remember { mutableStateOf("") }
    var confirmed       by remember { mutableStateOf(false) }

    val totalAmount = if (isDepositOnly) {
        // Deposit = half of order total
        CartState.orders.find { it.id == orderId }?.let { it.totalPrice / 2 } ?: 0L
    } else {
        CartState.cartTotal()
    }

    Column(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {

        // Header
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp))
                .background(colorScheme.surfaceVariant).clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center) {
                Text("←", fontSize = 18.sp, color = colorScheme.onBackground)
            }
            Spacer(Modifier.width(12.dp))
            Text(if (isDepositOnly) "Pay Deposit" else "Checkout",
                fontSize = 22.sp, fontWeight = FontWeight.Bold, color = colorScheme.onBackground)
        }

        if (confirmed) {
            // ── Success state ─────────────────────────────────────────
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("✅", fontSize = 60.sp)
                    Text("Payment Confirmed!", fontSize = 22.sp, fontWeight = FontWeight.Bold,
                        color = colorScheme.onBackground)
                    Text(if (isDepositOnly)
                        "Deposit of KES $totalAmount received.\nThe artist will begin working on your order."
                    else
                        "Your order has been placed.\nShipping details will be sent to your email.",
                        fontSize = 14.sp, color = colorScheme.onBackground.copy(0.6f),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }, shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth(0.7f).height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FamColors.PinterestRed)) {
                        Text("Back to Home", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {

                // Amount summary
                Card(shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                    border = BorderStroke(0.5.dp, colorScheme.outlineVariant)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (isDepositOnly) {
                            val order = CartState.orders.find { it.id == orderId }
                            order?.let {
                                Text(it.artTitle, fontSize = 16.sp, fontWeight = FontWeight.SemiBold,
                                    color = colorScheme.onBackground)
                                Text("Custom order by ${it.artistName}", fontSize = 13.sp,
                                    color = colorScheme.onBackground.copy(0.6f))
                                HorizontalDivider(color = colorScheme.outlineVariant)
                                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                    Text("Total artwork cost", fontSize = 14.sp,
                                        color = colorScheme.onBackground.copy(0.6f))
                                    Text("KES ${it.totalPrice}", fontSize = 14.sp,
                                        color = colorScheme.onBackground)
                                }
                            }
                        } else {
                            CartState.items.forEach { item ->
                                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                                    Text(item.title, fontSize = 14.sp,
                                        color = colorScheme.onBackground, modifier = Modifier.weight(1f))
                                    Text("KES ${item.price}", fontSize = 14.sp,
                                        color = colorScheme.onBackground)
                                }
                            }
                            HorizontalDivider(color = colorScheme.outlineVariant)
                        }
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text(if (isDepositOnly) "50% Deposit due now" else "Total",
                                fontSize = 15.sp, fontWeight = FontWeight.Bold,
                                color = colorScheme.onBackground)
                            Text("KES $totalAmount", fontSize = 18.sp,
                                fontWeight = FontWeight.Bold, color = FamColors.PinterestRed)
                        }
                        if (isDepositOnly) {
                            Text("Remaining balance due on completion",
                                fontSize = 12.sp, color = colorScheme.onBackground.copy(0.45f))
                        }
                    }
                }

                // Payment method
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
                            Text(desc, fontSize = 12.sp, color = colorScheme.onBackground.copy(0.5f))
                        }
                        if (sel) Text("✓", fontSize = 18.sp, color = FamColors.PinterestRed,
                            fontWeight = FontWeight.Bold)
                    }
                }

                // Phone number for M-Pesa
                if (selectedMethod == "M-Pesa") {
                    OutlinedTextField(value = phoneNumber, onValueChange = { phoneNumber = it },
                        label = { Text("M-Pesa Phone Number") },
                        placeholder = { Text("e.g. 0712 345 678") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = FamColors.PinterestRed,
                            focusedLabelColor  = FamColors.PinterestRed))
                }

                Spacer(Modifier.height(8.dp))

                // Pay button
                Button(
                    onClick  = { confirmed = true },
                    enabled  = selectedMethod.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = FamColors.PinterestRed)
                ) {
                    Text("Pay KES $totalAmount", fontSize = 16.sp,
                        fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}