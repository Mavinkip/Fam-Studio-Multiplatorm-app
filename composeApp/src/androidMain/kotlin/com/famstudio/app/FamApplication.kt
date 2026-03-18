package com.famstudio.app

import android.app.Application
import com.famstudio.app.di.androidModule
import com.famstudio.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FamApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FamApplication)
            modules(appModule, androidModule)
        }
    }
}