package com.famstudio.app.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// androidMain — actual
@Composable
actual fun getActivityContext(): Any = LocalContext.current