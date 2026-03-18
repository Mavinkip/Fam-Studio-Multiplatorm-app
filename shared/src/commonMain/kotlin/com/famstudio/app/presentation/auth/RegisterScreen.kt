package com.famstudio.app.presentation.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.famstudio.app.domain.model.UserRole
import com.famstudio.app.presentation.navigation.Screen
import com.famstudio.app.presentation.theme.FamColors
import com.famstudio.app.presentation.viewmodel.RegisterViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(navController: NavHostController) {
    val vm: RegisterViewModel = koinViewModel<RegisterViewModel>()
    val state by vm.state.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = getActivityContext()
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Register.route) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("FAM", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = FamColors.PinterestRed)
            Text("Create your account", fontSize = 22.sp, fontWeight = FontWeight.SemiBold, color = FamColors.TextPrimary)

            GoogleSignInButton(
                text = "Sign up with Google", isLoading = state.isGoogleLoading,
                onClick = { focusManager.clearFocus(); vm.signUpWithGoogle(context) }
            )

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = FamColors.Border)
                Text("  or  ", color = FamColors.TextMuted, fontSize = 13.sp)
                HorizontalDivider(modifier = Modifier.weight(1f), color = FamColors.Border)
            }

            FamTextField(value = state.name, onValueChange = vm::onNameChange, label = "Full Name",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }))
            FamTextField(value = state.email, onValueChange = vm::onEmailChange, label = "Email",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }))
            FamTextField(value = state.password, onValueChange = vm::onPasswordChange, label = "Password",
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                trailingIcon = {
                    Text(if (passwordVisible) "Hide" else "Show", color = FamColors.TextMuted, fontSize = 13.sp,
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }.padding(end = 12.dp))
                })
            FamTextField(value = state.confirmPass, onValueChange = vm::onConfirmPassChange, label = "Confirm Password",
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }))

            Text("I am joining as a...", fontSize = 14.sp, color = FamColors.TextMuted, modifier = Modifier.fillMaxWidth())
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                RoleChip("🖼  Client", "Browse & buy art", state.role == UserRole.CLIENT, Modifier.weight(1f)) { vm.onRoleChange(UserRole.CLIENT) }
                RoleChip("🎨  Artist", "Sell your art",    state.role == UserRole.ARTIST, Modifier.weight(1f)) { vm.onRoleChange(UserRole.ARTIST) }
            }

            AnimatedVisibility(visible = state.error != null, enter = fadeIn() + slideInVertically()) {
                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, fontSize = 13.sp, textAlign = TextAlign.Center) }
            }

            FamButton(text = "Create Account", isLoading = state.isLoading,
                onClick = { focusManager.clearFocus(); vm.register() })

            Row(horizontalArrangement = Arrangement.Center) {
                Text("Already have an account? ", color = FamColors.TextMuted, fontSize = 14.sp)
                Text("Sign in", color = FamColors.PinterestRed, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { navController.popBackStack() })
            }
        }
    }
}

@Composable
private fun RoleChip(label: String, subtitle: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(12.dp))
            .border(if (selected) 2.dp else 1.dp, if (selected) FamColors.PinterestRed else FamColors.Border, RoundedCornerShape(12.dp))
            .background(if (selected) FamColors.PinterestRed.copy(alpha = 0.05f) else Color.White)
            .clickable(onClick = onClick).padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(label, fontSize = 15.sp, fontWeight = FontWeight.SemiBold,
            color = if (selected) FamColors.PinterestRed else FamColors.TextPrimary)
        Text(subtitle, fontSize = 11.sp, color = FamColors.TextMuted, textAlign = TextAlign.Center)
    }
}