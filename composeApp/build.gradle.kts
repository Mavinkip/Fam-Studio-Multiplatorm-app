import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.googleServices)           // ← Firebase
}

kotlin {
    androidTarget {
        compilerOptions { jvmTarget = JvmTarget.JVM_17 }
    }
    iosX64(); iosArm64(); iosSimulatorArm64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs { browser(); binaries.executable() }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.compose.navigation)
        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
        }
        iosMain.dependencies {}
        wasmJsMain.dependencies {}
    }
}

android {
    namespace = "com.famstudio.app"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.famstudio.app"
        minSdk = 24; targetSdk = 36
        versionCode = 1; versionName = "1.0.0"
    }
    packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
    buildTypes { getByName("release") { isMinifyEnabled = false } }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}