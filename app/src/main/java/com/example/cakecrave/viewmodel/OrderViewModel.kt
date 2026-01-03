package com.example.cakecrave.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cakecrave.model.OrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database: DatabaseReference =
        FirebaseDatabase.getInstance().reference

    fun placeOrder(
        order: OrderModel,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: run {
            onError("User not logged in")
            return
        }

        if (order.userName.isBlank() || order.userPhone.isBlank()) {
            onError("Profile is incomplete")
            return
        }

        val orderId = database.child("orders").push().key ?: run {
            onError("Failed to create order")
            return
        }

        val readableDate = SimpleDateFormat(
            "dd MMM yyyy, hh:mm a",
            Locale.getDefault()
        ).format(Date())

        val orderMap = hashMapOf<String, Any>(
            "orderId" to orderId,
            "userId" to uid,
            "userName" to order.userName,
            "userPhone" to order.userPhone,
            "productId" to order.productId,
            "productName" to order.productName,
            "price" to order.price,
            "quantity" to order.quantity,
            "totalPrice" to order.totalPrice,
            "imageUrl" to order.imageUrl,
            "timestamp" to ServerValue.TIMESTAMP,
            "createdAt" to readableDate
        )

        database.child("orders")
            .child(orderId)
            .setValue(orderMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                onError(it.message ?: "Order failed")
            }
    }
}
