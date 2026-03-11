package com.famstudio.app.presentation.components

import androidx.compose.runtime.Composable

/**
 * Blocks screenshots on art detail screens.
 * Android  → FLAG_SECURE on the window
 * iOS      → UITextField(isSecureTextEntry=true) overlay
 * Wasm     → CSS pointer-events:none + context menu disabled
 *
 * Usage: call SecureScreenEffect() at the top of any screen that shows unpurchased art.
 */
@Composable
expect fun SecureScreenEffect()
