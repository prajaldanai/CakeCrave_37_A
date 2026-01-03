package com.example.cakecrave.model

data class ProductModel(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val description: String = "",
    val category: String = "",
    val imageUrl: String = ""   // ✅ URL ONLY
)
