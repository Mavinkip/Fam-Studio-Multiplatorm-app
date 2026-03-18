package com.famstudio.app.presentation.screens.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.famstudio.app.presentation.theme.FamColors

// ── Shared text field ─────────────────────────────────────────────────────
@Composable
fun FamTextField(
    value:                String,
    onValueChange:        (String) -> Unit,
    label:                String,
    modifier:             Modifier             = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions:      KeyboardOptions      = KeyboardOptions.Default,
    keyboardActions:      KeyboardActions      = KeyboardActions.Default,
    trailingIcon:         @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(label, fontSize = 14.sp) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = FamColors.PinterestRed,
            focusedLabelColor    = FamColors.PinterestRed,
            unfocusedBorderColor = FamColors.Border,
            unfocusedLabelColor  = FamColors.TextMuted,
            cursorColor          = FamColors.PinterestRed
        )
    )
}

// ── Primary button ────────────────────────────────────────────────────────
@Composable
fun FamButton(
    text:      String,
    onClick:   () -> Unit,
    isLoading: Boolean  = false,
    modifier:  Modifier = Modifier
) {
    Button(
        onClick  = { if (!isLoading) onClick() },
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape    = RoundedCornerShape(12.dp),
        colors   = ButtonDefaults.buttonColors(
            containerColor = FamColors.PinterestRed,
            contentColor   = Color.White
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
        } else {
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// ── Google Sign-In button ─────────────────────────────────────────────────
@Composable
fun GoogleSignInButton(
    onClick:   () -> Unit,
    isLoading: Boolean = false,
    text:      String  = "Continue with Google",
    modifier:  Modifier = Modifier
) {
    OutlinedButton(
        onClick  = { if (!isLoading) onClick() },
        modifier = modifier.fillMaxWidth().height(52.dp),
        shape    = RoundedCornerShape(12.dp),
        border   = androidx.compose.foundation.BorderStroke(1.dp, FamColors.Border),
        colors   = ButtonDefaults.outlinedButtonColors(contentColor = FamColors.TextPrimary)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = FamColors.PinterestRed, strokeWidth = 2.dp)
        } else {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Google "G" logo in brand colors
                Text("G", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4285F4))
                Text(text, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = FamColors.TextPrimary)
            }
        }
    }
}