package com.famstudio.app.presentation.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import com.famstudio.app.presentation.utils.getActivityContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.famstudio.app.presentation.navigation.Screen
import com.famstudio.app.presentation.theme.FamColors
import com.famstudio.app.presentation.viewmodel.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(navController: NavHostController) {
    val vm: LoginViewModel = koinViewModel<LoginViewModel>()
    val state by vm.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = getActivityContext()
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("FAM", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = FamColors.PinterestRed)
            Text("STUDIO", fontSize = 16.sp, color = FamColors.TextMuted, letterSpacing = 6.sp)
            Spacer(Modifier.height(4.dp))
            Text("Welcome back", fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = FamColors.TextPrimary)

            GoogleSignInButton(
                isLoading = state.isGoogleLoading,
                onClick   = { focusManager.clearFocus(); vm.signInWithGoogle(context) }
            )

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = FamColors.Border)
                Text("  or  ", color = FamColors.TextMuted, fontSize = 13.sp)
                HorizontalDivider(modifier = Modifier.weight(1f), color = FamColors.Border)
            }

            FamTextField(
                value = state.email, onValueChange = vm::onEmailChange, label = "Email",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            FamTextField(
                value = state.password, onValueChange = vm::onPasswordChange, label = "Password",
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); vm.signIn() }),
                trailingIcon = {
                    Text(if (passwordVisible) "Hide" else "Show", color = FamColors.TextMuted, fontSize = 13.sp,
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }.padding(end = 12.dp))
                }
            )

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text("Forgot password?", color = FamColors.PinterestRed, fontSize = 13.sp,
                    modifier = Modifier.clickable { navController.navigate(Screen.ForgotPass.route) })
            }

            AnimatedVisibility(visible = state.error != null, enter = fadeIn() + slideInVertically()) {
                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp, textAlign = TextAlign.Center) }
            }

            FamButton(text = "Sign In", isLoading = state.isLoading,
                onClick = { focusManager.clearFocus(); vm.signIn() })

            Row(horizontalArrangement = Arrangement.Center) {
                Text("Don't have an account? ", color = FamColors.TextMuted, fontSize = 14.sp)
                Text("Sign up", color = FamColors.PinterestRed, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { navController.navigate(Screen.Register.route) })
            }
        }
    }
}