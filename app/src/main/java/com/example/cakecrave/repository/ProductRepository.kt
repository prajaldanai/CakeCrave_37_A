package com.example.cakecrave.repository

import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.cakecrave.App
import com.example.cakecrave.model.ProductModel
import com.google.firebase.database.*
import java.io.InputStream
import java.util.concurrent.Executors

open class ProductRepository {

    // ================= FIREBASE DB =================
    private val dbRef: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("products")

    // ================= CLOUDINARY CONFIG =================
    private val cloudinary = Cloudinary(
        mapOf(
            "cloud_name" to "dcevekywh",
            "api_key" to "829685644631527",
            "api_secret" to "i_zTrr8kRBPzsMs-wX2PDygOPHk"
        )
    )

    // ================= OBSERVE PRODUCTS =================
    fun observeProducts(onResult: (List<ProductModel>) -> Unit) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ProductModel>()
                for (child in snapshot.children) {
                    val product = child.getValue(ProductModel::class.java)
                    if (product != null) list.add(product)
                }
                onResult(list)
            }

            override fun onCancelled(error: DatabaseError) {
                // Optional logging
            }
        })
    }

    // ================= ADD PRODUCT =================
    open fun addProduct(
        name: String,
        price: Double,
        description: String,
        category: String,
        imageUrl: String
    ) {
        val id = dbRef.push().key ?: return

        val product = ProductModel(
            id = id,
            name = name,
            price = price,
            description = description,
            category = category.lowercase(),
            imageUrl = imageUrl
        )

        dbRef.child(id).setValue(product)
    }

    // ================= UPDATE PRODUCT =================
    open fun updateProduct(product: ProductModel) {
        dbRef.child(product.id).setValue(product)
    }

    // ================= DELETE PRODUCT =================
    open fun deleteProduct(productId: String) {
        dbRef.child(productId).removeValue()
    }

    // ================= UPLOAD IMAGE TO CLOUDINARY =================
    // ✅ NO Context parameter anymore
    open fun uploadImage(
        imageUri: Uri,
        callback: (String?) -> Unit
    ) {
        val executor = Executors.newSingleThreadExecutor()

        executor.execute {
            try {
                val context = App.instance   // ✅ Safe Application context

                val inputStream: InputStream? =
                    context.contentResolver.openInputStream(imageUri)

                if (inputStream == null) {
                    postResult(null, callback)
                    return@execute
                }

                val fileName =
                    getFileNameFromUri(imageUri)?.substringBeforeLast(".")
                        ?: "uploaded_image"

                val response = cloudinary.uploader().upload(
                    inputStream,
                    ObjectUtils.asMap(
                        "public_id", fileName,
                        "resource_type", "image"
                    )
                )

                val imageUrl =
                    (response["secure_url"] as String?)
                        ?: (response["url"] as String?)

                postResult(imageUrl, callback)

            } catch (e: Exception) {
                e.printStackTrace()
                postResult(null, callback)
            }
        }
    }

    // ================= HELPERS =================
    private fun postResult(result: String?, callback: (String?) -> Unit) {
        Handler(Looper.getMainLooper()).post {
            callback(result)
        }
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        var fileName: String? = null

        val cursor: Cursor? =
            App.instance.contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) fileName = it.getString(index)
            }
        }
        return fileName
    }
}
