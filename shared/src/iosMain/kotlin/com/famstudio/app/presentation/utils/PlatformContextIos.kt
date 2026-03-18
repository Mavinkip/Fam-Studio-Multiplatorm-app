package com.famstudio.app.presentation.utils

import androidx.compose.runtime.Composable

// wasmJsMain — no Activity concept on Web, return Unit
@Composable
actual fun getActivityContext(): Any = Unit