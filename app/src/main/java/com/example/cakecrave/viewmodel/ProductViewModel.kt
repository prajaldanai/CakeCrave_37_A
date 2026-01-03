package com.example.cakecrave.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.cakecrave.model.ProductModel
import com.example.cakecrave.model.OrderModel
import com.example.cakecrave.repository.ProductRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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

    // ================= IMAGE UPLOAD =================
    fun uploadImage(
        imageUri: Uri?,
        onResult: (String?) -> Unit
    ) {
        if (imageUri == null) {
            onResult(null)
            return
        }

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

    // ================= UPDATE PRODUCT =================
    fun updateProduct(product: ProductModel) {
        repository.updateProduct(product)
    }

    // ================= DELETE PRODUCT =================
    fun deleteProduct(productId: String) {
        repository.deleteProduct(productId)
        _message.value = "Product deleted"
    }

    fun clearMessage() {
        _message.value = ""
    }

    // =================================================
    // ================= STEP 7: PLACE ORDER =================
    // =================================================
    fun placeOrder(
        productId: String,
        productName: String,
        price: Double,
        quantity: Int,
        imageUrl: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            onError("User not logged in")
            return
        }

        val dbRef = FirebaseDatabase.getInstance()
            .getReference("orders")
            .child(userId)

        val orderId = dbRef.push().key
        if (orderId == null) {
            onError("Failed to generate order ID")
            return
        }

        val order = OrderModel(
            orderId = orderId,
            productId = productId,
            productName = productName,
            price = price,
            quantity = quantity,
            totalPrice = price * quantity,
            imageUrl = imageUrl,
            timestamp = System.currentTimeMillis()
        )

        dbRef.child(orderId)
            .setValue(order)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(it.message ?: "Failed to place order")
            }
    }
}
