package com.famstudio.app.domain.model

import kotlinx.serialization.Serializable

enum class TransactionType {
    TOP_UP, PURCHASE, SALE_EARNING, COMMISSION_DEPOSIT,
    COMMISSION_RELEASE, REFUND, WITHDRAWAL
}

@Serializable
data class WalletTransaction(
    val id: String = "",
    val userId: String = "",
    val type: TransactionType = TransactionType.TOP_UP,
    val amount: Double = 0.0,
    val balanceBefore: Double = 0.0,
    val balanceAfter: Double = 0.0,
    val description: String = "",
    val referenceId: String = "",   // orderId or stripePaymentIntentId
    val createdAt: Long = 0L
)
