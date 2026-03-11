# FAM Studio

A dual-sided art marketplace built with Kotlin Multiplatform (Android + iOS + Web).

## Project Structure

```
FAM/
├── composeApp/          ← UI entry points (Android, iOS, Web)
│   └── src/
│       ├── androidMain/ ← MainActivity.kt + AndroidManifest.xml
│       ├── iosMain/     ← MainViewController.kt
│       ├── wasmJsMain/  ← main.kt (web entry)
│       └── commonMain/  ← App.kt (root composable)
│
├── shared/              ← All business logic (shared across platforms)
│   └── src/
│       ├── commonMain/kotlin/com/famstudio/app/
│       │   ├── di/              ← Koin modules
│       │   ├── domain/model/    ← User, Artwork, Order, etc.
│       │   ├── domain/repository/ ← Repository interfaces
│       │   ├── domain/usecase/  ← Business logic use cases
│       │   ├── data/remote/     ← Ktor + Firebase services
│       │   ├── data/local/      ← SQLDelight + DataStore
│       │   └── presentation/    ← ViewModels, Theme, Navigation
│       ├── androidMain/         ← Android-specific implementations
│       ├── iosMain/             ← iOS-specific implementations
│       └── wasmJsMain/          ← Web-specific implementations
│
└── iosApp/              ← Xcode project
```

## Tech Stack

| Layer | Library |
|---|---|
| UI | Compose Multiplatform 1.10.0 |
| Architecture | Clean Architecture + MVI |
| Networking | Ktor 3.0.0 |
| Images | Coil 3.0.0 |
| DI | Koin 4.0.0 |
| Local DB | SQLDelight (add in Phase 2) |
| Preferences | DataStore 1.1.0 |
| Backend | Firebase (Auth, Firestore, Storage, FCM) |
| Payments | Stripe (add in Phase 4) |

## Setup

### 1. Open in IntelliJ / Android Studio
Open the `FAM` folder. Wait for Gradle sync.

### 2. Add Firebase
- Create a Firebase project at console.firebase.google.com
- Add Android app (package: `com.famstudio.app`) → download `google-services.json` → place in `composeApp/`
- Add iOS app (bundle: `com.famstudio.app`) → download `GoogleService-Info.plist` → add via Xcode
- Enable: Authentication (Email/Password), Firestore, Storage

### 3. Run

**Android:**
Run → select Android emulator → Run

**Web:**
```bash
./gradlew :composeApp:wasmJsBrowserRun
```

**iOS:**
Open `iosApp/iosApp.xcworkspace` in Xcode → select simulator → Run

## Build Phases

- **Phase 1** ✅ Foundation (this folder)
- **Phase 2** Art Feed — masonry grid, progressive image loading
- **Phase 3** Artist Tools — upload, dashboard, resource shop
- **Phase 4** Orders, Wallet & Payments
- **Phase 5** Delivery, Events & Admin
- **Phase 6** Polish, Web & Store submission
# Fam-Studio-Multiplatorm
# Fam-Studio-Multiplatorm
# Fam-Studio-Multiplatorm
# Fam-Studio-Multiplatorm
