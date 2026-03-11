package com.famstudio.app.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val sharedModule = module {
    // Repositories and ViewModels will be registered here as you build each feature.
    // Example:
    //   single<ArtworkRepository> { ArtworkRepositoryImpl(get()) }
    //   viewModel { HomeViewModel(get()) }
}

/**
 * Call this from each platform entry point to initialise Koin.
 *
 * Android: MainActivity.onCreate()  → initKoin { androidContext(this@MainActivity) }
 * iOS:     AppDelegate.swift        → KoinInitializerKt.doInitKoin {}
 * Wasm:    main.kt                  → initKoin {}
 */
fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(sharedModule)
    }
}
