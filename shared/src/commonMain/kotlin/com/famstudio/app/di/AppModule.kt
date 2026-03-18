package com.famstudio.app.di

import com.famstudio.app.data.auth.AuthRepository
import com.famstudio.app.data.auth.AuthRepositoryImpl
import com.famstudio.app.data.auth.GoogleAuthHelper
import com.famstudio.app.presentation.viewmodel.ForgotPassViewModel
import com.famstudio.app.presentation.viewmodel.LoginViewModel
import com.famstudio.app.presentation.viewmodel.RegisterViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

// commonMain — only references interfaces, no Android classes
val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl() }
    // GoogleAuthHelper is provided by androidModule (see androidMain)
    viewModel { LoginViewModel(get<AuthRepository>(), get<GoogleAuthHelper>()) }
    viewModel { RegisterViewModel(get<AuthRepository>(), get<GoogleAuthHelper>()) }
    viewModel { ForgotPassViewModel(get()) }
}