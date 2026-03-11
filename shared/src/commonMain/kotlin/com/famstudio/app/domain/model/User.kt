package com.famstudio.app.domain.model

import kotlinx.serialization.Serializable

enum class UserRole { CLIENT, ARTIST, ADMIN }

@Serializable
data class User(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val avatarUrl: String = "",
    val bio: String = "",
    val roles: List<UserRole> = listOf(UserRole.CLIENT),
    val walletBalance: Double = 0.0,
    val isVerified: Boolean = false,
    val isSuspended: Boolean = false,
    val createdAt: Long = 0L
)
