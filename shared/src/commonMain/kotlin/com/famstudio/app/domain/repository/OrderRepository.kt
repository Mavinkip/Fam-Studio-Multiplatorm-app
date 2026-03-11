package com.famstudio.app.domain.repository

import com.famstudio.app.domain.model.Order
import com.famstudio.app.domain.model.WalletTransaction
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getClientOrders(clientId: String): Flow<List<Order>>
    fun getArtistOrders(artistId: String): Flow<List<Order>>
    suspend fun getOrderById(orderId: String): Result<Order>
    suspend fun createOrder(order: Order): Result<Order>
    suspend fun updateOrderStatus(orderId: String, status: com.famstudio.app.domain.model.OrderStatus): Result<Unit>
    suspend fun confirmDelivery(orderId: String): Result<Unit>
}

interface WalletRepository {
    fun getTransactions(userId: String): Flow<List<WalletTransaction>>
    suspend fun getBalance(userId: String): Result<Double>
    suspend fun topUp(userId: String, amount: Double, paymentIntentId: String): Result<WalletTransaction>
    suspend fun purchaseArtwork(clientId: String, artworkId: String): Result<Order>
}
