package com.famstudio.app.di

import com.famstudio.app.data.auth.AuthRepository
import com.famstudio.app.data.auth.AuthRepositoryImpl
import com.famstudio.app.data.auth.GoogleAuthHelper
import com.famstudio.app.data.auth.GoogleAuthHelperAndroid
import org.koin.dsl.module

val androidModule = module {
    single<GoogleAuthHelper> { GoogleAuthHelperAndroid() }
    single<AuthRepository>   { AuthRepositoryImpl(get()) }
}
