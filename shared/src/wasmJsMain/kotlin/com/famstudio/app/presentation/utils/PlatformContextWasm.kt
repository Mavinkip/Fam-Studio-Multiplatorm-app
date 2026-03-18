package com.famstudio.app.presentation.utils

import androidx.compose.runtime.Composable

// iosMain — no Activity concept on iOS, return Unit
@Composable
actual fun getActivityContext(): Any = Unit