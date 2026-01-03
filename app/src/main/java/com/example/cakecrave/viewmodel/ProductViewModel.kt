package com.example.cakecrave.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.cakecrave.model.ProductModel
import com.example.cakecrave.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()

    // ================= PRODUCTS =================
    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: StateFlow<List<ProductModel>> = _products

    // ================= MESSAGE =================
    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    init {
        repository.observeProducts { list ->
            _products.value = list
        }
    }

    // ================= IMAGE UPLOAD (FIXED) =================
    fun uploadImage(
        imageUri: Uri?,
        onResult: (String?) -> Unit
    ) {
        if (imageUri == null) {
            onResult(null)
            return
        }

        // ✅ CORRECT CALL — EXACT MATCH WITH REPOSITORY
        repository.uploadImage(imageUri) { url ->
            onResult(url)
        }
    }

    // ================= ADD PRODUCT =================
    fun addProduct(
        name: String,
        price: Double,
        description: String,
        category: String,
        imageUrl: String
    ) {
        if (name.isBlank() || imageUrl.isBlank()) {
            _message.value = "Please fill all required fields"
            return
        }

        repository.addProduct(
            name = name,
            price = price,
            description = description,
            category = category,
            imageUrl = imageUrl
        )

        _message.value = "Product added successfully"
    }

    // ================= UPDATE =================
    fun updateProduct(product: ProductModel) {
        repository.updateProduct(product)
    }

    // ================= DELETE =================
    fun deleteProduct(productId: String) {
        repository.deleteProduct(productId)
        _message.value = "Product deleted"
    }

    fun clearMessage() {
        _message.value = ""
    }
}
