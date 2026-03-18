package com.famstudio.app.data.auth

// commonMain — returns null + error message pair
interface GoogleAuthHelper {
    suspend fun getGoogleIdToken(activityContext: Any): GoogleTokenResult
}

data class GoogleTokenResult(
    val token: String? = null,
    val error: String? = null
)