package com.famstudio.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Artwork(
    val id: String = "",
    val artistId: String = "",
    val artistName: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val thumbnailUrl: String = "",      // 20px blurred preview (public)
    val previewUrl: String = "",        // 800px watermarked (public)
    val storageRef: String = "",        // original — private, signed URL only
    val aspectRatio: Float = 1.0f,      // height/width — used for masonry grid
    val dominantColorHex: String = "#1A1A24",
    val tags: List<String> = emptyList(),
    val category: String = "",
    val isAvailable: Boolean = true,
    val viewCount: Int = 0,
    val saveCount: Int = 0,
    val processingStatus: String = "pending", // pending | ready | failed
    val createdAt: Long = 0L
)
