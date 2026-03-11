package com.famstudio.app.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import platform.UIKit.UIApplication
import platform.UIKit.UITextField

@Composable
actual fun SecureScreenEffect() {
    DisposableEffect(Unit) {
        // Adding a secure UITextField prevents iOS from capturing the screen
        val field = UITextField()
        field.isSecureTextEntry = true
        field.alpha = 0.01
        UIApplication.sharedApplication.keyWindow?.addSubview(field)
        onDispose {
            field.removeFromSuperview()
        }
    }
}
