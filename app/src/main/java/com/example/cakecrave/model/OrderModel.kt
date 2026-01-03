package com.example.cakecrave.model

data class OrderModel(
    val orderId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhone: String = "",

    val productId: String = "",
    val productName: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1,
    val totalPrice: Double = 0.0,
    val imageUrl: String = "",

    // 🔥 Server timestamp (set in OrderViewModel)
    val timestamp: Long = 0L
)
