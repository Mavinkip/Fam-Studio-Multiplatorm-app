package com.famstudio.app

import androidx.compose.ui.window.ComposeUIViewController
import com.famstudio.app.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
