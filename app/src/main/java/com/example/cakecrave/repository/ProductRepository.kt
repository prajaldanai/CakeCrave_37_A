package com.example.cakecrave.repository

import android.net.Uri
import com.example.cakecrave.model.ProductModel
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class ProductRepository {

    private val dbRef =
        FirebaseDatabase.getInstance().reference.child("products")

    private val storageRef =
        FirebaseStorage.getInstance().reference.child("product_images")

    // ================= OBSERVE PRODUCTS =================
    fun observeProducts(onResult: (List<ProductModel>) -> Unit) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ProductModel>()
                for (child in snapshot.children) {
                    child.getValue(ProductModel::class.java)?.let {
                        list.add(it)
                    }
                }
                onResult(list)
            }

            override fun onCancelled(error: DatabaseError) {}
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

        // 🔥 MUST use Firebase push() ID
        val id = dbRef.push().key ?: return

        val imageRef = storageRef.child("$id.jpg")

        // 🔥 UPLOAD IMAGE TO FIREBASE STORAGE
        imageRef.putFile(imageUri)
            .addOnSuccessListener {

                // 🔥 GET DOWNLOAD URL
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->

                    val product = ProductModel(
                        id = id,
                        name = name,
                        price = price,
                        description = description,
                        category = category.lowercase(),
                        imageUrl = downloadUrl.toString() // ✅ FIX
                    )

                    // 🔥 SAVE PRODUCT TO DATABASE
                    dbRef.child(id).setValue(product)
                }
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
