package com.famstudio.app.presentation.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.famstudio.app.presentation.theme.FamColors
import com.famstudio.app.presentation.viewmodel.ForgotPassViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    val vm: ForgotPassViewModel = koinViewModel<ForgotPassViewModel>()
    val state by vm.state.collectAsState()
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("🔑", fontSize = 48.sp)
            Text("Reset Password", fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = FamColors.TextPrimary)
            Text("Enter your email and we'll send you a reset link",
                fontSize = 14.sp, color = FamColors.TextMuted, textAlign = TextAlign.Center)

            if (state.isSuccess) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors   = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Text("Reset link sent! Check your email.",
                        color = Color(0xFF2E7D32), textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(16.dp))
                }
                FamButton(text = "Back to Login", onClick = { navController.popBackStack() })
            } else {
                FamTextField(
                    value = state.email, onValueChange = vm::onEmailChange, label = "Email",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); vm.sendReset() })
                )
                AnimatedVisibility(visible = state.error != null, enter = fadeIn()) {
                    state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp, textAlign = TextAlign.Center) }
                }
                FamButton(text = "Send Reset Link", isLoading = state.isLoading, onClick = { focusManager.clearFocus(); vm.sendReset() })
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("← Back to Login", color = FamColors.TextMuted, fontSize = 14.sp)
                }
            }
        }
    }
}