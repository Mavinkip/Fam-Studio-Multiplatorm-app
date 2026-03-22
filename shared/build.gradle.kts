import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
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
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(libs.kotlin.coroutines.core)
            implementation(libs.kotlin.serialization.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.json)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.lifecycle.viewmodel)
            implementation(libs.compose.navigation)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            // Firebase removed from commonMain — wasmJs does not support it
        }
        androidMain.dependencies {
            implementation(libs.datastore.preferences)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.activity.compose)
            implementation(libs.compose.uiTooling)
            implementation(libs.koin.android)
            implementation(libs.credential.manager)
            implementation(libs.credential.manager.play)
            implementation(libs.google.id)
            // Firebase — Android only
            implementation(libs.firebase.auth)
            implementation(libs.firebase.firestore)
        }
        iosMain.dependencies {
            implementation(libs.datastore.preferences)
            implementation(libs.ktor.client.darwin)
            // Firebase — iOS only
            implementation(libs.firebase.auth)
            implementation(libs.firebase.firestore)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.client.js)
            // No Firebase for wasmJs — not supported yet
        }
    }
}

android {
    namespace = "com.famstudio.shared"
    compileSdk = 36
    defaultConfig { minSdk = 24 }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}