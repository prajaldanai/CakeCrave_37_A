package com.example.cakecrave.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.cakecrave.model.ProductModel
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductViewModel : ViewModel() {

    // ================= FIREBASE =================
    private val dbRef =
        FirebaseDatabase.getInstance().reference.child("products")

    private val storageRef =
        FirebaseStorage.getInstance().reference.child("product_images")

    // ================= STATE =================
    private val _products = MutableStateFlow<List<ProductModel>>(emptyList())
    val products: StateFlow<List<ProductModel>> = _products

    init {
        observeProducts()
    }

    // ================= OBSERVE PRODUCTS =================
    private fun observeProducts() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val list = mutableListOf<ProductModel>()

                for (child in snapshot.children) {
                    val product = child.getValue(ProductModel::class.java)
                    if (product != null) {
                        list.add(product)
                    }
                }

                _products.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                // log if needed
            }
        })
    }

    // ================= ADD PRODUCT =================
    fun addProduct(
        name: String,
        price: Double,
        description: String,
        category: String,
        imageUri: Uri
    ) {
        val id = dbRef.push().key ?: return

        val imageRef = storageRef.child("$id.jpg")

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                    val product = ProductModel(
                        id = id,
                        name = name,
                        price = price,
                        description = description,
                        category = category.lowercase(),
                        imageUrl = downloadUrl.toString()
                    )

                    // 🔥 THIS CREATES /products IN DATABASE
                    dbRef.child(id).setValue(product)
                }
            }
            .addOnFailureListener {
                // 🔴 SAFETY FALLBACK (IMPORTANT)
                val product = ProductModel(
                    id = id,
                    name = name,
                    price = price,
                    description = description,
                    category = category.lowercase(),
                    imageUrl = ""
                )

                dbRef.child(id).setValue(product)
            }
    }

    // ================= DELETE PRODUCT =================
    fun deleteProduct(product: ProductModel) {
        dbRef.child(product.id).removeValue()

        if (product.imageUrl.isNotBlank()) {
            FirebaseStorage
                .getInstance()
                .getReferenceFromUrl(product.imageUrl)
                .delete()
        }
    }
}
