package com.famstudio.app.di

import com.famstudio.app.di.appModule
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModule)
        // No Firebase/auth on wasmJs — appModule only has ViewModels
        // which won't work without AuthRepository, but web build compiles
    }
}
