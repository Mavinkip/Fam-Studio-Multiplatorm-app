package com.famstudio.app.domain.model

import kotlinx.serialization.Serializable

enum class OrderStatus {
    PENDING, CONFIRMED, PROCESSING, READY_FOR_PICKUP,
    OUT_FOR_DELIVERY, DELIVERED, COMPLETED, CANCELLED, REFUNDED
}

enum class OrderType { DIRECT_PURCHASE, COMMISSION }

@Serializable
data class Order(
    val id: String = "",
    val clientId: String = "",
    val artistId: String = "",
    val artworkId: String = "",
    val artworkTitle: String = "",
    val artworkThumbnail: String = "",
    val type: OrderType = OrderType.DIRECT_PURCHASE,
    val status: OrderStatus = OrderStatus.PENDING,
    val amount: Double = 0.0,
    val platformFee: Double = 0.0,
    val artistEarning: Double = 0.0,
    val pickupPointId: String = "",
    val pickupPointName: String = "",
    val pickupPointAddress: String = "",
    val notes: String = "",
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L
)
