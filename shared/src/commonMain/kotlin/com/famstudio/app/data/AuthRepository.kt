package com.famstudio.app.data.auth

import com.famstudio.app.domain.model.UserRole

data class FamUser(
    val uid:        String,
    val email:      String,
    val name:       String,
    val role:       UserRole,
    val avatarUrl:  String  = "",
    val isVerified: Boolean = false
)

sealed class AuthResult {
    data class Success(val user: FamUser) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

interface AuthRepository {
    suspend fun signIn(email: String, password: String): AuthResult
    suspend fun signInWithGoogle(idToken: String): AuthResult   // ← new
    suspend fun register(email: String, password: String, name: String, role: UserRole): AuthResult
    suspend fun sendPasswordReset(email: String): AuthResult
    suspend fun signOut()
    suspend fun currentUser(): FamUser?
    fun isLoggedIn(): Boolean
}