package com.famstudio.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val bannerUrl: String = "",
    val location: String = "",
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val rsvpCount: Int = 0,
    val isActive: Boolean = true,
    val createdAt: Long = 0L
)
